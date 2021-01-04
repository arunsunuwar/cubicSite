package com.rab3tech.vo;

public class CreditCardVO {	

	private long cardNumber;
	
	private String name;
	private String email;
	private String creditcardNumber;
	private String expiryDate;
	private String ccv;
	private byte[] front;
	private byte[] back;
	
	
	public long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCreditcardNumber() {
		return creditcardNumber;
	}
	public void setCreditcardNumber(String creditcardNumber) {
		this.creditcardNumber = creditcardNumber;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	
	public String getCcv() {
		return ccv;
	}
	public void setCcv(String ccv) {
		this.ccv = ccv;
	}
	
	public byte[] getFront() {
		return front;
	}
	public void setFront(byte[] front) {
		this.front = front;
	}
	public byte[] getBack() {
		return back;
	}
	public void setBack(byte[] back) {
		this.back = back;
	}
	@Override
	public String toString() {
		return "CreditCardVO [cardNumber=" + cardNumber + ", name=" + name + ", email=" + email + ", creditcardNumber="
				+ creditcardNumber + ", expiryDate=" + expiryDate + ", ccv=" + ccv + ", front=" + front + ", back="
				+ back + "]";
	}

	
}
