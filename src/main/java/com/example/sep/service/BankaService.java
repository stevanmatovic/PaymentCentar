package com.example.sep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sep.repository.BankaRepository;
import com.example.sep.entity.Banka;

@Service
public class BankaService {

	@Autowired
	private BankaRepository bankaRepository;
	
	public Banka getBanka(Long id) {
        return  bankaRepository.getOne(id);
    }
    
    public Banka get() {
    	return bankaRepository.findAll().get(0);
    }
	
}
