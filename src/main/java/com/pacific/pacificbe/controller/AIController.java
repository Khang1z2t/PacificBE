package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.services.AiServices;
import com.pacific.pacificbe.services.TourService;
import com.pacific.pacificbe.services.impl.AiServicesImpl;
import com.pacific.pacificbe.services.impl.TourServiceImpl;
import com.pacific.pacificbe.utils.UrlMapping;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AIController {
    private final AiServices aiServices;

    @PostMapping(UrlMapping.AI_QUERY)
    public ResponseEntity<String> processAiQuery(@RequestBody Map<String, String> request,
                                                 @RequestParam(required = false) String redirectTo) {
        String answer = aiServices.processQuery(request.get("query"), redirectTo);
        return ResponseEntity.ok(answer);
    }
}
