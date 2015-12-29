// Account Class
// Author: Matthew Cucuzza 
// 
// Manager class which loads a file into the Checkbook application 

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Account {
	
	// Vector of type Transaction in which the data from the file will be stored 
	private static Vector<Transaction> fileList; 
	
	// FileName could be changed to load a new file
	private String fileName = "CheckbookInfo.txt";
	
	// Constructor initializing the vector 
	public Account() { 
		fileList = new Vector<Transaction>(100);
	}

	// Getter in order to retrieve the Vector of Transactions 
	public Vector<Transaction> getList(){
		return fileList;
	}
	
	// Method to load a file into the Checkbook Application 
	public void loadFile() { 
		try { 
			// FileReader and BufferedReader are used to read in a file 
			FileReader fileReader = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fileReader);
	    
			// Parse the string into an int to declare how many Transactions will be loaded in
			int num = Integer.parseInt(br.readLine());
			
			// Create a Vector of Transaction objects from the file by looping 
			for(int i = 0; i < num; i++){
				String id = br.readLine();
				String date = br.readLine();
				String type = br.readLine();
				String name = br.readLine();
				double amount = Double.parseDouble(br.readLine());
				Transaction transaction = new Transaction(id, date, type, name, amount);
				fileList.add(transaction);
			}
	    		fileReader.close();
			} catch (IOException e) { 
				e.printStackTrace(System.err);
			}
	}
}
