package com.example.sep.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.example.sep.entity.*;

import lombok.Data;


@Entity
@Data
public class KlijentBanke {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String naziv;
    private String adresa;
    private String email;
    private String telefon;
    private TipKlijenta tipKlijenta;

    @OneToMany(mappedBy = "klijent", cascade = CascadeType.REMOVE)
    private List<Racun> racuni;

    private String merchantId;
    private String merchantPassword;
    
    public KlijentBanke() {
        racuni = new ArrayList<Racun>();
    }

    public KlijentBanke(String naziv, String adresa, String email, String telefon,
                   TipKlijenta tipKlijenta, List<Racun> racuni/*,
                   String merchantId, String merchantPassword*/) {
        this.naziv = naziv;
        this.adresa = adresa;
        this.email = email;
        this.telefon = telefon;
        this.tipKlijenta = tipKlijenta;
        this.racuni = racuni;
    }

    public KlijentBanke(String naziv, String adresa, String email, String telefon,
                   TipKlijenta tipKlijenta, List<Racun> racuni,
                   String merchantId, String merchantPassword) {
        this.naziv = naziv;
        this.adresa = adresa;
        this.email = email;
        this.telefon = telefon;
        this.tipKlijenta = tipKlijenta;
        this.racuni = racuni;
        this.merchantId = merchantId;
        this.merchantPassword = merchantPassword;
    }
	
}
