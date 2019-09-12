package com.example.sep.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.sep.dto.OrderResponseDTO;
import com.example.sep.entity.Client;
import com.example.sep.entity.Payment;
import com.example.sep.repository.ClientRepository;

@Service
public class BitcoinService {

	@Value("${bitcoinToken}")
	private String bitcoinToken;

	@Autowired
	private ClientRepository clientRepository;

	public Map<String, Object> createPayment(Payment p) {

		Map<String, Object> result = new HashMap<String, Object>();

		// saveOrder() repository ce primati orderid;
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		Client c = clientRepository.findById(p.getClient().getId()).get();
		p.setClient(c);
		
		headers.add("Authorization", "Token " + bitcoinToken);

		String successURL = "https://localhost:443/payment/success/" + p.getId();
		String cancelURL = "https://localhost:443/payment/failure/" + p.getId();
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

		map.add("order_id", p.getId());
		map.add("price_amount", p.getPrice());
		map.add("price_currency", p.getPriceCurrency());
		map.add("receive_currency", p.getReceiveCurrency());
		map.add("success_url", successURL);
		map.add("cancel_url", cancelURL);

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

		ResponseEntity<OrderResponseDTO> response = client.postForEntity("https://api-sandbox.coingate.com/v2/orders",
				request, OrderResponseDTO.class);
		

		result.put("status", "success");
		result.put("redirect_url", response.getBody().getPaymentUrl());
		result.put("payment", p);

		return result;

	}

}
