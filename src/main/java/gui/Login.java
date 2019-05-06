package gui;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
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
import security.RestServiceURL;
import security.SecurePassword;

public class Login extends JFrame {

	private static final long serialVersionUID = -4637751738234470485L;
	private JTextField userField;
	private JTextField passwordField;
	private JLabel register;
	private String baseUrl;

	public Login() {
		super("Login");
		setSize(700,300);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
		constructGraphicDetails();
		setResizable(false);
		setVisible(true);
		getBaseUrl();
	}

	private void constructGraphicDetails() {
		addTitle();
		addUsername();
		addPassword();
		addButtons();
		addRegistrationLink();
	}

	/**
	 * 	Creating a label to inform the user.
	 */
	private void addTitle(){
		JLabel title = new JLabel("Chess Over Kafka");
		title.setSize(400, 50);
		title.setLocation(300, 15);
		add(title);
	}

	/**
	 * 	Label used for Username field.
	 */
	private void addUsername(){
		JLabel username = new JLabel("Username");
		username.setSize(800,80);
		username.setLocation(80,55);
		userField = new JTextField();
		userField.addKeyListener(new KeyAdapterListener());
		userField.setColumns(100);
		userField.setSize(400, 30);
		userField.setLocation(200, 80);
		add(username);
		add(userField);
	}

	/**
	 * Label user for password field.
	 */
	private void addPassword(){
		JLabel password = new JLabel("Password");
		password.setSize(800,80);
		password.setLocation(80,105);
		passwordField = new JPasswordField();
		passwordField.addKeyListener(new KeyAdapterListener());
		passwordField.setColumns(100);
		passwordField.setSize(400, 30);
		passwordField.setLocation(200, 130);
		add(password);
		add(passwordField);
	}


	private void addCancelButton () {
		JButton cancelButton = new JButton("Clear");
		cancelButton.setSize(100, 30);
		cancelButton.setLocation(250, 180);
		cancelButton.addActionListener(event -> clear());
		add(cancelButton);
	}

	private void addSubmitButton () {
		JButton submitButton = new JButton("Login");
		submitButton.setSize(100, 30);
		submitButton.setLocation(400, 180);
		submitButton.addActionListener(event -> enterPressed());
		add(submitButton);
	}
	/**
	 * Buttons of the <code>Login</code> window. 
	 */
	private void addButtons(){
		addCancelButton ();
		addSubmitButton ();
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
		baseUrl = RestServiceURL.getInstance ().getBaseUrl();
	}

	/**
	 * Code executed when enter pressed or when Login button pressed
	 */
	private void enterPressed () {
		String username = userField.getText();
		String password = passwordField.getText();
		clear();
		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Empty username or password field\nPlease try again");
		}
		else {
			ObjectNode node = new ObjectMapper().createObjectNode().put("name", username).put("password", SecurePassword.sha256(password));
			WebResource webResource = Client.create().resource(baseUrl + "/login");
			switch (webResource.accept("application/json").type("application/json").post(ClientResponse.class, node.toString()).getStatus()) {
			case 200:	
				dispose();
				new Table(username);
				break;
			case 400:
				JOptionPane.showMessageDialog(this, "Incorrect username or password\nPlease try again");
				break;
			case 401:
				JOptionPane.showMessageDialog(this, "This user is already logged in\nPlease try again");
				break;
			default:
				JOptionPane.showMessageDialog(this, "An error occurred\nPlease try again");
			}
		}
	}

	/**
	 * On Clear button pressed 
	 */
	private void clear () {
		userField.setText("");
		passwordField.setText("");
		userField.requestFocus();
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

	private class RegisterListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			openRegisterWindow();
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
			openRegisterWindow();
		}

		private void openRegisterWindow () {
			dispose();
			new Register();
		}
	}
}