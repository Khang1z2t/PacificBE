package com.pacific.pacificbe.integration.discord;

import com.pacific.pacificbe.dto.request.oauth2.DiscordTokenRequest;
import com.pacific.pacificbe.dto.response.oauth2.DiscordTokenResponse;
import com.pacific.pacificbe.dto.response.oauth2.DiscordUserResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "discordClient", url = "https://discord.com/api/v10")
public interface DiscordClient {

    @PostMapping(value = "/oauth2/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    DiscordTokenResponse exchangeToken(
            @RequestHeader("Authorization") String authorization,
            @RequestBody MultiValueMap<String, String> formData
    );

    @GetMapping(value = "/users/@me", produces = MediaType.APPLICATION_JSON_VALUE)
    DiscordUserResponse getUserInfo(@RequestHeader("Authorization") String authorization);
}
