package com.rab3tech.customer.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.admin.dao.repository.AccountStatusRepository;
import com.rab3tech.admin.dao.repository.AccountTypeRepository;
import com.rab3tech.admin.dao.repository.MagicCustomerRepository;
import com.rab3tech.customer.dao.repository.CreditCardRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountApprovedRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountEnquiryRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.FundTransferRepository;
import com.rab3tech.customer.dao.repository.PayeeRepository;
import com.rab3tech.customer.dao.repository.RoleRepository;
import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.dao.entity.AccountStatus;
import com.rab3tech.dao.entity.AccountType;
import com.rab3tech.dao.entity.CreditCard;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.CustomerSaving;
import com.rab3tech.dao.entity.CustomerSavingApproved;
import com.rab3tech.dao.entity.FundTransfer;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.dao.entity.PayeeStatus;
import com.rab3tech.dao.entity.Role;
import com.rab3tech.email.service.EmailService;
import com.rab3tech.mapper.CustomerMapper;
import com.rab3tech.utils.AccountStatusEnum;
import com.rab3tech.utils.PasswordGenerator;
import com.rab3tech.utils.Utils;
import com.rab3tech.vo.AccountTypeVO;
import com.rab3tech.vo.CreditCardVO;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerUpdateVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.FundTransferVO;
import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.RoleVO;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private MagicCustomerRepository customerRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AccountStatusRepository accountStatusRepository;

	@Autowired
	private AccountTypeRepository accountTypeRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private CustomerAccountEnquiryRepository customerAccountEnquiryRepository;

	@Autowired
	private CustomerAccountApprovedRepository customerAccountApprovedRepository;

	@Autowired
	private CustomerAccountInfoRepository customerAccountInfoRepository;
	
	@Autowired
	private CustomerRepository CustomerRepository;
	
	@Autowired
	private PayeeRepository payeeRepository;
	
	@Autowired 
	private CreditCardRepository creditCardRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FundTransferRepository fundTransferRepository;

	private CustomerAccountInfoVO createBankAccount(int csaid,String email) {
		// logic
				String customerAccount = Utils.generateCustomerAccount();
				CustomerSaving customerSaving = customerAccountEnquiryRepository.findById(csaid).get();

				CustomerAccountInfo customerAccountInfo = new CustomerAccountInfo();
				customerAccountInfo.setAccountNumber(customerAccount);
				customerAccountInfo.setAccountType(customerSaving.getAccType());
				customerAccountInfo.setAvBalance(1000.0F);
				customerAccountInfo.setBranch(customerSaving.getLocation());
				customerAccountInfo.setCurrency("$");
				Customer customer = customerRepository.findByEmail(email==null?customerSaving.getEmail():email).get();
				customerAccountInfo.setCustomerId(customer.getLogin());
				customerAccountInfo.setStatusAsOf(new Date());
				customerAccountInfo.setTavBalance(1000.0F);
				CustomerAccountInfo customerAccountInfo2 = customerAccountInfoRepository.save(customerAccountInfo);

				CustomerSavingApproved customerSavingApproved = new CustomerSavingApproved();
				BeanUtils.copyProperties(customerSaving, customerSavingApproved);
				customerSavingApproved.setAccType(customerSaving.getAccType());
				customerSavingApproved.setStatus(customerSaving.getStatus());
				// saving entity into customer_saving_enquiry_approved_tbl
				customerAccountApprovedRepository.save(customerSavingApproved);

				// delete data from
				customerAccountEnquiryRepository.delete(customerSaving);

				CustomerAccountInfoVO accountInfoVO = new CustomerAccountInfoVO();
				BeanUtils.copyProperties(customerAccountInfo2, accountInfoVO);
				return accountInfoVO;
	}

	@Override
	public CustomerAccountInfoVO createBankAccount(int csaid) {
		return createBankAccount(csaid,null);
	}
		

	@Override
	public CustomerVO createAccount(CustomerVO customerVO) {

		Customer pcustomer = new Customer();
		BeanUtils.copyProperties(customerVO, pcustomer);
		Login login = new Login();
		login.setNoOfAttempt(3);
		login.setLoginid(customerVO.getEmail());
		login.setName(customerVO.getName());
		String genPassword = PasswordGenerator.generateRandomPassword(8);
		customerVO.setPassword(genPassword);
		login.setPassword(bCryptPasswordEncoder.encode(genPassword));
		login.setToken(customerVO.getToken());
		login.setLocked("no");

		Role entity = roleRepository.findById(3).get();
		Set<Role> roles = new HashSet<>();
		roles.add(entity);
		// setting roles inside login
		login.setRoles(roles);
		// setting login inside
		pcustomer.setLogin(login);
		Customer dcustomer = customerRepository.save(pcustomer);
		customerVO.setId(dcustomer.getId());
		customerVO.setUserid(customerVO.getUserid());

		Optional<CustomerSaving> optional = customerAccountEnquiryRepository.findByEmail(dcustomer.getEmail());
		if (optional.isPresent()) {
			CustomerSaving customerSaving = optional.get();
			AccountStatus accountStatus = accountStatusRepository.findByCode(AccountStatusEnum.REGISTERED.getCode())
					.get();
			customerSaving.setStatus(accountStatus);
		}
		//Do you know database!!!
		this.createBankAccount(dcustomer.getId(),pcustomer.getEmail());
		
		return customerVO;
	}


	@Override
	public List<CustomerVO> findCustomers() {
		List<Customer> customers = customerRepository.findAll();
		/*
		 * List<CustomerVO> customerVOs=new ArrayList<CustomerVO>(); 
		 * for(Customer customer:customers) {
		 *  CustomerVO customerVO=CustomerMapper.toVO(customer);
		 * customerVOs.add(customerVO); } return customerVOs;
		 */
		return customers.stream(). //Stream<Customer>
		map(CustomerMapper::toVO).//Stream<CustomerVO>
		collect(Collectors.toList()); //List<CustomerVO>
	}
	
	@Override
	public void updateProfile(CustomerUpdateVO customerVO) {
		//I have loaded entity inside persistence context - >>Session
		Customer customer=customerRepository.findById(customerVO.getCid()).get();
		try {
			customer.setImage(customerVO.getPhoto().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		customer.setName(customerVO.getName());
		customer.setMobile(customerVO.getMobile());
		customer.setDom(new Timestamp(new Date().getTime()));
		///customerRepository.save(customer);
	}
	
	
	@Override
	public byte[] findPhotoByid(int cid) {
		Optional<Customer> optionalCustomer=customerRepository.findById(cid);
		if(optionalCustomer.isPresent()) {
			return optionalCustomer.get().getImage();
		}else {
			return null;
		}
		
	}
	
   @Override
	public List<RoleVO> getRoles(){
		
		List<Role> roles = roleRepository.findAll();
		List<RoleVO> rolesVO = new ArrayList<RoleVO>(); 
		for(Role role : roles) {
			System.out.println("MY ROLE========"+role.toString());
			RoleVO roleVO = new RoleVO();
			BeanUtils.copyProperties(role, roleVO);
			rolesVO.add(roleVO);
		}
		
		return rolesVO;
	}
	
   @Override
   public String findCustomerByEmail(String email) {
	   Optional<CustomerSaving> customer = customerAccountEnquiryRepository.findByEmail(email);  
	   String result = "";
	   if(customer.isPresent()) {
		   result = "fail";
	   }
	   else result="success";
	   
	   return result;
   }
   
   @Override
   public String findCustomerByMobile(String mobile) {
	   Optional<CustomerSaving> customer = customerAccountEnquiryRepository.findByMobile(mobile);  
	   String result = "";
	   if(customer.isPresent()) {
		   result = "fail";
	   }
	   else result="success";
	   
	   return result;
   }
   
   @Override
   public  CustomerVO searchCustomer(String searchKey){
	   Optional<Customer> customer = CustomerRepository.findByName(searchKey.trim());
	   CustomerVO customerVO = null;
	   if(customer.isPresent()) {
		   customerVO = new CustomerVO();
		   customerVO.setId(customer.get().getId());
		   customerVO.setName(customer.get().getName().trim());
		   customerVO.setEmail(customer.get().getEmail());
		   customerVO.setAddress(customer.get().getAddress());
		   customerVO.setMobile(customer.get().getMobile());
		   customerVO.setImage(customer.get().getImage());
		   
	   }
	   
	   return customerVO;
   }

      @Override
    public List<AccountTypeVO> findAccountTypes() {
	     List<AccountType> accounts = accountTypeRepository.findAll();
	     List<AccountTypeVO> accountsVO =  new ArrayList<AccountTypeVO>();
	     for(AccountType account : accounts) {
		    AccountTypeVO accountVO = new AccountTypeVO();
		    BeanUtils.copyProperties(account, accountVO);
		     accountsVO.add(accountVO);
	      }
    	return accountsVO;
      }
 
		@Override
		public void addPayee(PayeeInfoVO payeeInfoVO) {
			/*
			 * PayeeStatus payeeStatus = new PayeeStatus(); payeeStatus.setId(1); PayeeInfo
			 * payeeInfo = new PayeeInfo();
			 * payeeInfo.setPayeeAccountNo(payeeInfoVO.getPayeeAccountNo());
			 * payeeInfo.setPayeeName(payeeInfoVO.getPayeeName());
			 * payeeInfo.setPayeeNickName(payeeInfoVO.getPayeeNickName());
			 * payeeInfo.setDom(payeeInfoVO.getDom());
			 * payeeInfo.setDoe(payeeInfoVO.getDoe());
			 * payeeInfo.setUrn(PasswordGenerator.getURN());
			 * payeeInfo.setRemarks(payeeInfoVO.getRemarks());
			 * BeanUtils.copyProperties(payeeInfoVO, payeeInfo); payeeInfo.setDoe(new
			 * Timestamp(new Date().getTime())); payeeRepository.save(payeeInfo);
			 */
			PayeeStatus payeeStatus = new PayeeStatus();
	    	 payeeStatus.setId(1);
	    	 PayeeInfo payeeInfo = new PayeeInfo();
	    	 payeeInfo.setPayeeStatus(payeeStatus);
	    	 String email = payeeInfoVO.getCustomerId();
	    	 int urn = PasswordGenerator.getURN();
	    	 payeeInfoVO.setUrn(urn);
			 BeanUtils.copyProperties(payeeInfoVO, payeeInfo);
			 payeeInfo.setDoe(new Timestamp(new Date().getTime()));
			 payeeInfo.setDom(new Timestamp(new Date().getTime()));
				/* int i = ((Integer) payeeInfoVO.getUrn()).intValue(); */
			 int i = payeeInfoVO.getUrn();
			 payeeRepository.save(payeeInfo);
			 emailService.sendURNemail(email,i);
			 
		} 
     
		
		/*
		 * @Override public List<PayeeInfoVO> pendingPayeeList(){
		 * 
		 * List<PayeeInfo> payeeInfoList = payeeRepository.findAll(); List<PayeeInfoVO>
		 * payeeInfoVOList = new ArrayList<PayeeInfoVO>(); for(PayeeInfo pi :
		 * payeeInfoList) { PayeeInfoVO piVO = new PayeeInfoVO();
		 * piVO.setPayeeStatus(pi.getPayeeStatus().getName());
		 * BeanUtils.copyProperties(pi, piVO); payeeInfoVOList.add(piVO); }
		 * 
		 * return payeeInfoVOList; }
		 */
	 
	 @Override
	 public List<PayeeInfoVO> pendingPayeeList(String loginid){
		   
		   List<PayeeInfo> payeeInfoList =  payeeRepository.findPendingPayee(loginid);
		   List<PayeeInfoVO> payeeInfoVOList = new ArrayList<PayeeInfoVO>();
		   for(PayeeInfo pi : payeeInfoList) {
			    PayeeInfoVO piVO = new PayeeInfoVO();
			    piVO.setPayeeStatus(pi.getPayeeStatus().getName());
			    BeanUtils.copyProperties(pi, piVO);
			    payeeInfoVOList.add(piVO);
		   }
		   
		   return payeeInfoVOList;
	   }
	 

	 @Override
	 public List<PayeeInfoVO> registeredPayeeList(String customerId){
		   
		   List<PayeeInfo> payeeInfoList =  payeeRepository.findApprovedPayee(customerId);
		   List<PayeeInfoVO> payeeInfoVOList = new ArrayList<PayeeInfoVO>();
		   for(PayeeInfo pi : payeeInfoList) {
			    PayeeInfoVO piVO = new PayeeInfoVO();
			    piVO.setPayeeStatus(pi.getPayeeStatus().getName());
			    BeanUtils.copyProperties(pi, piVO);
			    payeeInfoVOList.add(piVO);
		   }
		   
		   return payeeInfoVOList;
	   }
	 
		
		/*
		 * @Override public CustomerAccountInfoVO findCustomerAccountInfo(String
		 * customerId) { CustomerAccountInfo customerAccountInfo =
		 * customerAccountInfoRepository.customerInfoById(customerId);
		 * CustomerAccountInfoVO accountInfoVO = new CustomerAccountInfoVO();
		 * BeanUtils.copyProperties(customerAccountInfo, accountInfoVO);
		 * accountInfoVO.setName(customerAccountInfo.getCustomerId().getName());
		 * accountInfoVO.setAcccountType(customerAccountInfo.getAccountType().getName())
		 * ; return accountInfoVO; }
		 */
		 
		
		@Override
		public List<CustomerAccountInfoVO> findCustomerAccountInfo(String customerId) {

			List<CustomerAccountInfo> customerAccountInfoList = customerAccountInfoRepository.customerInfoById(customerId);
			List<CustomerAccountInfoVO> customerAccountVOList = new ArrayList<CustomerAccountInfoVO>();
			for (CustomerAccountInfo c : customerAccountInfoList) {
				CustomerAccountInfoVO cVO = new CustomerAccountInfoVO();
				BeanUtils.copyProperties(c, cVO);
				cVO.setName(c.getCustomerId().getName());
				cVO.setAcccountType(c.getAccountType().getName());
				customerAccountVOList.add(cVO);
			}

			return customerAccountVOList;
		}
		 
	 
		/*
		 * @Override public CustomerVO searchCustomerInfo(String searchText) {
		 * CustomerVO customervo = null; Optional<Customer> optional =
		 * CustomerRepository.searchCustomerInfo(searchText); if (optional.isPresent())
		 * { Customer customer = optional.get(); customervo = new CustomerVO();
		 * BeanUtils.copyProperties(customer, customervo, new String[] { "id", "userid"
		 * }); customervo.setUserid(customer.getLogin().getName());
		 * customervo.setId(customer.getId()); } return customervo;
		 * 
		 * }
		 */
		 
	 
		
		@Override
		public List<CustomerVO> searchCustomerInfo(String searchText) {
			List<CustomerVO> customervo = new ArrayList<CustomerVO>();
			List<Customer> customer = CustomerRepository.searchCustomerInfo(searchText);
			for (Customer c : customer) {
				CustomerVO cVO = new CustomerVO();
				BeanUtils.copyProperties(c, cVO, new String[] { "id", "userid" });
				cVO.setUserid(c.getLogin().getName());
				cVO.setId(c.getId());
				customervo.add(cVO);
			}
			return customervo;
		}
		
		@Override
		public byte[] cardFront(String email) {
			return creditCardRepository.findByEmail(email).get().getFront();
		}
	
		@Override
		public byte[] cardBack(String email) {
			return creditCardRepository.findByEmail(email).get().getBack();
		}
		
		@Override
		public byte[] findPhotoByEmail(String email) {
			return CustomerRepository.findByEmail(email).get().getImage();
			//return profileDao.findPhotoByUsername(pusername);
		}
		
		@Override
		public List<CustomerVO> allCustomers(){
			List<CustomerVO> customervo = new ArrayList<CustomerVO>();
			List<Customer> customer = CustomerRepository.findAll();
			for (Customer c : customer) {
				CustomerVO cVO = new CustomerVO();
				BeanUtils.copyProperties(c, cVO);
				customervo.add(cVO);
			}
				return customervo;
		}
		/*
		 * public CreditCardVO getall(String email) { CreditCard card =
		 * creditCardRepository.getAllByEmail(email); CreditCardVO cvo = new
		 * CreditCardVO(); cvo.setCardNumber(card.getCardNumber());
		 * cvo.setCreditcardNumber(card.getCreditcardNumber());
		 * cvo.setExpiryDate(card.getExpiryDate()); cvo.setName(card.getName()); return
		 * cvo; }
		 */
		
		@Override
		public  byte[] generateFrontCreditCard(String name, String number, String expireDate) {
			//String base64Img = null;
			byte[] photo = new byte[]{};
			Resource resource = new ClassPathResource("images/credit-card-front-template.jpg");
			
			try {
				InputStream resourceInputStream = resource.getInputStream();
				
				Image src = ImageIO.read(resourceInputStream);
				int wideth = src.getWidth(null);
				int height = src.getHeight(null);
				BufferedImage tag = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = tag.createGraphics();
				g.setBackground(new Color(200, 250, 200));
				g.clearRect(0, 0, wideth, height);
				g.setColor(Color.WHITE);
				g.drawImage(src, 0, 0, wideth, height, null);
				
				// credit card number
				g.setFont(new Font("Lucida Console", Font.BOLD, 36));
				g.drawString(number.substring(0, 4), 40, 207);
				g.drawString(number.substring(4, 8), 150, 207);
				g.drawString(number.substring(8, 12), 260, 207);
				g.drawString(number.substring(12, 16), 370, 207);
				
				// exp date
				g.setFont(new Font("Lucida Console", Font.PLAIN, 24));
				g.drawString(expireDate, 65, 250);
				// customer name
				g.setFont(new Font("Tahoma", Font.PLAIN, 28));
				g.drawString(name.toUpperCase(), 30, 290);
				
				//cardType
				g.setFont(new Font("Lucida Console",Font.ITALIC,20));
				g.drawString("VISA", 120, 20);
				//load new image
				Resource simage = new ClassPathResource("images/logo.png");
				InputStream simageInputStream = simage.getInputStream();
				Image img = ImageIO.read(simageInputStream);
				
				//Draw image on given card
				g.drawImage(img, 304, 255, 91, 45, null);
				g.dispose();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(tag, "jpg", baos);
				baos.flush();
				photo= baos.toByteArray();
				/*byte[] encodedBytes = Base64.getEncoder().encode(baos.toByteArray());
				base64Img = new String(encodedBytes);*/
			} catch (IOException e) {
				e.printStackTrace();
			}
			return photo;
		}
		
		@Override
		public byte[] generateBackCreditCard(String ccv) {
			//String base64Img = null;
			byte[] photo= new byte[]{};
			Resource resource = new ClassPathResource("images/credit-card-back-template.jpg");
			
			try {
				InputStream resourceInputStream = resource.getInputStream();
				
				Image src = ImageIO.read(resourceInputStream);
				int wideth = src.getWidth(null);
				int height = src.getHeight(null);
				BufferedImage tag = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = tag.createGraphics();
				g.setBackground(new Color(200, 250, 200));
				g.clearRect(0, 0, wideth, height);
				g.setColor(Color.BLACK);
				g.drawImage(src, 0, 0, wideth, height, null);
				
				g.setFont(new Font("Lucida Console", Font.BOLD, 18));
				g.drawString(ccv, 360, 135);
				g.dispose();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(tag, "jpg", baos);
				baos.flush();
				
				photo= baos.toByteArray();
				/*byte[] encodedBytes = Base64.getEncoder().encode(baos.toByteArray());
				base64Img = new String(encodedBytes);*/
			} catch (IOException e) {
				e.printStackTrace();
			}
			return photo;
		}
		
		@Override
		public CreditCardVO creatCreditCard(String name, String email) {
			CreditCardVO creditCardVO = new CreditCardVO(); 
			CreditCard creditCard = new CreditCard();
			String expiryDate = PasswordGenerator.generateCreditCardExpireDate();
			String ccv = PasswordGenerator.generateCCVNumber();
			String number = PasswordGenerator.generateCreditCardNumber();
			byte[] front = generateFrontCreditCard(name, number, expiryDate);
			byte[] back = generateBackCreditCard(ccv);
			creditCardVO.setName(name);
			creditCardVO.setEmail(email);
			creditCardVO.setCreditcardNumber(number);
			creditCardVO.setExpiryDate(expiryDate);
			creditCardVO.setCcv(ccv);
			creditCardVO.setFront(front);
			creditCardVO.setBack(back);
			BeanUtils.copyProperties(creditCardVO, creditCard);
			creditCardVO.setCardNumber(creditCard.getCardNumber());
			creditCardRepository.save(creditCard);
			emailService.sendCardGenerationConfirmation(name, email, front, back);
			return creditCardVO;
		}
		
		@Transactional
		@Override
		public void rejectPayee(int cid){
			payeeRepository.rejectPayee(cid);
		}

		@Transactional
		@Override
		public String approvePayee(PayeeInfo payeeInfoVO) {
			 Optional<PayeeInfo> optional=payeeRepository.findByUrnAndId(payeeInfoVO.getUrn(), payeeInfoVO.getId());
	         if(optional.isPresent()){		 
	        	 PayeeInfo payeeInfo=optional.get();
	        	 PayeeStatus payeeStatus=new PayeeStatus();
	        	 payeeStatus.setId(2);
	        	 payeeInfo.setPayeeStatus(payeeStatus);
	        	 return "approved";	 
	         }else{
	           return "notapproved"; 	 
	         }

		}
		
		@Override
		public Optional<CustomerAccountInfo> customerBalance(String email) {
			Optional<CustomerAccountInfo> optional=customerAccountInfoRepository.findByEmail(email);
			return optional;
		}

		@Override
		public void addFundTransfer(@Valid FundTransferVO fundTransferVO) {
			FundTransfer fundTransfer = new FundTransfer();
			fundTransfer.setOtp(PasswordGenerator.getOPT());
			fundTransfer.setSentFrom(fundTransferVO.getFromAccount());
			fundTransfer.setSentTo(fundTransferVO.getToAccount());
			fundTransfer.setTransactionDate(new Timestamp(new Date().getTime()));
			BeanUtils.copyProperties(fundTransferVO, fundTransfer);
			fundTransferRepository.save(fundTransfer);
		}
		
		@Override
		public byte[] findPhotoByAC(String accountNumber) {
			CustomerAccountInfo customerAccountInfo=customerAccountInfoRepository.findByAccountNumber(accountNumber).get();
			Customer customer=customerRepository.findByEmail(customerAccountInfo.getCustomerId().getLoginid()).get();
			return customer.getImage(); 

		}
		
		/*
		 * @Transactional
		 * 
		 * @Override public void approvePayee(int cid){
		 * payeeRepository.updateToApproved(cid);; }
		 */
}

