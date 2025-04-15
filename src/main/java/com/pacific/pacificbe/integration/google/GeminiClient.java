package com.pacific.pacificbe.integration.google;

import com.pacific.pacificbe.dto.request.gemini.GeminiRequest;
import com.pacific.pacificbe.dto.response.gemini.GeminiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gemini", url = "https://generativelanguage.googleapis.com")
public interface GeminiClient {
    // v1beta model gemini-1.5-flash:generateText
    @PostMapping(
            value = "/v1beta/models/gemini-1.5-flash:generateContent",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    GeminiResponse generateContent(@RequestParam("key") String apiKey, GeminiRequest request);
}
