package com.example.sep.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.example.sep.dto.TransakcijaDTO;
import com.example.sep.encryption.Encryptor;
import com.example.sep.entity.Transakcija;

@Component
public class TransakcijaObjectToDTO implements Converter<Transakcija, TransakcijaDTO>{

	private Encryptor encryptor;
	
	@Override
	public TransakcijaDTO convert(Transakcija source) {
		if(source == null) {
			return null;
		}
		TransakcijaDTO ret = new TransakcijaDTO();
		ret.setAmount(source.getAmount());
		ret.setAquirerOrderId(source.getAquirerOrderId());
		ret.setAquirerTimestamp(source.getAquirerTimestamp());
		ret.setBankAcquirer(source.getBankAcquirer());
		ret.setCardHolderName(source.getCardHolderName());
		ret.setExpirationDate(source.getExpirationDate());
		ret.setIssuerOrderId(source.getIssuerOrderId());
		ret.setIssuerTimestamp(source.getIssuerTimestamp());
	
		ret.setResult(source.getResult());
		
		String securityCode = String.valueOf(source.getSecurityCode());
        ret.setSecurityCode(encryptor.encrypt(encryptor.key, encryptor.initVector, securityCode));

        String panString = source.getPan().toString();
        String encrypted = encryptor.encrypt(encryptor.key, encryptor.initVector, panString);
        ret.setPan(encrypted);

		return ret;
	}

}
