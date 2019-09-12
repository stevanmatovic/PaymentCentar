package com.example.sep.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Banka {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sifra;
    //poreski identifikacioni broj
    private String pib;
    private String naziv;
    private String adresa;
    private String email;
    private String telefon;
    private String url;

    @OneToMany(mappedBy = "banka", cascade = CascadeType.REMOVE)
    private List<Racun> racuni;
	
}
