package com.example.sep.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.example.sep.dto.RacunDTO;
import com.example.sep.entity.Racun;
import com.example.sep.service.BankaService;
import com.example.sep.service.KlijentBankeService;
import com.example.sep.service.ValutaService;

@Component
public class RacunDTOToObject implements Converter<RacunDTO, Racun>{

	@Autowired
	private KlijentBankeService klijentBankeService;
	
	@Autowired
	private BankaService bankaService;
	
	@Autowired
	private ValutaService valutaService;
	
	@Override
	public Racun convert(RacunDTO racunDTO) {
		if(racunDTO == null) {
            return null;
        }
        Racun racun = new Racun();
        if(klijentBankeService.getKlijent(racunDTO.getIdKlijenta()) == null) {
            return null;
        }
        racun.setKlijent(klijentBankeService.getKlijent(racunDTO.getIdKlijenta()));

        if(bankaService.getBanka(racunDTO.getIdBanke()) == null) {
            return null;
        }
        racun.setBanka(bankaService.getBanka(racunDTO.getIdBanke()));

        if(valutaService.getValuta(racunDTO.getIdValute()) == null) {
            return null;
        }
        racun.setValuta(valutaService.getValuta(racunDTO.getIdValute()));
        racun.setDatumOtvaranja(racunDTO.getDatumOtvaranja());
        racun.setRaspolozivoStanje(racunDTO.getRaspolozivoStanje());

        return racun;
	}

}
