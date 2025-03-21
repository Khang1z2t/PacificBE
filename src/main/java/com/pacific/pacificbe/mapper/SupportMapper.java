package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.SupportResponse;
import com.pacific.pacificbe.model.Support;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupportMapper {

    public SupportResponse toResponse(Support support) {
        return new SupportResponse(
                support.getId(),
                support.getSubject(),
                support.getMessage(),
                support.getStatus(),
                support.getUser().getUsername(),
                support.getUser().getEmail()
        );
    }
}
