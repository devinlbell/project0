package dev.lbell.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dev.lbell.daos.AccountDao;
import dev.lbell.daos.AccountDaoImpl;
import dev.lbell.daos.UserDao;
import dev.lbell.daos.UserDaoImpl;
import dev.lbell.models.BankAccount;
import dev.lbell.models.Customer;
import dev.lbell.models.Employee;
import dev.lbell.models.Transactions;

public class EmployeeService {
	private UserDao uDao = new UserDaoImpl();
	private AccountDao aDao = new AccountDaoImpl();
	private Scanner scanner = new Scanner(System.in);
	private Employee employee;

	
	public EmployeeService(Employee employee) {
		this.employee = employee;
	}
	
	public void begin() {
		//System.out.println("begin says employee id is " + employee.getId());
		List<Customer> customers = uDao.getCustomers(employee.getId()); 
		System.out.println("[1] View your customers' accounts");
		System.out.println("[2] Evaluate accounts");
		System.out.println("[3] View transactions");
		System.out.println("Please select a number");
		int choice = scanner.nextInt();
		switch(choice) {
			case 1:
				displayCustomers(customers);
				break;
			case 2:
				displayUnapprovedAccounts(customers);
				break;
			case 3:
				displayTransactions();
				break;
		}
	}
	
	private void displayTransactions() {
		// TODO display clean transactions
		List<Transactions> allTransactions = aDao.viewTransactions();
		for(int i = 0; i < allTransactions.size(); i++) {
			System.out.println("[" + i + "] Transaction: " + allTransactions.get(i).getInteraction());
		}
		System.out.println("Would you like to continue working? (y, n)");
		String answer = scanner.next();
		if(answer.toLowerCase().charAt(0) == 'y') {
			begin();
		} else {
			System.out.println("Thank you for all your hard work with our bank. Have an amazing day!");
		}
	}

	private void displayUnapprovedAccounts(List<Customer> customers) {
		//TODO change to map to retain name of customer
		List<BankAccount> accounts = new ArrayList<>();
		for(int i = 0; i < customers.size(); i++) {
		
			//TODO change to map to retain name of customer
			accounts = aDao.getUnapprovedAccts(customers.get(i).getId(), accounts);
		}
		System.out.println("Which account which would you like to change? :");
		for(int i = 0; i < accounts.size(); i++) {
			BankAccount account = accounts.get(i);
			System.out.println("[" + (i+1) + "]: Customer ID: " + account.getCustomerId() + " Status: " + account.getStatus() + " Balance: " + account.getBalance());
		}
		int start = accounts.size() + 1;
		int quit = accounts.size() + 2;
		System.out.println("[" + start +"]: Go back to start");
		System.out.println("[" + quit +"]: Quit");
		System.out.println("Select a number.");
		int choice = scanner.nextInt();
		
		if(choice == start) {
				begin();
		} else if(choice == quit) {
				System.out.println("Thank you for working for us. Have a wonderful day!");
		} else {
			BankAccount toChange = accounts.get(choice - 1);
			System.out.println("How would you like to change this account? :");
			System.out.println("[1] Approve account");
			System.out.println("[2] Reject account");
			System.out.println("Please select a number.");
			choice = scanner.nextInt();
			switch(choice) {
				case 1:
					changeAccountStatus(toChange.getId(), true);
					break;
				case 2:
					changeAccountStatus(toChange.getId(), false);
					break;
			}
			displayUnapprovedAccounts(customers);
		}
			
	}
		
		
	

	public void displayCustomers(List<Customer> customers) {
		for(int i = 0; i < customers.size(); i++) {
			System.out.println("[" + (i+1) +"] : " + customers.get(i).getName());
		}
		System.out.println("Please select a number.");
		int choice = scanner.nextInt();
		displayAccounts(customers.get(choice -1).getId());
	}
	
	public void displayAccounts(int customerId) {
		List<BankAccount> accounts =  aDao.getCustomerAccounts(customerId);
		
		if(accounts.size() > 0) {
			System.out.println("We've found these accounts: ");
			for(int i = 0; i < accounts.size(); i++) {
				System.out.println("Bank Account [" + (i+1) + "]");
			}
			
			System.out.println("Which account would you like to view? (Choose a number)");
			int accountNum = scanner.nextInt();
			viewAccount(accounts.get(accountNum - 1));
		} else {
			System.out.println("We weren't able to find any accounts, for this customer. Would you like to continue working? (y,n)");
			String answer = scanner.next();
			if(answer.toLowerCase().equals("y")){
				begin();
			} else {
				System.out.println("Thank you for working for us. Have a wonderful day!");
			}
		}
	}
	
	public void viewAccount(BankAccount acct) {
		System.out.println("This account is " + acct.getStatus() + " and the balance is $" + acct.getBalance());
		System.out.println("What would you like to do?");
		System.out.println("[1] Approve account");
		System.out.println("[2] Reject account");
		System.out.println("[3] View customer's other accounts");
		System.out.println("[4] Go back to start");
		System.out.println("[5] Quit");
		System.out.println("Please select a number");
		int choice = scanner.nextInt();
		switch(choice) {
		case 1:
			if(changeAccountStatus(acct.getId(), true)) {
				acct.setStatus("APPROVED");
				viewAccount(acct);
				
			}
			break;
		case 2:
			if(changeAccountStatus(acct.getId(), false)) {
				acct.setStatus("REJECTED");
				viewAccount(acct);
			}
			break;
		case 3:
			displayAccounts(acct.getCustomerId());
			break;
		case 4:
			begin();
			break;
		case 5:
			System.out.println("Thank you for working for us. Have a wonderful day!");
			break;
		}
		
	}
	
	public boolean changeAccountStatus(int accountId, boolean approved) {
		if(approved) {
			if(aDao.addressAccount(accountId, "APPROVED")) {
				System.out.println("This account has been approved!");
				return true;
			}
			
		} else {
			if(aDao.addressAccount(accountId, "REJECTED")) {
				System.out.println("This account has been rejected!");
				return true;
			}	
		}
		return false;

	}
}
