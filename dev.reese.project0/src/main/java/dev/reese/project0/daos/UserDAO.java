package dev.reese.project0.daos;

import java.util.List;

import dev.reese.project0.entities.User;

public interface UserDAO {
	
	boolean createUser(User user);
	
	User getUser(String userName);
	
	User getUser(int id);
	
	List<User> getAllUsers();
	
	boolean updateUser(User changedUser);
	
	boolean deleteUser(int userId);
	
}
