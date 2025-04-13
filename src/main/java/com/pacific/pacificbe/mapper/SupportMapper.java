package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.SupportResponse;
import com.pacific.pacificbe.model.Support;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SupportMapper {

    public SupportResponse toResponse(Support support) {
        if (support == null) {
            log.warn("Support input is null");
            return null;
        }

        String userEmail = null;
        if (support.getUser() != null) {
            userEmail = support.getUser().getEmail();
        } else {
            log.warn("Support [{}] không có user gắn kèm", support.getId());
        }

        return new SupportResponse(
                support.getId(),
                support.getName(),
                support.getEmail(),
                support.getSubject(),
                support.getMessage(),
                support.getStatus(),
                userEmail,
                support.getCreatedAt()
        );
    }
}
