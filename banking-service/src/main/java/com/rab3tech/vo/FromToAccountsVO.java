package com.rab3tech.vo;

import java.util.List;

public class FromToAccountsVO {

	private List<String> fromAccount;
	private List<String> toAccounts;

	public List<String> getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(List<String> fromAccount2) {
		this.fromAccount = fromAccount2;
	}

	public List<String> getToAccounts() {
		return toAccounts;
	}

	public void setToAccounts(List<String> toAccounts) {
		this.toAccounts = toAccounts;
	}

	@Override
	public String toString() {
		return "FromToAccountsVO [fromAccount=" + fromAccount + ", toAccounts=" + toAccounts + "]";
	}

}