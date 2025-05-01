package com.pacific.pacificbe.integration.facebook;

import com.pacific.pacificbe.dto.request.oauth2.FacebookTokenRequest;
import com.pacific.pacificbe.dto.response.oauth2.FacebookTokenResponse;
import com.pacific.pacificbe.dto.response.oauth2.FacebookUserResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "facebookOauth2", url = "https://graph.facebook.com/v21.0")
public interface FacebookClient {

    @GetMapping(value = "/oauth/access_token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    FacebookTokenResponse exchangeToken(@QueryMap FacebookTokenRequest request);

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    FacebookUserResponse getUserInfo(@RequestParam("access_token") String accessToken,
                                     @RequestParam("fields") String fields);

}
