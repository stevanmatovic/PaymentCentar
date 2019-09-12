package com.example.sep.controller;

import com.example.sep.entity.PayPalPlan;
import com.example.sep.service.PayPalPlanService;
import com.paypal.api.payments.Agreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

  @Autowired
  private PayPalPlanService payPalPlanService;

  @PostMapping(value = "/create")
  public PayPalPlan createPlan(@RequestBody PayPalPlan plan) {
    return payPalPlanService.save(plan);
  }

  @GetMapping(value = "/acceptPlan/{id}")
  public ResponseEntity<?> acceptPlan(@PathVariable("id") String paypalId) {
    PayPalPlan payPalPlan = payPalPlanService.findByPayPalId(paypalId);
    Map<String, Object> response = new HashMap<String, Object>();
    if (payPalPlan != null) {
      response = payPalPlanService.completeAgreement(payPalPlan);
    }
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value ="/success")
  public ResponseEntity<?> executeAgreement(HttpServletRequest request){
    Map<String,Object> response = payPalPlanService.executeAgreement(request);
    URI index = null;
    try {
      index = new URI("https://localhost:443");
    } catch (URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(index);
    return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
  }


  @PostMapping(value = "/fail")
  public String failure() {
    return "fail";
  }
}
