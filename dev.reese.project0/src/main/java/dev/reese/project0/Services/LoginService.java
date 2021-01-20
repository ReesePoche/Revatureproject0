package dev.reese.project0.Services;

import dev.reese.project0.Services.ServiceExceptions.PasswordIncorrectException;
import dev.reese.project0.Services.ServiceExceptions.UserNameDoesNotExistException;
import dev.reese.project0.daos.DBImpUserDAO;
import dev.reese.project0.daos.UserDAO;
import dev.reese.project0.entities.User;


public class LoginService {
	
	private static UserDAO db = new DBImpUserDAO();
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @return a user with ID of 0 means the username does not exist in DB, -1 means the password was wrong, null meanns DB exception
	 */
	public User Login(String userName, String password) throws UserNameDoesNotExistException, PasswordIncorrectException{
		User user = db.getUser(userName);
		if(user.getUserId() == 0)
			throw new UserNameDoesNotExistException("the username " + userName + " does not exist in our system");
		if(!(password.equals(user.getPassword())))
			throw new PasswordIncorrectException("password entered did not match our records");
		return user;
	}
	

}
