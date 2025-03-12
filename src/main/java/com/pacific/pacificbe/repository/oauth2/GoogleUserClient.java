package com.pacific.pacificbe.repository.oauth2;

import com.pacific.pacificbe.dto.response.oauth2.GoogleUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "outbound-user-client", url = "https://www.googleapis.com")
public interface GoogleUserClient {
    @GetMapping(value = "/oauth2/v3/userinfo")
    GoogleUserResponse getUserInfo(@RequestHeader("Authorization") String authorization);
}
