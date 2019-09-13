package com.example.sep.dto;

import java.sql.Date;

import javax.persistence.Column;

import lombok.Data;

@Data
public class PaymentDTO {


  private Long clientId;

  private String paymentType;
  
  private String merchantId;

  private String merchantPassword;

  private boolean finished = false;

  private double price;
  
  private Long merchantOrderId;

  private Date merchantTimestamp;  

  private String successURI;

  private String failureURI;
  
  private String errorURL;
  
  private String priceCurrency;
	
  private String receiveCurrency;

}
