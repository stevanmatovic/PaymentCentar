package com.example.sep.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sep.repository.PlatnaKarticaRepository;

import com.example.sep.entity.PlatnaKartica;

@Service
public class PlatnaKarticaService {

	@Autowired
	private PlatnaKarticaRepository platnaKarticaRepository;
	
	public PlatnaKartica find(String PAN,
            String securityCode,
            String cardHolderName,
            Date expirationDate) {
	return this.platnaKarticaRepository.findByPanAndSecurityCodeAndCardHolderNameAndExpirationDate(PAN, securityCode, cardHolderName, expirationDate);
	}
	
	public List<PlatnaKartica> findAll() {
		return platnaKarticaRepository.findAll();
	}
	
	
	public PlatnaKartica save(PlatnaKartica platnaKartica) {
		return platnaKarticaRepository.save(platnaKartica);
	}
	
	public PlatnaKartica findOne(Long id) {
		return platnaKarticaRepository.getOne(id);
	}
}
