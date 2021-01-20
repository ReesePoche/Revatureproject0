package dev.reese.project0.daos;

import java.util.List;

import dev.reese.project0.entities.Transaction;

public interface TransactionDAO {
	
	boolean createTransaction(Transaction transaction);
	
	Transaction getTransaction(int id);
	
	List<Transaction> getAccountTransactions(int accountNum);
	
	List<Transaction> getUserTransactions(int userId);
	
	List<Transaction> getAllTransactions();
	
	boolean deleteTransaction(int id);
	

}
