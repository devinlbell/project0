package dev.lbell.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dev.lbell.models.BankAccount;
import dev.lbell.models.BankUser;
import dev.lbell.models.Customer;
import dev.lbell.models.Employee;
import dev.lbell.util.ConnectionUtil;

public class UserDaoImpl implements UserDao{

	@Override
	public BankUser getCustomer(String name, String email) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM CUSTOMERS WHERE NAME = ? AND EMAIL = ?";
		try(Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pStatement = connection.prepareStatement(sql)) {
			pStatement.setString(1, name);
			pStatement.setString(2, email);
			ResultSet rs = pStatement.executeQuery();
			if(rs.next()) {
				BankUser customer = new Customer(rs.getInt("CUSTOMER_ID"), name, email, rs.getInt("EMPLOYEE_ID"));
				customer.setEmployee(false);
				return customer;
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public BankUser addUser(String name, String email, boolean employee) {
		String sql, idSql;
		if(employee) {
			// create&add name, email to new employee row
			sql = "INSERT INTO EMPLOYEE (NAME, EMAIL) VALUES (?, ?)";
			idSql = "SELECT MAX(EMPLOYEE_ID) FROM EMPLOYEE";
			try(Connection connection = ConnectionUtil.getConnection();
					PreparedStatement pStatement = connection.prepareStatement(sql);
					Statement idStatement = connection.createStatement()) {
				pStatement.setString(1, name);
				pStatement.setString(2, email);
				pStatement.executeQuery();
				ResultSet rs = idStatement.executeQuery(idSql);
				if(rs.next()) {
					int id = rs.getInt(1);
					System.out.println("The id is: " + id);
					BankUser employeeAcct = new Employee(id, name, email);
					employeeAcct.setEmployee(true);
					return employeeAcct;
				} 
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		} else {
			// select random employeeid from employee table
			// create&add name, email, employeeID into new customers row
			String empIdSql = "SELECT EMPLOYEE_ID FROM (SELECT EMPLOYEE_ID FROM EMPLOYEE ORDER BY dbms_random.value) WHERE rownum = 1"; 
			sql ="INSERT INTO CUSTOMERS (NAME, EMAIL, EMPLOYEE_ID)  VALUES (?, ?, ?)";
			idSql = "SELECT MAX(CUSTOMER_ID) FROM CUSTOMERS";
			try(Connection connection = ConnectionUtil.getConnection();
					PreparedStatement pStatement = connection.prepareStatement(sql);
					Statement idStatement = connection.createStatement();
					Statement empIdStatement = connection.createStatement()) {
				ResultSet rs = empIdStatement.executeQuery(empIdSql);
				int empId = 0;
				if(rs.next()) {
					empId = rs.getInt(1);
					System.out.println("The empid is: " + empId);
					
				} 
				pStatement.setString(1, name);
				pStatement.setString(2, email);
				pStatement.setInt(3, empId);
				pStatement.executeQuery();
				rs = idStatement.executeQuery(idSql);
				if(rs.next()) {
					int id = rs.getInt(1);
					System.out.println("The regular id is: " + id);
					BankUser custAcct = new Customer(id, name, email, empId);
					custAcct.setEmployee(false);
					return custAcct;
				} 
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BankAccount getAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BankUser getEmployee(String name, String email) {
		String sql = "SELECT * FROM EMPLOYEE WHERE NAME = ? AND EMAIL = ?";
		try(Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pStatement = connection.prepareStatement(sql)) {
			pStatement.setString(1, name);
			pStatement.setString(2, email);
			ResultSet rs = pStatement.executeQuery();
			if(rs.next()) {
				BankUser employee = new Employee(rs.getInt("EMPLOYEE_ID"), name, email);
				employee.setEmployee(true);
				return employee;
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Customer> getCustomers(int employeeId) {
		// TODO Auto-generated method stub
		List<Customer> customers = new ArrayList<>();
		String sql = "SELECT * FROM CUSTOMERS WHERE EMPLOYEE_ID = ?";
		try(Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pStatement = connection.prepareStatement(sql)) {
			pStatement.setInt(1, employeeId);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()) {
				Customer customer = new Customer(rs.getInt("CUSTOMER_ID"), rs.getString("EMAIL"), rs.getString("NAME"), employeeId);
				customers.add(customer);
			}
			return customers;
		} catch(SQLException e) {
			e.printStackTrace();
		}

		
		return null;
	}
	

}
