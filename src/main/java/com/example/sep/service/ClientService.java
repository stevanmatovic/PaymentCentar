package com.example.sep.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sep.entity.Client;
import com.example.sep.repository.ClientRepository;


@Service
public class ClientService {

  @Autowired
  ClientRepository clientRepository;

  public Long save(Client c){
    return clientRepository.save(c).getId();
  }
  
  public List<Client> getClients() {
	  return clientRepository.findAll();
  }

}
