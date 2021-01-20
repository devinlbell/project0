package dev.lbell.models;

import java.util.Date;

public class Transactions {
	private int id;
	private String interaction;
	
	public Transactions(int id, String interaction) {
		this.id = id;
		this.interaction = interaction;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getInteraction() {
		return interaction;
	}

	public void setInteraction(String interaction) {
		this.interaction = interaction;
	}
	
	

	
	
}
