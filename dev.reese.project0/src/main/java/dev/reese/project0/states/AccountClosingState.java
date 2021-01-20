package dev.reese.project0.states;

import java.util.Scanner;

import dev.reese.project0.Services.UserAccountCreationAndClosingService;
import dev.reese.project0.Services.ServiceExceptions.AccountDoesNotExistException;
import dev.reese.project0.Services.ServiceExceptions.NegativeAccountBalanceException;
import dev.reese.project0.Services.ServiceExceptions.NotAccountOwnerException;

public class AccountClosingState implements AppState{
	
	private AppState parent;
	
	private UserAccountCreationAndClosingService service;
	
	public AccountClosingState(AppState parent, UserAccountCreationAndClosingService service) {
		this.parent = parent;
		this.service = service;
	}

	@Override
	public AppState handle() {
		System.out.print(this.getAccountClosingHeader());
		if(!AppState.answerYesOrNo("Continue to delete an account?"))
			return this.back();
		System.out.print(this.getInstructions());
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		String word1 = s.next().toUpperCase();
		String word2 = null;
		if(s.hasNext())
			word2 = s.next().toUpperCase();
		s.close();
		while(true) {
			if(word1.equals("BACK"))
				return this.back();
			if(word1.equals("CLOSE")) {
				if(word2 == null || !(AppState.isInt(word2))) {
					System.out.print("Did not enter an account number after CLOSE\n");
				}
				else {
					int accountToCloseId = Integer.parseInt(word2);
					boolean sucessfulClosing = this.handleAccountClosing(accountToCloseId);
					if(sucessfulClosing) {
						System.out.print("Account number " + accountToCloseId + " was successfully closed\n");
						if(!AppState.answerYesOrNo("Close another Account?"))
							return this.back();
					}
					else {
						System.out.print("Attempt to close account was unsucessful\n");
						if(!AppState.answerYesOrNo("Want to attempt to close an account again?"))
							return this.back();
					}
				}
			}
			else {
				System.out.print("invalid input\n");
			}
			System.out.print(this.getInstructions());
			line = AppState.scanner.nextLine();
			s = new Scanner(line);
			word1 = s.next().toUpperCase();
			word2 = null;
			if(s.hasNext())
				word2 = s.next().toUpperCase();
			s.close();
		}
	}
	
	
	public boolean handleAccountClosing(int accountId) {
		boolean success = false;
		if(!AppState.answerYesOrNo("Are you sure you want to attempt to close account number " + accountId + "?"))
			return false;
		try {
			success = this.service.closeAccount(accountId);
			return success;
		}
		catch(NotAccountOwnerException NAOE) {
			System.out.print("You do not have an account with an account number of " + accountId + "\n");
		}
		catch(NegativeAccountBalanceException NABE) {
			System.out.print("You cannot close an account that does not have a 0 balance\n");
			System.out.print("Please go back and withdraw or deposit money into the acount to make the balance 0 and then attempt to close again\n");
		}
		catch(AccountDoesNotExistException ADNEE) {
			System.out.print("An account with that account number does not exist in our system\n");
		}
		
		return false;
	}

	@Override
	public AppState back() {
		return this.parent;
	}
	
	
	private String getInstructions() {
		return "To close an account ENTER CLOSE [ACCOUNT#]\nTo see a list of all your accounts go back to the previous screen and select the option SHOW ALL\nENTER BACK to go back to the previous screen\n";
	}
	
	private String getAccountClosingHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("/////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("/////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("Welcome to the account Closing menu(ENTER BACK  to return to the user main menu)\n");
		return sb.toString();
	}

}
