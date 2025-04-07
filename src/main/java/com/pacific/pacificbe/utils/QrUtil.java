package com.pacific.pacificbe.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class QrUtil {

    public byte[] generateQRCode(String contents, int width, int height) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, width, height, hints);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "PNG", baos);

            return baos.toByteArray(); // Trả về mảng byte của QR code
        } catch (Exception e) {
            log.warn("Cannot generate QR code: {}", e.getMessage());
            throw new AppException(ErrorCode.CANNOT_GENERATE_QR);
        }
    }

    public String generateQRCodeBase64(String contents, int width, int height) {
        byte[] qrCodeBytes = generateQRCode(contents, width, height);
        return "data:image/png;base64," +
                Base64.getEncoder().encodeToString(qrCodeBytes);
    }

    public String generateQRCodeBase64(String contents) {
        return generateQRCodeBase64(contents, 100, 100);
    }

    public String generateQRCodeBase64(String contents, int width) {
        return generateQRCodeBase64(contents, width, 100);
    }
}
