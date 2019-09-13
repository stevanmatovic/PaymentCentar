package com.example.sep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.sep.convertor.PlatnaKarticaDTOtoObject;
import com.example.sep.dto.KarticaDTO;
import com.example.sep.encryption.Encryptor;
import com.example.sep.entity.PlatnaKartica;
import com.example.sep.service.PlatnaKarticaService;



@Controller
@RequestMapping(value = "/kartica")
public class PlatnaKarticaController {

	@Autowired
	private PlatnaKarticaService platnaKarticaService;
	
	@Autowired
	private PlatnaKarticaDTOtoObject platnaKarticaDTOtoObject;
	
	Encryptor encryptor;
	
	@RequestMapping(
            method = RequestMethod.POST,
            value = "/novaKartica"
    )
    public ResponseEntity<?> novaKartica(@RequestBody KarticaDTO karticaDTO) {
		System.out.println("Expiration date " +  karticaDTO.getExpiration_date());
        PlatnaKartica platnaKartica = platnaKarticaDTOtoObject.convert(karticaDTO);
        PlatnaKartica saved = platnaKarticaService.save(platnaKartica);

        if (saved != null) {
           //ovo sam testirala da vidim da li radi dekripcija
        
            String retured = (platnaKarticaService.findOne(platnaKartica.getId()).getPan());
            String decryopted = encryptor.decrypt(encryptor.key,encryptor.initVector,retured);
            System.out.println(decryopted);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
	
}
