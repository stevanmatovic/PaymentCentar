package com.example.sep.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PayPalPlan {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "client_id")
  private Client client;

  private String planName;

  private String planDescription;

  private String amount;

  private String planCurrency;

  private String planFrequency;

  private String planReturnUrl;

  private String planCancelUrl;

  //id of created plan on PayPal api
  private String payPalId;
}
