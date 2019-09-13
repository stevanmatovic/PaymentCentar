package com.example.sep.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sep.dto.ClientDTO;
import com.example.sep.entity.Client;
import com.example.sep.service.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {

	private static final ModelMapper MAPPER = new ModelMapper();
	
	@Autowired
  ClientService clientService;

  @PostMapping(value = "/register")
  public Long registerNewClient(Client c){
    return this.clientService.save(c);
  }

  @GetMapping(value = "/getClients")
  public List<ClientDTO> getClients() {
	  List<ClientDTO> clients = new ArrayList<ClientDTO>();
	  for(Client client : clientService.getClients()) {
		  clients.add(MAPPER.map(client,ClientDTO.class));
	  }
	  return clients;
  }
}
