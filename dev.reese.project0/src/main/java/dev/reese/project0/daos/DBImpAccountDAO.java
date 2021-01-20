package dev.reese.project0.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.reese.project0.entities.Account;

import dev.reese.project0.util.JDBCConnection;

public class DBImpAccountDAO implements AccountDAO {
	
	public static Connection conn = JDBCConnection.getConnection();

	@Override
	public boolean createAccount(Account account) {
		try {
			// Java is unaware of our actor_seq, so
			// its better or easier if we just use our Procedure
			// that abstracted away the use of the Sequence.
			String sql = "CALL add_account(?, ?, ?, ?)";
			CallableStatement cs = conn.prepareCall(sql);
			cs.setString(1, Integer.toString(account.getAccountOwnerId()));
			cs.setString(2, account.getDiscription());
			cs.setString(3, Double.toString(account.getBalance()));
			cs.setString(4, (account.allowsNSFFee() ? "T" : "F"));
			cs.execute();
			return true;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Account> getUsersAccounts(int userId) {
		List<Account> accounts = new ArrayList<Account>();
		try {

				String sql = "SELECT * FROM accounts WHERE account_holder = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, Integer.toString(userId));
				
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					Account account = new Account();
					account.setAccountId(rs.getInt("ID"));
					account.setAccountOwnerId(rs.getInt("ACCOUNT_HOLDER"));
					account.setDiscription(rs.getString("DESCRIPTION"));
					account.setBalance(rs.getBigDecimal("BALANCE").doubleValue());
					account.setAllowsNSFFee(rs.getString("ALLOWS_NSF").equals("T"));
					
					accounts.add(account);
			}

		} 
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return accounts;
	}

	@Override
	public List<Account> getAllAccounts() {
		List<Account> accounts = new ArrayList<Account>();
		try {

				String sql = "SELECT * FROM accounts";
				PreparedStatement ps = conn.prepareStatement(sql);
				
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					Account account = new Account();
					account.setAccountId(rs.getInt("ID"));
					account.setAccountOwnerId(rs.getInt("ACCOUNT_HOLDER"));
					account.setDiscription(rs.getString("DESCRIPTION"));
					account.setBalance(rs.getBigDecimal("BALANCE").doubleValue());
					account.setAllowsNSFFee(rs.getString("ALLOWS_NSF").equals("T"));
					
					accounts.add(account);
			}

		} 
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return accounts;
	}

	
	@Override
	/**
	 * returns an account with a 0 id num if it did not exist. a null if an error
	 */
	public Account getAccount(int accountId) {
		try {

			String sql = "SELECT * FROM accounts WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, Integer.toString(accountId));
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				Account account = new Account();
				account.setAccountId(rs.getInt("ID"));
				account.setAccountOwnerId(rs.getInt("ACCOUNT_HOLDER"));
				account.setDiscription(rs.getString("DESCRIPTION"));
				account.setBalance(rs.getBigDecimal("BALANCE").doubleValue());
				account.setAllowsNSFFee(rs.getString("ALLOWS_NSF").equals("T"));
				return account;
			}
			return new Account(0);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean updateAccount(Account changedAccount) {
		try {
			String sql = "UPDATE accounts SET description = ?, balance = ? , allows_nsf = ? WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, changedAccount.getDiscription());
			ps.setString(2, Double.toString(changedAccount.getBalance()));
			ps.setString(3, (changedAccount.allowsNSFFee() ? "T" : "F"));
			ps.setString(4, Integer.toString(changedAccount.getAccountId()));

			ps.executeQuery();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteAccount(int accountId) {
		try {
			String sql = "DELETE FROM accounts WHERE id = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			//ps.setString(1, Integer.toString(userId));
			ps.setInt(1, accountId);
			ps.executeQuery();
			return true;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
