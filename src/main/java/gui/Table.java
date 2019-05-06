package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import chess.game.Game;
import chess.game.GameCore;
import chess.pieces.PieceColor;
import kafka_consumer_producer.ConsumerCreator;
//import kafka_consumer_producer.ConsumerCreator;
import kafka_consumer_producer.ProducerCreator;
import security.RestServiceURL;

public class Table extends JFrame {

	private static final long serialVersionUID = 886705961481791855L;
	public String baseUrl;
	private String whoAmI;
	private String opponent;
	private JLabel title;
	private JLabel subtitle;
	private JLabel gifLabel;
	private JComboBox<String> dropdown;
	private DefaultListModel<String> model;
	private JList<String> list;
	private JScrollPane scrollPane;
	private JButton clearButton;
	private JButton submitButton;
	private JButton refreshButton;
	private JButton statsButton;

	public Table(String userLoggedIn) {
		super("Table");
		whoAmI = userLoggedIn;
		getBaseUrl();
		setSize(700,300);
		setLayout(null);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				exitFunction();
			}
		});
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
		setResizable(false);
		constructGraphicDetails();
		setVisible(true);
	}

	private void exitFunction() {
		destroyMyTable();
		logout();
		System.exit(0);
	}

	private void constructGraphicDetails() {
		addTitle();
		createDropDown();
		createSubtitle();
		addImageLabel();
		addList();
		addButtons();
	}

	/**
	 * 	Creating a label to inform the user.
	 */
	private void addTitle(){
		title = new JLabel("Create a new table or join another table?");
		title.setSize(300, 50);
		title.setLocation(100, 15);
		add(title);
	}

	private void createDropDown(){
		String[] selections = new String[] {"<html><b><i>Select Option</i></b></html>", "Create table", "Join table"};
		dropdown = new JComboBox<>(selections);
		dropdown.setSize(150, 25);
		dropdown.setLocation(400, 25);
		dropdown.setSelectedIndex(0);
		dropdown.addActionListener(new DropDownListener());
		add(dropdown);
	}

	private void createSubtitle (){
		subtitle = new JLabel();
		subtitle.setSize(300, 30);
		subtitle.setLocation(270, 35);
		add(subtitle);
	}

	private void addImageLabel() {
		URL url = this.getClass().getResource("/chess/images/gui/loading.gif");
		Icon loadingGif = new ImageIcon(url);
		gifLabel = new JLabel(loadingGif);
		gifLabel.setSize(150, 80);
		gifLabel.setLocation(270, 100);
		add(gifLabel);
		gifLabel.setVisible(false);
	}

	private void addList(){
		model = new DefaultListModel<>();
		list = new JList<>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane = new JScrollPane(list);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(100, 75, 450, 130);
		add(scrollPane);
		scrollPane.setVisible(false);
	}

	private void addClearButton() {
		clearButton = new JButton("Clear");
		clearButton.setSize(100, 30);
		clearButton.setLocation(220, 225);
		clearButton.addActionListener(event -> list.clearSelection());
		add(clearButton);
		clearButton.setVisible(false);
	}

	private void addSubmitButton() {
		submitButton = new JButton("Play");
		submitButton.setSize(100, 30);
		submitButton.setLocation(350, 225);
		submitButton.addActionListener(event -> selectOpponent());
		add(submitButton);
		submitButton.setVisible(false);
	}

	private void addRefreshButton() {
		URL url = this.getClass().getResource("/chess/images/gui/refresh.png");
		ImageIcon icon = new ImageIcon(url);
		refreshButton = new JButton(icon);
		refreshButton.setSize(50,50);
		refreshButton.setLocation(560, 120);
		refreshButton.addActionListener(event -> getOpponents());
		add(refreshButton);
		refreshButton.setVisible(false);
	}

	private void addStatsButton() {
		statsButton = new JButton("See stats");
		statsButton.setSize(120, 30);
		statsButton.setLocation(285, 180);
		statsButton.addActionListener(event -> {dispose(); new Stats(whoAmI);});
		add(statsButton);
		statsButton.setVisible(true);
	}

	private void addButtons(){
		addClearButton();
		addSubmitButton();
		addRefreshButton();
		addStatsButton();
	}

	private void getBaseUrl () {
		baseUrl = RestServiceURL.getInstance ().getBaseUrl();
	}

	private void destroyMyTable () {
		Client.create().resource(baseUrl + "/undocreatetable/" + whoAmI).get(ClientResponse.class);
	}

	private void logout () {
		Client.create().resource(baseUrl + "/logout/" + whoAmI).get(ClientResponse.class);
	}

	private void createMyTable () {
		Client.create().resource(baseUrl + "/createtable/" + whoAmI).get(ClientResponse.class);
	}

	private void addTopics () {
		Client.create().resource(baseUrl + "/createtopics/" + whoAmI).get(ClientResponse.class);
	}

	private void createNewGameTable() {
		ObjectNode node = new ObjectMapper().createObjectNode().put("white", whoAmI).put("black", opponent);
		Client.create().resource(baseUrl + "/newgametable").accept("application/json").type("application/json").post(ClientResponse.class, node.toString());
	}

	private void getOpponents () {
		ClientResponse response = Client.create().resource(baseUrl + "/getopponents/" + whoAmI).get(ClientResponse.class);
		JsonArray players = new JsonParser().parse(response.getEntity(String.class)).getAsJsonArray();
		model.clear();
		for (JsonElement player:players) {
			model.addElement(player.getAsJsonObject().get("name").getAsString());
		}
	}


	private void selectOpponent() {
		String opponent = list.getSelectedValue();
		if (opponent == null) {
			JOptionPane.showMessageDialog(null, "No opponent selected");
		}
		else {
			addTopics();
			//send your name to your opponent
			Producer<Long, String> black_producer = ProducerCreator.createProducer();
			ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(opponent , whoAmI);
			black_producer.send(record);
			black_producer.close();

			System.out.println("Iam: " + whoAmI);
			System.out.println("Opponent: " + opponent);

			//begin the game
			System.out.println("Ready to play!");
			setVisible(false);

			new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					Game g = new Game(PieceColor.BLACK,whoAmI, opponent);
					GameCore gamec = new GameCore(PieceColor.BLACK, g, opponent, whoAmI);
					gamec.startgame(opponent , whoAmI);
					g._gui.frame.setVisible(false);
					g._gui.frame.dispose();
					dispose();
					new Table(whoAmI);
					return null;
				}

				@Override
				protected void done() {}
			}.execute();
		}
	}


	private class DropDownListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			@SuppressWarnings("unchecked")
			JComboBox<String> combo = (JComboBox<String>) actionEvent.getSource();
			String selectedOption = (String) combo.getSelectedItem();
			if (selectedOption.equals("Create table")) {
				anOptionSelected();
				onCreateTableSelected();
			}
			else if (selectedOption.equals("Join table")) {
				anOptionSelected();
				onJoinTableSelected();
			}
		}

		private void anOptionSelected() {
			dropdown.setVisible(false);
			title.setVisible(false);
		}

		private void onCreateTableSelected(){
			subtitle.setText("Wait for an opponent");
			gifLabel.setVisible(true);
			statsButton.setVisible(false);
			new SwingWorker<Void, Void>() {
				protected Void doInBackground() throws InterruptedException {
					addTopics();
					
					Consumer<Long, String> white_consumer = ConsumerCreator.createConsumer(whoAmI);
					//consume any left messages
					GameCore.consumeMessages(white_consumer);

					//create the white_consumer and wait for someone to join you
					String msg = "";

					createMyTable();
					System.out.println("Waiting For Message!");
					while (true) {
						@SuppressWarnings("deprecation")
						ConsumerRecords<Long, String> consumerRecords = white_consumer.poll(10);
						if (consumerRecords.count() == 0) {
							TimeUnit.SECONDS.sleep(1);
							continue;
						}
						for(ConsumerRecord<Long, String> record: consumerRecords) {
							msg = record.value();
							System.out.println(msg);
							//JOptionPane.showMessageDialog(null, record.value());	
						}
						// commits the offset of record to broker.
						white_consumer.commitAsync();
						break;
					}

					setVisible(false);

					TimeUnit.SECONDS.sleep(1);

					opponent = msg;
					System.out.println("Iam: " + whoAmI);
					System.out.println("Opponent: " + opponent);
					System.out.println("Ready to play!");
					dispose();
					createNewGameTable();
					destroyMyTable();
					Game g = new Game(PieceColor.WHITE, whoAmI, opponent);
					GameCore gamec = new GameCore(PieceColor.WHITE, g, whoAmI, opponent);
					gamec.startgame(whoAmI, opponent);
					g._gui.frame.setVisible(false);
					g._gui.frame.dispose();
					dispose();
					new Table(whoAmI);
					return null;
				}

				@Override
				protected void done() {}
			}.execute();
		}

		private void onJoinTableSelected(){

			new SwingWorker<Void, Void>() {
				protected Void doInBackground()  {
					getOpponents();
					return null;
				}

				@Override
				protected void done() {
					statsButton.setVisible(false);
					subtitle.setText("Select an opponent");
					scrollPane.setVisible(true);
					clearButton.setVisible(true);
					submitButton.setVisible(true);
					refreshButton.setVisible(true);
				}
			}.execute();
		}
	}
}