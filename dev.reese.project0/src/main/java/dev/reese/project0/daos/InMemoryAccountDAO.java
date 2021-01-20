package dev.reese.project0.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import dev.reese.project0.entities.Account;
import dev.reese.project0.entities.User;

public class InMemoryAccountDAO implements AccountDAO{
	
	private static HashMap<Integer, Account> accounts = null;
	private static int counter;
	
	public static void initIMADB(){
		if(InMemoryAccountDAO.accounts == null) {
			counter = 0; 
			accounts = new HashMap<Integer, Account>();
		}
	}
	
	public InMemoryAccountDAO() {
		initIMADB();
	}
	
//	public boolean createAccount(int accountOwner, String Description, double initalBalance, boolean allowsNSF) {
//		Account newAccount = new Account(++counter, accountOwner, Description, initalBalance, allowsNSF);
//		accounts.put(newAccount.getAccountId(), newAccount);
//		return true;
//	}

	public List<Account> getUsersAccounts(int userId) {
		Object[] allKeys = accounts.keySet().toArray();
		List<Account> userAccounts = new ArrayList<Account>();
		for(int i = 0; i < allKeys.length; i++) {
			String intString = allKeys[i].toString();
			int keyIntValue = Integer.parseInt(intString);
			Account curAccount = accounts.get(keyIntValue);
			if(curAccount.getAccountOwnerId() == userId) {
				userAccounts.add(curAccount);
			}
		}
		return userAccounts;
	}
	
	/**
	 * @return returns an account with an Id of 0 if account with ID does not exist
	 */
	public Account getAccount(int accountId) {
		Account account = accounts.get(accountId);
		if(account == null)
			return new Account(0);
		return accounts.get(accountId);
	}

	
	public Account withdraw(int accountId, double amount) {
		Account account = accounts.get(accountId);
		double oldBalance = account.getBalance();
		account.setBalance(oldBalance - amount);
		return account;
	}

	public Account deposit(int accountId, double amount) {
		Account account = accounts.get(accountId);
		double oldBalance = account.getBalance();
		account.setBalance(oldBalance + amount);
		return account;
	}

	public boolean deleteAccount(int accountId) {
		Account account = accounts.get(accountId);
		accounts.remove(accountId);
		return false;
	}

	
	public boolean createAccount(int accountOwner, String Description, double initalBalance, boolean allowsNSF) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Account> getAllAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateAccount(Account account) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createAccount(Account account) {
		// TODO Auto-generated method stub
		return false;
	}

	
	

}
