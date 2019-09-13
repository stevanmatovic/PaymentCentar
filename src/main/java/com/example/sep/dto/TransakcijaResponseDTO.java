package com.example.sep.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TransakcijaResponseDTO {

	private String pan; //payment card number, primary account number
    private String securityCode;
    private String cardHolderName;
    
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date expirationDate;

    private BigDecimal amount;
    private Long issuerOrderId;
    private Timestamp issuerTimestamp;
    private Long bankAcquirer;
    
    private Long aquirerOrderId;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp aquirerTimestamp;
    
    private String result;
	
   
    public TransakcijaResponseDTO(String pan, String securityCode, String cardHolderName, Date expirationDate) {
        this.pan = pan;
        this.securityCode = securityCode;
        this.cardHolderName = cardHolderName;
        this.expirationDate = expirationDate;
    }
	
}
