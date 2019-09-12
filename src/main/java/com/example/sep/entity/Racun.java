package com.example.sep.entity;

import javax.persistence.*;

import com.example.sep.entity.*;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Racun {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    //  3   - fiksni broj banke
	    //  13  - broj racuna
	    //  2   - kontrolni broj
	    @Column(length = 18)
	    private String brojRacuna;

	    private Date datumOtvaranja;
	    private boolean vazeci;
	    private BigDecimal raspolozivoStanje;

	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "id_klijenta")
	    private KlijentBanke klijent;

	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "id_banke")
	    private Banka banka;

	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "id_valute")
	    private Valuta valuta;

	    @OneToMany(mappedBy = "racun", cascade = CascadeType.REMOVE)
	    private List<PlatnaKartica> platneKartice;

	    
	    public Racun() {
	        platneKartice = new ArrayList<PlatnaKartica>();
	        vazeci = true;
	    }

	    public Racun(String brojRacuna, Date datumOtvaranja, boolean vazeci, KlijentBanke klijent, Banka banka, Valuta valuta, List<PlatnaKartica> platneKartice) {
	        this.brojRacuna = brojRacuna;
	        this.datumOtvaranja = datumOtvaranja;
	        this.vazeci = vazeci;
	        this.klijent = klijent;
	        this.banka = banka;
	        this.valuta = valuta;
	        this.platneKartice = platneKartice;
	    }
}
