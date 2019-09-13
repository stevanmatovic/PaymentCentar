package com.example.sep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.sep.dto.KlijentBankeDTO;
import com.example.sep.dto.MerchantInfoDTO;
import com.example.sep.encryption.Encryptor;
import com.example.sep.entity.KlijentBanke;
import com.example.sep.service.KlijentBankeService;




@RestController
@RequestMapping(value = "/klijent")
public class KlijentBankeController {

	@Autowired
	private KlijentBankeService klijentBankeService;
	
	Encryptor encryptor;
	
	@RequestMapping(
            method = RequestMethod.POST,
            value = "/saveKlijent"
    )
    public ResponseEntity<?> saveKlijent(@RequestBody KlijentBankeDTO klijentDTO) {
        KlijentBanke klijent = klijentBankeService.saveKlijent(klijentDTO);

        //testiram dekriptovanje
        KlijentBanke test = klijentBankeService.findOne(klijent.getId());
        
        String decrypted = encryptor.decrypt(encryptor.key,encryptor.initVector,test.getMerchantPassword());
        System.out.println(decrypted);
    
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
	
	
	 @RequestMapping(
	            method = RequestMethod.POST,
	            value = "/retrieveKlijent"
	    )
	    public ResponseEntity<?> retrieveKlijent(@RequestBody MerchantInfoDTO merchantInfoDTO) {
	       //od klijenta preuzimam merchantpass i merchant id
	    	KlijentBanke klijent = klijentBankeService.findByEmail(merchantInfoDTO.getEmail());
	    	MerchantInfoDTO merchantInfoDTORet = new MerchantInfoDTO();
	        if(klijent !=null){
	        	
	        	merchantInfoDTORet.setEmail(klijent.getEmail());
	        	merchantInfoDTORet.setMerchantId(klijent.getMerchantId());
	        	merchantInfoDTORet.setMerchantPassword(klijent.getMerchantPassword());
	        }
	        return new ResponseEntity<>(merchantInfoDTORet,HttpStatus.OK);
	    }
	
	
}
