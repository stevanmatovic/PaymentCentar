package com.example.sep.service;

import com.example.sep.entity.Client;

import com.example.sep.entity.PayPalPlan;
import com.example.sep.repository.ClientRepository;
import com.example.sep.repository.PayPalPlanRepository;
import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.*;

@Service
public class PayPalPlanService {

  @Value("${clientId}")
  private String clientId;

  @Value("${clientSecret}")
  private String clientSecret;

  @Autowired
  private PayPalPlanRepository payPalPlanRepository;

  @Autowired
  private ClientRepository clientRepository;

  private Client client;



  public PayPalPlan save(PayPalPlan payPalPlan){
    this.client = clientRepository.findById(payPalPlan.getClient().getId()).orElseGet(null);

    Map<String,Object> response = new HashMap<String,Object>();
    Plan plan = new Plan();

    plan.setName(payPalPlan.getPlanName());
    plan.setDescription(payPalPlan.getPlanDescription());
    plan.setType("fixed");

    // Payment_definitions
    PaymentDefinition paymentDefinition = new PaymentDefinition();
    paymentDefinition.setName("Regular Payments");
    paymentDefinition.setType("REGULAR");
    paymentDefinition.setFrequency(payPalPlan.getPlanFrequency());
    paymentDefinition.setFrequencyInterval("1");
    paymentDefinition.setCycles("12");

    // Currency
    Currency currency = new Currency();
    currency.setCurrency("USD");
    currency.setValue(payPalPlan.getAmount().toString());
    paymentDefinition.setAmount(currency);

    // Charge_models
    ChargeModels chargeModels = new com.paypal.api.payments.ChargeModels();
    chargeModels.setType("SHIPPING");
    chargeModels.setAmount(currency);
    List<ChargeModels> chargeModelsList = new ArrayList<ChargeModels>();
    chargeModelsList.add(chargeModels);
    paymentDefinition.setChargeModels(chargeModelsList);

    // Payment_definition
    List<PaymentDefinition> paymentDefinitionList = new ArrayList<PaymentDefinition>();
    paymentDefinitionList.add(paymentDefinition);
    plan.setPaymentDefinitions(paymentDefinitionList);

    // Merchant_preferences
    MerchantPreferences merchantPreferences = new MerchantPreferences();
    merchantPreferences.setSetupFee(currency);
    merchantPreferences.setCancelUrl(payPalPlan.getPlanCancelUrl());
    merchantPreferences.setReturnUrl(payPalPlan.getPlanReturnUrl());
    merchantPreferences.setMaxFailAttempts("0");
    merchantPreferences.setAutoBillAmount("YES");
    merchantPreferences.setInitialFailAmountAction("CONTINUE");
    plan.setMerchantPreferences(merchantPreferences);

    try {
      // Create payment
      APIContext context = new APIContext(client.getClientId(), client.getClientSecret(), "sandbox");

      Plan createdPlan = plan.create(context);
      payPalPlan.setPayPalId(createdPlan.getId());
      payPalPlan = payPalPlanRepository.save(payPalPlan);
      System.out.println("Created plan with id = " + createdPlan.getId());
      System.out.println("Plan state = " + createdPlan.getName());
      response.put("plan", payPalPlan);
      // Set up plan activate PATCH request
      List<Patch> patchRequestList = new ArrayList<Patch>();
      Map<String, String> value = new HashMap<String, String>();
      value.put("state", "ACTIVE");

      // Create update object to activate plan
      Patch patch = new Patch();
      patch.setPath("/");
      patch.setValue(value);
      patch.setOp("replace");
      patchRequestList.add(patch);

      // Activate plan
      createdPlan.update(context, patchRequestList);
      System.out.println("Plan state = " + createdPlan.getState());
    } catch (PayPalRESTException e) {
      System.err.println(e.getDetails());
    }

    return payPalPlan;
  }

  public PayPalPlan findByPayPalId(String id){
    return payPalPlanRepository.findByPayPalId(id);
  }

  public Map<String, Object> completeAgreement(PayPalPlan payPalPlan) {
    Map<String, Object> response = new HashMap<String,Object> ();
    Agreement agreement = new Agreement();
    agreement.setName("Base Agreement");
    agreement.setDescription("Basic Agreement");
    agreement.setStartDate("2019-10-1T0:06:24Z");

    // Set plan ID
    Plan plan = new Plan();
    plan.setId(payPalPlan.getPayPalId());
    agreement.setPlan(plan);

    // Add payer details
    Payer payer = new Payer();
    payer.setPaymentMethod("paypal");
    agreement.setPayer(payer);

    try {
      APIContext context = new APIContext(clientId, clientSecret, "sandbox");
      agreement = agreement.create(context);
      System.out.println("Agreement ID "+agreement.getId());
      System.out.println("Agreement ID "+agreement.getState());

      String redirectUrl = "";
      for (Links links : agreement.getLinks()) {
        if ("approval_url".equals(links.getRel())) {
          redirectUrl = (links.getHref());
          // REDIRECT USER TO url
          response.put("redirectURL",redirectUrl);
          break;
        }
      }
    } catch (PayPalRESTException e) {
      System.err.println(e.getDetails());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return response;
  }

  public Map<String,Object> executeAgreement(HttpServletRequest request) {
    Map<String, Object> response = new HashMap<String,Object> ();
    Agreement agreement = new Agreement();
    APIContext context = new APIContext(client.getClientId(), client.getClientSecret(), "sandbox");
    try {
      Agreement createdAgreement = agreement.execute(context, request.getParameter("token"));
      if(createdAgreement != null ) {
        System.out.println(createdAgreement.getPlan().getMerchantPreferences().getReturnUrl());
        response.put("state", createdAgreement.getState());
      }
    } catch (PayPalRESTException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return response;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }
}
