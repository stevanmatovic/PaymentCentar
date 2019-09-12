package com.example.sep.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.*;

import com.example.sep.entity.*;
import lombok.Data;

@Entity
@Data
public class Transakcija {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
    private String pan; //payment card number, primary account number
    private String securityCode;
    private String cardHolderName;
    private Date expirationDate;

    private BigDecimal amount;
    private Long issuerOrderId;
    private Timestamp issuerTimestamp;
    private Long bankAcquirer;
    
    private Long aquirerOrderId;
    private Timestamp aquirerTimestamp;
    
    private String result;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_id")
    private Payment payment;
    
    @Enumerated
	@Column(name = "paid")
	private PaymentStatus paymentStatus;
    
    public Transakcija() {

    }
    
    public Transakcija(String pan, String securityCode, String cardHolderName, Date expirationDate) {
        this.pan = pan;
        this.securityCode = securityCode;
        this.cardHolderName = cardHolderName;
        this.expirationDate = expirationDate;
    }

}
