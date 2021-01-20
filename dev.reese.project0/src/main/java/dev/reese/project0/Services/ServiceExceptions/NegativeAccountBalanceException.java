package dev.reese.project0.Services.ServiceExceptions;

public class NegativeAccountBalanceException extends Exception {
	
	public NegativeAccountBalanceException(String message) {
		super(message);
	}

}
