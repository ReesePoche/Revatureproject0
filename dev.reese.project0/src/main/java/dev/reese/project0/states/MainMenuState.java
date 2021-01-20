package dev.reese.project0.states;

import java.util.Scanner;

public class MainMenuState implements AppState {

	@Override
	public AppState handle() {
		
		System.out.print(this.mainMenuHeader());
		System.out.print(this.getInstructionString());
		String Line = AppState.scanner.nextLine();
		Scanner s = new Scanner(Line);
		String input = s.next().toUpperCase();
		s.close();
		while(true) {
			if(input.equals("LOGIN"))
				return new LoggingInState(this);
			if(input.equals("CREATE"))
				return new CreatingUserState(this);
			if(input.equals("EXIT"))
				return this.back();
			else {
				System.out.println(this.wrongInputMessage(input));
				System.out.println(this.getInstructionString());
				Line = AppState.scanner.nextLine();
				s = new Scanner(Line);
				input = s.next().toUpperCase();
				s.close();
			}
		}
	}

	/**
	 * @return a value of null implies the user wants to exit the application. 
	 */
	@Override
	public AppState back() {
		return null;
	}
	
	private String mainMenuHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("/////\t\tSimple Java App\t\t/////\n");
		sb.append("/////\t\t               \t\t/////\n");
		sb.append("////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");
		sb.append("Welcome to the main menu\n");
		return sb.toString();
	}
	
	private String getInstructionString() {
		return "ENTER LOGIN to login\nENTER CREATE to create an account\nENTER EXIT to close the application\n";
	}
	
	private String wrongInputMessage(String wrongInput) {
		StringBuilder sb = new StringBuilder();
		sb.append("Sorry, ");
		sb.append(wrongInput);
		sb.append(" is not a valid option\n");
		return sb.toString();
	}
	

	
	
	
	

}
