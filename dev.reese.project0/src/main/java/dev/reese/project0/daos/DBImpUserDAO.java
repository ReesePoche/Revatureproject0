package dev.reese.project0.daos;

import java.util.List;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import dev.reese.project0.entities.User;
import dev.reese.project0.util.JDBCConnection;


public class DBImpUserDAO implements UserDAO {
	
	public static Connection conn = JDBCConnection.getConnection();

	@Override
	public boolean createUser(User user) {
		try {
			// Java is unaware of our actor_seq, so
			// its better or easier if we just use our Procedure
			// that abstracted away the use of the Sequence.
			String sql = "CALL add_user(?, ?, ?)";
			CallableStatement cs = conn.prepareCall(sql);
			cs.setString(1, user.getUserName());
			cs.setString(2, user.getPassword());
			cs.setString(3, (user.isSuper() ? "T" : "F"));
			cs.execute();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	/**
	 * @return returns a user with an id of 0 if it does not exist. a null if an error occured
	 */
	public User getUser(String userName) {
		try {
			String sql = "SELECT * FROM users WHERE username = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userName);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt("ID"));
				user.setUserName(rs.getString("USERNAME"));
				user.setPassword(rs.getString("PASSWORD"));
				String TorF = rs.getString("IS_SUPER_USER");
				user.setSuper(TorF.equals("T"));
				return user;
			}
			return new User(0);

		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	/**
	 * @return returns a user with id of 0 if not found a null if error occured
	 */
	public User getUser(int id) {
		try {

			String sql = "SELECT * FROM users WHERE id = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, Integer.toString(id));
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				User user = new User();
				user.setUserId(rs.getInt("ID"));
				user.setUserName(rs.getString("USERNAME"));
				user.setPassword(rs.getString("PASSWORD"));
				String TorF = rs.getString("IS_SUPER_USER");
				user.setSuper(TorF.equals("T"));
				return user;
			}
			return new User(0);

		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	/**
	 * returns an empty list if no users exist. a null if an error occured
	 */
	public List<User> getAllUsers() {
		
		List<User> users = new ArrayList<User>();
		try {

				String sql = "SELECT * FROM users";
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					User user = new User();
					user.setUserId(rs.getInt("ID"));
					user.setUserName(rs.getString("USERNAME"));
					user.setPassword(rs.getString("PASSWORD"));
					String TorF = rs.getString("IS_SUPER_USER");
					user.setSuper(TorF.equals("T"));
					
					users.add(user);
			}

		} 
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return users;
	}

	@Override
	public boolean updateUser(User changedUser) {
		try {
			String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, changedUser.getUserName());
			ps.setString(2, changedUser.getPassword());
			ps.setString(3, Integer.toString(changedUser.getUserId()));

			ps.executeQuery();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean deleteUser(int userId) {
		try {
			String sql = "DELETE FROM users WHERE id = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			//ps.setString(1, Integer.toString(userId));
			ps.setInt(1, userId);
			ps.executeQuery();
			return true;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
