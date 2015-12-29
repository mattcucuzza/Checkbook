// CheckbookGUI Class
// Author: Matthew Cucuzza 
// 
// This class is used to create a Transaction to be stored in the Checkbook Application 

public class Transaction  {
	
	// Variables 
	private String ID; 
	private String date; 
	private String type, name;
	private double amount; 
	
	// Constructor initalizing variables 
	public Transaction(String ID, String date, String type, String name, double amount) { 
		this.ID = ID; 
		this.date = date; 
		this.type = type; 
		this.name = name;
		this.amount = amount; 
	}

	// Getter and setters are used to retrieve and set the attributes a Transaction
	
	public String getName() {
		return name; 
	}

	public void setName(String name) {
		this.name = name; 
	}

	
	public double getAmount() {
		return amount; 
	}

	public void setAmount(double amount) {
		this.amount = amount; 
	}

	public String getDate() {
		return date; 
	}

	public void setDate(String date) {
		this.date = date; 
	}

	public String getIDNumber() {
		return ID; 
	}

	public void setIDNumber(String ID) {
		this.ID = ID; 
	}
	
	public String getType() { 
		return type; 
	}
	
	public void setType(String type) { 
		this.type = type; 
	}
}
