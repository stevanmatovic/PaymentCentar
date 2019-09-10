package com.example.sep.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
public class Payment {

@Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "client_id")
  private Client client;

  private String paymentType;

  private boolean failed = false;

  private boolean finished = false;

  private double price;

  private String successURI;

  private String failureURI;
  
  private String status;
  
  private String priceCurrency;
	
  private String receiveCurrency;

}
