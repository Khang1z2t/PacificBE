package com.pacific.pacificbe.integration.supabase;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@FeignClient(name = "supabase", url = "${supabase.url}")
public interface SupabaseClient {
    @PostMapping(value = "/storage/v1/object/{bucket}/{path}", consumes = MediaType.IMAGE_JPEG_VALUE)
    void uploadFileToSupabase(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Content-Type") String contentType,
            @PathVariable("bucket") String bucket,
            @PathVariable("path") String path,
            @RequestBody byte[] content
    );

    @RequestMapping(method = RequestMethod.HEAD, value = "/storage/v1/object/public/{bucket}/{filePath}")
    Response checkFileExists(@PathVariable("bucket") String bucket,
                             @PathVariable("filePath") String filePath);

    @DeleteMapping
    Response deleteFileFromSupabase(
            URI fullUrl,
            @RequestHeader("Authorization") String authorization
    );
}
