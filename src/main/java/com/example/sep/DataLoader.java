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
    Client client = new Client(1L,"Stevan","6RZGQDDTCCP58",
    		"AQ_xyqUfblCfnD5D0YoVAd7BWibDWqLdPLZ_pkhdQSd05CanzVe3y_5g4MADrTVwikO07_JXNwhD5L-G",
    		"EHZs8a2T92hpvavcNoJq_SYyu-quQuOM6dlHIkw7kjCez7wQ0bY2Bjz7A9hHLmXlCcXeuN7rdb3939CV");
    clientRepository.save(client);
  }




}
