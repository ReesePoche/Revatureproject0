package dev.reese.project0.daos;

import java.util.List;

//import java.awt.List;

import dev.reese.project0.entities.Account;

public interface AccountDAO {
	
	
	boolean createAccount(Account account);
	
	
	List<Account> getUsersAccounts(int userId);
	
	List<Account> getAllAccounts();
	
	Account getAccount(int accountId);
	
	boolean updateAccount(Account changedAccount);
	
	boolean deleteAccount(int accountId);
	
	

}
