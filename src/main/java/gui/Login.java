package gui;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
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

public class Login extends JFrame {

	private static final long serialVersionUID = 5293085684843654813L;
	private JTextField userField;
	private JTextField passwordField;
	private JLabel register;
	private String baseUrl;

	public Login() {
		super("Login");
		setSize(700,300);	// size of login window
		setLayout(null);	// no default layout is used
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// close operation
		getBaseUrl();
		addTitle();
		addUsername();
		addPassword();
		addButtons();
		addRegistrationLink();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
		setResizable(false);
		setVisible(true);
	}
	
	/**
	 * 	Creating a label to inform the user.
	 */
	private void addTitle(){
		JLabel title=new JLabel("Chess Over Kafka");
		title.setSize(400, 50);
		title.setLocation(300, 15);
		add(title);
	}
	
	/**
	 * 	Label used for Username field.
	 */
	private void addUsername(){
        JLabel username = new JLabel("Username");	// 	adds a Username label
		username.setSize(800,80);			//	setting its size and
		username.setLocation(80,55);		//	location
		userField = new JTextField();		//	adds text field for username
		userField.addKeyListener(new KeyboardListener());	// sets a keylistener for enter
		userField.setColumns(100);			//	adds a username text field
		userField.setSize(400, 30);		//	sets its size and location
		userField.setLocation(200, 80);	//
		add(username);	//	adds username label and field
		add(userField);	//	to this window
	}

	/**
	 * Label user for password field.
	 */
	private void addPassword(){
        JLabel password = new JLabel("Password");	//	adds a password label
		password.setSize(800,80);			//	sets its size and location
		password.setLocation(80,105);
        passwordField = new JPasswordField();	//	adds a password field
        passwordField.addKeyListener(new KeyboardListener());	// adds key listener for enter
        passwordField.setColumns(100);			//	adds password field
        passwordField.setSize(400, 30);		//	sets its size and location
        passwordField.setLocation(200, 130);
		add(password);	//	adds password label and
		add(passwordField);	//	password field to this window
	}

	/**
	 * Buttons of the <code>Login</code> window. 
	 */
	private void addButtons(){
        JButton cancelButton = new JButton("Clear");		//	creates Clear button,
		cancelButton.setSize(100, 30);		//	sets its size and location
		cancelButton.setLocation(250, 180);
		cancelButton.addActionListener(event -> clear());	// sets listener for Cancel button
        JButton submitButton = new JButton("Login");		// 	creates Login button
		submitButton.setSize(100, 30);		
		submitButton.setLocation(400, 180);
		submitButton.addActionListener(event -> enterPressed());	// sets listener for Submit button
		add(cancelButton);	//
		add(submitButton);	// adds Cancel and Sumbit buttons to this window
	}
	
	private void addRegistrationLink() {
		register = new JLabel("<html><u>Click here to register</u></html>");
		register.setForeground(Color.BLUE);
		register.setSize(200,30);
		register.setLocation(300, 230);
		register.addMouseListener(new RegisterListener());
		add(register);
	}

	private void getBaseUrl () {
		try (FileInputStream fileInput = new FileInputStream( new File("src/main/resources/chess/configurations/config.properties"))) {
			Properties properties = new Properties();
			properties.load(fileInput);
			baseUrl = properties.getProperty("restAddress");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Code executed when enter pressed or when Login button pressed
	 */
	private void enterPressed () {
		String username = userField.getText();
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
		objectNode.put("name", username);
		objectNode.put("password", SecurePassword.sha256(passwordField.getText()));
		clear();
		WebResource webResource = Client.create().resource(baseUrl+ "/login");
		ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, objectNode.toString());
		if (response.getStatus() == 200) {
			dispose();
			new Table(username);
		}
		else if (response.getStatus() == 200) {
			JOptionPane.showMessageDialog(this, "Incorrect username or password!\nPlease try again");
		}
		else {
			JOptionPane.showMessageDialog(this, "This user is already logged in\nPlease try again");

		}
    }
	
	/**
	 * On Clear button pressed 
	 */
	private void clear () {
		userField.setText("");
        passwordField.setText("");
	}
	
	/** 
	 * Keyboard listener for <code>Login</code> window.
	 */
	private class KeyboardListener implements KeyListener {
	
		@Override
		public void keyPressed(KeyEvent keyEvent) {
			if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
				enterPressed();
			}
		}

		@Override
		public void keyReleased(KeyEvent keyEvent) {}

		@Override
		public void keyTyped(KeyEvent keyEvent) {}
	}

	private class RegisterListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			dispose();
			new Register();			
		}

		@Override
		public void mouseEntered(MouseEvent mouseEvent) {
			register.setForeground(Color.MAGENTA);
			register.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

		@Override
		public void mouseExited(MouseEvent mouseEvent) {
			register.setForeground(Color.BLUE);
			register.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		@Override
		public void mousePressed(MouseEvent mouseEvent) {}

		@Override
		public void mouseReleased(MouseEvent mouseEvent) {
			dispose();
			new Register();
		}
	}
}