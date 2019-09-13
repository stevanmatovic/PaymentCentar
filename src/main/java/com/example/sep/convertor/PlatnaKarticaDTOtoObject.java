package com.example.sep.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.example.sep.dto.KarticaDTO;
import com.example.sep.encryption.Encryptor;
import com.example.sep.entity.PlatnaKartica;
import com.example.sep.service.PlatnaKarticaService;
import com.example.sep.service.RacunService;

@Component
public class PlatnaKarticaDTOtoObject implements Converter<KarticaDTO, PlatnaKartica>{

	@Autowired
	private RacunService racunService;
	
	private Encryptor encryptor;
	
	@Autowired
	private PlatnaKarticaService platnaKarticaService;
	
	@Override
	public PlatnaKartica convert(KarticaDTO karticaDTO) {
		if(karticaDTO == null){
            return null;
        }
        PlatnaKartica platnaKartica = new PlatnaKartica();
        platnaKartica.setCardHolderName(karticaDTO.getCard_holder_name());
        //platnaKartica.setExpirationDate(karticaDTO.getExpiration_date());
        Long pan = karticaDTO.getPan();
       // platnaKartica.setId(2L);
        platnaKartica.setRacun(racunService.getRacun(karticaDTO.getId_racuna()));
        
        String securityCode = String.valueOf(karticaDTO.getSecurity_code());
        platnaKartica.setSecurityCode(encryptor.encrypt(encryptor.key, encryptor.initVector, securityCode));

        
        platnaKartica.setExpirationDate(karticaDTO.getExpiration_date()); 
        String panString = pan.toString();
        String encrypted = encryptor.encrypt(encryptor.key, encryptor.initVector, panString);
        platnaKartica.setPan(encrypted);


        return  platnaKartica;
	}

}
