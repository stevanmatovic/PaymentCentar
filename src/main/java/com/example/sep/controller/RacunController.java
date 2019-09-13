package com.example.sep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.sep.dto.RacunDTO;
import com.example.sep.service.RacunService;


@RestController
@RequestMapping(value = "/racun")
public class RacunController {

	@Autowired
	private RacunService racunService;
	
	@RequestMapping(
            method = RequestMethod.POST
    )
    public ResponseEntity<?> saveRacun(@RequestBody RacunDTO racunDTO) {
        racunService.saveRacun(racunDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
	
}
