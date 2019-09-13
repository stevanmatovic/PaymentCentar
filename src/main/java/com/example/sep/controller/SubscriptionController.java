package com.example.sep.controller;

import com.example.sep.entity.PayPalPlan;
import com.example.sep.service.PayPalPlanService;
import com.paypal.api.payments.Agreement;
import com.paypal.api.payments.AgreementDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
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

  @PostMapping(value = "/acceptPlan/{id}")
  public ResponseEntity<?> acceptPlan(@PathVariable String id) {
    PayPalPlan payPalPlan = payPalPlanService.findByPayPalId(id);
    Map<String, Object> response = new HashMap<String, Object>();
    if (payPalPlan != null) {
      response = payPalPlanService.completeAgreement(payPalPlan);
    }
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value ="/success")
  public String executeAgreement(HttpServletRequest request){
    Agreement executedAgreement = payPalPlanService.executeAgreement(request);
    PayPalPlan plan = payPalPlanService.findByPayPalId(executedAgreement.getPlan().getId());

    RestTemplate restTemplate = new RestTemplate();

    if (executedAgreement != null){

//      HttpHeaders headers = new HttpHeaders();
//      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//      MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
//      map.add("agreementId", executedAgreement.getId());
//      map.add("payerEmail", executedAgreement.getPayer().getPayerInfo().getEmail());
//      map.add("payee", plan.getClient().getTitle());
//
//      HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(map, headers);
//      ResponseEntity<String> response = restTemplate.postForEntity( plan.getPlanReturnUrl(), req,String.class);
      return executedAgreement.getPayer().getPayerInfo().getEmail() + " successfully subscribed, agreementId: " + executedAgreement.getId();
    }else{

      return "failed subscription";
    }

  }


  @PostMapping(value = "/fail")
  public String failure() {
    return "fail";
  }


  @GetMapping(value = "/agreement/{id}/status")
  public AgreementDetails getDetails(@PathVariable String id){
    return payPalPlanService.agreementStatus(id);
  }


}
