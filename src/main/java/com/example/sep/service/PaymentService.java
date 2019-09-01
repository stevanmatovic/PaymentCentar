package com.example.sep.service;

import com.example.sep.entity.Payment;
import com.example.sep.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  @Autowired
  private PaymentRepository paymentRepository;

  public Payment save(Payment payment){
    return this.paymentRepository.save(payment);
  }

  public Payment getByID(Long id){
    return this.paymentRepository.getOne(id);
  }


}
