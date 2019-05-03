package chess.game;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import chess.pieces.Piece;
import chess.pieces.PieceColor;
import kafka_consumer_producer.ConsumerCreator;
import kafka_consumer_producer.Destination;
import kafka_consumer_producer.ProducerCreator;


public class GameCore {

	static Producer<Long, String> black_producer;
	static Consumer<Long, String> black_consumer;
	static Consumer<Long, String> black_consumer_chat;;
	static Producer<Long, String> white_producer;
	static Consumer<Long, String> white_consumer;
	static Consumer<Long, String> white_consumer_chat;
	static String WwritesBreads;
	static String BwritesWreads;
	static PieceColor pieceColor;
	Game game;

	public GameCore(PieceColor pieceColor, Game game, String WwritesBreads, String BwritesWreads) {		
		GameCore.pieceColor = pieceColor;
		this.game = game;
		GameCore.BwritesWreads = BwritesWreads;
		GameCore.WwritesBreads = WwritesBreads;
	}

	@SuppressWarnings("deprecation")
	public void startgame(String WwritesBreads, String BwritesWreads) throws InterruptedException {



		if(pieceColor.equals(PieceColor.BLACK)) {

			boolean termination = false;

			GameCore.black_consumer = ConsumerCreator.createConsumer(WwritesBreads);
			GameCore.black_producer = ProducerCreator.createProducer();

			consumeMessages(black_consumer);

			//white's turn
			game._gui.setMHturn();

			while(true) {
				String dest_str = null;
				System.out.println("Waiting For Message!");
				while (true) {
					ConsumerRecords<Long, String> consumerRecords = black_consumer.poll(1);
					if (consumerRecords.count() == 0) {
						//System.out.println("NO message... trying to read from..." + WwritesBreads);
						TimeUnit.SECONDS.sleep(1);
						continue;
					}

					System.out.println("Black is reading from : " + WwritesBreads);

					for(ConsumerRecord<Long, String> record: consumerRecords) {
						dest_str = (String) record.value();
						if(dest_str.equals("Leaving the game")) {
							JOptionPane.showMessageDialog(null, "Your opponent left the game! Exiting...");
							black_consumer.commitAsync();
							terminateGame();
							termination = true;
							break;
						}
						System.out.println(dest_str);
						//JOptionPane.showMessageDialog(null, record.value());	
					}

					if(!termination) {
						// commits the offset of record to broker.
						black_consumer.commitAsync();
						//black_consumer.close();
					}
					break;
				}

				if (termination) {
					break;
				}

				Gson gson = new Gson();
				Destination dest = gson.fromJson(dest_str, Destination.class);
				Piece selected = game._gui._game.get(dest.getInit_x(), dest.getInit_y());
				game._gui.repaintReceive(selected.makeValidMove(dest.getFin_x(), dest.getFin_y()));

				//check if game is over
				if(game.checkGameOver() == 1) {
					game._gui.setMHmouse();
					break;
				}

				//black's turn
				game._gui.setMHmouse();

				while(true) {
					Thread.sleep(30);
					//try to send message to black consumer
					if(game._gui.getFlag() != 1) {
						//System.out.println(game._gui.getFlag());
						continue;
					}
					else {
						Destination dest2 = new Destination(game._gui.getInit_x(), game._gui.getInit_y(), game._gui.getReleasedX() , game._gui.getReleasedY());
						String dest_str2 = gson.toJson(dest2);
						System.out.println("Black is writting in : " + BwritesWreads);
						ProducerRecord<Long, String> record2 = new ProducerRecord<Long, String>(BwritesWreads , dest_str2);
						black_producer.send(record2);	
						game._gui.setFlag(0);
						break;
					}
				}
				//repeat the procedure
				//check if game is over
				if(game.checkGameOver() == 1) {
					game._gui.setMHmouse();
					break;
				}

				game._gui.setMHturn();

			}

			if(!termination) {
				black_consumer.close();
				black_producer.close();
			}
			
			if(game.checkmatewhite == 1) {
				JOptionPane.showMessageDialog(null, "Game Over, black player wins!!");
			}
			else if(game.checkmateblack == 1) {
				JOptionPane.showMessageDialog(null, "Game Over, white player wins!!");
			}
			else {
				JOptionPane.showMessageDialog(null, "Game Over, the game ends in draw!!");
			}

		}

		else if(pieceColor.equals(PieceColor.WHITE)) {
			boolean termination = false;
			int mymoves = 0;
			int opponentmoves = 0;
			
			GameCore.white_consumer = ConsumerCreator.createConsumer(BwritesWreads);
			GameCore.white_producer = ProducerCreator.createProducer();

			consumeMessages(white_consumer);

			game._gui.setMHmouse();

			while(true) {
				//try to send
				while(true) {
					Thread.sleep(30);
					//try to send message to black consumer
					if(game._gui.getFlag() != 1) {
						//System.out.println(game._gui.getFlag());
						continue;
					}
					else {
						Destination dest = new Destination(game._gui.getInit_x(), game._gui.getInit_y(), game._gui.getReleasedX() , game._gui.getReleasedY());
						Gson gson = new Gson();
						String dest_str = gson.toJson(dest);
						System.out.println("White is writting in : " + WwritesBreads);
						ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(WwritesBreads , dest_str);
						white_producer.send(record);
						game._gui.setFlag(0);
						mymoves = mymoves + 1;
						break;
					}
				}

				//check if game is over
				if(game.checkGameOver() == 1) {
					game._gui.setMHmouse();
					break;
				}

				//try to receive
				game._gui.setMHturn();

				System.out.println("Waiting For Message!");
				String dest_str2 = null;
				while(true){
					ConsumerRecords<Long, String> consumerRecords2 = white_consumer.poll(1);
					if (consumerRecords2.count() == 0) { 
						TimeUnit.SECONDS.sleep(1);
						//System.out.println("NO message... trying to read from..." + BwritesWreads);
						continue;
					}
					System.out.println("White is reading from : " + BwritesWreads);
					for(ConsumerRecord<Long, String> record2: consumerRecords2) {
						dest_str2 = (String) record2.value();
						if(dest_str2.equals("Leaving the game")) {
							JOptionPane.showMessageDialog(null, "Your opponent left the game! Exiting...");
							white_consumer.commitAsync();
							terminateGame();
							termination = true;
							break;
						}
						opponentmoves = opponentmoves + 1;
						System.out.println(dest_str2); 
						//JOptionPane.showMessageDialog(null, record.value());	
					}



					// commits the offset of record to broker.
					if(!termination) {
						white_consumer.commitAsync();
					}

					//white_consumer.close();
					break;
				}
				if (termination) {
					break;
				}
				Gson gson = new Gson();
				Destination dest2 = gson.fromJson(dest_str2, Destination.class);
				Piece selected2 = game._gui._game.get(dest2.getInit_x(), dest2.getInit_y());
				game._gui.repaintReceive(selected2.makeValidMove(dest2.getFin_x(), dest2.getFin_y()));
				//repeat the procedure
				game._gui.setMHmouse();
				//check if game is over
				if(game.checkGameOver() == 1) {
					game._gui.setMHmouse();
					break;
				}
			}

			if(!termination) {
				white_consumer.close();
				white_producer.close();
				if(game.checkmatewhite == 1) {
					JOptionPane.showMessageDialog(null, "Game Over, black player wins!!");
				}
				else if(game.checkmateblack == 1) {
					JOptionPane.showMessageDialog(null, "Game Over, white player wins!!");
				}
				else {
					JOptionPane.showMessageDialog(null, "Game Over, the game ends in draw!!");
				}
			}
		}
	}

	public static void terminateGame() {
		System.out.println("Terminating the Game");
		if(pieceColor.equals(PieceColor.WHITE)) {
			white_producer.close();
			consumeMessages(white_consumer);
			white_consumer.close();
			logout(BwritesWreads);
		}
		else{
			black_producer.close();
			consumeMessages(black_consumer);
			black_consumer.close();
			logout(WwritesBreads);
		}
		System.exit(0);
	}


	public static void terminateGamefromX() {
		System.out.println("Terminating the Game from X!!");
		if(pieceColor.equals(PieceColor.WHITE)) {
			ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(WwritesBreads , "Leaving the game");
			white_producer.send(record);
			white_producer.close();
			consumeMessages(white_consumer);
			white_consumer.close();
			logout(BwritesWreads);
		}
		else{
			ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(BwritesWreads , "Leaving the game");
			black_producer.send(record);
			black_producer.close();
			consumeMessages(black_consumer);
			black_consumer.close();
			logout(WwritesBreads);
		}
		
	}

	public static void consumeMessages(Consumer<Long, String> consumer) {
		int tries = 0;
		while (tries < 1000) {
			@SuppressWarnings("deprecation")
			ConsumerRecords<Long, String> consumerRecords = consumer.poll(1);
			if (consumerRecords.count() == 0) {
				tries ++;
				continue;
			}

			for(ConsumerRecord<Long, String> record: consumerRecords) {
				System.out.println("Record Key " + record.key());
				System.out.println("Record value " + record.value());
				System.out.println("Record partition " + record.partition());
				System.out.println("Record offset " + record.offset());
			}
			// commits the offset of record to broker. 
			consumer.commitAsync();
		}
	}

	private static void logout(String user) {
		Client.create().resource(getBaseUrl() + "/logout/" + user).get(ClientResponse.class);
	}

	private static String getBaseUrl() {
		String baseUrl = "";
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream input = classloader.getResourceAsStream("config.properties");
		Properties properties = new Properties();
		try {
			properties.load(input);
			baseUrl = properties.getProperty("restAddress");

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return baseUrl;
	}
}