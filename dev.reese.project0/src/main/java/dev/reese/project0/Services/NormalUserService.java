package dev.reese.project0.Services;

import java.util.List;

import dev.reese.project0.Services.ServiceExceptions.AccountDoesNotExistException;
import dev.reese.project0.Services.ServiceExceptions.NegativeAccountBalanceException;
import dev.reese.project0.Services.ServiceExceptions.NegativeAmountException;
import dev.reese.project0.Services.ServiceExceptions.NotAccountOwnerException;
import dev.reese.project0.daos.AccountDAO;
import dev.reese.project0.daos.DBImpAccountDAO;
import dev.reese.project0.daos.DBImpUserDAO;
import dev.reese.project0.entities.Account;
import dev.reese.project0.entities.User;

public class NormalUserService {
	
	private User user;
	private static AccountDAO db = new DBImpAccountDAO();
	
	
	public NormalUserService(User user) {
		this.user = user;
	}
	
	
	
	public User getUser() {
		return this.user;
	}
	
	public List<Account> getAccounts(){
		return db.getUsersAccounts(user.getUserId());
	}
	
	public Account getAccount(int accountId) throws AccountDoesNotExistException, NotAccountOwnerException{
		Account account = db.getAccount(accountId);
		if(account.getAccountId() == 0)
			throw new AccountDoesNotExistException("an account with the account number " + accountId + " does not exist in our system\n");
		if(account.getAccountOwnerId() != this.user.getUserId())
			throw new NotAccountOwnerException("User does not own and therefore does not have access to this account");
		return account;
	}
	
	public void refreshService() {
		DBImpUserDAO temp = new DBImpUserDAO();
		this.user = temp.getUser(this.user.getUserId());
	}
	

	
	public UserAccountCreationAndClosingService getAccountCreationAndClosingService() {
		return new UserAccountCreationAndClosingService(this.user, this.user);
	}
	
	public UserAccountTransactionService getTransactionServicesForAccount(int accountId) throws NotAccountOwnerException, AccountDoesNotExistException {
		Account account = db.getAccount(accountId);
		if(account.getAccountId() == 0)
			throw new AccountDoesNotExistException("An account with that account number does not exist");
		if(account.getAccountOwnerId() != user.getUserId())
			throw new NotAccountOwnerException("User does not access to this account");
		return new UserAccountTransactionService(account, this.user);
	}
	
	public boolean transferMoneyBetweenOwnedAccounts(int fromAccountNum, int toAccountNum, double amount) throws NotAccountOwnerException, NegativeAccountBalanceException, AccountDoesNotExistException, NegativeAmountException {
		if(amount <= 0.00)
			throw new NegativeAmountException("A negative amount cannot be transfered between accounts");
		
		Account fromAccount = db.getAccount(fromAccountNum);
		if(fromAccount.getAccountId() == 0)
			throw new AccountDoesNotExistException("The account you want to send money from does not exist\n");
		if(fromAccount.getAccountOwnerId() != user.getUserId())
			throw new NotAccountOwnerException("User does not access to the account you wish to send money from");
		if(fromAccount.getBalance() < amount)
			throw new NegativeAccountBalanceException("From Account has insufficent funds to do the transfer. NSF not allowed for a transfer.");
		
		Account toAccount = db.getAccount(toAccountNum);
		if(toAccount.getAccountId() == 0)
			throw new AccountDoesNotExistException("The account you want to send money to does not exist\n");
		if(toAccount.getAccountOwnerId() != user.getUserId())
			throw new NotAccountOwnerException("User does not access to the account you wish to send money to");
		
		UserAccountTransactionService fromAccountService = new UserAccountTransactionService(fromAccount, this.user);
		fromAccountService.withdraw(amount);
		
		UserAccountTransactionService toAccountService = new UserAccountTransactionService(toAccount, this.user);
		toAccountService.deposit(amount);
		
		return true;
	}

	public UserEditUserService getUserEditUserService() {
		return new UserEditUserService(this.user, this.user);
	}
	

}
