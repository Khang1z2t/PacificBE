package com.pacific.pacificbe.integration.facebook;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "facebookOauth2", url = "https://graph.facebook.com")
public interface FacebookOauth2Client {
}
