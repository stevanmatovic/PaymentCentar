package com.example.sep.entity;

import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlatnaKartica {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_racuna")
	private Racun racun;
	
	private String pan; //payment card number, primary account number
    private String securityCode;
    private String cardHolderName;
    @Column(nullable = true)
    private Date expirationDate;
    
    
}
