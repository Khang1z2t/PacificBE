package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pacific.pacificbe.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, String>{

}
