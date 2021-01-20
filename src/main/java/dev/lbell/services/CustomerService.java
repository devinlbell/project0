package dev.lbell.services;

import java.util.List;
import java.util.Scanner;

import dev.lbell.daos.AccountDao;
import dev.lbell.daos.AccountDaoImpl;
import dev.lbell.daos.UserDao;
import dev.lbell.daos.UserDaoImpl;
import dev.lbell.models.BankAccount;
import dev.lbell.models.Customer;

public class CustomerService {
	private UserDao uDao = new UserDaoImpl();
	private AccountDao aDao = new AccountDaoImpl();
	private Scanner scanner = new Scanner(System.in);
	private Customer customer;
	
	public CustomerService(Customer customer) {
		this.customer = customer;
	}
	
	public void displayAccounts() {
		List<BankAccount> accounts = aDao.getCustomerAccounts(customer.getId());
		int accountNum;
		if(accounts.size() > 0) {
			for(int i = 0; i < accounts.size(); i++) {
				System.out.println("Bank Account [" + (i+1) + "]");
			}
			
			System.out.println("Which account would you like to view? (Choose a number)");
			accountNum = scanner.nextInt();
			viewAccount(accounts.get(accountNum - 1));
		} else {
			System.out.println("We weren't able to find any accounts, would you like to apply for one? (y,n)");
			String answer = scanner.next();
			if(answer.toLowerCase().equals("y")){
				applyAccount();
			} else {
				System.out.println("Thank you for banking with us. Have a wonderful day!");
			}
		}
	}
	
	public void begin() {
		System.out.println("[1] Apply for a new account");
		System.out.println("[2] View accounts");
		System.out.println("Please select a number");
		char answer = scanner.next().charAt(0);
		switch(answer) {
			case '1':
				applyAccount();
				break;
			case '2':
				displayAccounts();
				break;
		}		
	}
	
	public void applyAccount() {
		System.out.println("Great! How much money would you like to deposit in this new account?");
		double amount = scanner.nextDouble();
		//TODO check for valid initial deposit amounts
		if(aDao.applyBankAccount(customer.getId(), amount)) {
			System.out.println("Awesome! After an employee approves your application, your account will be deposited with $" + amount +
					", and you will be able to make transactions.");
			System.out.println("Would you like to continue using our service? (y,n)");
			String answer = scanner.next();
			
			//might reuse
			if(answer.toLowerCase().equals("y")){
				begin();
			} else {
				System.out.println("Thank you for banking with us. Have a wonderful day!");
			}
			
		} else {
			System.out.println("We failed to apply for an account");
		}
	}
	
	public void viewAccount(BankAccount account) {
		System.out.println("This account is " + account.getStatus() + " and the balance is $" + account.getBalance());
		System.out.println("What would you like to do?");
		System.out.println("[1] Make a deposit");
		System.out.println("[2] Make a withdrawal");
		System.out.println("[3] Go back to your other accounts");
		System.out.println("[4] Quit");
		System.out.println("Please select a number");
		int choice = scanner.nextInt();
		if((account.getStatus().equals("PENDING") || account.getStatus().equals("REJECTED")) && (choice == 1 || choice == 2)) {
			System.out.println("Sorry this account is still pending or ineligible. You won't be able to make any changes to your account at this time");
			System.out.println("What would you like to do?");
			System.out.println("[1] Go back to your other accounts");
			System.out.println("[2] Quit");
			System.out.println("Please select a number");
			choice = scanner.nextInt();
			switch(choice) {
				case 1:
					displayAccounts();
					break;
				case 2:
					System.out.println("Thank you for banking with us. Have a wonderful day!");
					break;
			}
		} else {
			switch(choice) {
				case 1:
					if(deposit(account)) {
						viewAccount(account);
					}
					break;
				case 2:
					if(withdraw(account)) {
						viewAccount(account);
					}
					break;
				case 3:
					displayAccounts();
					break;
				case 4:
					System.out.println("Thank you for banking with us. Have a wonderful day!");
					break;
			}
		}	
	}
	public boolean deposit(BankAccount account) {
		System.out.println("How much money would you like to deposit?");
		double amount = scanner.nextDouble();
		if(aDao.makeTransaction(account.getId(), amount, account.getBalance(), true)) {
			account.setBalance(amount + account.getBalance());
			System.out.println("$" + amount + " has successfully been deposited into your account.");
			return true;
		} else {
			System.out.println("Would you like to continue with making a deposit? (y, n)");
			String answer = scanner.next();
			if(answer.toLowerCase().charAt(0) == 'y') {
				return withdraw(account);
			} else {
				System.out.println("Are you done working with your bank accounts?");
				answer = scanner.next();
				if(answer.toLowerCase().charAt(0) == 'y') {
					System.out.println("Thank you for using our banking service. Have a nice day!");
					return false;
				} else {
					return true;
				}
			}	
		}
//		if(amount > 0) {
//			account.setBalance(account.getBalance() + amount);
//			if(aDao.makeTransaction(account.getId(), amount, account.getBalance(), "APPROVED")) {
//				System.out.println("Congratulations! $" + amount + " has been deposited into your account");
//				return true;
//			} else {
//				System.out.println("Sorry we weren't able to make that deposit. Have a wonderful day!");
//				return false;
//			}
//			
//		} else {
//			System.out.println("Please enter an amount over $0");
//			aDao.makeTransaction(account.getId(), amount, account.getBalance(),"REJECTED");
//			return deposit(account);
//		}
	}
	public boolean withdraw(BankAccount account) {
		System.out.println("How much money would you like to withdraw?");
		double amount = scanner.nextDouble();
		if(aDao.makeTransaction(account.getId(), amount, account.getBalance(), false)) {
			account.setBalance(account.getBalance() - amount);
			System.out.println("$" + amount + " has successfully been withdrawn from your account");
			return true;
		} else {
			System.out.println("Would you like to continue making a withdrawal? (y, n)");
			String answer = scanner.next();
			if(answer.toLowerCase().charAt(0) == 'y') {
				return withdraw(account);
			} else {
				System.out.println("Are you done working with your bank accounts? (y, n)");
				answer = scanner.next();
				if(answer.toLowerCase().charAt(0) == 'y') {
					System.out.println("Thank you for using our banking service. Have a nice day!");
					return false;
				} else {
					return true;
				}
				
			}
			
		}
//		if(amount > 0) {
//			double newBalance = account.getBalance() - amount;
//			
//			if(newBalance < 0) {
//				System.out.println("Withdrawal unsuccessful. There isn't enough money in your account to withdraw that amount");
//				return aDao.makeTransaction(account.getId(), -amount, account.getBalance(), "REJECTED");
//			} else {
//				account.setBalance(newBalance);
//				if(aDao.makeTransaction(account.getId(), -amount, account.getBalance(), "APPROVED")) {
//					System.out.println("$" + amount + " has successfully been withdrawn from your account");
//					return true;
//				} else {
//					System.out.println("Sorry we weren't able to make that withdrawal. Have a wonderful day!");
//					return false;
//				}
//			}
//		} else {
//			System.out.println("Please enter an amount over $0");
//			aDao.makeTransaction(account.getId(), amount, account.getBalance(),"REJECTED");
//			return withdraw(account);
//		}
		
	}
}
