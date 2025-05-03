package com.pacific.pacificbe.integration.mail;

import com.pacific.pacificbe.dto.request.mail.BrevoEmailRequest;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "brevo", url = "https://api.brevo.com/v3")
public interface BrevoClient {
    @PostMapping(value = "/smtp/email", consumes = MediaType.APPLICATION_JSON_VALUE)
    String sendEmail(
            @RequestHeader("api-key") String apiKey,
            BrevoEmailRequest brevoEmailRequest);

}