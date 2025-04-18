package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.QueryAnalysis;
import com.pacific.pacificbe.dto.request.gemini.GeminiRequest;
import com.pacific.pacificbe.dto.response.gemini.GeminiResponse;
import com.pacific.pacificbe.integration.google.GeminiClient;
import com.pacific.pacificbe.model.*;
import com.pacific.pacificbe.repository.*;
import com.pacific.pacificbe.services.AiServices;
import io.github.bucket4j.Bucket;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.pacific.pacificbe.utils.UrlMapping.FE_URL;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AiServicesImpl implements AiServices {

    final TourRepository tourRepository;
    final TourDetailRepository tourDetailRepository;
    final UserRepository userRepository;
    final BookingRepository bookingRepository;
    final VoucherRepository voucherRepository;
    final WishlistRepository wishlistRepository;
    final GeminiClient geminiClient;
    final RateLimiterService rateLimiterService;

    @Value("${google.ai.api.key}")
    String apiKey;

    private final Map<String, List<String>> conversationHistory = new HashMap<>();

    @Override
    @Cacheable(value = "aiResponses", key = "#query", unless = "#result == null")
    public String processQuery(String query) {
        // Kiểm tra quyền và câu hỏi nhạy cảm
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
        String username = auth != null && auth.getName() != null ? auth.getName() : "anonymous";

        if (!isAdmin) {
            String lowercaseQuery = query.toLowerCase();
            if (lowercaseQuery.contains("doanh thu") || lowercaseQuery.contains("lợi nhuận") ||
                    lowercaseQuery.contains("thống kê") || lowercaseQuery.contains("revenue") ||
                    lowercaseQuery.contains("profit")) {
                return "Hỏi mấy câu này là hư lắm đó nhaaa❤️";
            }
        }

        // Kiểm tra rate limit với Bucket4j
        if (!isAdmin) {
            Bucket bucket = rateLimiterService.resolveBucket(username);
            if (!bucket.tryConsume(1)) {
                return "Bạn đang hỏi quá nhanh! Vui lòng thử lại sau vài giây.";
            }
        }

        // Phân tích câu hỏi
        QueryAnalysis queryAnalysis = analyzeQuery(query.toLowerCase());

        // Lấy ngữ cảnh hội thoại
        List<String> previousConversation = conversationHistory.getOrDefault(username, new ArrayList<>());
        String previousContext = previousConversation.isEmpty() ? "" :
                "Ngữ cảnh trước đó:\n" + String.join("\n", previousConversation) + "\n";

        // Xây dựng context
        StringBuilder context = new StringBuilder();

        context.append(analyzeTravelTrends()).append("\n");

        // Sở thích người dùng
        context.append("Sở thích người dùng:\n");
        List<String> userPreferences = getUserPreferences(username);
        if (userPreferences.isEmpty()) {
            context.append("Không có thông tin sở thích.\n");
        } else {
            context.append(String.join(", ", userPreferences)).append("\n");
        }

        // Danh sách tour
        List<Tour> tours = getFilteredTours(queryAnalysis);
        context.append("\nDanh sách tour du lịch:\n");
        if (tours.isEmpty()) {
            context.append("Hiện tại không có tour nào khả dụng.\n");
        } else {
            for (Tour tour : tours) {
                List<TourDetail> details = tourDetailRepository.findByTourIdAndActiveTrue(tour.getId());
                if (details.isEmpty()) {
                    context.append(String.format("- %s (%s, %s, Tour ID: %s): Chưa có thông tin giá. Mô tả: %s, Thời lượng: %d ngày\n",
                            tour.getTitle(),
                            tour.getCategory() != null ? tour.getCategory().getTitle() : "Không rõ",
                            tour.getDestination() != null ? tour.getDestination().getCity() : "Không rõ",
                            tour.getId(),
                            tour.getDescription() != null ? tour.getDescription() : "Không có mô tả",
                            tour.getDuration()));
                    continue;
                }

                for (TourDetail detail : details) {
                    context.append(String.format("- %s (%s, %s, Tour ID: %s): %s VNĐ/người lớn, %s VNĐ/trẻ em, Khởi hành: %s, Kết thúc: %s. Mô tả: %s, Thời lượng: %d ngày\n",
                            tour.getTitle(),
                            tour.getCategory() != null ? tour.getCategory().getTitle() : "Không rõ",
                            tour.getDestination() != null ? tour.getDestination().getCity() : "Không rõ",
                            tour.getId(),
                            detail.getPriceAdults(),
                            detail.getPriceChildren(),
                            detail.getStartDate() != null ? detail.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Không rõ",
                            detail.getEndDate() != null ? detail.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Không rõ",
                            tour.getDescription() != null ? tour.getDescription() : "Không có mô tả",
                            tour.getDuration()));
                }
            }
        }

        // Thông tin đặt tour
        context.append("\nThông tin đặt tour:\n");
        List<Booking> bookings;
        if (isAdmin) {
            bookings = bookingRepository.findAll();
        } else if (!username.equals("anonymous")) {
            User user = userRepository.findByUsername(username).orElse(null);
            bookings = user != null ? bookingRepository.findByUserId(user.getId()) : List.of();
        } else {
            bookings = List.of();
        }

        if (bookings.isEmpty()) {
            context.append("Không có thông tin đặt tour.\n");
        } else {
            Map<String, Long> bookingCounts = bookings.stream()
                    .collect(Collectors.groupingBy(
                            b -> b.getTourDetail().getTour().getId(),
                            Collectors.counting()
                    ));

            List<Map.Entry<String, Long>> topBooked = bookingCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(3)
                    .collect(Collectors.toList());

            if (!topBooked.isEmpty()) {
                context.append("Các tour được đặt nhiều nhất:\n");
                for (Map.Entry<String, Long> entry : topBooked) {
                    Tour tour = tours.stream()
                            .filter(t -> t.getId().equals(entry.getKey()))
                            .findFirst()
                            .orElse(null);
                    if (tour != null) {
                        context.append(String.format("- %s (Tour ID: %s): Được đặt %d lần\n",
                                tour.getTitle(),
                                tour.getId(),
                                entry.getValue()));
                    }
                }
            }

            if (!isAdmin && !bookings.isEmpty()) {
                context.append("\nBooking của bạn:\n");
                for (Booking booking : bookings) {
                    Tour tour = booking.getTourDetail().getTour();
                    context.append(String.format("- %s (Tour ID: %s): Trạng thái %s, Đặt ngày %s\n",
                            tour.getTitle(),
                            tour.getId(),
                            booking.getStatus(),
                            booking.getCreatedAt() != null ? booking.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Không rõ"));
                }
            }
        }

        // Thông tin voucher
        context.append("\nThông tin voucher:\n");
        List<Voucher> vouchers = getRelevantVouchers(queryAnalysis, tours);
        if (vouchers.isEmpty()) {
            context.append("Hiện tại không có voucher nào khả dụng.\n");
        } else {
            for (Voucher voucher : vouchers) {
                String applicableTo = voucher.getApplyTo() != null ? voucher.getApplyTo() : "Tất cả";
                String tourTitle = voucher.getTour() != null ? voucher.getTour().getTitle() : "Không cụ thể";
                String categoryTitle = voucher.getCategory() != null ? voucher.getCategory().getTitle() : "Không cụ thể";
                context.append(String.format("- Mã: %s, Giảm giá: %s phần trăm, Hết hạn: %s, Áp dụng cho: %s (Tour: %s, Danh mục: %s), Tối thiểu đơn hàng: %s VNĐ\n",
                        voucher.getCodeVoucher(),
                        voucher.getDiscountValue(),
                        voucher.getEndDate() != null ? voucher.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Không rõ",
                        applicableTo,
                        tourTitle,
                        categoryTitle,
                        voucher.getMinOrderValue() != null ? voucher.getMinOrderValue() : "Không yêu cầu"));
            }
        }

        // Phân tích cảm xúc
        String sentimentTone = analyzeSentiment(query.toLowerCase());

        // Chuẩn bị prompt
        String prompt = String.format(
                "Bạn là trợ lý du lịch thân thiện. Dựa trên dữ liệu sau:\n%s\n" +
                        "%s" +
                        "Hướng dẫn trả lời:\n" +
                        "- Trả lời bằng tiếng Việt, ngắn gọn, tự nhiên. Giọng điệu: %s.\n" +
                        "- Sử dụng gạch đầu dòng (-) cho danh sách, tối đa 3 mục (tour hoặc voucher), mỗi mục trên một dòng riêng.\n" +
                        "- Không hiển thị Tour ID trong câu trả lời.\n" +
                        "- Chèn link tên tour bằng định dạng Markdown: [Tên Tour](URL).\n" +
                        "- URL tour: " + FE_URL + "/tour-chi-tiet/{tourId} (thay tourId bằng ID thực tế).\n" +
                        "- Ưu tiên tour phù hợp với sở thích người dùng (nếu có).\n" +
                        "- Khi hỏi 'tour nào được đặt nhiều nhất', liệt kê tối đa 3 tour có số lượng đặt cao nhất.\n" +
                        "- Khi hỏi về voucher, liệt kê tối đa 3 voucher đang active, ưu tiên voucher liên quan đến tour được hỏi.\n" +
                        "- Khi hỏi tour rẻ nhất, liệt kê các tour có giá thấp nhất (dựa trên giá người lớn).\n" +
                        "- Nếu câu hỏi liên quan đến tour cụ thể, gợi ý voucher áp dụng cho tour đó.\n" +
                        "- Nếu có ngữ cảnh trước, trả lời dựa trên thông tin đã cung cấp (ví dụ: 'tour đầu tiên' là tour được liệt kê đầu tiên).\n" +
                        "- Nếu không có dữ liệu phù hợp, trả lời lịch sự: 'Hiện tại không có thông tin phù hợp. Bạn muốn thử câu hỏi khác không?'.\n" +
                        "- Ví dụ:\n" +
                        "  Câu hỏi: 'Tour nào rẻ nhất?'\n" +
                        "  Trả lời:\n" +
                        "    - [Khám phá Sao Thổ cực hot 7N6Đ](" + FE_URL + "/tour-chi-tiet/xxxx-xxxx-xxxx): 20.000 VNĐ/người lớn, 6.000 VNĐ/trẻ em, khởi hành 09/04/2025\n" +
                        "  Câu hỏi: 'Tour nào được đặt nhiều nhất?'\n" +
                        "  Trả lời:\n" +
                        "    - [Tour Đà Nẵng 3N2Đ](" + FE_URL + "/tour-chi-tiet/xxxx-xxxx-xxxx): Được đặt 50 lần\n" +
                        "  Câu hỏi: 'Có voucher nào đang giảm giá?'\n" +
                        "  Trả lời:\n" +
                        "    - Mã SUMMER2025: Giảm 500.000 VNĐ, hết hạn 30/06/2025, áp dụng cho tour Đà Nẵng\n" +
                        "  Câu hỏi: 'Chi tiết tour Đà Nẵng 3N2Đ?'\n" +
                        "  Trả lời:\n" +
                        "    - [Tour Đà Nẵng 3N2Đ](" + FE_URL + "/tour-chi-tiet/xxxx-xxxx-xxxx):\n" +
                        "      - Giá: 4 triệu VNĐ/người lớn\n" +
                        "      - Mô tả: Khám phá Bà Nà, Hội An\n" +
                        "      - Thời lượng: 3 ngày\n" +
                        "      - Khởi hành: 01/05/2025\n" +
                        "      - Voucher áp dụng: Mã SUMMER2025 giảm 500.000 VNĐ\n" +
                        "  Câu hỏi: 'Tôi có booking nào?'\n" +
                        "  Trả lời:\n" +
                        "    - [Tour Đà Nẵng 3N2Đ](" + FE_URL + "/tour-chi-tiet/xxxx-xxxx-xxxx): Trạng thái xác nhận, đặt ngày 15/04/2025\n" +
                        "Trả lời câu hỏi: %s",
                context, previousContext, sentimentTone, query);

        // Log prompt
        log.info("Prompt gửi đi: {}", prompt);

        // Gọi Gemini API
        try {
            GeminiRequest request = GeminiRequest.builder()
                    .contents(List.of(
                            new GeminiRequest.Content(
                                    List.of(new GeminiRequest.Part(prompt))
                            )
                    ))
                    .build();
            GeminiResponse response = geminiClient.generateContent(apiKey, request);
            String result = response.getCandidates().getFirst().getContent().getParts().getFirst().getText();

            // Lưu ngữ cảnh hội thoại
            previousConversation.add(String.format("Câu hỏi: %s\nTrả lời: %s", query, result));
            if (previousConversation.size() > 5) {
                previousConversation.remove(0);
            }
            conversationHistory.put(username, previousConversation);

            return result.isEmpty() ? "Hiện tại không có thông tin phù hợp. Bạn muốn thử câu hỏi khác không?" : result;
        } catch (Exception e) {
            log.error("Lỗi khi gọi Gemini API: {}", e.getMessage());
            return "Lỗi khi xử lý yêu cầu. Vui lòng thử lại sau!";
        }
    }

    // Các phương thức phụ trợ (giữ nguyên từ mã trước)
    private QueryAnalysis analyzeQuery(String query) {
        QueryAnalysis analysis = new QueryAnalysis();
        query = query.toLowerCase();

        // Trích xuất điểm đến
        List<String> cities = tourRepository.findAll().stream()
                .map(t -> t.getDestination() != null ? t.getDestination().getCity().toLowerCase() : "")
                .collect(Collectors.toList());
        for (String city : cities) {
            if (query.contains(city)) {
                analysis.setDestination(city);
                break;
            }
        }

        // Trích xuất giá
        if (query.contains("dưới") || query.contains("nhỏ hơn")) {
            String[] words = query.split(" ");
            for (String word : words) {
                if (word.matches("\\d+")) {
                    analysis.setMaxPrice(new BigDecimal(word).multiply(BigDecimal.valueOf(1000000)));
                    break;
                }
            }
        }

        // Trích xuất thời gian khởi hành
        if (query.contains("tháng")) {
            Pattern monthPattern = Pattern.compile("tháng\\s*(\\d{1,2})");
            Matcher matcher = monthPattern.matcher(query);
            if (matcher.find()) {
                analysis.setStartMonth(Integer.parseInt(matcher.group(1)));
            }
        }

        // Trích xuất tiện ích (ví dụ: khách sạn 4 sao)
        if (query.contains("khách sạn") && query.contains("sao")) {
            Pattern starPattern = Pattern.compile("(\\d+)\\s*sao");
            Matcher matcher = starPattern.matcher(query);
            if (matcher.find()) {
                analysis.setHotelStar(Integer.parseInt(matcher.group(1)));
            }
        }

        return analysis;
    }

    private List<String> getUserPreferences(String username) {
        List<String> preferences = new ArrayList<>();
        if (!username.equals("anonymous")) {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                List<Wishlist> wishlists = wishlistRepository.findByUserId(user.getId());
                wishlists.forEach(w -> {
                    Tour tour = w.getTour();
                    if (tour.getDestination() != null) {
                        preferences.add("Thích tour đến " + tour.getDestination().getCity());
                    }
                    if (tour.getCategory() != null) {
                        preferences.add("Thích danh mục " + tour.getCategory().getTitle());
                    }
                });

                List<Booking> bookings = bookingRepository.findByUserId(user.getId());
                bookings.forEach(b -> {
                    Tour tour = b.getTourDetail().getTour();
                    if (tour.getDuration() <= 3) {
                        preferences.add("Thích tour ngắn ngày");
                    } else {
                        preferences.add("Thích tour dài ngày");
                    }
                });
            }
        }
        return preferences.stream().distinct().collect(Collectors.toList());
    }

    private List<Tour> getFilteredTours(QueryAnalysis analysis) {
        List<Tour> tours = tourRepository.findByActiveTrueAndStatus("PUBLISHED");

        if (analysis.getDestination() != null) {
            tours = tours.stream()
                    .filter(t -> t.getDestination() != null &&
                            t.getDestination().getCity().toLowerCase().contains(analysis.getDestination()))
                    .collect(Collectors.toList());
        }

        if (analysis.getMaxPrice() != null) {
            List<String> tourIds = tourDetailRepository.findAll().stream()
                    .filter(td -> td.getPriceAdults() != null && td.getPriceAdults().compareTo(analysis.getMaxPrice()) <= 0)
                    .map(td -> td.getTour().getId())
                    .collect(Collectors.toList());
            tours = tours.stream()
                    .filter(t -> tourIds.contains(t.getId()))
                    .collect(Collectors.toList());
        }

        if (analysis.getDuration() != null) {
            tours = tours.stream()
                    .filter(t -> Objects.equals(t.getDuration(), analysis.getDuration()))
                    .collect(Collectors.toList());
        }

        Map<String, Long> bookingCounts = bookingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        b -> b.getTourDetail().getTour().getId(),
                        Collectors.counting()
                ));

        return tours.stream()
                .sorted((t1, t2) -> Long.compare(
                        bookingCounts.getOrDefault(t2.getId(), 0L),
                        bookingCounts.getOrDefault(t1.getId(), 0L)))
                .limit(50)
                .collect(Collectors.toList());
    }

    private String analyzeTravelTrends() {
        StringBuilder trends = new StringBuilder("Xu hướng du lịch hiện tại:\n");

        // Top 3 điểm đến
        Map<String, Long> destinationCounts = bookingRepository.findAll().stream()
                .filter(b -> b.getTourDetail().getTour().getDestination() != null)
                .collect(Collectors.groupingBy(
                        b -> b.getTourDetail().getTour().getDestination().getCity(),
                        Collectors.counting()
                ));
        List<Map.Entry<String, Long>> topDestinations = destinationCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toList());

        trends.append("Top điểm đến:\n");
        if (topDestinations.isEmpty()) {
            trends.append("- Chưa có dữ liệu đặt tour.\n");
        } else {
            for (Map.Entry<String, Long> entry : topDestinations) {
                trends.append(String.format("- %s: Được đặt %d lần\n", entry.getKey(), entry.getValue()));
            }
        }

        // Top 3 loại tour
        Map<String, Long> categoryCounts = bookingRepository.findAll().stream()
                .filter(b -> b.getTourDetail().getTour().getCategory() != null)
                .collect(Collectors.groupingBy(
                        b -> b.getTourDetail().getTour().getCategory().getTitle(),
                        Collectors.counting()
                ));
        List<Map.Entry<String, Long>> topCategories = categoryCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toList());

        trends.append("\nTop loại tour:\n");
        if (topCategories.isEmpty()) {
            trends.append("- Chưa có dữ liệu loại tour.\n");
        } else {
            for (Map.Entry<String, Long> entry : topCategories) {
                trends.append(String.format("- %s: Được đặt %d lần\n", entry.getKey(), entry.getValue()));
            }
        }

        // Thời gian cao điểm
        Map<String, Long> monthCounts = bookingRepository.findAll().stream()
                .filter(b -> b.getTourDetail().getStartDate() != null)
                .collect(Collectors.groupingBy(
                        b -> b.getTourDetail().getStartDate().getMonth().toString(),
                        Collectors.counting()
                ));
        List<Map.Entry<String, Long>> topMonths = monthCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toList());

        trends.append("\nThời gian cao điểm:\n");
        if (topMonths.isEmpty()) {
            trends.append("- Chưa có dữ liệu thời gian.\n");
        } else {
            for (Map.Entry<String, Long> entry : topMonths) {
                trends.append(String.format("- Tháng %s: %d booking\n", entry.getKey(), entry.getValue()));
            }
        }

        return trends.toString();
    }


    private List<Voucher> getRelevantVouchers(QueryAnalysis analysis, List<Tour> tours) {
        List<Voucher> vouchers = voucherRepository.findByActiveTrueAndStatus("ACTIVE");

        if (analysis.getDestination() != null) {
            List<String> relevantTourIds = tours.stream().map(Tour::getId).collect(Collectors.toList());
            List<String> relevantCategoryIds = tours.stream()
                    .filter(t -> t.getCategory() != null)
                    .map(t -> t.getCategory().getId())
                    .collect(Collectors.toList());

            vouchers = vouchers.stream()
                    .filter(v -> (v.getTour() != null && relevantTourIds.contains(v.getTour().getId())) ||
                            (v.getCategory() != null && relevantCategoryIds.contains(v.getCategory().getId())) ||
                            v.getApplyTo().equalsIgnoreCase("ALL"))
                    .limit(3)
                    .collect(Collectors.toList());
        }

        return vouchers;
    }

    private String analyzeSentiment(String query) {
        List<String> negativeKeywords = Arrays.asList("tệ", "hủy", "kém", "phàn nàn", "xấu");
        for (String keyword : negativeKeywords) {
            if (query.contains(keyword)) {
                return "Xin lỗi và lịch sự";
            }
        }
        return "Thân thiện và vui vẻ";
    }
}