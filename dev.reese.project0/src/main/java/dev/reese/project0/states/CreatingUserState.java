package dev.reese.project0.states;

import java.util.Scanner;

import dev.reese.project0.Services.UserCreationService;
import dev.reese.project0.Services.ServiceExceptions.InvalidPasswordException;
import dev.reese.project0.Services.ServiceExceptions.InvalidUserNameException;
import dev.reese.project0.Services.ServiceExceptions.UserNameTakenException;


public class CreatingUserState implements AppState{
	
	private AppState parent;
	
	private UserCreationService service;
	
	public CreatingUserState(AppState parent) {
		this.parent = parent;
		this.service = new UserCreationService();
	}

	@Override
	public AppState handle() {
		System.out.println(this.getWelcomeToAccountCreationMessage());
		if(!AppState.answerYesOrNo("Continue with User account creation?"))
			return this.back();
		while(true) {
			boolean creationSuccessful = this.handleUserCreation();
			if(creationSuccessful) {
				System.out.print("Account was successfully created\n");
				if(!AppState.answerYesOrNo("Do you wish to create another account? (You can ENTER BACK to return to main menu)\n"))
					return this.back();
			}
			else {
				System.out.print("Account creation failed\n");
				if(!AppState.answerYesOrNo("Do you wish to try again?"))
					return this.back();
			}
		}
	}
		
		

	@Override
	public AppState back() {
		return parent;
	}
	
	
	private boolean handleUserCreation() {
		System.out.print("ENTER desired Username\n");
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		String userName = s.next();
		s.close();
		System.out.print("ENTER desired password\n");
		line = AppState.scanner.nextLine();
		s = new Scanner(line);
		String password = s.next();
		s.close();
		boolean isSuper = AppState.answerYesOrNo("Does Account belong to a super User?");
		boolean success;
		try {
			success = this.service.createUser(userName, password, isSuper);
		}
		catch(UserNameTakenException UNTE) {
			System.out.print("Sorry, the Username " + userName + " was already taken\n" );
			return false;
		}
		catch(InvalidUserNameException IUNE) {
			System.out.print("Sorry, the userName entered was invalid\n");
			return false;
		}
		catch(InvalidPasswordException IPE) {
			System.out.print("Sorry, the password entered was invalid\n");
			return false;
		}
		return success;
	}
	
	private String getWelcomeToAccountCreationMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("Welcome To Account Creation\nENTER BACK to return to main menu\n");
		return sb.toString();
	}
	
	
	
	

	

}
