package com.rab3tech.vo;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FundTransferVO {
	
	private String fromAccount;
	
	private String toAccount;
	
	@NotNull
	@Size(min=4, max=12, message="digit min 4 and max 12")
	private String remarks;
	
	@NotNull(message="Min 1 to Max 5000") //message is not reading
	@Min(value = 0L, message = "The value must be positive")
	@DecimalMin("1.00") 
	@DecimalMax("5000.00") 
	private double amount;
	
	private int otp;

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
		this.otp = otp;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "FundTransferVO [fromAccount=" + fromAccount + ", toAccount=" + toAccount + ", remarks=" + remarks
				+ ", amount=" + amount + "]";
	}

}