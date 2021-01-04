package com.rab3tech.customer.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rab3tech.customer.dao.repository.CreditCardRepository;
import com.rab3tech.customer.dao.repository.PayeeRepository;
import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.dao.entity.CreditCard;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.utils.PasswordGenerator;
import com.rab3tech.vo.ApplicationResponseVO;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.FromToAccountsVO;
import com.rab3tech.vo.LoginVO;
import com.rab3tech.vo.PayeeInfoVO;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v3")
public class CustomerSearchController {
	
	@Autowired
	private CreditCardRepository creditCardRepository;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private PayeeRepository payeeRepository;
	
	

	
	@GetMapping("/customer/customerSearch")
	public List<CustomerVO> searchCustomer(@RequestParam String searchText) {
		List<CustomerVO> customer = customerService.searchCustomerInfo(searchText);
		return customer;
	}
	
	@GetMapping("/customer/customerAllSearch")
	public List<CustomerVO> searchAllCustomer() {
		List<CustomerVO> customer = customerService.allCustomers();
		return customer;
	}
	
	@GetMapping("/load/photoSearchFrontCard")
	public byte[] frontCard(@RequestParam String email) {
		return customerService.cardFront(email);
	}
	
	@GetMapping("/load/photoSearchBackCard")
	public byte[] backCard(@RequestParam String email) {
		return customerService.cardBack(email);

	}
	
	@GetMapping("/search/creditCardInfo")
	public Optional<CreditCard> creditCardInfo(@RequestParam String email){
		Optional<CreditCard> credit = creditCardRepository.findByEmail(email);
		return credit;
	}
	
	@GetMapping("/search/searchURN")
	public Optional<PayeeInfo> urnNumberById(@RequestParam Integer cid) {
		return payeeRepository.findById(cid);
	}
	
	/*
	 * @GetMapping("/search/updatePendingPayeeToApproved") public void
	 * updateApprovedById(@RequestParam Integer cid) {
	 * customerService.approvePayee(cid); }
	 */
	
	@PostMapping("/search/updatePendingPayeeToApproved")
	public ApplicationResponseVO updateApprovedById(@RequestBody PayeeInfo payeeInfoVO) {
		String response=customerService.approvePayee(payeeInfoVO);
		ApplicationResponseVO applicationResponseVO=new ApplicationResponseVO();
		if("approved".equalsIgnoreCase(response)){
			applicationResponseVO.setMessage("Yeap , it has been approved!!!!!!!!!!!");
			applicationResponseVO.setStatus("pass");
		}else{
			applicationResponseVO.setStatus("fail");
			applicationResponseVO.setMessage("Sorry , Your urn is not correct , please check it again!");
		}
		return applicationResponseVO;
	}
	
	@GetMapping("/search/rejectPayee")
	public void rejectPendingPayeeById(@RequestParam Integer cid) {
		customerService.rejectPayee(cid);
	}
	
	@GetMapping("/customer/from-to-accounts")
	public FromToAccountsVO getCustomerFromToAccounts(@RequestParam("loginid") String loginid) {
		List<CustomerAccountInfoVO> customerAccountInfoVO = customerService.findCustomerAccountInfo(loginid);
		List<String> fromAccount=new ArrayList<String>();
		for(CustomerAccountInfoVO c:customerAccountInfoVO){
			fromAccount.add(c.getAccountNumber() +"-"+ c.getAcccountType()+ "-"+ c.getName());
		}
		List<PayeeInfoVO> payeeInfoVOs=customerService.registeredPayeeList(loginid);
		List<String> toAccounts=new ArrayList<String>();
		for(PayeeInfoVO payeeInfoVO:payeeInfoVOs){
			toAccounts.add(payeeInfoVO.getPayeeAccountNo()+" - "+payeeInfoVO.getPayeeName());
		}
		
		
		FromToAccountsVO fromToAccountsVO=new FromToAccountsVO();
		fromToAccountsVO.setToAccounts(toAccounts);
		/* fromToAccountsVO.setFromAccount("0172625625252 - Saving - Arun Sunuwar"); */
		fromToAccountsVO.setFromAccount(fromAccount);	
		return fromToAccountsVO;
	}
	
	@GetMapping("/customer/getAccountBalance")
	public Optional<CustomerAccountInfo> getCustomerBalance(@RequestParam String username) {	
		return customerService.customerBalance(username) ;
	}
	
	@GetMapping("/customer/getNewOtp")
	public int getNewOtp() {	
		int newOtp=PasswordGenerator.getOPT();
		return newOtp;
	}
	
	
	
}
