package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String> {

    @Query("SELECT v FROM Voucher v WHERE v.codeVoucher = :codeVoucher")
    Optional<Voucher> findByCodeVoucher(String codeVoucher);

    List<Voucher> findByActiveTrueAndStatus(String status);
}
