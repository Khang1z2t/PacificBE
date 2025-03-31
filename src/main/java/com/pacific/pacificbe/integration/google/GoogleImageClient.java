package com.pacific.pacificbe.integration.google;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "googleImageClient", url = "https://lh3.googleusercontent.com")
public interface GoogleImageClient {
    @GetMapping(value = "/d/{fileId}", produces = "image/*")
    byte[] getImage(@PathVariable("fileId") String fileId);
}
