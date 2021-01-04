package com.rab3tech.vo;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PayeeInfoVO {

	private int id;
	
	@NotNull
	@Size(min =6, max=15)
	private String payeeAccountNo;
	
	@NotNull
	@Size(min=4, max=10)
	private String payeeName;
	
	@NotNull
	private String payeeNickName;
	
	private String customerId;
	private Timestamp doe;
	private Timestamp dom;
	private String accNumberConfirm;
	
	@NotNull
	@Size(min=4, max=12)
	private String remarks;
	
	private String payeeStatus;
	private int urn;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPayeeAccountNo() {
		return payeeAccountNo;
	}
	public void setPayeeAccountNo(String payeeAccountNo) {
		this.payeeAccountNo = payeeAccountNo;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayeeNickName() {
		return payeeNickName;
	}
	public void setPayeeNickName(String payeeNickName) {
		this.payeeNickName = payeeNickName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public Timestamp getDoe() {
		return doe;
	}
	public void setDoe(Timestamp doe) {
		this.doe = doe;
	}
	public Timestamp getDom() {
		return dom;
	}
	public void setDom(Timestamp dom) {
		this.dom = dom;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getPayeeStatus() {
		return payeeStatus;
	}
	public void setPayeeStatus(String payeeStatus) {
		this.payeeStatus = payeeStatus;
	}
	public int getUrn() {
		return urn;
	}
	public void setUrn(int urn) {
		this.urn = urn;
	}
	public String getAccNumberConfirm() {
		return accNumberConfirm;
	}
	public void setAccNumberConfirm(String accNumberConfirm) {
		this.accNumberConfirm = accNumberConfirm;
	}
	@Override
	public String toString() {
		return "PayeeInfoVO [id=" + id + ", payeeAccountNo=" + payeeAccountNo + ", payeeName=" + payeeName
				+ ", payeeNickName=" + payeeNickName + ", customerId=" + customerId + ", doe=" + doe + ", dom=" + dom
				+ ", accNumberConfirm=" + accNumberConfirm + ", remarks=" + remarks + ", payeeStatus=" + payeeStatus
				+ ", urn=" + urn + "]";
	}
	
}
