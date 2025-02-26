package com.pacific.pacificbe.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GoogleAuth {
    @Value("${google.credential.type}")
    String type;
    @Value("${google.credential.projectId}")
    String projectId;
    @Value("${google.credential.privateKeyId}")
    String privateKeyId;
    @Value("${google.credential.privateKey}")
    String privateKey;
    @Value("${google.credential.clientEmail}")
    String clientEmail;
    @Value("${google.credential.clientId}")
    String clientId;
    @Value("${google.credential.authUri}")
    String authUri;
    @Value("${google.credential.tokenUri}")
    String tokenUri;
    @Value("${google.credential.authProviderX509CertUrl}")
    String authProviderX509CertUrl;
    @Value("${google.credential.clientX509CertUrl}")
    String clientX509CertUrl;
    @Value("${google.credential.universeDomain}")
    String universeDomain;

    public ByteArrayInputStream getGoogleCredentialsJson() throws JsonProcessingException {
        Map<String, String> googleCredentials = new HashMap<>();
        googleCredentials.put("type", type);
        googleCredentials.put("project_id", projectId);
        googleCredentials.put("private_key_id", privateKeyId);
        googleCredentials.put("private_key", privateKey);
        googleCredentials.put("client_email", clientEmail);
        googleCredentials.put("client_id", clientId);
        googleCredentials.put("auth_uri", authUri);
        googleCredentials.put("token_uri", tokenUri);
        googleCredentials.put("auth_provider_x509_cert_url", authProviderX509CertUrl);
        googleCredentials.put("client_x509_cert_url", clientX509CertUrl);
        googleCredentials.put("universe_domain", universeDomain);

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] jsonData = objectMapper.writeValueAsBytes(googleCredentials);
        System.out.println(new ByteArrayInputStream(jsonData));
        return new ByteArrayInputStream(jsonData);
    }

}
