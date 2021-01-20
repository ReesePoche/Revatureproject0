package dev.reese.project0.states;

import java.util.List;
import java.util.Scanner;

import dev.reese.project0.Services.NormalUserService;
import dev.reese.project0.Services.UserAccountTransactionService;
import dev.reese.project0.Services.ServiceExceptions.AccountDoesNotExistException;
import dev.reese.project0.Services.ServiceExceptions.DeletingAccountWithBalanceException;
import dev.reese.project0.Services.ServiceExceptions.NotAccountOwnerException;

import dev.reese.project0.entities.Account;
import dev.reese.project0.entities.User;

public class NormalUserMainMenuState implements AppState{
	
	private AppState parent;
	
	
	
	private NormalUserService service;
	
	public NormalUserMainMenuState(AppState parent, NormalUserService service) {
		this.parent = parent;
		this.service = service;
	}

	@Override
	public AppState handle() {
		this.service.refreshService();
		System.out.print(this.getWelcomeUserHeader());
		System.out.print(this.getInstructionsString());
		String word1, word2, word3;
		word1 = word2 = word3 = null;
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		word1 = s.next().toUpperCase();
		if(s.hasNext())
			word2 = s.next().toUpperCase();
		if(s.hasNext())
			word3 = s.next().toUpperCase();
		s.close();
		while(true) {
			if(word1.equals("BACK") || word1.equals("LOGOUT")) {
				return this.parent;
			}
			else if(word1.equals("CREATE")) {
				return new AccountCreationState(this, service.getAccountCreationAndClosingService());
			}
			else if(word1.equals("CLOSE")) {
				return new AccountClosingState(this, service.getAccountCreationAndClosingService());
			}
			else if(word1.equals("SETTINGS")) {
				return new EditUserState(this, this.service.getUserEditUserService());
			}
			else if(word1.equals("SELECT")) {
				if(word2 != null && AppState.isInt(word2)) {
					int accountNum = Integer.parseInt(word2);
					UserAccountTransactionService UATS = this.tryGetAccountTransactionServices(accountNum);
					if(UATS == null) 
						System.out.print("invalid input\nThe account number entered after SELECT command was not valid\n");
					else 
						return new AccountViewState(this, UATS);
				}
				else {
					System.out.print("invalid input\nAn account number was not found after the command SELECT\n");
				}
			}
			
			else if(word1.equals("SHOW") && word2 != null) {
				if(word2.equals("ALL")) {
					if(word3 != null && word3.equals("INFO"))
						System.out.print(this.getAccountsInfoString());
					else
						System.out.print(this.getAccountNums());
				}
				else if(AppState.isInt(word2)) {
					int accountNum = Integer.parseInt(word2);
					System.out.print(this.getAccountString(accountNum));
				}
				else {
					System.out.print("invalid input\nAn account number nor ALL was found after the command SHOW\n");
				}
			}
			else {
				System.out.print("invalid input\n");
			}
			System.out.print(this.getInstructionsString());
			word1 = word2 = word3 = null;
			line = AppState.scanner.nextLine();
			s = new Scanner(line);
			word1 = s.next().toUpperCase();
			if(s.hasNext())
				word2 = s.next().toUpperCase();
			if(s.hasNext())
				word3 = s.next().toUpperCase();
			s.close();
		}
	}
		
	@Override
	public AppState back() {
		return this.parent;
	}
	
	
	private UserAccountTransactionService tryGetAccountTransactionServices(int accountId) {
		try {
			UserAccountTransactionService s = this.service.getTransactionServicesForAccount(accountId);
			return s;
		}
		catch(AccountDoesNotExistException ADNEE) {
			System.out.print("The account number entered does not exist\n");
			return null;
		}
		catch(NotAccountOwnerException NAOE) {
			System.out.print("Sorry, you do not have access to that account\n");
			return null;
		}
	}

	
	
	
	private String getWelcomeUserHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("Normal user Accounts menu\n");
		sb.append("Welcome " + this.service.getUser().getUserName() + " \n");
		return sb.toString();
	}
	
	private String getInstructionsString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ENTER SHOW ALL to see all your accounts' Numbers\n");
		sb.append("ENTER SHOW ALL INFO to see all your accounts' info\n");
		sb.append("ENTER SHOW (ACCOUNT#) to see that accounts info\n");
		sb.append("ENTER SELECT (ACCOUNT#) to select an account\n");
		sb.append("ENTER CREATE to create an account\n");
		sb.append("ENTER CLOSE to close an account (Account must have a 0 balance in order to close)\n");
		sb.append("ENTER SETTINGS to edit username or password\n");
		sb.append("ENTER BACK or LOGOUT to logout\n");
		return sb.toString();
	}
	
	
	
	private String getAccountNums() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.service.getUser().getUserName());
		sb.append("'s Current Open Accounts Numbers:\n");
		List<Account> accounts = service.getAccounts();
		for(int i = 0; i < accounts.size(); i++) {
			int accountNum  = accounts.get(i).getAccountId();
			sb.append(accountNum);
			sb.append("\n");
		}
		if(accounts.size() == 0)
			sb.append("\tYou currently have no open accounts\n");
		sb.append("\tSHOW COMPLETED\n");
		return sb.toString();
	}
	
	private String getAccountsInfoString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.service.getUser().getUserName());
		sb.append("'s Current Open Accounts:\n");
		List<Account> accounts = service.getAccounts();
		for(int i = 0; i < accounts.size(); i++) {
			Account account = accounts.get(i);
			sb.append("Account Number: ");
			sb.append(account.getAccountId());
			sb.append("\n");
			sb.append("\tBalance: ");
			sb.append(String.format("$%.2f%n", account.getBalance()));
			sb.append("\tDescription: ");
			sb.append(account.getDiscription());
			sb.append("\n");
			sb.append("\t");
			sb.append((account.allowsNSFFee() ? "This account Allows OverDraws/NSF fees\n" : "This Account does not allow for OverDraws/NSF fees\n"));
		}
		if(accounts.size() == 0)
			sb.append("\tYou currently have no open accounts\n");
		sb.append("\tSHOW COMPLETED\n");
		return sb.toString();
	}
	
	private String getAccountString(int accountNum)  {
		Account account = null;
		try {
			account = this.service.getAccount(accountNum);
		}
		catch(AccountDoesNotExistException ADNEE) {
			System.out.print("The account number entered does not exist\n");
			return "Account does not exist\n\tSHOW COMPLETED\n";
		}
		catch(NotAccountOwnerException NAOE) {
			System.out.print("Sorry, you do not have access to that account\n");
			return "Access denied\n\tSHOW COMPLETED\n";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Account Number: ");
		sb.append(account.getAccountId());
		sb.append("\n");
		sb.append("\tBalance: ");
		sb.append(String.format("$%.2f%n", account.getBalance()));
		sb.append("\tDescription: ");
		sb.append(account.getDiscription());
		sb.append("\n");
		sb.append("\t");
		sb.append((account.allowsNSFFee() ? "This account Allows OverDraws/NSF fees\n" : "This Account does not allow for OverDraws/NSF fees\n"));
		sb.append("\tSHOW COMPLETED\n");
		return sb.toString();
	}
	
	

}
