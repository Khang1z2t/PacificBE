package com.pacific.pacificbe.services;

import java.util.Map;


public interface MailSenderInternal {
    void sendEmail(String to, String subject, String body, Map<String, byte[]> attachments);
}
