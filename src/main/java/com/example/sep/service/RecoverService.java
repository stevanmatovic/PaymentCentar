package com.example.sep.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.sep.dto.TransakcijaDTO;
import com.example.sep.entity.Payment;
import com.example.sep.entity.PaymentStatus;
import com.example.sep.entity.Racun;
import com.example.sep.entity.Transakcija;
import com.example.sep.repository.PaymentRepository;
import com.example.sep.repository.RacunRepository;
import com.example.sep.repository.TransakcijaRepository;



@Service
public class RecoverService {

	@Autowired
	private PaymentRepository prr;
	
	@Autowired
	private TransakcijaRepository tr;
	
	@Autowired
	private RacunRepository rr;
	
	private TransakcijaDTO sendRequestToPcc(TransakcijaDTO pccRequest) {
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TransakcijaDTO> entity = new HttpEntity<TransakcijaDTO>(pccRequest, headers);

		TransakcijaDTO ret = client.postForObject("http://localhost:9000/request/redirect", entity,
				TransakcijaDTO.class);
		return ret;
	}
	
	private void saveTransacion(PaymentStatus paid, Transakcija transakcija, Payment pr) {
		pr.setStatus(paid.toString());;
		prr.save(pr);
		transakcija.setPayment(pr);
		tr.save(transakcija);
	}
	
	private void transferMoneyToMerchant(Racun racun, BigDecimal amount) {
		racun.setRaspolozivoStanje(racun.getRaspolozivoStanje().add(amount));
		rr.save(racun);
	}
}
