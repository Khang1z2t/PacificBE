package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.SupportRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusSupportRequest;
import com.pacific.pacificbe.dto.response.SupportResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.SupportMapper;
import com.pacific.pacificbe.model.Support;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.repository.SupportRepository;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.MailService;
import com.pacific.pacificbe.services.SupportService;
import com.pacific.pacificbe.utils.UrlMapping;
import com.pacific.pacificbe.utils.enums.SupportStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService {

    private final SupportRepository supportRepository;
    private final SupportMapper supportMapper;
    private final MailService mailService;
    private final UserRepository userRepository;

    @Override
    public SupportResponse getSupportById(String id) {
        Support support = supportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUPPORT_NOT_FOUND));
        return supportMapper.toResponse(support);
    }

    @Override
    public List<SupportResponse> getAllSupports() {
        return supportRepository.findAll().stream()
                .map(supportMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SupportResponse> getSupportByEmail(String email) {
        if (email == null || email.trim().isEmpty()) return Optional.empty();
        return supportRepository.findByUserEmail(email)
                .map(supportMapper::toResponse);
    }

    @Override
    public SupportResponse createSupport(SupportRequest request) {
        log.info("Đã nhận support request: {}", request);

        User user = request.getUserEmail() != null
                ? userRepository.findByEmail(request.getUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND))
                : null;

        Support support = new Support();
        support.setName(request.getName());
        support.setEmail(request.getEmail());
        support.setSubject(request.getSubject());
        support.setMessage(request.getMessage());
        support.setStatus(request.getStatus());
        support.setUser(user);

        Support saved = supportRepository.save(support);

        return supportMapper.toResponse(saved);
    }


    @Override
    public SupportResponse updateStatus(String id, UpdateStatusSupportRequest request) {
        Support support = supportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUPPORT_NOT_FOUND));

        String newStatus = request.getStatus();
        if (newStatus != null && !newStatus.trim().isEmpty()) {
            try {
                support.setStatus(SupportStatus.valueOf(newStatus.toUpperCase()).name());
            } catch (IllegalArgumentException e) {
                throw new AppException(ErrorCode.INVALID_SUPPORT_STATUS);
            }
        } else {
            SupportStatus currentStatus = SupportStatus.valueOf(support.getStatus().toUpperCase());
            support.setStatus(currentStatus == SupportStatus.PENDING
                    ? SupportStatus.RESOLVED.name()
                    : SupportStatus.PENDING.name());
        }

        supportRepository.save(support);
        log.info("Đã cập nhật trạng thái Support [%s] thành [%s]".formatted(id, support.getStatus()));

        return supportMapper.toResponse(support);
    }

    @Override
    public Optional<Support> getUserById(String userId) {
        return supportRepository.findById(userId);
    }

    @Override
    public void sendSupportResponseEmail(Support support) {
            String email = support.getEmail(); // Đảm bảo rằng luôn có email của người dùng
            String username = support.getUser() != null && support.getUser().getUsername() != null
                    ? support.getUser().getUsername()
                    : support.getName();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String formattedDate = support.getCreatedAt() != null
                    ? support.getCreatedAt().format(formatter)
                    : "Không xác định";

            String body = String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 650px; margin: 0 auto; border: 1px solid #eaeaea; border-radius: 8px; overflow: hidden;">

                <div style="background-color: #f58220; padding: 16px; text-align: center;">
                                          <h1 style="
                                              font-family: 'Georgia', serif;
                                              font-size: 48px;
                                              font-weight: bold;
                                              margin: 0;
                                              color: black;
                                              text-shadow: 2px 2px 4px rgba(0,0,0,0.5);
                                          ">
                                              PACIFIC TOUR
                                          </h1>
                                          <h2 style="color: white; margin: 10px 0;">Phản hồi yêu cầu hỗ trợ của Quý khách</h2>
                </div>

                <div style="padding: 24px;">
                    <p>Kính gửi <strong>%s</strong>,</p>
                    <p>Chúng tôi đã nhận được yêu cầu hỗ trợ từ Quý khách với nội dung như sau:</p>

                    <div style="background-color: #f5f5f5; padding: 16px; border-left: 4px solid #f58220; margin: 16px 0;">
                        <p><strong>Tiêu đề:</strong> %s</p>
                        <p><strong>Nội dung:</strong> %s</p>
                        <p><strong>Thời gian gửi:</strong> %s</p>
                    </div>

                    <p><strong style="color: #f58220;">Phản hồi từ Pacific Travel:</strong></p>
                    <div style="margin: 12px 0; padding: 16px; background-color: #fffbe6; border-left: 4px solid #f58220;">
                        %s
                    </div>

                    <p>Trạng thái hiện tại của yêu cầu: <strong style="color: #f58220;">%s</strong>.</p>
                    <p>Nếu Quý khách cần thêm hỗ trợ, xin vui lòng phản hồi lại email này hoặc truy cập trang chính:</p>
                    <p><a href="%s" style="color: #f58220;">%s</a></p>

                    <p>Trân trọng,<br/>
                    Bộ phận Hỗ trợ khách hàng<br/>
                    <strong>Pacific Travel</strong></p>
                </div>
            </div>
            """,
                    username,
                    support.getSubject(),
                    support.getMessage(),
                    formattedDate,
                    support.getResponseMessage(),
                    support.getStatus() != null ? support.getStatus().toUpperCase() : "PENDING",
                    UrlMapping.FE_URL,
                    UrlMapping.FE_URL
            );

            mailService.queueEmail(email, "Phản hồi yêu cầu hỗ trợ", body, null);
        }
    }
