package com.example.sep.service;

import com.example.sep.entity.Client;

import com.example.sep.repository.ClientRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class PayPalService {

  @Value("${clientId}")
  private String clientId;

  @Value("${clientSecret}")
  private String clientSecret;

  @Autowired
  private ClientRepository clientRepository;



  public Map<String, Object> createPayment(com.example.sep.entity.Payment p){

    Map<String, Object> response = new HashMap<String, Object>();
    Amount amount = new Amount();
    amount.setCurrency("USD");
    amount.setTotal(Double.toString(p.getPrice()));
    Transaction transaction = new Transaction();
    transaction.setAmount(amount);

    Client c = clientRepository.findById(p.getClient().getId()).get();

    transaction.setPayee(new Payee().setMerchantId(c.getMerchantId()));
    List<Transaction> transactions = new ArrayList<Transaction>();
    transactions.add(transaction);

    Payer payer = new Payer();
    payer.setPaymentMethod("paypal");

    Payment payment = new Payment();
    payment.setIntent("sale");
    payment.setPayer(payer);
    payment.setTransactions(transactions);

    RedirectUrls redirectUrls = new RedirectUrls();
    redirectUrls.setCancelUrl("https://localhost:443/payment/failure/" + p.getId());
    redirectUrls.setReturnUrl("https://localhost:443/payment/success/" + p.getId());
    payment.setRedirectUrls(redirectUrls);
    Payment createdPayment;
    try {
      String redirectUrl = "";
      APIContext context = new APIContext(clientId, clientSecret, "sandbox");
      createdPayment = payment.create(context);
      if(createdPayment!=null){
        List<Links> links = createdPayment.getLinks();
        for (Links link:links) {
          if(link.getRel().equals("approval_url")){
            redirectUrl = link.getHref();
            break;
          }
        }
        response.put("status", "success");
        response.put("redirect_url", redirectUrl);
      }
    } catch (PayPalRESTException e) {
      System.out.println("Error happened during payment creation!");
    }
    return response;

  }

  public Map<String, Object> completePayment(HttpServletRequest req){
    Map<String, Object> response = new HashMap();
    Payment payment = new Payment();
    payment.setId(req.getParameter("paymentId"));

    PaymentExecution paymentExecution = new PaymentExecution();
    paymentExecution.setPayerId(req.getParameter("PayerID"));
    try {
      APIContext context = new APIContext(clientId, clientSecret, "sandbox");
      Payment createdPayment = payment.execute(context, paymentExecution);
      if(createdPayment!=null){
        response.put("status", "success");
        response.put("payment", createdPayment);
      }
    } catch (PayPalRESTException e) {
      System.err.println(e.getDetails());
    }
    return response;
  }




}
