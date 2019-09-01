package com.example.sep.service;

import com.example.sep.entity.Client;

import com.example.sep.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClientService {

  @Autowired
  ClientRepository clientRepository;

  public Long save(Client c){
    return clientRepository.save(c).getId();
  }

}
