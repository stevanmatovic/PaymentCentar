package com.example.sep.controller;

import com.example.sep.entity.Client;
import com.example.sep.service.ClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

  ClientService clientService;

  @PostMapping(value = "/register")
  public Long registerNewClient(Client c){
    return this.clientService.save(c);
  }


}
