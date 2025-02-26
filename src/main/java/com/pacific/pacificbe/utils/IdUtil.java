package com.pacific.pacificbe.utils;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class IdUtil {
    public String generateId() {
        return java.util.UUID.randomUUID().toString();
    }

    public String getIdImage(String url) {
        Pattern pattern = Pattern.compile("/d/([^/]+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return url;
    }

    public String createNewID(String chars) {
        return chars + String.format("%03d", 1);
    }

    public String createIDFromLastID(String chars, Integer index, String lastID) {
        Integer IDNumber = Integer.parseInt(lastID.substring(index));
        IDNumber++;
        return chars + String.format("%03d", IDNumber);
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

}
