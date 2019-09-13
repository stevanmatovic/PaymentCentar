package com.example.sep.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sep.entity.PaymentStatus;
import com.example.sep.entity.Transakcija;

public interface TransakcijaRepository extends JpaRepository<Transakcija, Long>{
	
	List<Transakcija> findByPaymentStatus(PaymentStatus paymentStatus);

}
