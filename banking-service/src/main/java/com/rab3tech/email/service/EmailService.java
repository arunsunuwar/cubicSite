package com.rab3tech.email.service;

import com.rab3tech.vo.EmailVO;

public interface EmailService {

	String sendEquiryEmail(EmailVO mail)  ;

	String sendRegistrationEmail(EmailVO mail);

	String sendUsernamePasswordEmail(EmailVO mail);

	String sendCardGenerationConfirmation(String customerName, String customerEmail, byte[] front, byte[] back);

	String sendURNemail(String email, Integer urnEmail);

}

