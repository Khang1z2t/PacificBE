package com.pacific.pacificbe.utils;

import jakarta.xml.bind.DatatypeConverter;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class IdUtil {
    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
    private final String VOUCHER_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int PASSWORD_LENGTH = 12;

    public String generateId() {
        return UUID.randomUUID().toString();
    }

    public String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

    public String getIdImage(String url) {
        Pattern pattern = Pattern.compile("/d/([^/]+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return url;
    }

    public String getIdAvatar(String url) {
        Pattern pattern = Pattern.compile(".*/a/([^=]+)");
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : url;
    }

    public String createNewID(String chars) {
        return chars + String.format("%04d", 1);
    }

    public String createIDFromLastID(String chars, String lastID) {
        int index = chars.length();
        Integer IDNumber = Integer.parseInt(lastID.substring(index));
        IDNumber++;
        return chars + String.format("%04d", IDNumber);
    }

    public String generateRandomID() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Tạo 15 số ngẫu nhiên
        for (int i = 0; i < 15; i++) {
            sb.append(random.nextInt(10)); // Số từ 0-9
        }

        return sb.toString();
    }

    public String generateVoucherCode(int minLength, int maxLength) {
        if (minLength < 1 || maxLength < minLength) {
            throw new IllegalArgumentException("Invalid length range");
        }

        Random random = new Random();
        int length = random.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder code = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(VOUCHER_CHARACTERS.length());
            code.append(VOUCHER_CHARACTERS.charAt(index));
        }

        return code.toString();
    }

    public String generateTransactionId(String lastTransactionId) {
        Date now = new Date();
        String dayPart = new SimpleDateFormat("ddMMyy").format(now);
        String prefix = "T";
        String chars = prefix + dayPart;
        if (lastTransactionId == null || !lastTransactionId.startsWith(prefix + dayPart)) {
            return this.createNewID(chars);
        } else {
            return this.createIDFromLastID(chars, lastTransactionId);
        }
    }

    public String generateUnsubscribeToken(String email, long timestamp) {
        try {
            String input = email + timestamp;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(hash).toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

}
