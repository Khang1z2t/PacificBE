package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.response.EnumResponse;
import com.pacific.pacificbe.utils.UrlMapping;
import com.pacific.pacificbe.utils.enums.GenderEnums;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(UrlMapping.UTIL)
@RequiredArgsConstructor
public class UtilController {
    @GetMapping(UrlMapping.GET_ALL_UTILS)
    public ResponseEntity<ApiResponse<EnumResponse>> getAllEnums() {
        EnumResponse enumResponse = new EnumResponse();
        enumResponse.setGenders(Arrays.stream(GenderEnums.values())
                .map(Enum::name)
                .collect(Collectors.toList()));
        return ResponseEntity.ok(new ApiResponse<>(200, null, enumResponse));
    }
}
