package com.rab3tech.customer.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.vo.AccountTypeVO;
import com.rab3tech.vo.CreditCardVO;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerUpdateVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.FundTransferVO;
import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.RoleVO;

public interface CustomerService {

	CustomerVO createAccount(CustomerVO customerVO);

	CustomerAccountInfoVO createBankAccount(int csaid);

	List<CustomerVO> findCustomers();

	byte[] findPhotoByid(int cid);

	void updateProfile(CustomerUpdateVO customerVO);
	
	public List<RoleVO> getRoles();

	CustomerVO searchCustomer(String name);
	
	List<AccountTypeVO> findAccountTypes();

	String findCustomerByEmail(String email);

	String findCustomerByMobile(String mobile);

	void addPayee(PayeeInfoVO payeeInfoVO);

	List<PayeeInfoVO> pendingPayeeList(String loginid);

	List<PayeeInfoVO> registeredPayeeList(String customerId);

	List<CustomerVO> searchCustomerInfo(String searchText);

	byte[] findPhotoByEmail(String email);

	List<CustomerVO> allCustomers();

	CreditCardVO creatCreditCard(String name, String email);

	byte[] generateFrontCreditCard(String name, String number, String expireDate);

	byte[] generateBackCreditCard(String ccv);

	byte[] cardBack(String email);

	byte[] cardFront(String email);

	void rejectPayee(int cid);

	String approvePayee(PayeeInfo payeeInfoVO);

	List<CustomerAccountInfoVO> findCustomerAccountInfo(String customerId);

	Optional<CustomerAccountInfo> customerBalance(String email);

	void addFundTransfer(@Valid FundTransferVO fundTransferVO);

	byte[] findPhotoByAC(String accountNumber);
}
