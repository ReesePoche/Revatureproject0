package dev.reese.project0.states;

import java.util.List;
import java.util.Scanner;

import dev.reese.project0.Services.SuperUserService;
import dev.reese.project0.Services.UserEditUserService;
import dev.reese.project0.Services.ServiceExceptions.AccountDoesNotExistException;
import dev.reese.project0.Services.ServiceExceptions.TransactionDoesNotExistException;
import dev.reese.project0.Services.ServiceExceptions.UserNameDoesNotExistException;
import dev.reese.project0.entities.Account;
import dev.reese.project0.entities.Transaction;
import dev.reese.project0.entities.User;

public class SuperUserMainMenuState implements AppState{
	
	private AppState parent;
	
	private SuperUserService service;
	
	public SuperUserMainMenuState(AppState parent, SuperUserService service) {
		this.parent = parent;
		this.service = service;
	}

	@Override
	public AppState handle() {
		this.service.refreshService();
		System.out.print(this.getMenuHeader());
		System.out.print(this.getMenuInstructions());
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		String word1 = null;
		String word2 = null;
		String word3 = null;
		String word4 = null;
		String word5 = null;
		String word6 = null;
		word1 = s.next().toUpperCase();
		if(s.hasNext())
			word2 = s.next().toUpperCase();
		if(s.hasNext())
			word3 = s.next();
		if(s.hasNext())
			word4 = s.next();
		if(s.hasNext())
			word5 = s.next();
		if(s.hasNext())
			word6 = s.next().toUpperCase();
		
		
		s.close();
		while(true) {
			if(word1.equals("BACK") || word1.equals("LOGOUT")) {
				return this.back();
			}
			else if(word1.equals("CREATE") && word2!=null) {
				if(word2.equals("USER"))
					return new CreatingUserState(this);
				else
					System.out.print("Must enter an option after the CREATE command(like USER)\nInvalid input\n");
			}
			else if(word1.equals("EDIT") && word2 != null) {
				
				if(word2.equals("USER") && word3 != null) {
					UserEditUserService editUserService = this.handleEditUser(word3, word4);
						if(editUserService != null)
							return new EditUserState(this, editUserService);
						else
							System.out.print("Invalid input\n");
				}
				else {
					System.out.print("Must enter an option after the EDIT command(like USER)\nInvalid input\n");
				}
			}
			else if(word1.equals("SHOW") && word2 != null && word3 != null) {
				if(word2.equals("USER")) {
					String result = this.handleShowUser(word3, word4);
					System.out.print(result);
				}
				else if (word2.equals("ACCOUNT")) {
					String result =this.handleShowAccount(word3);
					System.out.print(result);
				}
				else if (word2.equals("TRANSACTION")) {
					String result = this.handleShowTransaction(word3);
					System.out.print(result);
				}
				else if(word2.equals("ALL")) {
					word3 = word3.toUpperCase();
					if(word3.equals("USERS")) {
						String result = this.handleShowAllUsers();
						System.out.print(result);
					}
					else if(word3.equals("ACCOUNTS")) {
						String result = this.handleShowAllAccounts();
						System.out.print(result);
					}
					else if(word3.equals("TRANSACTIONS")) {
						String result = this.handleShowAllTransactions();
						System.out.print(result);
					}
					else if(word3.equals("ACCOUNT") && word4 != null && word4.equals("TRANSACTIONS")) {
						String result = this.handleShowAllAccountTransactions(word5);
						System.out.print(result);
					}
					else if(word3.equals("USER") && word4 != null && word5 != null) {
						word4 = word4.toUpperCase();
						if(word4.equals("ACCOUNTS")) {
							String result = this.handleShowAllUserAccounts(word5, word6);
							System.out.print(result);
						}
						else if(word4.equals("TRANSACTIONS")) {
							String result = this.handleShowAllUserTransactions(word5, word6);
							System.out.print(result);
						}
						else
							System.out.print("No valid options added after SHOW ALL USER\nInvalid input\n");
					}
				}
				else {
					System.out.print("No options added after SHOW\nINPUT ERROR\n");
				}
			}
			else {
				System.out.print("Invalid input\n");
			}
			word1 = null;
			word2 = null;
			word3 = null;
			word4 = null;
			word5 = null;
			word6 = null;
			System.out.print(this.getMenuInstructions());
			line = AppState.scanner.nextLine();
			s = new Scanner(line);
			word1 = s.next().toUpperCase();
			if(s.hasNext())
				word2 = s.next().toUpperCase();
			if(s.hasNext())
				word3 = s.next();
			if(s.hasNext())
				word4 = s.next();
			if(s.hasNext())
				word5 = s.next();
			if(s.hasNext())
				word6 = s.next().toUpperCase();
		}//end of while
	}//end of handle

	@Override
	public AppState back() {
		return this.parent;
	}
	
	
	private UserEditUserService handleEditUser(String word3, String word4) {
		UserEditUserService editService;
		if(word4 != null && word4.toUpperCase().equals("ID") && AppState.isInt(word3)) {
			int userId = Integer.parseInt(word3);
			try {
				editService = this.service.getUserEditService(userId);
			}
			catch(UserNameDoesNotExistException UNDNEE) {
				System.out.printf("The user id %d does not exist in our system\n", userId);
				editService = null;
			}
		}
		else {
			try {
				editService = this.service.getUserEditService(word3);
			}
			catch(UserNameDoesNotExistException UNDNEE) {
				System.out.printf("The username %s does not exist in our system\n", word3);
				editService = null;
			}
		}
		return editService;
	}
	
	
	private String handleShowUser(String word3, String word4) {
		User userToShow;
		if(word4 != null && word4.toUpperCase().equals("ID") && AppState.isInt(word3)) {
			int userID = Integer.parseInt(word3);
			try {
				userToShow = this.service.getUser(userID);
			}
			catch (UserNameDoesNotExistException UNDNEE) {
				System.out.printf("A user with that userId (%d) does not exist\n", userID);
				return "Input Error\n";
			}
		}
		else {
			try {
				userToShow = this.service.getUser(word3);
			}
			catch (UserNameDoesNotExistException UNDNEE) {
				System.out.printf("A user with that username (%s) does not exist\n", word3);
				return "Input Error\n";
			}
		}
		return String.format("SHOWING USER\n\tUserID:\t%d\n\tUsername:\t%s\n\tPassword:\t%s\n\tSuperUser?:\t%s", 
								userToShow.getUserId(), userToShow.getUserName(), userToShow.getPassword(), Boolean.toString(userToShow.isSuper()));
		
	}
	
	
	private String handleShowAllUsers() {
		List<User> users = this.service.getAllUsers();
		StringBuilder sb = new StringBuilder();
		sb.append("SHOWING ALL USERS\n");
		for(int i = 0 ; i < users.size(); i++) {
			User user = users.get(i);
			sb.append(String.format("============\n\tUserID:\t%d\n\tUsername:\t%s\n\tPassword:\t%s\n\tSuperUser?:\t%s\n", 
					user.getUserId(), user.getUserName(), user.getPassword(), Boolean.toString(user.isSuper())));
		}
		if(users.size() < 1)
			sb.append("\tNo users Exist\n");
		sb.append("============\n");
		sb.append("ALL USERS SHOWN\n");
		return sb.toString();
	}
	
	private String handleShowAccount(String word3) {
		if(!AppState.isInt(word3))
			return String.format("You did not enter a valid account ID %s is not a number\nInput error\n", word3);
		Account account;
		int accountNum = Integer.parseInt(word3);
		try {
			account = this.service.getAccount(accountNum);
		}
		catch(AccountDoesNotExistException ADNEE) {
			System.out.print("Account number entered does not exist in our system\n");
			return "Input Error\n";
		}
		return String.format("SHOWING ACCOUNT\n\tAccount#:\t%d\n\tOwnerUserID:\t %d\n\tdisciption:\t%s\n\tbalance:\t%.2f\n\tallowsNSF?:\t%s\n", 
				account.getAccountId(), account.getAccountOwnerId(), account.getDiscription(), account.getBalance(), Boolean.toString(account.allowsNSFFee()));
	}
	
	private String handleShowAllUserAccounts(String word5, String word6) {
		User user;
		List<Account> accounts;
		if(word6 != null && word6.equals("ID") && AppState.isInt(word5)) {
			int userID = Integer.parseInt(word5);
			try {
				user = this.service.getUser(userID);
				accounts = this.service.getUsersAccounts(user.getUserId());
			}
			catch (UserNameDoesNotExistException UNDNEE) {
				System.out.printf("SHOW ALL USER ACCOUNTS COMMAND FAILED\nA user with that userId (%d) does not exist\n", userID);
				return "Input Error\n";
			}
		}
		else {
			try {
				user = this.service.getUser(word5);
				accounts = this.service.getUsersAccounts(user.getUserId());
			}
			catch (UserNameDoesNotExistException UNDNEE) {
				System.out.printf("SHOW ALL USER ACCOUNTS COMMAND FAILED\nA user with that username (%s) does not exist\n", word5);
				return "Input Error\n";
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("SHOW ALL USER ACCOUNT\n");
		sb.append("For User: " + user.getUserName() + " , UserID: " + user.getUserId() + "\n");
		for(int i = 0 ; i < accounts.size(); i++) {
			Account account = accounts.get(i);
			sb.append(String.format("============\n\tAccount#:\t%d\n\tOwnerUserID:\t %d\n\tdisciption:\t%s\n\tbalance:\t%.2f\n\tallowsNSF?:\t%s\n",
								account.getAccountId(), account.getAccountOwnerId(), account.getDiscription(), account.getBalance(), Boolean.toString(account.allowsNSFFee())));
		}
		if(accounts.size() < 1)
			sb.append("\tNo open accounts Exist for this user\n");
		sb.append("============\n");
		sb.append("ALL USER ACCOUNTS SHOWN\n");
		return sb.toString();
	}
	
	private String handleShowAllAccounts() {
		List<Account> accounts = this.service.getAllAccounts();
		StringBuilder sb = new StringBuilder();
		sb.append("SHOW ALL ACCOUNT\n");
		for(int i = 0 ; i < accounts.size(); i++) {
			Account account = accounts.get(i);
			sb.append(String.format("============\n\tAccount#:\t%d\n\tOwnerUserID:\t %d\n\tdisciption:\t%s\n\tbalance:\t%.2f\n\tallowsNSF?:\t%s\n",
								account.getAccountId(), account.getAccountOwnerId(), account.getDiscription(), account.getBalance(), Boolean.toString(account.allowsNSFFee())));
		}
		if(accounts.size() < 1)
			sb.append("\tNo open accounts Exist\n");
		sb.append("============\n");
		sb.append("ALL ACCOUNTS SHOWN\n");
		return sb.toString();
		
	}
	
	
	
	private String handleShowTransaction(String word3) {
		if(!AppState.isInt(word3))
			return String.format("You did not enter a valid transaction number %s is not a number\nInput error\n", word3);
		Transaction t;
		int tnum = Integer.parseInt(word3);
		try {
			t = this.service.getTransaction(tnum);
		}
		catch(TransactionDoesNotExistException ADNEE) {
			System.out.printf("Transaction number entered(%d) does not exist in our system\n", tnum);
			return "Input Error\n";
		}
		return String.format("SHOW TRANSACTION\n============\n\tTransaction#:\t\t%d\n\tTransactorID:\t\t%d\n\tAccount#:\t\t%d\n\tTransactionType:\t%s\n\tAmount:\t\t\t%.2f\n", 
				t.getId(), t.getDone_by(), t.getAccount_num(), t.getTransaction_type(), t.getAmount());
	}
	
	private String handleShowAllAccountTransactions(String word5) {
		Account account;
		List<Transaction> transactions;
		if(word5 == null || !AppState.isInt(word5)) {
			System.out.print("SHOW ALL ACCOUNT TRANSACTIONS COMMAND failed\nNo account number entered\n");
			return "Invalid Input\n";
		}
		int accountID = Integer.parseInt(word5);
		try {
			account = this.service.getAccount(accountID);
			transactions = this.service.getAllAccountTransactions(accountID);
		}
		catch(AccountDoesNotExistException ADNEE) {
			System.out.print("SHOW ALL ACCOUNT TRANSACTIONS COMMAND failed\nAn account with that account Number does not exist in our system\n");
			return "Invalid Input\n";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("SHOW ALL ACCOUNT TRANSACTIONS\n");
		sb.append("For Account number: " + account.getAccountId());
		
		for(int i = 0 ; i < transactions.size(); i++) {
			Transaction t = transactions.get(i);
			sb.append(String.format("============\n\tTransaction#:\t\t%d\n\tTransactorID:\t\t%d\n\tAccount#:\t\t%d\n\tTransactionType:\t%s\n\tAmount:\t\t\t%.2f\n", 
					t.getId(), t.getDone_by(), t.getAccount_num(), t.getTransaction_type(), t.getAmount()));
		}
		if(transactions.size() < 1)
			sb.append("\tNo transactinos Exist for this account\n");
		sb.append("============\n");
		sb.append("ALL ACCOUNT TRANSACTIONS SHOWN\n");
		return sb.toString();
	}
	
	
	private String handleShowAllUserTransactions(String word5, String word6) {
		User user;
		List<Transaction> transactions;
		if(word6 != null && word6.equals("ID") && AppState.isInt(word5)) {
			int userID = Integer.parseInt(word5);
			try {
				user = this.service.getUser(userID);
				transactions = this.service.getAllUserTransactions(user.getUserId());
			}
			catch (UserNameDoesNotExistException UNDNEE) {
				System.out.printf("SHOW ALL USER TRANSACTIONS COMMAND failed \nA user with that userId (%d) does not exist\n", userID);
				return "Input Error\n";
			}
		}
		else {
			try {
				user = this.service.getUser(word5);
				transactions = this.service.getAllUserTransactions(user.getUserId());
			}
			catch (UserNameDoesNotExistException UNDNEE) {
				System.out.printf("SHOW ALL USER TRANSACTIONS COMMAND FAILED\nA user with that username (%s) does not exist\n", word5);
				return "Input Error\n";
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("SHOW ALL USER TRANSACTIONS\n");
		sb.append("For User: " + user.getUserName() + " , UserID: " + user.getUserId() + "\n");
		for(int i = 0 ; i < transactions.size(); i++) {
			Transaction t = transactions.get(i);
			sb.append(String.format("============\n\tTransaction#:\t\t%d\n\tTransactorID:\t\t%d\n\tAccount#:\t\t%d\n\tTransactionType:\t%s\n\tAmount:\t\t\t%.2f\n", 
					t.getId(), t.getDone_by(), t.getAccount_num(), t.getTransaction_type(), t.getAmount()));
		}
		if(transactions.size() < 1)
			sb.append("\tNo transactinos have been made by this user\n");
		sb.append("============\n");
		sb.append("ALL TRANSACTIONS SHOWN\n");
		return sb.toString();
	}
	
	private String handleShowAllTransactions() {
		List<Transaction> transactions = this.service.getAllTransactions();
		StringBuilder sb = new StringBuilder();
		sb.append("SHOW ALL TRANSACTIONS\n");
		for(int i = 0 ; i < transactions.size(); i++) {
			Transaction t = transactions.get(i);
			sb.append(String.format("============\n\tTransaction#:\t\t%d\n\tTransactorID:\t\t%d\n\tAccount#:\t\t%d\n\tTransactionType:\t%s\n\tAmount:\t\t\t%.2f\n", 
					t.getId(), t.getDone_by(), t.getAccount_num(), t.getTransaction_type(), t.getAmount()));
		}
		if(transactions.size() < 1)
			sb.append("\tNo transactinos Exist\n");
		sb.append("============\n");
		sb.append("ALL TRANSACTIONS SHOWN\n");
		return sb.toString();
	}
	
	
	private String getMenuInstructions() {
		StringBuilder sb = new StringBuilder();
		sb.append("ENTER CREATE USER to create a user\n");
		sb.append("ENTER EDIT USER [userId] ID to edit the user\n");
		sb.append("ENTER EDIT USER [USERNAME] to edit the user\n");
		//show Commands
			// show user commands
		sb.append("ENTER SHOW ALL USERS to view all user info\n");
		sb.append("ENTER SHOW USER [USERiD] ID to view the user info\n");
		sb.append("ENTER SHOW USER [USERNAME] to view the user info\n");
		
			//show account commands
		sb.append("ENTER SHOW ALL ACCOUNTS to view all account info\n");
		sb.append("ENTER SHOW ACCOUNT [ACCOUNT#] to view an account\n");
		sb.append("ENTER SHOW ALL USER ACCOUNTS [username] to view all accounts for that user\n");
		sb.append("ENTER SHOW ALL USER ACCOUNTS [userID] ID to view all accounts for that user\n");
		
			//show transaction commands
		sb.append("ENTER SHOW ALL TRANSACTIONS to view all transactions\n");
		sb.append("ENTER SHOW TRANSACTION [transactionID] to view that transactions\n");
		sb.append("ENTER SHOW ALL ACCOUNT TRANSACTIONS [ACCOUNT#] to view all transactions for that account\n");
		sb.append("ENTER SHOW ALL USER TRANSACTIONS [USERID] ID to view all transactions made by that user\n");
		sb.append("ENTER SHOW ALL USER TRANSACTIONS [USERNAME] to view all transactions made by that user\n");
		sb.append("ENTER BACK or LOGOUT to logout and return to the main menu\n");
		return sb.toString();
	}
	
	
	private String getMenuHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("///////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("///////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append(String.format("Welcome %s to your Super user account menu page\n", this.service.getUser().getUserName()));
		return sb.toString();
	}

}
