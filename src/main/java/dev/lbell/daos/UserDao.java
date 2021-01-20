package dev.lbell.daos;

import java.util.List;

import dev.lbell.models.BankAccount;
import dev.lbell.models.BankUser;
import dev.lbell.models.Customer;

public interface UserDao {
	public BankUser getCustomer(String name, String email);
	public BankUser getEmployee(String name, String email);
	public BankUser addUser(String name, String email, boolean employee);
	public BankAccount getAccount();
	public List<Customer> getCustomers(int employeeId);
	public void insertTransaction(String interaction);

}
