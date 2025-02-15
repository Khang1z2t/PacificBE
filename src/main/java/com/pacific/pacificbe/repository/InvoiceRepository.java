package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, String>{

}
