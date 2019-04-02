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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import security.SecurePassword;

public class Login extends JFrame {

	private static final long serialVersionUID = 5293085684843654813L;
	private JLabel title;
	private JLabel username;
	private JTextField userfield;
	private JLabel password;
	private JTextField passfield;
	private JLabel register;
	private JButton cancelButton;
	private JButton submitButton;

	public Login() {
		super("Login");
		setSize(700,300);	// size of login window
		setLayout(null);	// no default layout is used
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// close operation
		addTitle();
		addUsername();
		addPassword();
		addButtons();
		addRegistrationLink();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				// program ends if Login window is closed
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
		title=new JLabel("Chess Over Kafka");
		title.setSize(400, 50);
		title.setLocation(300, 15);
		add(title);
	}
	
	/**
	 * 	Label used for Username field.
	 */
	private void addUsername(){
		username = new JLabel("Username");	// 	adds a Username label
		username.setSize(800,80);			//	setting its size and
		username.setLocation(80,55);		//	location
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
		password = new JLabel("Password");	//	adds a password label
		password.setSize(800,80);			//	sets its size and location
		password.setLocation(80,105);
		passfield=new JPasswordField();	//	adds a password field
		passfield.addKeyListener(new KeyboardListener());	// adds key listener for enter
		passfield.setColumns(100);			//	adds password field
		passfield.setSize(400, 30);		//	sets its size and location
		passfield.setLocation(200, 130);	
		add(password);	//	adds password label and
		add(passfield);	//	password field to this window
	}

	/**
	 * Buttons of the <code>Login</code> window. 
	 */
	private void addButtons(){
		cancelButton= new JButton("Clear");		//	creates Clear button,
		cancelButton.setSize(100, 30);		//	sets its size and location
		cancelButton.setLocation(250, 180);
		cancelButton.setActionCommand("Clear");	// sets action command for Cancel button
		cancelButton.addActionListener(new LoginListeners());	// sets listener for Cancel button
		submitButton = new JButton("Login");		// 	creates Login button
		submitButton.setSize(100, 30);		
		submitButton.setLocation(400, 180);
		submitButton.setActionCommand("Login");	// sets action command for Sumbit button
		submitButton.addActionListener(new LoginListeners());	// sets listener for Submit button
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
	
	/**
	 * Code executed when enter pressed or when Login button pressed
	 */
	private void enterPressed () {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
		objectNode.put("username", userfield.getText());
		objectNode.put("password", SecurePassword.sha256(passfield.getText()));
		System.out.println(objectNode.toString());
		clear();
		// tsekarisma an uparxei o sunduasmos username kai password
		// an uparxei, anoikse to skaki
		// an den uparxei, gurna sto login me sfalma
		boolean flag = false;
		if (flag) {
			System.out.println("Have to open the game");
		}
		else {
			// show message in case of invalid credentials
			JOptionPane.showMessageDialog(this, "Incorrect username or password!\nPlease try again");
		}
	}
	
	/**
	 * On Clear button pressed 
	 */
	private void clear () {
		userfield.setText("");
		passfield.setText("");
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
	
	/** 
	 * Listener for Login and Clear buttons.
	 * http://www.tutorialspoint.com/swing/swing_event_handling.htm
	 */
	private class LoginListeners implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if (actionEvent.getActionCommand().equals("Login")) {
				enterPressed();
			}
			else {
				clear();
			}
		}
		
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
