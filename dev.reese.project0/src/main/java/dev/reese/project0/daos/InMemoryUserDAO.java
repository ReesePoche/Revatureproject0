package dev.reese.project0.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.reese.project0.entities.User;


public class InMemoryUserDAO implements UserDAO{
	
	
	private static HashMap<String, User> users = null;// = new HashMap<String, User>();
	private static int counter;// = 0;
	
	
	public static void initIMUDB() {
		if(InMemoryUserDAO.users == null) {
			counter = 0;
			users = new HashMap<String, User>();
		}
	}
	

	public InMemoryUserDAO() {
		initIMUDB();
	}
	

	public User createUser(String userName, String password, boolean isSuper) {
		// TODO error checking done in services 
		User newUser = new User( ++counter, userName, isSuper, password);
		users.put(userName, newUser);
		return newUser;
	}

	/**
	 * @return returns a User with ID of value 0, and the rest null/default if password is incorrect, -1 if username does not exist
	 */
	public User getUser(String userName, String password) {
		// 
		User user = users.get(userName);
		if(user == null)
			return new User(-1);
		if(!password.equals(user.getPassword())) {
			return new User(0);
		}
		return user;
	}
	
	public User getUserByName(String userName) {
		User user =  users.get(userName);
		if(user == null)
			return new User(-1);
		return user;
	}
	
	

	public List<User> getAllUsers() {
		Set<String> userNames = users.keySet();
		List<User> listOfUsers = new ArrayList<User>();
		
		Object[] un = userNames.toArray();
		for(int i = 0; i < un.length; i++) {
			listOfUsers.add(users.get(un[i].toString()));
		}
		return listOfUsers;
	}

	public User changePassword(String userName, String newPassword) {
		User user = users.get(userName);
		user.setPassword(newPassword);
		return user;
	}

	public User deleteUser(String username) {
		User user = users.get(username);
		users.remove(username);
		return user;
	}



	public boolean nameHasBeenTaken(String Username) {
		Set<String> names = users.keySet();
		return names.contains(Username);
	}


	@Override
	public boolean createUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public User getUser(String userName) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public User getUser(int id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean updateUser(User changedUser) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean deleteUser(int userId) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
