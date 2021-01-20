package dev.reese.project0.Services.ServiceExceptions;

public class StartingAccountWithNegativeBalanceException extends Exception {
	
	public StartingAccountWithNegativeBalanceException(String message) {
		super(message);
	}

}
