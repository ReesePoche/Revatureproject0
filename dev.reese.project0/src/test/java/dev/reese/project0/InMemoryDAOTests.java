package dev.reese.project0;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import dev.reese.project0.daos.InMemoryAccountDAO;
import dev.reese.project0.daos.InMemoryUserDAO;
import dev.reese.project0.entities.Account;
import dev.reese.project0.entities.User;

class InMemoryDAOTests {
	
//	static InMemoryUserDAO dao;
//	static User user1;
//	static InMemoryAccountDAO accountDAO;
//	static Account account1;
//	
//	
//
//		
//	@Nested
//	static class TestsWithOneEntry{
//		
//		@Test
//		@Order(1)
//		void createOneUserTestNotSuper() {
//			dao = new InMemoryUserDAO();
//			user1 = dao.createUser("testUserName", "testPassword" , false);
//			assertAll(
//					() -> assertEquals("testUserName", user1.getUserName(), "userNames did not match"), 
//					() -> assertEquals("testPassword", user1.getPassword(), "password did not match"), 
//					() -> assertEquals(false, user1.isSuper(), "user should not be a super user"), 
//					() -> assertEquals( 1 , user1.getUserId(), "user ID should be 1")
//					);
//		}
//		
//		
//		@Test
//		@Order(2)
//		void createAndGetOneUserWithCorrectPassword() {
//			dao = new InMemoryUserDAO();
//			user1 = dao.createUser("testUserName", "testPassword" , false);
//			User gottenUser = dao.getUser("testUserName", "testPassword");
//			assertEquals(user1.getUserName(), gottenUser.getUserName(), "the users are not equal");
//		}
//		
//		@Test
//		@Order(3)
//		void attemptToGetUserThatDoesNotExist() {
//			dao = new InMemoryUserDAO();
//			user1 = dao.createUser("testUserName", "testPassword" , false);
//			User gottenUser = dao.getUser("userNameThatDoesntExist", "passwordDoesNotMatter");
//			assertEquals(-1, gottenUser.getUserId());
//		}
//		
//		@Test
//		@Order(4)
//		void attemptToGetUserWithIncorrectPassword() {
//			dao = new InMemoryUserDAO();
//			user1 = dao.createUser("testUserName", "testPassword" , false);
//			User gottenUser = dao.getUser("testUserName", "passwordThatIsWrong");
//			assertEquals(0, gottenUser.getUserId(), "The password was correct unfortunatly");
//		}
//		
//		@Test
//		@Order(5)
//		void getAllOfOneUser() {
//			dao = new InMemoryUserDAO();
//			user1 = dao.createUser("testUserName", "testPassword" , false);
//			List<User> listOfUsers = dao.getAllUsers();
//			assertAll(
//					() -> assertEquals(1, listOfUsers.size(), "the size was not 1 as expected"), 
//					() -> assertEquals("testUserName", listOfUsers.get(0).getUserName(), "the users are not equal"), 
//					() -> assertFalse(listOfUsers.get(0).isSuper(), "The user was a super when it shouldnt be")
//					);
//		}
//		
//		@Test
//		@Order(6)
//		void changeThePassword() {
//			dao = new InMemoryUserDAO();
//			user1 = dao.createUser("testUserName", "testPassword" , false);
//			User changedUser = dao.changePassword("testUserName", "newPassword");
//			User gottenUserWithOldPassword = dao.getUser("testUserName", "testPassword");
//			User gottenUserWithNewPassword = dao.getUser("testUserName", "newPassword");
//			assertAll(
//					() -> assertEquals(0, gottenUserWithOldPassword.getUserId(), "the old password worked; it was not changed" ), 
//					() -> assertEquals(1, gottenUserWithNewPassword.getUserId(), "The user ID was not 1"), 
//					() -> assertEquals("testUserName", gottenUserWithNewPassword.getUserName(), "The username Did not match")
//					);
//		}
//		
//		@Test
//		@Order(7)
//		void deletingTheUser() {
//			dao = new InMemoryUserDAO();
//			user1 = dao.createUser("testUserName", "testPassword" , false);
//			User deletedUser = dao.deleteUser("testUserName");
//			User attemptedToGetAfterDelete = dao.getUser("testUserName", "testPassword");
//			assertAll(
//					() -> assertEquals(1, deletedUser.getUserId(), "delete method did not return the user that was deleted" ), 
//					() -> assertEquals(-1, attemptedToGetAfterDelete.getUserId(), "The user was not removed from the DB")
//					);
//		}
//	}
//	
//	
//	@Nested
//	class InMemoryAccountDAOSimpleTests{
//		
//		@BeforeEach
//		void setUp() {
//			dao = new InMemoryUserDAO();
//			user1 = dao.createUser("testUserName", "testPassword" , false);
//			accountDAO = new InMemoryAccountDAO();
//			account1 = accountDAO.createAccount(user1.getUserId(), "this is an account description", 0, false);
//		}
//		
//		@Test
//		void WasAccountCreated() {
//			assertAll(
//						() -> assertEquals(1, account1.getAccountId(), "the account Id was not what was expected"),
//						() -> assertEquals(1, account1.getAccountOwnerId(), "the accountOwnerId was not what was expected"),
//						() -> assertEquals("this is an account description", account1.getDiscription(), "the account Description was not what was expected"),
//						() -> assertEquals(0, account1.getBalance(), "the balance was not what was expected"),
//						() -> assertFalse(account1.allowsNSFFee())
//					);
//		}
//		
//		
//		@Test
//		void canGetAccount() {
//			Account a = accountDAO.getAccount(1);
//			assertAll(
//					() -> assertEquals(1, a.getAccountId(), "the account Id was not what was expected"),
//					() -> assertEquals(1, a.getAccountOwnerId(), "the accountOwnerId was not what was expected"),
//					() -> assertEquals("this is an account description", a.getDiscription(), "the account Description was not what was expected"),
//					() -> assertEquals(0, a.getBalance(), "the balance was not what was expected"),
//					() -> assertFalse(a.allowsNSFFee())
//				);
//			
//		}
//		
//		@Test
//		void canGetUserAccounts() {
//			List<Account> userAccounts = accountDAO.getUsersAccounts(user1.getUserId());
//			assertAll(
//					() -> assertEquals(1, userAccounts.size() , "the list of accounts was not the size expected"),
//					() -> assertEquals(1, userAccounts.get(0).getAccountId(), "the account Id was not what was expected"),
//					() -> assertEquals(1, userAccounts.get(0).getAccountOwnerId(), "the accountOwnerId was not what was expected"),
//					() -> assertEquals("this is an account description", userAccounts.get(0).getDiscription(), "the account Description was not what was expected"),
//					() -> assertEquals(0, userAccounts.get(0).getBalance(), "the balance was not what was expected"),
//					() -> assertFalse(userAccounts.get(0).allowsNSFFee())
//				);
//		}
//		
//		@Test
//		void canDepositMoney() {
//			Account changedAccount = accountDAO.deposit(1, 100.50);
//			assertEquals(100.50, changedAccount.getBalance() , "the balance of accounts was not the expected value");
//		}
//		
//		@Test
//		void canWithdrawMoney() {
//			accountDAO.deposit(1, 100.50);
//			Account changedAccount = accountDAO.withdraw(1, 50.50);
//			assertEquals(50, changedAccount.getBalance() , "the balance of accounts was not the expected value");
//		}
//		
//		@Test
//		void canDelete() {
//			accountDAO.deleteAccount(1);
//			Account result = accountDAO.getAccount(1);
//			assertEquals(0, result.getAccountId(), "account was not removed");
//		}
//		
		
		
	//}

}
