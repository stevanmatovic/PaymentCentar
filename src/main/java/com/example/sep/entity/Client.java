package com.example.sep.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(nullable = false, length = 50)
  private String title;
 
  //PayPal fields
  @Column(nullable = true)
  private String merchantId;

  @Column(nullable = true)
  private String clientId;

  @Column(nullable = true)
  private String clientSecret;
  
  @Column(nullable = true)
  private String merchantPassword;
  
  @Column(nullable = true)
  private Long merchantOrderId;

  @Column(nullable = true)
  private Date merchantTimestamp;
  
  public Client(Long id, String title, String merchantId, String clientId, String clientSecret) {
	  this.id = id;
	  this.title = title;
	  this.merchantId = merchantId;
	  this.clientId = clientId;
	  this.clientSecret = clientSecret;
  }
}
