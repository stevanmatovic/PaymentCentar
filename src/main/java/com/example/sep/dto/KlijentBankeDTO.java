package com.example.sep.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KlijentBankeDTO {

	private Long id;
    private String naziv;
    private String adresa;
    private String email;
    private String telefon;
    private String tipKlijenta;
    private String merchantId;
    private String merchantPassword;
    
    public KlijentBankeDTO(Long id, String naziv, String adresa, String email, String telefon, String tipKlijenta) {
        this.id = id;
        this.naziv = naziv;
        this.adresa = adresa;
        this.email = email;
        this.telefon = telefon;
        this.tipKlijenta = tipKlijenta;
    }
}
