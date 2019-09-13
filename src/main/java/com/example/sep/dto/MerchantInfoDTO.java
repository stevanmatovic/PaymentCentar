package com.example.sep.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MerchantInfoDTO {
	
	private String merchantId;
    private String merchantPassword;
    private String email;

}
