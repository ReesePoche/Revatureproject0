package dev.reese.project0.Services.ServiceExceptions;

public class DeletingUserWithOpenAccountsException extends Exception {
	
	public DeletingUserWithOpenAccountsException(String message) {
		super(message);
	}

}
