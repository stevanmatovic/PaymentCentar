package com.example.sep.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KarticaDTO {

	private Long id;
    private Long id_racuna;
    private Long pan;
    private String card_holder_name;
    private Date expiration_date;
    private Long security_code;
    
    public KarticaDTO(Long id_racuna, Long pan, String card_holder_name, Date expiration_date, Long security_code) {
        this.id_racuna = id_racuna;
        this.pan = pan;
        this.card_holder_name = card_holder_name;
        this.expiration_date = expiration_date;
        this.security_code = security_code;
    }
}
