package com.pacific.pacificbe.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TourDTO {
    private Long id;
    private String tenTour;
    private String gioiThieuTour;
    private Integer soNgay;
    private String noiDungTour;
    private Date ngayKhoiHanh;
    private Date ngayKetThuc;
    private String diemDen;
    private Integer loaiTour;
    private String anhTour;
    private String diemKhoiHanh;
    private Integer trangThai;
    private Long giaTour;
}
