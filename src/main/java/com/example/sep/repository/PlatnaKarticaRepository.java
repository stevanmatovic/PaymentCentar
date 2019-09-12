package com.example.sep.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sep.entity.PlatnaKartica;

public interface PlatnaKarticaRepository extends JpaRepository<PlatnaKartica, Long> {

	PlatnaKartica findByPan(Long PAN);
    PlatnaKartica findByPanAndSecurityCodeAndCardHolderNameAndExpirationDate(String PAN,
    																		 String securityCode,
                                                                             String cardHolderName,
                                                                             Date expirationDate);
	
}
