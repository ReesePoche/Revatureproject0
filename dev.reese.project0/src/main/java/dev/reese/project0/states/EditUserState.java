package dev.reese.project0.states;

import java.util.Scanner;

import dev.reese.project0.Services.UserEditUserService;
import dev.reese.project0.Services.ServiceExceptions.DeletingUserWithOpenAccountsException;
import dev.reese.project0.Services.ServiceExceptions.InvalidPasswordException;
import dev.reese.project0.Services.ServiceExceptions.InvalidUserNameException;
import dev.reese.project0.Services.ServiceExceptions.UserNameTakenException;

public class EditUserState implements AppState{
	
	private AppState parent;
	
	private UserEditUserService service;
	
	public EditUserState(AppState parent, UserEditUserService service) {
		this.parent = parent;
		this.service = service;
	}
	
	public AppState handle() {
		System.out.print(this.getHeader());
		if(this.service.isUserChangingOwnAccount())
			System.out.print(this.getInstructionString());
		else
			System.out.print(this.getSuperInstructionsString());
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		String input2 = null;
		String input1 = s.next().toUpperCase();
		if(s.hasNext())
			input2 = s.next().toUpperCase();
		boolean sucessfulChange = true;
		s.close();
		while(true) {
			if(input1.equals("BACK")) {
				return this.back();
			}
			if(input1.equals("PASSWORD")) {
				sucessfulChange = this.handlePasswordChange();
				if(sucessfulChange) 
					System.out.print("password sucessfully changed\n");
				else
					System.out.print("Attempt to change password was unsucessful\n");
			}
			else if(input1.equals("USERNAME")) {
				sucessfulChange = this.handleUsernameChange();
				if(sucessfulChange)
					System.out.print("Username sucessfully changed\n");
				else
					System.out.print("Attempt to change Username was unsucessful\n");
			}
			else if(input1.equals("DELETE") && this.service.isCallerSuper()) {
				sucessfulChange = this.handleDelete();
				if(sucessfulChange) {
					System.out.print("User was sucessfully deleted\n");
					if(this.service.isUserChangingOwnAccount())
						return new MainMenuState();
					else
						return this.back();
				}
				else
					System.out.print("Attempt to delete user was unsucessful\n");
			}
			else {
				System.out.print("invalid input\n");
			}
			if(!sucessfulChange) {
				this.service.refreshService();
			}
			if(this.service.isUserChangingOwnAccount())
				System.out.print(this.getInstructionString());
			else
				System.out.print(this.getSuperInstructionsString());
			line = AppState.scanner.nextLine();
			s = new Scanner(line);
			input2 = null;
			input1 = s.next().toUpperCase();
			if(s.hasNext())
				input2 = s.next().toUpperCase();
			s.close();
		}
		
	}

	
	
	public AppState back() {
		return this.parent;
	}
	
	public boolean handlePasswordChange() {
		if(!this.service.isUserChangingOwnAccount())
			return this.handleSuperPasswordChange();
		if(!AppState.answerYesOrNo("Continue and attempt to change password?"))
			return false;
		System.out.print("Entered desired password: \n");
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		String password = s.next();
		s.close();
		if(!AppState.answerYesOrNo("change your password to " + password))
			return false;
		boolean sucess;
		try {
			sucess = this.service.changePassword(password);
		}
		catch(InvalidPasswordException IPE) {
			System.out.print("The password you entered was invalid\n");
			sucess = false;
		}
		return sucess;
	}
	
	public boolean handleSuperPasswordChange() {
		if(!AppState.answerYesOrNo("Continue and attempt to change User's password?"))
			return false;
		System.out.print("Entered desired password: \n");
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		String password = s.next();
		s.close();
		if(!AppState.answerYesOrNo("change the User's password to " + password))
			return false;
		boolean sucess;
		try {
			sucess = this.service.changePassword(password);
		}
		catch(InvalidPasswordException IPE) {
			System.out.print("The password you entered was invalid\n");
			sucess = false;
		}
		return sucess;
	}
	
	public boolean handleUsernameChange() {
		if(!this.service.isUserChangingOwnAccount())
			return this.handleSuperUsernameChange();
		boolean sucess = false;
		if(!AppState.answerYesOrNo("Continue and attempt to change your username?"))
			return false;
		System.out.print("Entered desired Username: \n");
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		String username = s.next();
		s.close();
		if(!AppState.answerYesOrNo("change your username to " + username+ "?"))
			return false;
		
		try {
			sucess = this.service.changedUserName(username);
		}
		catch(UserNameTakenException UNTE) {
			System.out.print("The username you entered was already taken in our system\n");
			sucess = false;
		}
		catch(InvalidUserNameException IUNE) {
			System.out.print("The username you entered was invalid\n");
			sucess = false;
		}
		return sucess;
	}
	
	public boolean handleSuperUsernameChange() {
		boolean sucess = false;
		if(!AppState.answerYesOrNo("Continue and attempt to change the user's username?"))
			return false;
		System.out.print("Entered desired Username: \n");
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		String username = s.next();
		s.close();
		if(!AppState.answerYesOrNo("change the user's username to " + username + "?"))
			return false;
		
		try {
			sucess = this.service.changedUserName(username);
		}
		catch(UserNameTakenException UNTE) {
			System.out.print("The username you entered was already taken in our system\n");
			sucess = false;
		}
		catch(InvalidUserNameException IUNE) {
			System.out.print("The username you entered was invalid\n");
			sucess = false;
		}
		return sucess;
	}
	
	
	
	
	
	public boolean handleDelete() {
		if(!AppState.answerYesOrNo("Continue and attempt to delete the user?"))
			return false;
		System.out.print("Deleting the User will close all open accounts they hold regardless of balance and delete all records associated with the user\n");
		System.out.print("After sucessful deletion you will be returned to the previous menu.\n");
		System.out.print("If you are deleting the account you are currently logged into you will be logged out and returned the main menu after this process is complete\n");
		System.out.printf("To finalize deletion process, ENTER DELETE USER %s\n", this.service.getUserBeingEdited().getUserName());
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		String word1 = s.next().toUpperCase();
		String word2 = null;
		String word3 = null;
		if(s.hasNext())
			word2 = s.next().toUpperCase();
		if(s.hasNext())
			word3 = s.next();
		s.close();
		boolean result = false;;
		while(true) {
			if(word1.equals("DELETE") && word2 != null &&  word2.equals("USER") && word3 != null &&  word3.equals(this.service.getUserBeingEdited().getUserName()))
				try {
					result = this.service.deleteAccount();
				}
				catch(DeletingUserWithOpenAccountsException DUWOA) {
					System.out.print("You cannot delete your user account if you have an account open\n");
					return false;
				}
			else {
				System.out.print("Invalid input\n");
				if(!AppState.answerYesOrNo("try again and attempt to delete the user?"))
					return false;
				System.out.printf("To finalize deletion process, ENTER DELETE USER %s\n", this.service.getUserBeingEdited().getUserName());
				line = AppState.scanner.nextLine();
				s = new Scanner(line);
				word1 = s.next().toUpperCase();
				word2 = null;
				word3 = null;
				if(s.hasNext())
					word2 = s.next().toUpperCase();
				if(s.hasNext())
					word3 = s.next().toUpperCase();
				s.close();
			}
			return result;
		}
	}
	
	private String getInstructionString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ENTER PASSWORD to change your password\n");
		sb.append("ENTER USERNAME to change your username\n");
		if(this.service.isCallerSuper())
			sb.append("ENTER DELETE USER to delete your user account\n");
		sb.append("ENTER BACK to return to user main menu\n");
		return sb.toString();
	}
	
	private String getSuperInstructionsString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ENTER PASSWORD to change the users password\n");
		sb.append("ENTER USERNAME to change the users username\n");
		sb.append("ENTER DELETE USER to delete the user's account\n");
		sb.append("ENTER BACK to return to user main menu\n");
		return sb.toString();
	}
	
	
	private String getHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("//////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("//////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("Welcome to Edit User screen\n");
		return sb.toString();
	}
}
