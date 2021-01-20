package dev.reese.project0.Services;

import dev.reese.project0.Services.ServiceExceptions.AccountDoesNotExistException;
import dev.reese.project0.Services.ServiceExceptions.NegativeAccountBalanceException;
import dev.reese.project0.Services.ServiceExceptions.NotAccountOwnerException;
import dev.reese.project0.daos.AccountDAO;
import dev.reese.project0.daos.DBImpAccountDAO;
import dev.reese.project0.entities.Account;
import dev.reese.project0.entities.User;

public class UserAccountCreationAndClosingService {
	
	
	private User userWhoCalledTheService;
	private User userWhoTheAccountsAreFor;
	
	
	private static AccountDAO db = new DBImpAccountDAO();
	
	public UserAccountCreationAndClosingService(User userWhoCalledTheService, User userWhoTheAccountsAreFor) {
		this.userWhoCalledTheService = userWhoCalledTheService;
		this.userWhoTheAccountsAreFor = userWhoTheAccountsAreFor;
	}
	
	public boolean createNewAccount(String description, double initalBalance, boolean allowNSF) throws NegativeAccountBalanceException {
		if(initalBalance < 0.00)
			throw new NegativeAccountBalanceException("User cannot start an account with a negative balance");
		Account newAccount = new Account();
		newAccount.setAccountOwnerId(userWhoTheAccountsAreFor.getUserId());
		newAccount.setDiscription(description);
		newAccount.setBalance(initalBalance);
		newAccount.setAllowsNSFFee(allowNSF);
		return db.createAccount(newAccount);
	}
	
	public boolean closeAccount(int accountId) throws NotAccountOwnerException, NegativeAccountBalanceException, AccountDoesNotExistException{
		Account account = db.getAccount(accountId);
		if(account.getAccountId() == 0)
			throw new AccountDoesNotExistException("an account with that account number does not exist");
		if(!userWhoCalledTheService.isSuper()) {
		if(userWhoCalledTheService.getUserId() != account.getAccountOwnerId())
			throw new NotAccountOwnerException("User does not own account or privledges on the account");
		if(account.getBalance() != 0.00)
			throw new NegativeAccountBalanceException("Only accounts with a 0 balance are allowed to be closed");
		}
		return db.deleteAccount(accountId);
		
	}

}
