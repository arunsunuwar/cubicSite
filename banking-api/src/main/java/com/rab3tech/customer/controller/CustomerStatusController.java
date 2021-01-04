package com.rab3tech.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rab3tech.customer.service.impl.CustomerEnquiryService;
import com.rab3tech.vo.CustomerSavingVO;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v3")
public class CustomerStatusController {

	@Autowired
	private CustomerEnquiryService customerEnquiryService;

	@GetMapping("/customer/app/status")
	public CustomerSavingVO findCustomerStatus(@RequestParam String searchText){
		CustomerSavingVO customerSavingVO =customerEnquiryService.findAppStatus(searchText);
		return customerSavingVO;
	}

}