package dev.lbell.models;

import java.util.Date;

public class Transactions {
	private int id, accountId;
	private double amount;
	private String status;
	
	public Transactions(int id, int accountId, double amount, String status) {
		this.id = id;
		this.accountId = accountId;
		this.amount = amount;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

	
	
}
