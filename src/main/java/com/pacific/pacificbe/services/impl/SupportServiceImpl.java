package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.UpdateStatusSupportRequest;
import com.pacific.pacificbe.dto.response.SupportResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.SupportMapper;
import com.pacific.pacificbe.model.Support;
import com.pacific.pacificbe.repository.SupportRepository;
import com.pacific.pacificbe.services.MailService;
import com.pacific.pacificbe.services.SupportService;
import com.pacific.pacificbe.utils.enums.SupportStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final MailService mailService; // Inject JavaMail để gửi email phản hồi

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
        return supportRepository.findByUserEmail(email)
                .map(supportMapper::toResponse);
    }

//    @Override
//    public SupportResponse updateStatus(String id, UpdateStatusSupportRequest request) {
//        Support support = supportRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCode.SUPPORT_NOT_FOUND));
//
//        String newStatus = request.getStatus();
//        if (newStatus != null && !newStatus.trim().isEmpty()) {
//            try {
//                support.setStatus(SupportStatus.valueOf(newStatus.toUpperCase()).name());
//            } catch (IllegalArgumentException e) {
//                throw new AppException(ErrorCode.INVALID_SUPPORT_STATUS);
//            }
//        } else {
//            // Toggle giữa ACTIVE <-> INACTIVE
//            SupportStatus currentStatus = SupportStatus.valueOf(support.getStatus().toUpperCase());
//            support.setStatus(currentStatus == SupportStatus.PENDING ? SupportStatus.RESOLVED.name() : SupportStatus.PENDING.name());
//        }
//
//        supportRepository.save(support);
//        log.info("Updated support [{}] to status: {}", id, support.getStatus());
//        return supportMapper.toResponse(support);
//    }

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
            // Toggle giữa PENDING <-> RESOLVED
            SupportStatus currentStatus = SupportStatus.valueOf(support.getStatus().toUpperCase());
            support.setStatus(currentStatus == SupportStatus.PENDING ? SupportStatus.RESOLVED.name() : SupportStatus.PENDING.name());
        }

        supportRepository.save(support);
        log.info("Updated support [{}] to status: {}", id, support.getStatus());

        // Gửi email phản hồi
        sendSupportResponseEmail(support);

        return supportMapper.toResponse(support);
    }

    private void sendSupportResponseEmail(Support support) {
        String email = support.getUser().getEmail();
        String subject = "Cập nhật trạng thái hỗ trợ";
        String body = "<h2>Trạng thái hỗ trợ của bạn đã được cập nhật</h2>"
                + "<p>Xin chào " + support.getUser().getFirstName() + " " + support.getUser().getLastName() + ",</p>"
                + "<p>Yêu cầu hỗ trợ của bạn hiện đã được cập nhật sang trạng thái: <strong>" + support.getStatus() + "</strong>.</p>"
                + "<p>Cảm ơn bạn đã liên hệ với chúng tôi.</p>";

        mailService.queueEmail(email, subject, body, null);
    }

    public Optional<Support> getUserById(String userId) {
        return supportRepository.findById(userId);
    }
}
