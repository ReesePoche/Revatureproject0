package dev.reese.project0.entities;

public class Transaction {
	
	private int id;
	private int done_by;
	private int account_num;
	private String transaction_type;
	private double amount;
	
	public Transaction() {
		
	}
	
	public Transaction(int id) {
		this.id = id;
	}
	
	
	public Transaction(int id, int done_by, int account_num, String transaction_type, double amount) {
		super();
		this.id = id;
		this.done_by = done_by;
		this.account_num = account_num;
		this.transaction_type = transaction_type;
		this.amount = amount;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDone_by() {
		return done_by;
	}
	public void setDone_by(int done_by) {
		this.done_by = done_by;
	}
	public int getAccount_num() {
		return account_num;
	}
	public void setAccount_num(int account_num) {
		this.account_num = account_num;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	

}
