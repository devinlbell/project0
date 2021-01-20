package dev.lbell.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import dev.lbell.models.BankAccount;
import dev.lbell.models.Transactions;
import dev.lbell.util.ConnectionUtil;

public class AccountDaoImpl implements AccountDao {
	
	private static Logger log = Logger.getRootLogger();
	private static String interactionSql = "INSERT INTO TRANSACTIONS (INTERACTION) VALUES (?)";
	@Override
	public List<BankAccount> getCustomerAccounts(int customerId) {
		// TODO Auto-generated method stub
		List<BankAccount> accounts = new ArrayList<>();
		String sql = "SELECT * FROM BANK_ACCOUNT WHERE CUSTOMER_ID = ?";
		String interaction = sql + " -> (" + customerId + ")";
		try(Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pStatement = connection.prepareStatement(sql);
				PreparedStatement interactionPStatement = connection.prepareStatement(interactionSql)) {
			pStatement.setInt(1, customerId);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()) {
				int accountId = rs.getInt("ACCOUNT_ID");
				String status = rs.getString("STATUS");
				double balance = rs.getDouble("BALANCE");
				accounts.add(new BankAccount(accountId, status, balance, customerId));
			}
			log.info(interaction);
			interactionPStatement.setString(1, interaction);
			interactionPStatement.executeQuery();
			return accounts;
		} catch(SQLException e) {
			log.warn(e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean applyBankAccount(int customerId, double balance) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO BANK_ACCOUNT (CUSTOMER_ID, STATUS, BALANCE) VALUES (?, ?, ?)";
		String interaction = sql + " -> (" + customerId + ", " + " PENDING, " + balance + ")";
		try(Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pStatement = connection.prepareStatement(sql);
				PreparedStatement interactionPStatement = connection.prepareStatement(interactionSql)) {
			pStatement.setInt(1, customerId);
			pStatement.setString(2, "PENDING");
			pStatement.setDouble(3, balance);
			pStatement.executeQuery();
			log.info(interaction);
			interactionPStatement.setString(1, interaction);
			interactionPStatement.executeQuery();
			return true;
		} catch(SQLException e) {
			log.warn(e);
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addressAccount(int accountId, String status) {
		// TODO Auto-generated method stub
		String sql = "UPDATE BANK_ACCOUNT SET STATUS = ? WHERE ACCOUNT_ID = ?";
		String interaction = sql + " -> (" + status + ", " + " " + accountId + ")";
		try(Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pStatement = connection.prepareStatement(sql);
				PreparedStatement interactionPStatement = connection.prepareStatement(interactionSql)) {
			pStatement.setString(1, status);
			pStatement.setInt(2, accountId);
			pStatement.executeQuery();
			log.info(interaction);
			interactionPStatement.setString(1, interaction);
			interactionPStatement.executeQuery();
			return true;
			
		} catch(SQLException e) {
			log.warn(e);
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean makeTransaction(int accountId, double amount, double balance, boolean deposit) {
		// TODO Auto-generated method stub
//		String tSql = "INSERT INTO TRANSACTIONS (ACCOUNT_ID, AMOUNT, STATUS) VALUES (?, ?, ?)" ; 
		String acctSql = "UPDATE BANK_ACCOUNT SET BALANCE = ? WHERE ACCOUNT_ID = ?";
		double newBalance;
		if(amount < 0) {
			System.out.println("Amount withdrawn or deposited must not be lower than 0.");
			return false;
		}
		if(deposit) {
			newBalance = balance + amount;
		} else {
			newBalance = balance - amount;
			if(newBalance < 0) {
				System.out.println("The balance of this account is not enough to make a withdrawal. Please enter a lower amount.");
				return false;
			}
		}
//		String interaction1 =tSql + " -> (" + accountId + ", " + amount + ", " + status + ")";
		String interaction2 = acctSql + " -> (" + newBalance + ", " + " " + accountId + ")";
		try(Connection connection = ConnectionUtil.getConnection();
//				PreparedStatement trPStatement = connection.prepareStatement(tSql);
				PreparedStatement acctPStatement = connection.prepareStatement(acctSql);
				PreparedStatement interactionPStatement = connection.prepareStatement(interactionSql)) {
//			trPStatement.setInt(1, accountId);
//			trPStatement.setDouble(2, amount);
//			trPStatement.setString(3, status);
//			trPStatement.executeQuery();
//			log.info(interaction1);
//			interactionPStatement.setString(1, interaction1);
//			interactionPStatement.executeQuery();
			acctPStatement.setDouble(1, newBalance);
			acctPStatement.setInt(2, accountId);
			acctPStatement.executeQuery();
			log.info(interaction2);
			interactionPStatement.setString(1, interaction2);
			interactionPStatement.executeQuery();
			return true;
		} catch(SQLException e) {
			log.warn(e);
			e.printStackTrace();
		}
		return false;

	}

	@Override
	public List<Transactions> viewTransactions() {
		// TODO Auto-generated method stub
		List<Transactions> transactions = new ArrayList<>();
		String sql = "SELECT * FROM TRANSACTIONS";
		try(Connection connection = ConnectionUtil.getConnection();
				Statement statement = connection.createStatement();
				PreparedStatement interactionPStatement = connection.prepareStatement(interactionSql)) {
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				transactions.add(new Transactions(rs.getInt("TRANSACTION_ID"), rs.getString("INTERACTION")));
			}
			log.info(sql);
			interactionPStatement.setString(1, sql);
			interactionPStatement.executeQuery();
			return transactions;
		} catch(SQLException e) {
			log.warn(e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<BankAccount> getUnapprovedAccts(int customerId, List<BankAccount> accts) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM BANK_ACCOUNT WHERE STATUS != 'APPROVED' AND CUSTOMER_ID = ?";
		String interaction = sql + " -> (" + customerId + ")";
		try(Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pStatement = connection.prepareStatement(sql);
				PreparedStatement interactionPStatement = connection.prepareStatement(interactionSql)) {
			pStatement.setInt(1, customerId);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()) {
				accts.add(new BankAccount(rs.getInt("ACCOUNT_ID"), rs.getString("STATUS"), rs.getDouble("BALANCE"), rs.getInt("CUSTOMER_ID")));
			}
			log.info(interaction);
			interactionPStatement.setString(1, interaction);
			interactionPStatement.executeQuery();
			return accts;
		} catch (SQLException e) {
			log.warn(e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void insertTransaction(String interaction) {
//		// TODO Auto-generated method stub
//		String sql = "INSERT INTO TRANSACTIONS (INTERACTION) VALUES (?)";
//		try(Connection connection = ConnectionUtil.getConnection();
//				PreparedStatement pStatement = connection.prepareStatement(sql)) {
//			pStatement.setString(1, interaction);
//			pStatement.executeQuery();
//			log.info(sql +  " -> (" + interaction + ") ");
//			
//		} catch (SQLException e) {
//			log.warn(e);
//			e.printStackTrace();
//			
//		}
//		
	}

}
