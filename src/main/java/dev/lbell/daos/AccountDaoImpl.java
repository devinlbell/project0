package dev.lbell.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dev.lbell.models.BankAccount;
import dev.lbell.models.Transactions;
import dev.lbell.util.ConnectionUtil;

public class AccountDaoImpl implements AccountDao {

	@Override
	public List<BankAccount> getCustomerAccounts(int customerId) {
		// TODO Auto-generated method stub
		List<BankAccount> accounts = new ArrayList<>();
		String sql = "SELECT * FROM BANK_ACCOUNT WHERE CUSTOMER_ID = ?";
		try(Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pStatement = connection.prepareStatement(sql)) {
			pStatement.setInt(1, customerId);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()) {
				int accountId = rs.getInt("ACCOUNT_ID");
				String status = rs.getString("STATUS");
				double balance = rs.getDouble("BALANCE");
				accounts.add(new BankAccount(accountId, status, balance, customerId));
			}
			return accounts;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean applyBankAccount(int customerId, double balance) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO BANK_ACCOUNT (CUSTOMER_ID, STATUS, BALANCE) VALUES (?, ?, ?)";
		try(Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pStatement = connection.prepareStatement(sql)) {
			pStatement.setInt(1, customerId);
			pStatement.setString(2, "PENDING");
			pStatement.setDouble(3, balance);
			pStatement.executeQuery();
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addressAccount(int accountId, String status) {
		// TODO Auto-generated method stub
		String sql = "UPDATE BANK_ACCOUNT SET STATUS = ? WHERE ACCOUNT_ID = ?";
		try(Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pStatement = connection.prepareStatement(sql)) {
			pStatement.setString(1, status);
			pStatement.setInt(2, accountId);
			pStatement.executeQuery();
			return true;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean makeTransaction(int accountId, double amount, double newBalance, String status) {
		// TODO Auto-generated method stub
		String tSql = "INSERT INTO TRANSACTIONS (ACCOUNT_ID, AMOUNT, STATUS) VALUES (?, ?, ?)" ; 
		String acctSql = "UPDATE BANK_ACCOUNT SET BALANCE = ? WHERE ACCOUNT_ID = ?";
		try(Connection connection = ConnectionUtil.getConnection();
				PreparedStatement trPStatement = connection.prepareStatement(tSql);
				PreparedStatement acctPStatement = connection.prepareStatement(acctSql)) {
			trPStatement.setInt(1, accountId);
			trPStatement.setDouble(2, amount);
			trPStatement.setString(3, status);
			trPStatement.executeQuery();
			acctPStatement.setDouble(1, newBalance);
			acctPStatement.setInt(2, accountId);
			acctPStatement.executeQuery();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		if(status.toLowerCase().equals("approved")) {
			return true;
		} else {
			return false;
		}
		
	}

	@Override
	public List<Transactions> viewTransactions() {
		// TODO Auto-generated method stub
		List<Transactions> transactions = new ArrayList<>();
		String sql = "SELECT * FROM TRANSACTIONS";
		try(Connection connection = ConnectionUtil.getConnection();
				Statement statement = connection.createStatement()) {
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				transactions.add(new Transactions(rs.getInt("TRANSACTION_ID"), rs.getInt("ACCOUNT_ID"), rs.getDouble("AMOUNT"), rs.getString("STATUS")));
			}
			return transactions;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<BankAccount> getUnapprovedAccts(int customerId, List<BankAccount> accts) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM BANK_ACCOUNT WHERE STATUS != 'APPROVED' AND CUSTOMER_ID = ?";
		try(Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pStatement = connection.prepareStatement(sql)) {
			pStatement.setInt(1, customerId);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()) {
				accts.add(new BankAccount(rs.getInt("ACCOUNT_ID"), rs.getString("STATUS"), rs.getDouble("BALANCE"), rs.getInt("CUSTOMER_ID")));
			}
			return accts;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
