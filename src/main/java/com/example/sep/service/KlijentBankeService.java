package com.example.sep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sep.convertor.KlijentBankeDTOtoObject;
import com.example.sep.dto.KlijentBankeDTO;
import com.example.sep.entity.KlijentBanke;
import com.example.sep.repository.KlijentRepository;



@Service
public class KlijentBankeService {

	@Autowired
	private KlijentRepository klijentRepository;
	
	@Autowired
	private KlijentBankeDTOtoObject klijentBankeDTOtoObject;
	
	public KlijentBanke saveKlijent(KlijentBankeDTO klijentDTO) {
        return this.klijentRepository.save(klijentBankeDTOtoObject.convert(klijentDTO));
    }

    public KlijentBanke getKlijent(Long id) {
        return klijentRepository.getOne(id);
    }

    public KlijentBanke findOne(Long id) {
        return klijentRepository.getOne(id);
    }

    public KlijentBanke findMerchant(String merchantId, String merchantPassword) {
    	return this.klijentRepository.findByMerchantIdAndMerchantPassword(merchantId, merchantPassword);
    }

	public KlijentBanke findByEmail(String email) {
		// TODO Auto-generated method stub
		return klijentRepository.findByEmail(email);
	}
	
}
