package dev.reese.project0.App;



import dev.reese.project0.states.AppState;
import dev.reese.project0.states.MainMenuState;

public class App {

	public static void main(String[] args) {
		AppState currentState = new MainMenuState();
		while(currentState != null) {
			currentState = currentState.handle();
		}
		
	
	}

}
