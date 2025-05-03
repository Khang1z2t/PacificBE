package com.pacific.pacificbe.dto.request.mail;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrevoEmailRequest {
    private Sender sender;
    private List<To> to;
    private String subject;
    private String htmlContent;
    private List<Attachment> attachment;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Sender {
        String email;
        String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class To {
        String email;
        String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Attachment {
        private String name;
        private String content; // Base64 encoded content
        private String url; // Optional URL for inline images
    }
}
