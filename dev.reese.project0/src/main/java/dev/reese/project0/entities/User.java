package dev.reese.project0.entities;


public class User {
	
	private int userId;
	private String userName;
	private boolean isSuper;
	private String password;
	//private ArrayList<Account> accounts;
	
	public User() {
		super();
	}
	
	public User(int userId) {
		this.userId = userId;
	}
	
	
	public User(int userId, String userName, boolean isSuper, String password) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.isSuper = isSuper;
		this.password = password;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public boolean isSuper() {
		return isSuper;
	}
	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}
	
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return this.password;
	}
//	public ArrayList<Account> getAccounts() {
//		return accounts;
//	}
//	public void setAccounts(ArrayList<Account> accounts) {
//		this.accounts = accounts;
//	}
	
	
	

}
