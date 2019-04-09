package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import security.SecurePassword;


public class Register extends JFrame {

	/**
	 * identifier for this window.
	 */
	private static final long serialVersionUID = 4110874552205024327L;
	private JLabel title;
	private JLabel username;
	private JTextField userfield;
	private JLabel password;
	private JTextField passfield;
	private JLabel repeatPassword;
	private JTextField repeatPassfield;
	private JButton clearButton;
	private JButton registerButton;
	private JButton backButton;


	public Register() {
		super("Register");
		setSize(700,300);	// size of register window
		setLayout(null);	// no default layout is used
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// close operation
		addTitle();
		addUsername();
		addPassword();
		addRepeatPassword();
		addButtons();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				// program ends if register window is closed
				System.exit(0);
			}        
		}); 
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
		setResizable(false);
		
		setVisible(true);
	}
	
	/**
	 * 	Creating a label to inform the user.
	 */
	private void addTitle(){
		title=new JLabel("Register to use app");
		title.setSize(400, 50);
		title.setLocation(300, 15);
		add(title);
	}
	
	/**
	 * 	Label used for Username field.
	 */
	private void addUsername(){
		username = new JLabel("Pick username");	// 	adds a Username label
		username.setSize(800,80);			//	setting its size and
		username.setLocation(60,55);		//	location
		userfield=new JTextField();		//	adds text field for username
		userfield.addKeyListener(new KeyboardListener());	// sets a keylistener for enter
		userfield.setColumns(100);			//	adds a username text field
		userfield.setSize(400, 30);		//	sets its size and location
		userfield.setLocation(200, 80);	//
		add(username);	//	adds username label and field
		add(userfield);	//	to this window
	}
	
	/**
	 * Label user for password field.
	 */
	private void addPassword(){
		password = new JLabel("Type your password");	//	adds a password label
		password.setSize(800,90);			//	sets its size and location
		password.setLocation(60,90);
		passfield=new JPasswordField();	//	adds a password field
		passfield.addKeyListener(new KeyboardListener());	// adds key listener for enter
		passfield.setColumns(100);			//	adds password field
		passfield.setSize(400, 30);		//	sets its size and location
		passfield.setLocation(200, 120);
		add(password);	//	adds password label and
		add(passfield);	//	password field to this window
	}

	/**
	 * Label user for password field.
	 */
	private void addRepeatPassword(){
		repeatPassword = new JLabel("Re-type your password");	//	adds a password label
		repeatPassword.setSize(800,90);			//	sets its size and location
		repeatPassword.setLocation(60,130);
		repeatPassfield=new JPasswordField();	//	adds a password field
		repeatPassfield.addKeyListener(new KeyboardListener());	// adds key listener for enter
		repeatPassfield.setColumns(100);			//	adds password field
		repeatPassfield.setSize(400, 30);		//	sets its size and location
		repeatPassfield.setLocation(200, 160);
		add(repeatPassword);	//	adds password label and
		add(repeatPassfield);	//	password field to this window
	}
	
	/**
	 * Buttons of the <code>Login</code> window. 
	 */
	private void addButtons(){
		clearButton= new JButton("Clear");		//	creates Clear button,
		clearButton.setSize(100, 30);		//	sets its size and location
		clearButton.setLocation(350, 220);
		clearButton.setActionCommand("Clear");	// sets action command for Cancel button
		clearButton.addActionListener(new RegisterListeners());	// sets listener for Cancel button
		registerButton = new JButton("Register");		// 	creates Login button
		registerButton.setSize(100, 30);		
		registerButton.setLocation(500, 220);
		registerButton.setActionCommand("Register");	// sets action command for Sumbit button
		registerButton.addActionListener(new RegisterListeners());	// sets listener for Submit button
		backButton= new JButton("< Back");		//	creates Back button,
		backButton.setSize(100, 30);		//	sets its size and location
		backButton.setLocation(60, 220);
		backButton.setActionCommand("Back");	// sets action command for Back button
		backButton.addActionListener(new RegisterListeners());	// sets listener for Back button

		add(clearButton);
		add(registerButton);
		add(backButton); // adds Cancel and Sumbit buttons to this window


	}
	
	/**
	 * Code executed when enter pressed or when Login button pressed
	 */
	private void enterPressed () {

		if (userfield.getText().isEmpty()) {
			clear();
			JOptionPane.showMessageDialog(this, "Username connot be empty\nPlease try again");
		}
		else if (!passfield.getText().equals(repeatPassfield.getText())) {
			clear();
			JOptionPane.showMessageDialog(this, "Passwords need to match\nPlease try again");
		}
		else {
	        ObjectNode objectNode = new ObjectMapper().createObjectNode();
			objectNode.put("name", userfield.getText());
			objectNode.put("password",  SecurePassword.sha256(passfield.getText()));
			System.out.println(objectNode.toString());
			clear();
			try (FileInputStream fileInput = new FileInputStream( new File("src/main/resources/chess/configurations/config.properties"))) {
				Properties properties = new Properties();
				properties.load(fileInput);
				final String restAddress = properties.getProperty("restAddress");
				WebResource webResource = Client.create().resource(restAddress + "/register");
				ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, objectNode.toString());
				System.out.println(response.getStatus());
				System.out.println(response.getEntity(String.class));
				if (response.getStatus() == 200) {
					JOptionPane.showMessageDialog(this, "Registration succeeded");
					back();
				}
				else {
					JOptionPane.showMessageDialog(this, "Username already exists\nPlease try again");
				}
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * On Clear button pressed 
	 */
	private void clear () {
		userfield.setText("");
		passfield.setText("");
		repeatPassfield.setText("");
	}
	
	/**
	 * On Back button pressed 
	 */
	private void back() {
		dispose();
		new Login();
	}
	
	/** 
	 * Keyboard listener for <code>Login</code> window.
	 */
	private class KeyboardListener implements KeyListener {
	
		@Override
		public void keyPressed(KeyEvent keyEvent) {
			if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
				enterPressed();
		}

		@Override
		public void keyReleased(KeyEvent keyEvent) {}

		@Override
		public void keyTyped(KeyEvent keyEvent) {}
	}
	
	/** 
	 * Listener for Register and Clear buttons.
	 * http://www.tutorialspoint.com/swing/swing_event_handling.htm
	 */
	private class RegisterListeners implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if (actionEvent.getActionCommand().equals("Register"))
				enterPressed();
			else if (actionEvent.getActionCommand().equals("Clear"))
				clear();
			else
				back();
		}
	}
	
}