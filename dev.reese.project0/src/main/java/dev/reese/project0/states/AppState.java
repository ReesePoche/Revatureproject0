package dev.reese.project0.states;

import java.util.Scanner;

public interface AppState {
	
	public static final Scanner scanner = new Scanner(System.in);
	
	AppState handle();
	
	AppState back();
	
	public static boolean isInt(String s){
		try {
			Integer.parseInt(s);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}

	
	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}

	
	public static boolean answerYesOrNo(String question) {
		System.out.print(question);
		System.out.print("(ENTER YES or NO)\n");
		String line = AppState.scanner.nextLine();
		Scanner s = new Scanner(line);
		String answer = s.next().toUpperCase();
		s.close();
		while(true) {
			if(answer.equals("YES"))
				return true;
			else if(answer.equals("NO") || answer.equals("BACK"))
				return false;
			System.out.print("Invalid input\n");
			System.out.println(question);
			System.out.print("(ENTER YES or NO)\n");
			line = AppState.scanner.nextLine();
			s = new Scanner(line);
			answer = s.next().toUpperCase();
		}
	}
}
