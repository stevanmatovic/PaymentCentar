package com.example.sep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sep.entity.Valuta;
import com.example.sep.repository.ValutaRepository;

@Service
public class ValutaService {

	@Autowired 
	private ValutaRepository valutaRepository;
	
	
	public Valuta getValuta(Long id) {
        return valutaRepository.getOne(id);
    }
}
