package com.example.sep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderResponseDTO {

	
	private int id;

	private String status;

	@JsonProperty("price_amount")
	private String priceAmount;
	
	@JsonProperty("price_currency")
	private String priceCurrency;

	@JsonProperty("receive_amount")
	private String receiveAmount;
	
	@JsonProperty("receive_currency")
	private String receiveCurrency;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("order_id")
	private String orderId;

	@JsonProperty("payment_url")
	private String paymentUrl;
	
	private String token;

	
}
