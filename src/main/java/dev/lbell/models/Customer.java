package dev.lbell.models;

public class Customer extends BankUser {
	private int employeeId;
	
	public Customer() {
		super();
	}
	
	public Customer(int id,String email, String name, int employeeId) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.employeeId = employeeId;
	}
	
	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	// Retrieves account and displays balance
	public void viewAccount() {
		// TODO Auto-generated method stub
		
		System.out.println("This is your account");
		
	}

}
