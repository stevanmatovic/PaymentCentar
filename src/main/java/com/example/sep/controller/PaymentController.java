package com.example.sep.controller;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.sep.dto.OrderLocalDTO;
import com.example.sep.dto.PaymentDTO;
import com.example.sep.entity.Payment;
import com.example.sep.service.BitcoinService;
import com.example.sep.service.PayPalService;
import com.example.sep.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

  private static final ModelMapper MAPPER = new ModelMapper();

  @Autowired
  private PaymentService paymentService;
  private PayPalService payPalService;
  @Autowired
  private BitcoinService bitcoinService;
  
  public PaymentController(PaymentService paymentService, PayPalService payPalService) {
    this.paymentService = paymentService;
    this.payPalService = payPalService;
  }

  @PostMapping(value = "/create")
  public String createPayment(@RequestBody PaymentDTO paymentDTO){

    Payment p = MAPPER.map(paymentDTO,Payment.class);
    p = paymentService.save(p);
    Map<String, Object> result;
    switch (paymentDTO.getPaymentType()){
      case "PayPal":
    	  result = payPalService.createPayment(p);
        return "redirect:" + result.get("redirect_url");
      case "Card":
        break;
      case "Bitcoin":
    	  result = bitcoinService.createPayment(p);
    	  paymentService.save((Payment)result.get("payment"));
          return "redirect:" + result.get("redirect_url");
    }
    return "error";
  }

  @GetMapping(value = "success/{id}")
  public void success(@PathVariable Long id){
    Payment p = paymentService.getByID(id);
    p.setFinished(true);
    RestTemplate restTemplate = new RestTemplate();
    String result = restTemplate.getForObject(p.getSuccessURI(), String.class);
    System.out.println(result);
  }
  

  @GetMapping(value = "failure/{id}")
  public void failure(@PathVariable Long id){
    Payment p = paymentService.getByID(id);
    p.setFinished(true);
    p.setFailed(true);
    RestTemplate restTemplate = new RestTemplate();
    String result = restTemplate.getForObject(p.getSuccessURI(), String.class);
    System.out.println(result);
  }
  
	@PostMapping("/updateOrder")
	public void updateOrder(@RequestBody OrderLocalDTO orderLocalDTO) {
		Payment payment = paymentService.getByID(orderLocalDTO.getOrderId());
		payment.setStatus(orderLocalDTO.getStatus());

		paymentService.save(payment);
	}


}
