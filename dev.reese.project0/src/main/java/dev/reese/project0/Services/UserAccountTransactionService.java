package dev.reese.project0.Services;

import dev.reese.project0.Services.ServiceExceptions.NegativeAccountBalanceException;
import dev.reese.project0.Services.ServiceExceptions.NegativeAmountException;
import dev.reese.project0.daos.AccountDAO;
import dev.reese.project0.daos.DBImpAccountDAO;
import dev.reese.project0.daos.DBImpTransactionDAO;
import dev.reese.project0.daos.DBImpUserDAO;
import dev.reese.project0.daos.TransactionDAO;
import dev.reese.project0.daos.UserDAO;
import dev.reese.project0.entities.Account;
import dev.reese.project0.entities.Transaction;
import dev.reese.project0.entities.User;

public class UserAccountTransactionService {
	
	private User userWhoRequestedService;
	private User accountHolder;
	private Account account;
	private UserDAO userDAO = new DBImpUserDAO();
	private AccountDAO accountDB = new DBImpAccountDAO();
	private TransactionDAO transactionDB = new DBImpTransactionDAO();
	
	public UserAccountTransactionService(Account account, User userWhoRequestedService) {
		this.account = account;
		this.userWhoRequestedService = userWhoRequestedService;
		this.accountHolder = userWhoRequestedService; //////////temp fix only normal users get this service rn
	}
	
	public  UserAccountTransactionService(int accountID, User userWhoRequestedService) {
		this.account = accountDB.getAccount(accountID);
		this.userWhoRequestedService = userWhoRequestedService;
		this.accountHolder = userDAO.getUser(account.getAccountOwnerId());
	}
	
	public void refreshService() {
		this.account = accountDB.getAccount(this.account.getAccountId());
	}
	
	public boolean withdraw(double amount) throws NegativeAmountException, NegativeAccountBalanceException{
		if(amount <= 0.00)
			throw new NegativeAmountException("You must withdraw an amount greater than 0.00");
		double currentBalance = account.getBalance();
		boolean willOverDraw = (currentBalance - amount) < 0.00;
		if(!account.allowsNSFFee() && willOverDraw) 
			throw new NegativeAccountBalanceException("Account does not allow for NSF Fees so cannot have a negative balance");
		double totalAmount  =  (willOverDraw) ? (amount + 35.00) : amount;
		account.setBalance(currentBalance - totalAmount);
		boolean wasSuccessful = accountDB.updateAccount(account);
		if(wasSuccessful)
			this.recordTransaction("WITHDRAW", totalAmount);
		return wasSuccessful;
	}
	
	public boolean deposit(double amount) throws NegativeAmountException{
		if(amount <= 0.00)
			throw new NegativeAmountException("You must withdraw an amount greater than 0.00");
		double currentBalance = account.getBalance();
		this.account.setBalance(currentBalance + amount);
		boolean wasSuccessful = accountDB.updateAccount(account);
		if(wasSuccessful)
			this.recordTransaction("DEPOSIT", amount);
		return wasSuccessful;
	}
	
	public boolean turnNSFOffOrON() {
		boolean success = false;
		String type;
		this.account.setAllowsNSFFee((this.account.allowsNSFFee() ? false : true));
		if(this.account.allowsNSFFee()) {
			this.account.setAllowsNSFFee(false);
			type = "NOTALLOWNSF";
		}
		else {
			this.account.setAllowsNSFFee(true);
			type = "ALLOWINGNSF";
		}
		success = this.accountDB.updateAccount(this.account);
		if(success) {
			success = this.recordTransaction(type, 0.00);
		}
		return success;
	}
	
	private boolean recordTransaction(String type, double amount) {
		Transaction transaction = new Transaction();
		transaction.setAccount_num(this.account.getAccountId());
		transaction.setAmount(amount);
		transaction.setDone_by(this.userWhoRequestedService.getUserId());
		transaction.setTransaction_type(type);
		boolean wasSucessful = this.transactionDB.createTransaction(transaction);
		return wasSucessful;
	}

	public User getAccountHolder() {
		return accountHolder;
	}
	
	public Account getAccount(){
		return this.account;
	}

	
	
	
	

}
