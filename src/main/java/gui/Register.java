package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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


class Register extends JFrame {

	private static final long serialVersionUID = -7447484008923022852L;
	private JTextField userField;
	private JTextField passwordField;
	private JTextField repeatPasswordField;

	Register() {
		super("Register");
		setSize(700,300);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
		constructGraphicDetails();
		setResizable(false);
		setVisible(true);
	}

	private void constructGraphicDetails() {
		addTitle();
		addUsername();
		addPassword();
		addRepeatPassword();
		addButtons();
	}

	/**
	 * 	Creating a label to inform the user.
	 */
	private void addTitle(){
		JLabel title = new JLabel("Register to use app");
		title.setSize(400, 50);
		title.setLocation(300, 15);
		add(title);
	}

	/**
	 * 	Label used for Username field.
	 */
	private void addUsername(){
		JLabel username = new JLabel("Pick username");
		username.setSize(800,80);
		username.setLocation(60,55);
		add(username);
		userField = new JTextField();
		userField.addKeyListener(new KeyAdapterListener());
		userField.setColumns(100);
		userField.setSize(400, 30);
		userField.setLocation(250, 80);	//
		add(userField);
	}

	/**
	 * Label user for password field.
	 */
	private void addPassword(){
		JLabel password = new JLabel("Type your password");
		password.setSize(800,90);
		password.setLocation(60,90);
		add(password);
		passwordField = new JPasswordField();
		passwordField.addKeyListener(new KeyAdapterListener());
		passwordField.setColumns(100);
		passwordField.setSize(400, 30);
		passwordField.setLocation(250, 120);
		add(passwordField);
	}

	/**
	 * Label user for password field.
	 */
	private void addRepeatPassword(){
		JLabel repeatPassword = new JLabel("Re-type your password");
		repeatPassword.setSize(800,90);
		repeatPassword.setLocation(60,130);
		add(repeatPassword);
		repeatPasswordField = new JPasswordField();
		repeatPasswordField.addKeyListener(new KeyAdapterListener());
		repeatPasswordField.setColumns(100);
		repeatPasswordField.setSize(400, 30);
		repeatPasswordField.setLocation(250, 160);
		add(repeatPasswordField);
	}

	private void addClearButton(){
		JButton clearButton = new JButton("Clear");
		clearButton.setSize(100, 30);
		clearButton.setLocation(400, 220);
		clearButton.addActionListener(event -> clear());
		add(clearButton);
	}

	private void addRegisterButton(){
		JButton registerButton = new JButton("Register");
		registerButton.setSize(100, 30);
		registerButton.setLocation(550, 220);
		registerButton.addActionListener(event -> enterPressed());
		add(registerButton);
	}

	private void addBackButton(){
		JButton backButton = new JButton("< Back");
		backButton.setSize(100, 30);
		backButton.setLocation(60, 220);
		backButton.addActionListener(event -> back());
		add(backButton);
	}

	/**
	 * Buttons of the <code>Login</code> window. 
	 */
	private void addButtons(){
		addClearButton();
		addRegisterButton();
		addBackButton();
	}

	/**
	 * Code executed when enter pressed or when Login button pressed
	 */
	private void enterPressed () {
		String username = userField.getText();
		String password = passwordField.getText();
		String repeatPassword = repeatPasswordField.getText();
		clear();
		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Username or password field cannot be empty\nPlease try again");
		}
		else if (!password.equals(repeatPassword)) {
			JOptionPane.showMessageDialog(this, "Passwords need to match\nPlease try again");
		}
		else {
			ObjectNode objectNode = new ObjectMapper().createObjectNode();
			objectNode.put("name", username);
			objectNode.put("password", SecurePassword.sha256(password));
			try {
				ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			    InputStream input = classloader.getResourceAsStream("config.properties");
			    Properties properties = new Properties();
				properties.load(input);
				WebResource webResource = Client.create().resource(properties.getProperty("restAddress") + "/register");
				ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, objectNode.toString());
				if (response.getStatus() == 200) {
					JOptionPane.showMessageDialog(this, "Registration succeeded");
					back();
				}
				else {
					JOptionPane.showMessageDialog(this, "Username already exists\nPlease try again");
				}
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
		userField.setText("");
		passwordField.setText("");
		repeatPasswordField.setText("");
		userField.requestFocus();
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
	private class KeyAdapterListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent keyEvent) {
			if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
				enterPressed();
			}
		}
	}
}