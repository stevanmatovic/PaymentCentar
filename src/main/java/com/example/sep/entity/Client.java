package com.example.sep.entity;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
