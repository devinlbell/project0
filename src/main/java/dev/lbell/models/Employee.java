package dev.lbell.models;

public class Employee extends BankUser {
	
	public Employee(int id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}
	
	@Override
	public void viewAccount() {
		// TODO Auto-generated method stub
		
	}

}
