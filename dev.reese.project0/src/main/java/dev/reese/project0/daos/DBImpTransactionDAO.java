package dev.reese.project0.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.reese.project0.entities.Transaction;
import dev.reese.project0.util.JDBCConnection;

public class DBImpTransactionDAO implements TransactionDAO {
	
	public static Connection conn = JDBCConnection.getConnection();

	@Override
	public boolean createTransaction(Transaction transaction) {
		try {
			// Java is unaware of our actor_seq, so
			// its better or easier if we just use our Procedure
			// that abstracted away the use of the Sequence.
			String sql = "CALL add_transaction(?, ?, ?, ?)";
			CallableStatement cs = conn.prepareCall(sql);
			cs.setString(1, Integer.toString(transaction.getDone_by()));
			//cs.setInt(1, transaction.);
			cs.setString(2, Integer.toString(transaction.getAccount_num()));
			cs.setString(3, transaction.getTransaction_type());
			cs.setString(4, Double.toString(transaction.getAmount()));
			cs.execute();
			return true;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Transaction getTransaction(int id) {
		try {
			String sql = "SELECT * FROM transactions WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, Integer.toString(id));
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				Transaction transaction = new Transaction();
				
				transaction.setId(rs.getInt("ID"));
				transaction.setDone_by(rs.getInt("DONE_BY"));
				transaction.setAccount_num(rs.getInt("ACCOUNT_NUM"));
				transaction.setTransaction_type(rs.getString("TRANSACTION_TYPE"));
				transaction.setAmount(rs.getBigDecimal("AMOUNT").doubleValue());
				return transaction;
			}
			return new Transaction(0);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Transaction> getAccountTransactions(int accountNum) {
		List<Transaction> transactions = new ArrayList<Transaction>();
		try {

				String sql = "SELECT * FROM transactions WHERE account_num = ? ";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, accountNum);
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					Transaction transaction = new Transaction();
					
					transaction.setId(rs.getInt("ID"));
					transaction.setDone_by(rs.getInt("DONE_BY"));
					transaction.setAccount_num(rs.getInt("ACCOUNT_NUM"));
					transaction.setTransaction_type(rs.getString("TRANSACTION_TYPE"));
					transaction.setAmount(rs.getBigDecimal("AMOUNT").doubleValue());
//					
					transactions.add(transaction);
			}

		} 
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return transactions;
	}

	@Override
	public List<Transaction> getUserTransactions(int userId) {
		List<Transaction> transactions = new ArrayList<Transaction>();
		try {

				String sql = "SELECT * FROM transactions WHERE done_by = ? ";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, userId);
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					Transaction transaction = new Transaction();
					
					transaction.setId(rs.getInt("ID"));
					transaction.setDone_by(rs.getInt("DONE_BY"));
					transaction.setAccount_num(rs.getInt("ACCOUNT_NUM"));
					transaction.setTransaction_type(rs.getString("TRANSACTION_TYPE"));
					transaction.setAmount(rs.getBigDecimal("AMOUNT").doubleValue());
//					
					transactions.add(transaction);
			}

		} 
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return transactions;
	}

	@Override
	public List<Transaction> getAllTransactions() {
		List<Transaction> transactions = new ArrayList<Transaction>();
		try {

				String sql = "SELECT * FROM transactions ";
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					Transaction transaction = new Transaction();
					
					transaction.setId(rs.getInt("ID"));
					transaction.setDone_by(rs.getInt("DONE_BY"));
					transaction.setAccount_num(rs.getInt("ACCOUNT_NUM"));
					transaction.setTransaction_type(rs.getString("TRANSACTION_TYPE"));
					transaction.setAmount(rs.getBigDecimal("AMOUNT").doubleValue());
					
					transactions.add(transaction);
			}

		} 
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return transactions;
	}

	@Override
	public boolean deleteTransaction(int id) {
		try {
			String sql = "DELETE  FROM transactions WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, Integer.toString(id));
			
			ps.executeQuery();
			return true;
		} 
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}
}
