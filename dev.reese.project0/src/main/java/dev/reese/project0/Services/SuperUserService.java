package dev.reese.project0.Services;

import java.util.List;

import dev.reese.project0.Services.ServiceExceptions.AccountDoesNotExistException;
import dev.reese.project0.Services.ServiceExceptions.InvalidPasswordException;
import dev.reese.project0.Services.ServiceExceptions.InvalidUserNameException;
import dev.reese.project0.Services.ServiceExceptions.PasswordIncorrectException;
import dev.reese.project0.Services.ServiceExceptions.TransactionDoesNotExistException;
import dev.reese.project0.Services.ServiceExceptions.UserNameDoesNotExistException;
import dev.reese.project0.Services.ServiceExceptions.UserNameTakenException;
import dev.reese.project0.daos.AccountDAO;
import dev.reese.project0.daos.DBImpAccountDAO;
import dev.reese.project0.daos.DBImpTransactionDAO;
import dev.reese.project0.daos.DBImpUserDAO;
import dev.reese.project0.daos.TransactionDAO;
import dev.reese.project0.daos.UserDAO;
import dev.reese.project0.entities.Account;
import dev.reese.project0.entities.Transaction;
import dev.reese.project0.entities.User;

public class SuperUserService {
	
	private User user;
	
	private static AccountDAO accountDAO = new DBImpAccountDAO();
	
	private static UserDAO userDAO = new DBImpUserDAO();
	
	private static TransactionDAO transactionDAO = new DBImpTransactionDAO();

	public SuperUserService(User user) {
		super();
		this.user = user;
	}
	
	public User getUser(){
		return this.user;
	}
	
	public void refreshService() {
		this.user = userDAO.getUser(this.user.getUserId());
	}
	
	public List<User> getAllUsers(){
		return userDAO.getAllUsers();
	}
	
	public User getUser(int userID) throws UserNameDoesNotExistException{
		User user =  userDAO.getUser(userID);
		if(user.getUserId() == 0)
			throw new UserNameDoesNotExistException("User Does not Exist");
		return user;
	}
	
	public User getUser(String userName) throws UserNameDoesNotExistException {
		User user = userDAO.getUser(userName);
		if(user.getUserId() == 0)
			throw new UserNameDoesNotExistException("the username " + userName + " does not exist in our system");
		return user;
	}
	
	
	public Account getAccount(int accountNum) throws AccountDoesNotExistException{
		Account account =  accountDAO.getAccount(accountNum);
		if(account.getAccountId() == 0)
			throw new AccountDoesNotExistException("Account number entered Does not exist");
		return account;
	}
	
	public List<Account> getUsersAccounts(int userID) throws UserNameDoesNotExistException{
		User user =  userDAO.getUser(userID);
		if(user.getUserId() == 0)
			throw new UserNameDoesNotExistException("User Does not Exist");
		return accountDAO.getUsersAccounts(userID);
	}
	
	public List<Account> getAllAccounts(){
		return accountDAO.getAllAccounts();
	}
	
	public Transaction getTransaction(int transactionId) throws TransactionDoesNotExistException{
		Transaction transaction = transactionDAO.getTransaction(transactionId);
		if(transaction.getId() == 0)
			throw new TransactionDoesNotExistException("transaction id entered does not exist");
		return transaction;
	}
	
	public List<Transaction> getAllTransactions(){
		return transactionDAO.getAllTransactions();
	}
	
	public List<Transaction> getAllAccountTransactions(int accountNumber) throws AccountDoesNotExistException{
		Account account =  accountDAO.getAccount(accountNumber);
		if(account.getAccountId() == 0)
			throw new AccountDoesNotExistException("Account number entered Does not exist");
		return transactionDAO.getAccountTransactions(accountNumber);
	}
	
	public List<Transaction> getAllUserTransactions(int userID) throws UserNameDoesNotExistException{
		User user =  userDAO.getUser(userID);
		if(user.getUserId() == 0)
			throw new UserNameDoesNotExistException("User Does not Exist");
		return transactionDAO.getUserTransactions(userID);
	}
	
	
	public UserCreationService getUserCreationService() {
		return new UserCreationService();
	}
	
	public UserEditUserService getUserEditService(int userToBeEditedId) throws UserNameDoesNotExistException{
		User userToBeEdited = userDAO.getUser(userToBeEditedId);
		if(userToBeEdited.getUserId() == 0)
			throw new UserNameDoesNotExistException("User with that ID does not exist");
		return new UserEditUserService(userToBeEdited, this.user);
	}
	
	public UserEditUserService getUserEditService(String usernameOfUserToBeEdited) throws UserNameDoesNotExistException{
		User userToBeEdited = userDAO.getUser(usernameOfUserToBeEdited);
		if(userToBeEdited.getUserId() == 0)
			throw new UserNameDoesNotExistException("User with that ID does not exist");
		return new UserEditUserService(userToBeEdited, this.user);
	}

	//TODO add account functionality and save data on accounts taht have been close or user deleted. 
	
	
	
	

}
