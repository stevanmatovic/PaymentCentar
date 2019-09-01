package com.example.sep;

import com.example.sep.entity.Client;

import com.example.sep.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataLoader {

  @Autowired
  ClientRepository clientRepository;

  @PostConstruct
  public void load() {
    Client client = new Client(1L,"Stevan","6RZGQDDTCCP58");
    clientRepository.save(client);
  }




}
