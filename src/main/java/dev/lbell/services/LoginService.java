package dev.lbell.services;

import java.util.Scanner;

import dev.lbell.daos.UserDao;
import dev.lbell.daos.UserDaoImpl;
import dev.lbell.models.BankUser;
import dev.lbell.models.Customer;
import dev.lbell.models.Employee;

public class LoginService {
	private UserDao uDao = new UserDaoImpl();
	
	private Scanner scanner = new Scanner(System.in);
	
	public BankUser login() {
		System.out.println("Welcome to BankWithRevature, have you used our service before? (y,n)");
		String answer = scanner.next();
		String name, email, employee;
		
		// login they've used service
		if(answer.toLowerCase().equals("y")) {
			System.out.println("Great we'll need your credentials to login.");
			System.out.println("Please enter your name.");
			name = scanner.next();
			System.out.println("Alright, please enter your email address.");
			email = scanner.next();
			System.out.println("And are you an employee? (y,n)");
			employee = scanner.next();
			// get employee object 
			if(employee.toLowerCase().equals("y")) {
				BankUser user = uDao.getEmployee(name, email);
				if(user != null) {
					System.out.println("Employee account found! Your id is " + user.getId());
					return user;
				} else {
					System.out.println("We couldn't find your account. Sorry, please try again.");
					return null;
				}
			} else { // get customer object
			
				Customer customer = (Customer) uDao.getCustomer(name, email);
				if(customer != null) {
					System.out.println("Thank you. We've found your account. You're representative is " + customer.getEmployeeId());
					return customer;
				} else {
					System.out.println("We couldn't find your account. Sorry, please try again.");
					return null;
				}
				
			}
			//TODO check for an employee account, use regex to match email domain and then use getEmployee method instead
			//check account in database
			
			
		} else if(answer.toLowerCase().equals("n")){ // no account for user
			System.out.println("Would you like to create an account? (y,n)");
			answer = scanner.next();
			if(answer.toLowerCase().equals("y")) { //create an account
				System.out.println("Please enter your name.");
				name = scanner.next();
				System.out.println("Alright, please enter your email address.");
				email = scanner.next();
				System.out.println("And are you an employee? (y,n)");
				employee = scanner.next();
				if(employee.toLowerCase().equals("y")) { //create an employee account
					BankUser user = uDao.addUser(name, email, true);
					System.out.println("Employee account created! Your id is " + user.getId());
					return user;
				} else {
					BankUser user = uDao.addUser(name, email, false);
				}
				// create account in database
				System.out.println("Thank you. We've created your account. Please remember these credentials to login later.");
			} else {
				System.out.println("please enter (y,n)");
			}
		} else  {
			login();
			//return false || restart
		}
		return new Customer();
		
		// return account that gets logged into
	}
}
