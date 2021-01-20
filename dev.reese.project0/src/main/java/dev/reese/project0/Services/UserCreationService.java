package dev.reese.project0.Services;

import dev.reese.project0.Services.ServiceExceptions.InvalidPasswordException;
import dev.reese.project0.Services.ServiceExceptions.InvalidUserNameException;
import dev.reese.project0.Services.ServiceExceptions.UserNameTakenException;
import dev.reese.project0.daos.DBImpUserDAO;
import dev.reese.project0.daos.UserDAO;
import dev.reese.project0.entities.User;

public class UserCreationService {
	
	private static UserDAO db = new DBImpUserDAO();
	
	
	public boolean createUser(String userName, String password, boolean isSuper) throws UserNameTakenException, InvalidUserNameException, InvalidPasswordException{
		if(userName == null)
			throw new InvalidUserNameException("UserName cannot be null");
		if(userName == "")
			throw new InvalidUserNameException("Username cannot be an empty string");
		if(userName.toUpperCase() == "BACK")
			throw new InvalidUserNameException("Username cannot be BACK, a reserved word in the system");
		if(password == null)
			throw new InvalidPasswordException("password cannot be null");
		if(password == "")
			throw new InvalidPasswordException("password cannot be an empty string");
		boolean userNameTaken = db.getUser(userName).getUserId() != 0;
		if(userNameTaken)
			throw new UserNameTakenException("Username already in user");
		User newUser = new User();
		newUser.setUserName(userName);
		newUser.setPassword(password);
		newUser.setSuper(isSuper);
		return db.createUser(newUser);
		
	}
	

}
