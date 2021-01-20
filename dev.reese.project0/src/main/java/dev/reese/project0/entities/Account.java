package dev.reese.project0.entities;

public class Account {
	
	private int accountId;
	private int accountOwnerId;
	private String discription;
	private double balance;
	private boolean allowsNSFFee;
	
	
	
	public Account() {
		super();
	}
	
	

	public Account(int accountId, int accountOwnerId, String discription, double balance, boolean allowsNSFFee) {
		super();
		this.accountId = accountId;
		this.accountOwnerId = accountOwnerId;
		this.discription = discription;
		this.balance = balance;
		this.allowsNSFFee = allowsNSFFee;
	}
	
	
	public Account(int accountId) {
		this.accountId = accountId;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getAccountOwnerId() {
		return accountOwnerId;
	}
	public void setAccountOwnerId(int accountOwnerId) {
		this.accountOwnerId = accountOwnerId;
	}
	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}

	public boolean allowsNSFFee() {
		return allowsNSFFee;
	}


	public void setAllowsNSFFee(boolean allowsNSFFee) {
		this.allowsNSFFee = allowsNSFFee;
	}

	
	
	
	

}
