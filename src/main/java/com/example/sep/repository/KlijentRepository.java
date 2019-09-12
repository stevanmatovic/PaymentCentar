package com.example.sep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sep.entity.KlijentBanke;


public interface KlijentRepository extends JpaRepository<KlijentBanke, Long>{
	
	KlijentBanke findByMerchantIdAndMerchantPassword(String merchantId, String merchantPassword);

	KlijentBanke findByEmail(String email);

}
