package dev.reese.project0.states;

import java.util.Scanner;

import dev.reese.project0.Services.UserAccountCreationAndClosingService;
import dev.reese.project0.Services.UserCreationService;
import dev.reese.project0.Services.ServiceExceptions.NegativeAccountBalanceException;
import dev.reese.project0.Services.ServiceExceptions.StartingAccountWithNegativeBalanceException;
import dev.reese.project0.entities.Account;
import dev.reese.project0.entities.User;

public class AccountCreationState implements AppState{
	
	private AppState parent;
	
	private UserAccountCreationAndClosingService service;
	
	
	
	public AccountCreationState(AppState parent, UserAccountCreationAndClosingService service) {
		this.parent = parent;
		this.service = service;
	}

	@Override
	public AppState handle() {
		System.out.print(this.getAccountCreationHeader());
		if(!AppState.answerYesOrNo("Continue with account creation?"))
			return this.back();
		while(true) {
			boolean creationSuccessful = this.handleAccountCreation();
			if(creationSuccessful) {
				System.out.print("Account was successfully created\nThe account can be seen in the accounts screen\n");
				if(!AppState.answerYesOrNo("Would you like to create another Account?(ENTER NO or BACK to go to the user main menu screen)\n"))
					return this.back();
			}
			else {
				System.out.print("Account creation failed\n");
				if(!AppState.answerYesOrNo("Would you like to try to create an account again?(ENTER NO or BACK to go to the main menu)"))
					return this.back();
			}
		}
			
	}

	@Override
	public AppState back() {
		return this.parent;
	}
	
	private boolean handleAccountCreation() {
		System.out.print("ENTER a description of the account(type description then press ENTER)\n");
		String description = AppState.scanner.nextLine();
		if(description.equals(""))
			description = "No Description given";
		double initalBalance = this.getStartingBalanceFromUser();
		boolean allowNSF = AppState.answerYesOrNo("Do you want to allow this account to be overdrawn and allow NSF Fees*?\n\t*An NSF Fee of $35.00 will apply to each transaction once account balance is below $0.00");
		boolean sucess;
		if(!AppState.answerYesOrNo("Are you sure you want to create this account?"))
			return false;
		try {
			sucess = this.service.createNewAccount(description, initalBalance, allowNSF);
		}
		catch(NegativeAccountBalanceException UNTE) {
			System.out.print("You cannot open an account with a negative balance\n" );
			sucess = false;
		}
		return sucess;
	}
	
	
	private double getStartingBalanceFromUser() {
		if(!AppState.answerYesOrNo("Do you want give this account an inital balance?"))
			return 0.00;
		System.out.print("Enter starting balance amount(enter only numbers can include decimal ex: 56.34)\n");
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		String input = s.next();
		s.close();
		while(true) {
			if(AppState.isDouble(input)) {
				return Double.parseDouble(input);
			}
			else {
				System.out.print("invalid input.\n");
			}
			System.out.print("Enter starting balance amount(enter only numbers can include decimal ex: 56.34)\n");
			line = AppState.scanner.nextLine();
			s = new Scanner(line);
			input = s.next();
			s.close();
		}
	}
	
	
	private String getAccountCreationHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("Account Creation Menu\nENTER BACK to return to your Accounts menu\n");
		return sb.toString();
	}

}
