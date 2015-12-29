// CheckbookPanel Class
// Author: Matthew Cucuzza 
// 
// Controller panel class which essentially manages the display of the checkbook 
// and all of the data inside of the checkbook 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;


public class CheckbookPanel extends JPanel {
	
	// Variables 
	private String fileName = "CheckbookInfo.txt";
	private String[] labels = {"ID", "Date", "Type", "Name", "Amount"}; 
	private Vector<String> dataList;
	private JPanel viewTab, fileTab, saveTab; 
	private double accountBalance = 0.00; 
	private JPanel emptyPanel, topPanel, midPanel, bottomPanel, addPanel; 
	private JPanel saveLoadBPanel, helpPanel; 
	private JLabel idLabel, dateLabel, typeLabel, nameLabel, amountLabel;
	private JLabel balanceLabel; 
	private JLabel helpLabel; 
	private JLabel numErrorLabel; 
	private JTable table; 
	private DefaultTableModel model; 
	private JTextField idField, dateField, nameField, amountField;
	private JRadioButton withdrawButton, depositButton; 
	private JButton removeButton, addButton, saveButton, loadButton, errorButton; 
	private String idNumber, date, type, name, amount;
	private double tempAmount; 
	final static int extraWindowWidth = 350;
	DecimalFormat numberFormat = new DecimalFormat("#.00");
	
	// Constructor 
	public CheckbookPanel() { 
		// Create the layout for the three tabs
		JPanel tabPanel = new JPanel(); 
		GridLayout grid = new GridLayout(0,1);
		
		// Initialize the three tabs 
		viewTab = new JPanel() {
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
		fileTab = new JPanel(); 
		saveTab = new JPanel(); 
		
		// Set the layouts for the tab panel, and each of the tab pages  
		viewTab.setLayout(new BorderLayout());
		tabPanel.setLayout(new BorderLayout());
		fileTab.setLayout(grid);
		saveTab.setLayout(grid);
		
		// JTable creation  
		table = new JTable(); 
		model = new DefaultTableModel();
		model.setColumnIdentifiers(labels);
		table.setModel(model);
		table.setAutoCreateRowSorter(true);
		table.setGridColor(Color.BLACK);
		
		// Create the remove button and the balance label at the bottom of the View Panel 
		removeButton = new JButton("Remove Transaction");
		removeButton.setPreferredSize(new Dimension(50,50));
		viewTab.add(removeButton, BorderLayout.SOUTH);
		balanceLabel = new JLabel("Total Balance: " + this.getBalance()); 
		viewTab.add(balanceLabel, BorderLayout.EAST);
		
		// Create the sub panel in which the save/load buttons are on 
		saveLoadBPanel = new JPanel();
		helpPanel = new JPanel();
		saveTab.add(saveLoadBPanel);
		saveTab.add(helpPanel);
		

		ImageIcon icon = createImageIcon("help.png","help icon");
		helpLabel = new JLabel(icon);
		helpLabel.setPreferredSize(new Dimension(150,150));
		helpLabel.setOpaque(true);
		helpPanel.add(helpLabel);
		
		// Creates "File a Transaction" form sub panels
		emptyPanel = new JPanel(); 
		topPanel = new JPanel();
		midPanel = new JPanel();
		bottomPanel = new JPanel();
		addPanel = new JPanel(); 
		fileTab.add(emptyPanel);
		fileTab.add(topPanel);
		fileTab.add(midPanel);
		fileTab.add(bottomPanel);
		fileTab.add(addPanel);
		
		numErrorLabel = new JLabel("Error: No number was inputed where required, try again!");
		emptyPanel.add(numErrorLabel);
		numErrorLabel.setVisible(false);
		
		// Adds the form to each of the subpanels under File a Transaction 
		topPanel.add(idLabel = new JLabel("ID Number: "));
		topPanel.add(idField = new JTextField(10));
		topPanel.add(dateLabel = new JLabel("Date: "));
		topPanel.add(dateField = new JTextField(8));
		midPanel.add(typeLabel = new JLabel("Type of Transaction: "));
		ButtonGroup buttonGroup = new ButtonGroup(); 
		midPanel.add(withdrawButton = new JRadioButton("Withdrawal"));
		midPanel.add(depositButton = new JRadioButton("Deposit"));
		buttonGroup.add(withdrawButton);
		buttonGroup.add(depositButton);
		bottomPanel.add(nameLabel = new JLabel("Name: "));
		bottomPanel.add(nameField = new JTextField(20));
		bottomPanel.add(amountLabel = new JLabel("Amount: "));
		bottomPanel.add(amountField = new JTextField(20));
		addButton = new JButton("Add Transaction");
		errorButton = new JButton("Remove Error Message");
		addPanel.add(addButton);
		addPanel.add(errorButton);
		
		// Save and load buttons created and added to subpanel  
		saveButton = new JButton("Save File");
		loadButton = new JButton("Load File");
		saveLoadBPanel.add(saveButton);
		saveLoadBPanel.add(loadButton);
	
		// This line makes the table scrollable 
		viewTab.add(new JScrollPane(table), BorderLayout.NORTH);

		// Creates each of the tabbed pages 
		this.add(tabPanel);
		JTabbedPane tabs = new JTabbedPane(); 
		tabs.add("View All Transactions", viewTab); 
		tabs.add("File a Transaction", fileTab);
		tabs.add("File Manager & Help", saveTab);
		
		// Add the tabs to the top of the panel 
		this.add(tabs);
			
		// Add Transaction button action listener 
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{ 
					// Gets the information inputed by the user and resets the forms 
					dataList = new Vector<String>(100);
					idNumber = idField.getText();
					idField.setText("");
					date = dateField.getText();
					dateField.setText("");
					tempAmount = Math.abs(Double.parseDouble(amountField.getText()));
					amount = numberFormat.format(tempAmount);
					amountField.setText("");
					
					// Checks which button is selected and sets the number negative if withdrawing money
					if (withdrawButton.isSelected()) { 
						type = "Withdraw"; 
						buttonGroup.clearSelection();
						tempAmount = (tempAmount * -1); 
						amount = Double.toString(tempAmount);
						calculateBalance(tempAmount);
					} else if (depositButton.isSelected()) {
						type = "Deposit"; 
						calculateBalance(tempAmount);
						buttonGroup.clearSelection();
					}
					
					name = nameField.getText();
					nameField.setText("");
					
					// Adds the variables to the String vector, posts them into the table, and updates balance
					dataList.add(idNumber); 
					dataList.add(date);
					dataList.add(type);
					dataList.add(name); 
					dataList.add(amount);
					model.addRow(dataList);
					balanceLabel.setText("Total Balance: " + numberFormat.format(accountBalance)); 
				} catch(NumberFormatException n) { 
					numErrorLabel.setVisible(true);					
					System.out.println("No number was inputed where required, try again!");
					amountField.setText("");
				} 
			}
		});
		
		// Remove Transaction button action listener 
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Selected row has been removed!");
				
				// Removes row and recalculates the account balance 
				double amount = Double.parseDouble((String) model.getValueAt(table.getSelectedRow(),4));
				model.removeRow(table.getSelectedRow());
				double curValue = Double.parseDouble(balanceLabel.getText().substring(15));
				double newValue = curValue-amount;
				balanceLabel.setText("Total Balance: " + numberFormat.format(newValue));
			}
		});	
		
		// Load Button action Listener 
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				System.out.println("----FILE LOADED----");
				
				Account account = new Account(); 
				account.loadFile();
				
				// Loops through the list of Transactions from the Account class
				// and inputs it into the JTable 
				for (Transaction d: account.getList()) {
					model.addRow(new String[]{d.getIDNumber(),
							d.getDate(),d.getType(),d.getName(),Double.toString(d.getAmount())});
					calculateBalance(d.getAmount());
					balanceLabel.setText("Total Balance: " + numberFormat.format(accountBalance)); 
				}
			}
		});
		
		// Save Button action listener 
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				System.out.println("----FILE SAVED----");
				try { 
					saveFile();
				} catch (Exception f) { 
					f.printStackTrace();
				}
			}
		});
		
		// Error Button removes the error if a number isn't inputed 
		errorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				numErrorLabel.setVisible(false);
			}
		});
		
		// Mouse Listener for the Help button 
		helpLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
            	
            	// Creates a new Help Menu 
            	JFrame frame = new JFrame("Checkbook Help Menu");
            	JPanel frameHelpPanel = new JPanel(new BorderLayout());
            	GridLayout grid = new GridLayout(0,1);
            	JPanel explainPanel = new JPanel(grid);
            	frame.add(frameHelpPanel);
            	frame.setSize(550, 350);
            	frameHelpPanel.setBackground(Color.WHITE);
                frame.setVisible(true); 
                frame.setResizable(false);
                
            	frameHelpPanel.add(explainPanel);
                JLabel buttons = new JLabel("What Each Button Does", SwingConstants.CENTER);
                Font normalFont = buttons.getFont();
                Font boldFont = new Font(normalFont.getFontName(), Font.BOLD, normalFont.getSize());
                buttons.setFont(boldFont);
                
                // Labels explaining what each button does 
                JLabel remove = new JLabel("Remove Transaction................" + "Removes a row from the table");
                JLabel add = new JLabel("Add Transaction......................" +  "Adds a transaction to the table"); 
                JLabel errorMessage = new JLabel("Remove Error Message..........." + 
                		"Removes the number error from the top of the screen"); 
                JLabel save = new JLabel("Save File................................." +
                		"Save the table into a text file"); 
                JLabel load = new JLabel("Load File................................." +
                		"Load the previously saved text file"); 
                
                explainPanel.setBackground(Color.WHITE);
                frameHelpPanel.add(buttons, BorderLayout.NORTH);
                explainPanel.add(remove, BorderLayout.SOUTH);
                explainPanel.add(add, BorderLayout.SOUTH);
                explainPanel.add(errorMessage, BorderLayout.SOUTH);
                explainPanel.add(save, BorderLayout.SOUTH);
                explainPanel.add(load, BorderLayout.SOUTH);  
            }
		});
	}
	
	
	// Calculate the balance of the checkbook 
	private double calculateBalance(double amount) { 
		if (accountBalance == 0) { 
			accountBalance = amount; 
		} else accountBalance = amount + accountBalance;
		System.out.println("Account Balance: " + numberFormat.format(accountBalance));
		return accountBalance; 
	}
	
	public double getBalance() { 
		return this.accountBalance;
	}
	
	// Save the information in the checkbook application into a file 
	public void saveFile() throws IOException { 
		try { 
			File file = new File(fileName);
			PrintWriter writer = new PrintWriter(new FileOutputStream(file, false));
			
			writer.println(model.getRowCount());
			for(int i = 0;i < model.getRowCount();i++){
				writer.println(model.getValueAt(i, 0));
				writer.println(model.getValueAt(i, 1));
				writer.println(model.getValueAt(i, 2));
				writer.println(model.getValueAt(i, 3));
				writer.println(model.getValueAt(i, 4));
			}
			writer.close();
		} catch (FileNotFoundException e) { 
			e.printStackTrace();
		}
	}
	
	// Creates a path for the help image icon 
	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
} // end class