package com.example.sep.entity;

import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity 
@NoArgsConstructor
@Data
public class Valuta {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 3)
    private String sifra;

    private String naziv;

    @OneToMany(mappedBy = "valuta", cascade = CascadeType.REMOVE)
    private List<Racun> racuni;
	
}
