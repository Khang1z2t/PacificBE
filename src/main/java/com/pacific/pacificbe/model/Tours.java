package com.pacific.pacificbe.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Entity
@Table(name = "tour")
public class Tours {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String ten_tour;
	
	private String gioi_thieu_tour;
	
	private Integer so_ngay;
	
	private String noi_dung_tour;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
//	@Temporal(TemporalType.DATE)
	private Date ngay_khoi_hanh;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
//	@Temporal(TemporalType.DATE)
	private Date ngay_ket_thuc;
	
	private String diem_den;
	
	private Integer loai_tour;
	
	private String anh_tour;
	
	private String diem_khoi_hanh;
	
	private Integer trang_thai;
	
	private Long gia_tour;
	
}
