package com.pacific.pacificbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDTO {
    private Double value; // Phần trăm thay đổi
    private String type; // "increase", "decrease", "neutral"
}
