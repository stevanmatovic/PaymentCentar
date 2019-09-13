package com.example.sep.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PCCRequest {

	private Long aquirerOrderId;
    private Timestamp aquirerTimestamp;
    private Long issuerOrderId;
    private Timestamp issuerTimestamp;
    private String pan;
    private Long securityCode;
    private String cardHolderName;
    private Date expirationDate;
    private BigDecimal amount;
    private Long bankAcquirer;
	
}
