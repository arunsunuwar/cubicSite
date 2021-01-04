package com.rab3tech.customer.ui.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rab3tech.email.service.EmailService;

@Controller
//@RequestMapping("/customer1")
public class CreditCardController {

	@Autowired
	private EmailService emailService;
	
	/*
	 * @PostMapping("/customer/cardGenerator") public String
	 * generateCard(@RequestParam("customerEmail") String
	 * customerEmail,@RequestParam("customerName") String customerName) throws
	 * IOException { //emailService.sendCardGenerationConfirmation(customerEmail,
	 * customerName); return "customer/customerSearch"; }
	 */
}
