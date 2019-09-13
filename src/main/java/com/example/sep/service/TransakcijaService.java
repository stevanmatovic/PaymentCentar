package com.example.sep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sep.repository.TransakcijaRepository;
import com.example.sep.entity.Transakcija;

@Service
public class TransakcijaService {

	@Autowired
	private TransakcijaRepository transakcijaRepository;
	
	public Transakcija save(Transakcija transakcija) {
		return this.transakcijaRepository.save(transakcija);
	}
	
}
