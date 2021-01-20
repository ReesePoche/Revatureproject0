package dev.reese.project0.Services;

import dev.reese.project0.Services.ServiceExceptions.DeletingUserWithOpenAccountsException;
import dev.reese.project0.Services.ServiceExceptions.InvalidPasswordException;
import dev.reese.project0.Services.ServiceExceptions.InvalidUserNameException;
import dev.reese.project0.Services.ServiceExceptions.UserNameTakenException;
import dev.reese.project0.daos.AccountDAO;
import dev.reese.project0.daos.DBImpAccountDAO;
import dev.reese.project0.daos.DBImpUserDAO;
import dev.reese.project0.daos.UserDAO;
import dev.reese.project0.entities.User;

public class UserEditUserService {
	
	private User userToBeEdited;
	private User userThatOpenedTheService;
	
	private static UserDAO db = new  DBImpUserDAO();
	private static AccountDAO accountDB = new DBImpAccountDAO();
	
	public UserEditUserService(User userToBeEdited, User userThatOpenedTheService) {
		this.userToBeEdited = userToBeEdited;
		this.userThatOpenedTheService = userThatOpenedTheService;
	}
	
	public UserEditUserService(int userId) {
		this.userToBeEdited = db.getUser(userId);
	}
	
	public User getUserBeingEdited() {
		return this.userToBeEdited;
	}
	
	public void refreshService() {
		this.userToBeEdited = db.getUser(userToBeEdited.getUserId());
		this.userThatOpenedTheService = db.getUser(this.userThatOpenedTheService.getUserId());
	}
	
	
	public boolean changedUserName(String newUsername) throws InvalidUserNameException, UserNameTakenException {
		if(newUsername == null)
			throw new InvalidUserNameException("UserName cannot be null");
		if(newUsername == "")
			throw new InvalidUserNameException("Username cannot be an empty string");
		if(newUsername.toUpperCase() == "BACK")
			throw new InvalidUserNameException("Username cannot be BACK, a reserved word in the system");
		boolean userNameTaken = db.getUser(newUsername).getUserId() != 0;
		if(userNameTaken)
			throw new UserNameTakenException("Username already in user");
		this.userToBeEdited.setUserName(newUsername);
		return db.updateUser(userToBeEdited);
	}
	
	
	public boolean changePassword(String newPassword) throws InvalidPasswordException{
		if(newPassword == null)
			throw new InvalidPasswordException("password cannot be null");
		if(newPassword == "")
			throw new InvalidPasswordException("password cannot be an empty string");
		this.userToBeEdited.setPassword(newPassword);
		return db.updateUser(this.userToBeEdited);
	}
	
	
	public boolean deleteAccount() throws DeletingUserWithOpenAccountsException{
		if(!userThatOpenedTheService.isSuper()) {
			int numberOfAccountsOpen = accountDB.getUsersAccounts(this.userToBeEdited.getUserId()).size();
			if (numberOfAccountsOpen > 0 )
				throw new DeletingUserWithOpenAccountsException("Cannot delete a user with an open account");
		}
		return db.deleteUser(this.userToBeEdited.getUserId());
	}
	
	public boolean isUserChangingOwnAccount() {
		return (this.userThatOpenedTheService.getUserId() == this.userToBeEdited.getUserId());
	}
	
	public boolean isCallerSuper() {
		return this.userThatOpenedTheService.isSuper();
	}
		

}
