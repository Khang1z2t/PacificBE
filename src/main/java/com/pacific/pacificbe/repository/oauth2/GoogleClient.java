package com.pacific.pacificbe.repository.oauth2;

import com.pacific.pacificbe.dto.request.oauth2.GoogleTokenRequest;
import com.pacific.pacificbe.dto.response.oauth2.GoogleTokenResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "outbound-identity", url = "https://oauth2.googleapis.com")
public interface GoogleClient {
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleTokenResponse exchangeToken(@QueryMap GoogleTokenRequest request);
}
