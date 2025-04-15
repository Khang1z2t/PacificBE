package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.gemini.GeminiRequest;
import com.pacific.pacificbe.dto.response.gemini.GeminiResponse;
import com.pacific.pacificbe.integration.google.GeminiClient;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.model.TourDetail;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.model.Booking;
import com.pacific.pacificbe.repository.TourDetailRepository;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.repository.BookingRepository;
import com.pacific.pacificbe.services.AiServices;
import com.pacific.pacificbe.utils.UrlMapping;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.pacific.pacificbe.utils.UrlMapping.FE_URL;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AiServicesImpl implements AiServices {

    private final TourRepository tourRepository;
    private final TourDetailRepository tourDetailRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final GeminiClient geminiClient;

    @Value("${google.ai.api.key}")
    private String apiKey;

    /**
     * Phương thức này xử lý câu hỏi của người dùng và trả lời từ Gemini API.
     *
     * @param query Câu hỏi của người dùng
     * @return Câu trả lời từ Gemini API
     */
    @Override
    public String processQuery(String query) {
        // Kiểm tra quyền và câu hỏi nhạy cảm
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
        String username = auth != null && auth.getName() != null ? auth.getName() : null;

        if (!isAdmin) {
            String lowercaseQuery = query.toLowerCase();
            if (lowercaseQuery.contains("doanh thu") || lowercaseQuery.contains("lợi nhuận") ||
                    lowercaseQuery.contains("thống kê") || lowercaseQuery.contains("revenue") ||
                    lowercaseQuery.contains("profit")) {
                return "Hỏi mấy câu này là hư lắm đó nhaaa❤️";
            }
        }

        // Lấy dữ liệu tour làm ngữ cảnh
        List<Tour> tours = tourRepository.findByActiveTrueAndStatus("PUBLISHED");
        StringBuilder context = new StringBuilder("Danh sách tour du lịch:\n");
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
                            detail.getStartDate(),
                            detail.getEndDate(),
                            tour.getDescription() != null ? tour.getDescription() : "Không có mô tả",
                            tour.getDuration()));
                }
            }
        }

        // Lấy dữ liệu booking
        context.append("\nThông tin đặt tour:\n");
        List<Booking> bookings;
        if (isAdmin) {
            bookings = bookingRepository.findAll();
        } else if (username != null) {
            User user = userRepository.findByUsername(username).orElse(null);
            bookings = user != null ? bookingRepository.findByUserId(user.getId()) : List.of();
        } else {
            bookings = List.of();
        }

        if (bookings.isEmpty()) {
            context.append("Không có thông tin đặt tour.\n");
        } else {
            var bookingCounts = bookings.stream()
                    .collect(Collectors.groupingBy(
                            b -> b.getTourDetail().getTour().getId(),
                            Collectors.counting()
                    ));

            for (Tour tour : tours) {
                Long count = bookingCounts.getOrDefault(tour.getId(), 0L);
                if (count > 0) {
                    context.append(String.format("- %s (Tour ID: %s): Được đặt %d lần\n",
                            tour.getTitle(),
                            tour.getId(),
                            count));
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
                            booking.getCreatedAt()));
                }
            }
        }

        // Chuẩn bị prompt với ví dụ
        String prompt = String.format(
                "Bạn là trợ lý du lịch thân thiện. Dựa trên dữ liệu sau:\n%s\n" +
                        "Hướng dẫn trả lời:\n" +
                        "- Trả lời bằng tiếng Việt, ngắn gọn, tự nhiên.\n" +
                        "- Sử dụng gạch đầu dòng (-) cho danh sách, tối đa 3 tour, mỗi tour trên một dòng riêng.\n" +
                        "- Không hiển thị Tour ID trong câu trả lời.\n" +
                        "- **Chèn link tên tour bằng định dạng Markdown: [Tên Tour](URL)**.\n" +  // <== THÊM DÒNG NÀY
                        "- Cách để chèn link url cơ bản: " + FE_URL + " cộng thêm /tour-chi-tiet/{tourId} thay thế tour id vào url (id ghi đầy đủ) \n" +
//                        "- Không chèn link chi tiết tour (VD: không dùng /tour-chi-tiet/...).\n" +
                        "- Khi hỏi tour rẻ nhất, liệt kê các tour có giá thấp nhất (dựa trên giá người lớn), không yêu cầu thêm thông tin.\n" +
                        "- Đảm bảo mỗi tour cách nhau rõ ràng, không dính liền.\n" +
                        "- Nếu không có dữ liệu phù hợp, trả lời lịch sự: 'Hiện tại không có thông tin phù hợp. Bạn muốn thử câu hỏi khác không?'.\n" +
                        "- Ví dụ:\n" +
                        "  Câu hỏi: 'Tour nào rẻ nhất?'\n" +
                        "  Trả lời:\n" +
                        "    - [Khám phá Sao Thổ cực hot 7N6Đ](" + FE_URL + "/tour-chi-tiet/xxxx-xxxx-xxxx): 20.000 VNĐ/người lớn, 6.000 VNĐ/trẻ em, khởi hành 09/04/2025\n" +
                        "    - [Tour Campuchia - Siem Reap - Phnom Penh](" + FE_URL + "/tour-chi-tiet/xxxx-xxxx-xxxx): 521.387 VNĐ/người lớn, 181.520 VNĐ/trẻ em, khởi hành 05/04/2025\n" +
                        "    - [Trải nghiệm Đà Lạt - Thành phố ngàn hoa](" + FE_URL + "/tour-chi-tiet/xxxx-xxxx-xxxx): 592.286 VNĐ/người lớn, 321.327 VNĐ/trẻ em, khởi hành 08/04/2025\n" +
                        "  Câu hỏi: 'Chi tiết tour Đà Nẵng 3N2Đ?'\n" +
                        "  Trả lời:\n" +
                        "    - [Tour Đà Nẵng 3N2Đ](" + FE_URL + "/tour-chi-tiet/xxxx-xxxx-xxxx):\n" +
                        "      - Giá: 4 triệu VNĐ/người lớn\n" +
                        "      - Mô tả: Khám phá Bà Nà, Hội An\n" +
                        "      - Thời lượng: 3 ngày\n" +
                        "      - Khởi hành: 01/05/2025\n" +
                        "  Câu hỏi: 'Tôi có booking nào?'\n" +
                        "  Trả lời:\n" +
                        "    - Tour Đà Nẵng 3N2Đ: Trạng thái xác nhận, đặt ngày 15/04/2025\n" +
                        "Trả lời câu hỏi: %s",
                context, query);

        // Log để debug
        log.info("Prompt gửi đi: {}", prompt);

        GeminiRequest request = GeminiRequest.builder()
                .contents(List.of(
                        new GeminiRequest.Content(
                                List.of(new GeminiRequest.Part(prompt))
                        )
                ))
                .build();

        // Gọi Gemini API
        try {
            GeminiResponse response = geminiClient.generateContent(apiKey, request);
            return response.getCandidates().getFirst().getContent().getParts().getFirst().getText();
        } catch (
                Exception e) {
            log.error("Exception while calling Gemini API: {}", e.getMessage());
            return "Lỗi: " + e.getMessage();
        }
    }
}