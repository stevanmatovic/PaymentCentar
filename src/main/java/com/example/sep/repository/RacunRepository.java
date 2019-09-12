package com.example.sep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sep.entity.Racun;

public interface RacunRepository extends JpaRepository<Racun, Long>{

	Racun findByBrojRacuna(String brojRacuna);
}
