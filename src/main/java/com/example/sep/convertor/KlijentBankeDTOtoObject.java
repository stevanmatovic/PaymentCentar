package com.example.sep.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.example.sep.dto.KlijentBankeDTO;
import com.example.sep.encryption.Encryptor;
import com.example.sep.entity.KlijentBanke;
import com.example.sep.entity.TipKlijenta;



@Component
public class KlijentBankeDTOtoObject implements Converter<KlijentBankeDTO, KlijentBanke>{

	Encryptor encryptor;
	
	@Override
	public KlijentBanke convert(KlijentBankeDTO klijentDTO) {
		if(klijentDTO == null) {
            return null;
        }
		KlijentBanke klijent = new KlijentBanke();
        klijent.setNaziv(klijentDTO.getNaziv());
        klijent.setAdresa(klijentDTO.getAdresa());
        klijent.setEmail(klijentDTO.getEmail());
        klijent.setTelefon(klijentDTO.getTelefon());
        klijent.setTipKlijenta(stringToTipKlijenta(klijentDTO.getTipKlijenta()));

        if(klijentDTO.getMerchantId()!=null && klijentDTO.getMerchantPassword()!=null){
            klijent.setMerchantId(klijentDTO.getMerchantId());
           
            String encrypted = encryptor.encrypt(encryptor.key,encryptor.initVector,klijentDTO.getMerchantPassword());
            klijent.setMerchantPassword(encrypted);
        }
        return klijent;
	}
	
	private TipKlijenta stringToTipKlijenta(String tipKlijenta) {
        if(tipKlijenta.equals("fizicko"))
            return TipKlijenta.FIZICKO_LICE;
        else if(tipKlijenta.equals("pravno"))
            return TipKlijenta.PRAVNO_LICE;
        else
            return null;
    }

}
