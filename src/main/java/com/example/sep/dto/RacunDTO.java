package com.example.sep.dto;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RacunDTO {

	    private String fiksniBrojBanke;
	    private Long idKlijenta;
	    private Long idBanke;
	    private Long idValute;
	    private Date datumOtvaranja;
	    private BigDecimal raspolozivoStanje;
	    
	    
	    public RacunDTO(String fiksniBrojBanke, Long idKlijenta, Long idBanke, Long idValute, Date datumOtvaranja, BigDecimal raspolozivoStanje ) {
	        this.fiksniBrojBanke = fiksniBrojBanke;
	        this.idKlijenta = idKlijenta;
	        this.idBanke = idBanke;
	        this.idValute = idValute;
	        this.datumOtvaranja = datumOtvaranja;
	        this.raspolozivoStanje = raspolozivoStanje;
	    }
	
}
