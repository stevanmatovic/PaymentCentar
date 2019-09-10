package com.example.sep.dto;

import lombok.Data;

@Data
public class PaymentDTO {


  private Long clientId;

  private String paymentType;

  private boolean finished = false;

  private double price;

  private String successURI;

  private String failureURI;
  
  private String priceCurrency;
	
  private String receiveCurrency;

}
