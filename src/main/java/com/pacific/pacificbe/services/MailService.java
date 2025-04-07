package com.pacific.pacificbe.services;

import java.util.Map;

public interface MailService {
    void queueEmail(String to, String subject, String body);

    void queueEmail(String to, String subject, String body, Map<String, byte[]> attachments);
}
