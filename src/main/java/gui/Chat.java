package gui;

import chess.chessgui.ChessGUI;
import structures.ChatMemory;
import structures.Message;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;

import static chess.chessgui.GameDisplay.BOARD;

public class Chat extends JFrame {

	private static final long serialVersionUID = -3689107953291083901L;
	private JTextPane textArea;
	private JTextField messageField;
	private JButton sendButton;
	private ChessGUI chessGUI;
	private ChatMemory chatMemory = ChatMemory.getInstance();

	public Chat(ChessGUI cc) throws  BadLocationException {
		super("Chat");
		chessGUI = cc;
		setSize(700,300);	// size of login window
		setLayout(null);	// no default layout is used
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				chessGUI.setChat(null);
				dispose();
			}
		});
		setLocation(5, BOARD + 70);
		setResizable(false);
		textAreaLabel();
		addInputField();
		addButton();
		setVisible(true);

		if (!ChatMemory.chatexists) {
			new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {

					while(true) {}
				}

				@Override
				protected void done() {

				}
			}.execute();

			System.out.println("Swing worker spawned asynchronously!");

		}



	}

	private void textAreaLabel() throws BadLocationException {

		JLabel areaLabel = new JLabel();
		areaLabel.setLayout(new BorderLayout());
		areaLabel.setSize(670, 220);
		areaLabel.setLocation(10, 5);
		textArea = new JTextPane();

		/***********************************************************************/
		chatMemory.retrieveHistory(chessGUI.myself, textArea);
		/***********************************************************************/

		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		areaLabel.add(scrollPane);
		add(areaLabel);
	}

	private void addInputField() {
		messageField = new JTextField();
		messageField.setColumns(200);
		messageField.setSize(550, 30);
		messageField.setLocation(10, 230);
		messageField.addKeyListener(new KeyboardListener());
		add(messageField);
	}

	private void  addButton() {
		sendButton = new JButton("Send");
		sendButton.setSize(70, 30);
		sendButton.setLocation(610, 230);
		sendButton.setActionCommand("Send");
		sendButton.addActionListener(new ButtonListener());
		add(sendButton);
	}

	private void addMessage (Message message, String me) throws BadLocationException {
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		if (message.getUser().equals(me)) StyleConstants.setForeground(keyWord, Color.BLACK);
		else StyleConstants.setForeground(keyWord, Color.BLUE);
		doc.insertString(doc.getLength(),message.returnMessage(), keyWord);
	}

	private void enterPressed() throws BadLocationException {
		String myMessage = messageField.getText();
		if (!myMessage.equals("")) {
			Message m = new Message(chessGUI.myself, myMessage);
			addMessage(m, chessGUI.myself);
			chatMemory.add(m);
			messageField.setText("");

			/* Edw to stelneis. Kapou prepei na perimeneis gia data */

		}
	}


	private class KeyboardListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent keyEvent) {
			if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
				try {
					enterPressed();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent keyEvent) {}

		@Override
		public void keyTyped(KeyEvent keyEvent) {}
	}


	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				enterPressed();
			} 
			catch (BadLocationException e) {
				e.printStackTrace();
			}

		}

	}

}