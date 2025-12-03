package com.pacific.pacificbe.integration.supabase;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name= "supabase", url = "${supabase.url}")
public interface SupabaseClient {
    @PostMapping(value = "/storage/v1/object/sign/{bucket}/{path}", consumes = MediaType.IMAGE_JPEG_VALUE)
    void uploadFileToSupabase(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Content-Type") String contentType,
            @PathVariable("bucket") String bucket,
            @PathVariable("path") String path,
            @RequestBody byte[] content
    );
}
