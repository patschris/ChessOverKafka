package gui;

import kafka_consumer_producer.ProducerCreator;
import structures.ChatMemory;
import structures.Message;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.awt.*;
import java.awt.event.*;

import static chess.chessgui.GameDisplay.BOARD;

public class Chat extends JFrame{

	private static final long serialVersionUID = -3689107953291083901L;
	private JTextPane textArea;
	private JTextField messageField;
	private JButton sendButton;
	private ChatMemory chatMemory = ChatMemory.getInstance();
	private String myself;
	private String opponent;
	private Thread t;
	private volatile boolean isRunning = true;
	
	public Chat(String myself, String opponent, Consumer<Long, String> chat_consumer) throws  BadLocationException {
		super("Chat");
		
		this.myself = myself;
		this.opponent = opponent;

		
		setSize(700,300);	// size of login window
		setLayout(null);	// no default layout is used
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
		        isRunning = false;
		        
		    }
		});
		setLocation(5, BOARD + 70);
		setResizable(false);

		textAreaLabel();
		addInputField();
		addButton();
		setVisible(true);
		
		// Runs outside of the Swing UI thread
		t = new Thread(new Runnable() {
			public void run() {

				String msg = "";
				while (isRunning) {
					@SuppressWarnings("deprecation")
					ConsumerRecords<Long, String> consumerRecords = chat_consumer.poll(10);
					if (consumerRecords.count() == 0) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					for(ConsumerRecord<Long, String> record: consumerRecords) {

						msg = record.value();
						Message m = new Message(opponent, msg);

						SwingUtilities.invokeLater(() -> {
							try {
								addMessage(m, myself);
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
							chatMemory.add(m);
						});


					}
					// commits the offset of record to broker.
					chat_consumer.commitAsync();	
				}
				
				System.out.println("Chat thread is dying...");

			}
		});
		t.start();

	}

	


	private void textAreaLabel() throws BadLocationException {

		JLabel areaLabel = new JLabel();
		areaLabel.setLayout(new BorderLayout());
		areaLabel.setSize(670, 220);
		areaLabel.setLocation(10, 5);
		textArea = new JTextPane();

		/***********************************************************************/
		chatMemory.retrieveHistory(myself, textArea);
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

	private void addButton() {
		sendButton = new JButton("Send");
		sendButton.setSize(70, 30);
		sendButton.setLocation(610, 230);
		sendButton.setActionCommand("Send");
		sendButton.addActionListener(new ButtonListener());
		add(sendButton);
	}

	public void addMessage (Message message, String me) throws BadLocationException {
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		if (message.getUser().equals(me)) StyleConstants.setForeground(keyWord, Color.BLACK);
		else StyleConstants.setForeground(keyWord, Color.BLUE);
		doc.insertString(doc.getLength(),message.returnMessage(), keyWord);
	}

	private void enterPressed() throws BadLocationException {
		String myMessage = messageField.getText();
		if (!myMessage.equals("")) {
			//print message on my chat box
			Message m = new Message(myself, myMessage);
			addMessage(m, myself);
			chatMemory.add(m);
			messageField.setText("");

			//open producer and send to my opponents chat topic
			Producer<Long, String> chat_producer = ProducerCreator.createProducer();
			ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(opponent + "Chat" , myMessage);
			chat_producer.send(record);
			chat_producer.close();

			System.out.println("Produced message to topic : " + opponent + "Chat");


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