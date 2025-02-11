package com.pacific.pacificbe.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourDTO {
    private Long id;
    private String ten_tour;
    private String gioi_thieu_tour;
    private Integer so_ngay;
    private String noi_dung_tour;
    private Date ngay_khoi_hanh;
    private Date ngay_ket_thuc;
    private String diem_den;
    private Integer loai_tour;
    private String anh_tour;
    private String diem_khoi_hanh;
    private Integer trang_thai;
    private Long gia_tour;
}
