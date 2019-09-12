package com.example.sep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardDTO {

	private String expirationDate;
    private String securityCode;
    private String pan;
    private String cardHolderName;
}
