package com.example.sep.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.TaskNamespaceHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.sep.convertor.TransakcijaDTOToObject;
import com.example.sep.convertor.TransakcijaResponseDTOtoObject;
import com.example.sep.dto.CardDTO;
import com.example.sep.dto.OrderLocalDTO;
import com.example.sep.dto.PaymentDTO;
import com.example.sep.dto.TransakcijaDTO;
import com.example.sep.dto.TransakcijaResponseDTO;
import com.example.sep.encryption.Encryptor;
import com.example.sep.entity.Banka;
import com.example.sep.entity.Client;
import com.example.sep.entity.KlijentBanke;
import com.example.sep.entity.Payment;
import com.example.sep.entity.PaymentStatus;
import com.example.sep.entity.PlatnaKartica;
import com.example.sep.entity.Racun;
import com.example.sep.entity.Transakcija;
import com.example.sep.service.BankaService;
import com.example.sep.service.BitcoinService;
import com.example.sep.service.ClientService;
import com.example.sep.service.KlijentBankeService;
import com.example.sep.service.PayPalService;
import com.example.sep.service.PaymentService;
import com.example.sep.service.PlatnaKarticaService;
import com.example.sep.service.RacunService;
import com.example.sep.service.TransakcijaService;


@RestController
@RequestMapping("/payment")
public class PaymentController {

  private static final ModelMapper MAPPER = new ModelMapper();

  @Autowired
  private PaymentService paymentService;
  private PayPalService payPalService;
  @Autowired
  private BitcoinService bitcoinService;
  
  @Autowired
  private RacunService racunService;
  
  @Autowired
  private TransakcijaService transakcijaService;
  
  @Autowired
  private BankaService bankaService;
  
  @Autowired
  private KlijentBankeService klijentBankeService;
  
  @Autowired
  private ClientService clientService;
  
  @Autowired
  private PlatnaKarticaService platnaKarticaService;
  
  @Autowired
  private TransakcijaDTOToObject transakcijaDTOToObject;

  @Autowired
  private TransakcijaResponseDTOtoObject transakcijaResponseDTOtoObject;
  
  Encryptor encryptor;
  
  public PaymentController(PaymentService paymentService, PayPalService payPalService) {
    this.paymentService = paymentService;
    this.payPalService = payPalService;
  }

  @PostMapping(value = "/create")
  public String createPayment(@RequestBody PaymentDTO paymentDTO){
	  Payment p;
	  
	if(paymentDTO.getPaymentType().equals("Card")) {
		p = this.mapValues(paymentDTO);
	} else {
		p = MAPPER.map(paymentDTO,Payment.class);
	}
	  
    p = paymentService.save(p);
    Map<String, Object> result;
    switch (paymentDTO.getPaymentType()){
      case "PayPal":
    	  result = payPalService.createPayment(p);
        return "redirect:" + result.get("redirect_url");
      case "Card":
    	  Banka b = bankaService.get();
    	  return "redirect: " + b.getUrl() + "/payment/paymentPage/" + p.getId();
      case "Bitcoin":
    	  result = bitcoinService.createPayment(p);
    	  paymentService.save((Payment)result.get("payment"));
          return "redirect:" + result.get("redirect_url");
    }
    return "error";
  }
  
  
  	//kcc salje request banci gde se proverava da li je to banka kupca, ako
	// jeste vrsi se placanje
	// ako nije, salje se zahtev pcc-u
	@RequestMapping(method = RequestMethod.POST, value = "/pay/{paymentId}", consumes = "application/json")
	public ResponseEntity<?> checkPaymentCard(HttpServletRequest request, @PathVariable("paymentId") Long paymentId,
			@RequestBody CardDTO cardDTO) throws NumberFormatException, ParseException {
		Payment paymentRequest = paymentService.getByID(paymentId);
		String encrypted = encryptor.encrypt(encryptor.key, encryptor.initVector, cardDTO.getPan());
		String encryptedSecCode = encryptor.encrypt(encryptor.key, encryptor.initVector, cardDTO.getSecurityCode());
		// platna kartica kupca
		

		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date inputDate = dateFormat.parse(cardDTO.getExpirationDate().concat(" 00:00:00"), new ParsePosition(0) );
		
		System.out.println("Pretraga: " +  encrypted + 
				", sec code: " + encryptedSecCode + 
				", name: "  + cardDTO.getCardHolderName()
				+ "date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cardDTO.getExpirationDate().concat(" 00:00:00")));
		PlatnaKartica platnaKartica = platnaKarticaService.find(encrypted,
				encryptedSecCode, cardDTO.getCardHolderName(),
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cardDTO.getExpirationDate().concat(" 00:00:00")));
		

		// prodavac
		KlijentBanke prodavac = klijentBankeService.findMerchant(paymentRequest.getClient().getMerchantId(),
				paymentRequest.getClient().getMerchantPassword());
		if (prodavac == null) {
			System.out.println("Nema prodavca");
			return new ResponseEntity<>(paymentRequest.getFailureURI(), HttpStatus.OK);
		}
		if (prodavac.getRacuni().get(0) == null) {
			System.out.println("Nema prodavca racuna");
			return new ResponseEntity<>(paymentRequest.getFailureURI(), HttpStatus.OK);
		}
		
		TransakcijaDTO transakcijaDTO = new TransakcijaDTO();
		transakcijaDTO.setPan(cardDTO.getPan());
		transakcijaDTO.setSecurityCode(cardDTO.getSecurityCode());
		transakcijaDTO.setCardHolderName(cardDTO.getCardHolderName());
		transakcijaDTO.setExpirationDate(new SimpleDateFormat("yyyy-MM-dd").parse(cardDTO.getExpirationDate()));
		transakcijaDTO.setAquirerOrderId((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
		transakcijaDTO.setAquirerTimestamp(new Timestamp(System.currentTimeMillis()));
		transakcijaDTO.setBankAcquirer(Long.valueOf(cardDTO.getPan().substring(0, 6)));
		transakcijaDTO.setAmount(BigDecimal.valueOf(paymentRequest.getPrice()));
		transakcijaDTO.setResult("success");
		
		Transakcija transakcija = transakcijaDTOToObject.convert(transakcijaDTO);

		System.out.println("T dto: " + transakcijaDTO.toString());
		
		// nije ista banka
		if (platnaKartica == null) {
			try {
				System.out.println("Request sent to PCC");
				TransakcijaDTO ret = sendRequestToPcc(transakcijaDTO);
				Transakcija tr = transakcijaDTOToObject.convert(ret);
				if (ret.getResult().equals("success")) {
					saveTransacion(PaymentStatus.PAID, tr, paymentRequest);
					transferMoneyToMerchant(prodavac.getRacuni().get(0), BigDecimal.valueOf(paymentRequest.getPrice()));
					System.out.println("Payment successful");
					return new ResponseEntity<>(paymentRequest.getSuccessURI(), HttpStatus.OK);
				} else if (ret.getResult().equals("failure")) {
					saveTransacion(PaymentStatus.NOT_PAID, tr, paymentRequest);
					System.out.println("Payment unsuccessful - failed");
					return new ResponseEntity<>(paymentRequest.getFailureURI(), HttpStatus.BAD_REQUEST);
				} else {
					saveTransacion(PaymentStatus.ERROR, tr, paymentRequest);
					System.out.println("Payment unsuccessful - error");
					return new ResponseEntity<>(paymentRequest.getErrorURL(), HttpStatus.BAD_REQUEST);
				}
			} catch (Exception e) {
				saveTransacion(PaymentStatus.ERROR, transakcija, paymentRequest);
				System.out.println("Payment unsuccessful - error");
				return new ResponseEntity<>(paymentRequest.getErrorURL(), HttpStatus.BAD_REQUEST);
			}
		}

		// ista banka
		

		// raspolozvo stanje manje od amount
		if (platnaKartica.getRacun().getRaspolozivoStanje().compareTo(BigDecimal.valueOf(paymentRequest.getPrice())) == -1) {
			saveTransacion(PaymentStatus.NO_MONEY, transakcija, paymentRequest);
			System.out.println("Payment unsuccessful - failed");
			return new ResponseEntity<>(paymentRequest.getFailureURI(), HttpStatus.BAD_REQUEST);
		}
		if (paymentRequest.getStatus().equals(PaymentStatus.NOT_PAID)) {
			platnaKartica.getRacun().setRaspolozivoStanje(
					platnaKartica.getRacun().getRaspolozivoStanje().subtract(BigDecimal.valueOf(paymentRequest.getPrice())));
			racunService.update(platnaKartica.getRacun());
			transferMoneyToMerchant(prodavac.getRacuni().get(0), BigDecimal.valueOf(paymentRequest.getPrice()));
			paymentRequest.setStatus(PaymentStatus.PAID.toString());
			paymentService.save(paymentRequest);
			saveTransacion(PaymentStatus.PAID, transakcija, paymentRequest);
			System.out.println("Order paid");
			return new ResponseEntity<String>(paymentRequest.getSuccessURI(), HttpStatus.OK);
		} else {
			saveTransacion(PaymentStatus.PAID, transakcija, paymentRequest);
			System.out.println("Payment already paid");
			return new ResponseEntity<>(paymentRequest.getFailureURI(), HttpStatus.BAD_REQUEST);
		}
	}
  
  	//pcc salje zahtev banci kupca (kada banka kupca i banka prodavca nisu
	// iste)
	@RequestMapping(value = "/transactionProcessing", method = RequestMethod.POST)
	private ResponseEntity<?> transactionProcessing(@RequestBody TransakcijaResponseDTO transakcijaDTO) throws ParseException {
		System.out.println("Usao u transactionProcessing");	
		
		String panDTO = transakcijaDTO.getPan();
		String secCode = transakcijaDTO.getSecurityCode();
		String encrypted = encryptor.encrypt(encryptor.key, encryptor.initVector, panDTO);
		String encryptedSecCode = encryptor.encrypt(encryptor.key, encryptor.initVector, secCode);
		
		String date = new SimpleDateFormat("yyyy-MM-dd").format(transakcijaDTO.getExpirationDate());
		System.out.println("Pretraga: " +  encrypted + 
					", sec code: " + encryptedSecCode + 
					", name: "  + transakcijaDTO.getCardHolderName()
					+ "date: " + new SimpleDateFormat("yyyy-MM-dd").format(transakcijaDTO.getExpirationDate())
					+ "date final : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date.concat(" 00:00:00")));	
					
		
		PlatnaKartica platnaKartica = platnaKarticaService.find(encrypted,
				encryptedSecCode, transakcijaDTO.getCardHolderName(),
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date.concat(" 00:00:00")));
		System.out.println("transactionProcessing : nasao karticu");	
		
		
		if (platnaKartica != null) {
			// raspolozivo stanje manje od amount
			if (platnaKartica.getRacun().getRaspolozivoStanje().compareTo(transakcijaDTO.getAmount()) == -1) {
				transakcijaDTO.setResult("error");
				System.out.println("transactionProcessing : Amount on card is lower than transaction amount");
				return new ResponseEntity<TransakcijaResponseDTO>(transakcijaDTO, HttpStatus.BAD_REQUEST);
			}
			platnaKartica.getRacun().setRaspolozivoStanje(
					platnaKartica.getRacun().getRaspolozivoStanje().subtract(transakcijaDTO.getAmount()));
			racunService.update(platnaKartica.getRacun());
			System.out.println("transactionProcessing : Transaction paid");
			transakcijaDTO.setIssuerOrderId((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
			transakcijaDTO.setIssuerTimestamp(new Timestamp(System.currentTimeMillis()));
			transakcijaDTO.setResult("success");
			transakcijaService.save(transakcijaResponseDTOtoObject.convert(transakcijaDTO));
			return new ResponseEntity<TransakcijaResponseDTO>(transakcijaDTO, HttpStatus.OK);
		} else {
			transakcijaDTO.setResult("error");
			System.out.println("transactionProcessing : Wrong credit card data");
			transakcijaService.save(transakcijaResponseDTOtoObject.convert(transakcijaDTO));
			return new ResponseEntity<TransakcijaResponseDTO>(transakcijaDTO, HttpStatus.BAD_REQUEST);
		}
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


	
	private void transferMoneyToMerchant(Racun racun, BigDecimal amount) {
		racun.setRaspolozivoStanje(racun.getRaspolozivoStanje().add(amount));
		racunService.update(racun);
	}
	
	private void saveTransacion(PaymentStatus paid, Transakcija transakcija, Payment pr) {
		pr.setStatus(paid.toString());;
		paymentService.save(pr);
		transakcija.setPayment(pr);
		transakcijaService.save(transakcija);
	}
	
	private Payment mapValues(PaymentDTO paymentDTO) {
		Client c =  new Client();
		
		Payment p = new Payment();
		
		KlijentBanke kb = klijentBankeService.findMerchant(paymentDTO.getMerchantId(), paymentDTO.getMerchantPassword());
		
		if(kb != null) {
			
			c.setMerchantId(kb.getMerchantId());
			c.setMerchantPassword(kb.getMerchantPassword());
			p.setPrice(paymentDTO.getPrice());
			c.setMerchantOrderId(paymentDTO.getMerchantOrderId());
			c.setMerchantTimestamp(paymentDTO.getMerchantTimestamp());
			p.setSuccessURI(paymentDTO.getSuccessURI());
			p.setFailureURI(paymentDTO.getFailureURI());
			p.setErrorURL(paymentDTO.getErrorURL());
			p.setClient(c);
			p.setStatus(PaymentStatus.NOT_PAID.toString());
			p.setPaymentType(paymentDTO.getPaymentType());
			c.setTitle(kb.getNaziv());
		
			clientService.save(c);
			
			return p;
			
		} else {
			return null;
		}
		
	}
	
	private TransakcijaDTO sendRequestToPcc(TransakcijaDTO pccRequest) {
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TransakcijaDTO> entity = new HttpEntity<TransakcijaDTO>(pccRequest, headers);

		TransakcijaDTO ret = client.postForObject("http://localhost:9000/request/redirect", entity,
				TransakcijaDTO.class);
		return ret;
	}
}
