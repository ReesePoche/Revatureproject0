package dev.reese.project0.states;

import java.util.Scanner;

import dev.reese.project0.Services.UserAccountTransactionService;
import dev.reese.project0.Services.ServiceExceptions.NegativeAccountBalanceException;
import dev.reese.project0.Services.ServiceExceptions.NegativeAmountException;

public class AccountViewState implements AppState{

	private AppState parent;
	
	
	private UserAccountTransactionService service;
	
	public AccountViewState(AppState parent, UserAccountTransactionService service) {
		this.parent = parent;
		this.service = service;
	}
	
	@Override
	public AppState handle() {
		System.out.print(this.getMenuHeader());
		System.out.print(this.getAccountInfoString());
		System.out.print(this.getPossibleCommandsString());
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		String input = s.next().toUpperCase();
		s.close();
		while(true) {
			boolean successfulTransaction = true;
			if(input.equals("BACK"))
				return this.back();
			else if(input.equals("DEPOSIT")) {
				successfulTransaction = this.handleDeposit();
			}
			else if (input.equals("WITHDRAW")) {
				successfulTransaction = this.handleWithdraw();
			}
			//TODO ADD AN ABILITY TO SEE TRANSACTIONS
			else {
				System.out.print("invalid input\n");
			}
			if(!successfulTransaction) {
				this.service.refreshService();
				System.out.print(this.getAccountInfoString());
			}
			System.out.print(this.getPossibleCommandsString());
			line = AppState.scanner.nextLine();
			s = new Scanner(line);
			input = s.next().toUpperCase();
			s.close();
		}
	}

	@Override
	public AppState back() {
		return this.parent;
	}
	
	private double getAmountFromUser(boolean isDeposit) {
		String deposit = "deposit";
		String withdraw = "withdraw";
		System.out.print("How much do you want to " + (isDeposit ? deposit : withdraw) + "\n");
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		String input = s.next();
		s.close();
		while(true) {
			if(AppState.isDouble(input)) {
				return Double.parseDouble(input);
			}
			else {
				System.out.print("invalid input\n");
			}
			System.out.print("How much do you want to " + (isDeposit ? deposit : withdraw) + "\n");
			line = AppState.scanner.nextLine();
			s = new Scanner(line);
			input = s.next();
			s.close();
		}
		
	}
	
	
	private boolean handleDeposit() {
		if(!AppState.answerYesOrNo("Continue and make a deposit?"))
			return false;
		double amount = this.getAmountFromUser(true);
		if(!AppState.answerYesOrNo(String.format("Deposit $%.2f into account number %d? ", amount, this.service.getAccount().getAccountId()))) {
			System.out.print("Deposit cancled\n");
			return false;
		}
		boolean depositSucessful = false;
		try {
			depositSucessful = this.service.deposit(amount);
		}
		catch(NegativeAmountException NAE) {
			System.out.print("You cannot deposit a negative amount\n");
			depositSucessful = false;
		}
		if(depositSucessful) {
			System.out.print(String.format("Deposit Sucessful\n$%.2f was depositied into account number %d\n", amount, this.service.getAccount().getAccountId()));
			System.out.print(this.getAccountInfoString());
			return true;
		}
		else {
			System.out.print("Deposit unsuccessful\n");
			return false;
		}
	}
	
	private boolean handleWithdraw() {
		if(!AppState.answerYesOrNo("Continue and make a withdraw?"))
			return false;
		double amount = this.getAmountFromUser(false);
		if(!AppState.answerYesOrNo(String.format("Withdraw $%.2f from account number %d? ", amount, this.service.getAccount().getAccountId()))) {
			System.out.print("Withdraw cancled\n");
			return false;
		}
		boolean withdrawSuccessful = false;
		try {
			withdrawSuccessful  = this.service.withdraw(amount);
		}
		catch(NegativeAmountException NAE) {
			System.out.print("You must withdraw an amount greater than 0.00\n");
			return false;
		}
		catch(NegativeAccountBalanceException NABE) {
			System.out.print("InsufficentFunds and your account does not allow for NSF\n");
			return false;
		}
		if(withdrawSuccessful) {
			System.out.print(String.format("Withdraw Sucessful\n$%.2f was withdrawn from account number %d\n", amount, this.service.getAccount().getAccountId()));
			System.out.print(this.getAccountInfoString());
			return true;
		}
		else {
			System.out.print("Withdraw unsuccessful\n");
			return false;
		}
	}
	

	
	private String getPossibleCommandsString() {
		return "ENTER DEPOSIT to deposit money\nENTER WITHDRAW to withdraw money\nENTER BACK to return to Accounts screen\n";
	}
	
	private String getMenuHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("Account menu view\n");
		return sb.toString();
	}
	
	private String getAccountInfoString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("For Account Number %d%n\tAccount Holder: %s%n\tAccount balance: $%.2f%n\tdescription: %s%n",
				this.service.getAccount().getAccountId(), this.service.getAccountHolder().getUserName(), this.service.getAccount().getBalance(), this.service.getAccount().getDiscription()));
		if(this.service.getAccount().allowsNSFFee()) {
			sb.append("\tNSF charges allowed\n");
		}
		else {
			sb.append("\tNSF charges NOT allowed\n");
		}
		return sb.toString();
	}

}
