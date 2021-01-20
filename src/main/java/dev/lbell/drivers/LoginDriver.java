package dev.lbell.drivers;

import org.apache.log4j.Logger;

import dev.lbell.models.BankUser;
import dev.lbell.models.Customer;
import dev.lbell.models.Employee;
import dev.lbell.services.CustomerService;
import dev.lbell.services.EmployeeService;
import dev.lbell.services.LoginService;

public class LoginDriver {
	private static Logger log = Logger.getRootLogger();
	
	private static void start() {
		LoginService s = new LoginService();
		BankUser user = s.login();
		try {
			if(user.isEmployee()) {
				System.out.println("You're an employee! What would you like to do?" );
				EmployeeService es = new EmployeeService((Employee)user);
				es.begin();
				// get all users with employee's id
				// get user bank accounts that are pending
				// get all transactions
				
			} else {
				System.out.println("Welcome back! What would you like to do?" );
				CustomerService cs = new CustomerService((Customer)user);
				cs.begin();
				// apply for a bank account
				// view accounts and balance
					// make a transaction to account
			}
		} catch (Exception e) {
			log.warn(e);
			start();
		}
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		start();
		
	}


}
