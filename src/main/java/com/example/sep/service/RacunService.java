package com.example.sep.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sep.convertor.RacunDTOToObject;
import com.example.sep.repository.RacunRepository;

import com.example.sep.entity.Racun;
import com.example.sep.dto.RacunDTO;

@Service
public class RacunService {

	@Autowired
	private RacunRepository racunRepository;
	
	@Autowired
	private RacunDTOToObject racunDTOToObject;
	
	public Racun saveRacun(RacunDTO racunDTO) {
        Racun racun = racunDTOToObject.convert(racunDTO);
        String brojRacuna = generateNumber(13);
        String kontrolniBroj = generateNumber(2);
        String konacniBrojRacuna = racunDTO.getFiksniBrojBanke().concat(brojRacuna).concat(kontrolniBroj);
        racun.setBrojRacuna(konacniBrojRacuna);
        return racunRepository.save(racun);
    }

    public Racun update(Racun racun) {
        return racunRepository.save(racun);
    }

    public Racun getByBrojRacuna(String brojRacuna) {
        return racunRepository.findByBrojRacuna(brojRacuna);
    }

    private String generateNumber(int size) {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < size) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public Racun getRacun(Long id_racuna) {
        return racunRepository.getOne(id_racuna);
    }
}
