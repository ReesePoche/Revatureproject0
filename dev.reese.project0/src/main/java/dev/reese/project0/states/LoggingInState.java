package dev.reese.project0.states;


import java.util.Scanner;

import dev.reese.project0.Services.LoginService;
import dev.reese.project0.Services.NormalUserService;
import dev.reese.project0.Services.SuperUserService;
import dev.reese.project0.Services.ServiceExceptions.PasswordIncorrectException;
import dev.reese.project0.Services.ServiceExceptions.UserNameDoesNotExistException;


import dev.reese.project0.entities.User;

public class LoggingInState implements AppState{
	
	private AppState parent;
	
	private LoginService service;
	
	
	public LoggingInState(AppState parent) {
		this.parent = parent;
		this.service = new LoginService();
	}

	@Override
	public AppState handle() {
		System.out.print(this.loginScreenMenu());
		if(!AppState.answerYesOrNo("Continue to Logging in?"))
			return this.back();
		while(true) {
			User user = this.handleLogin();
			if(user != null) {
				System.out.print("Login Sucessful\n");
				if(user.isSuper())
					return new SuperUserMainMenuState(this.parent, new SuperUserService(user));
				else
					return new NormalUserMainMenuState(this.parent, new NormalUserService(user));
			}
			else {
				System.out.print("Login Failed\n");
				if(!AppState.answerYesOrNo("Try to login again? (you will be returned to the main menu if you select NO)"))
					return this.back();
			}
		}
	}

	@Override
	public AppState back() {
		return parent;
	}
	

	public User handleLogin() {
		System.out.print("ENTER Username\n");
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		String userName = s.next();
		s.close();
		
		System.out.print("ENTER password\n");
		line = AppState.scanner.nextLine();
		s = new Scanner(line);
		String password = s.next();
		s.close();
		User user;
		try {
			user = this.service.Login(userName, password);
		}
		catch(UserNameDoesNotExistException UNDNE) {
			System.out.print("Sorry, the Username " + userName + " does not exist in our records\n" );
			return null;
		}
		catch(PasswordIncorrectException PIE) {
			System.out.print("Sorry, the password entered was incorrect\n");
			return null;
		}
		return user;
	}
	
	
	private String loginScreenMenu() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("Login menu:(ENTER BACK to return to main menu)\n");
		return sb.toString();
		
	}
}
