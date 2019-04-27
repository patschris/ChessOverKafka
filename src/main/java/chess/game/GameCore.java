package chess.game;


import java.sql.Time;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;



import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.google.gson.Gson;

import chess.pieces.Piece;
import chess.pieces.PieceColor;
import kafka_consumer_producer.*;



public class GameCore {

	Producer<Long, String> black_producer;
	Consumer<Long, String> black_consumer;
	Producer<Long, String> white_producer;
	Consumer<Long, String> white_consumer;
	PieceColor pieceColor;
	Game game;


	public GameCore(PieceColor pieceColor, Game game, String WwritesBreads, String BwritesWreads) {		
		this.pieceColor = pieceColor;
		this.game = game;
	}

	@SuppressWarnings("deprecation")
	public void startgame(String WwritesBreads, String BwritesWreads) throws InterruptedException {
		
		this.black_consumer = ConsumerCreator.createConsumer(WwritesBreads);
		this.black_producer = ProducerCreator.createProducer();
		
		if(pieceColor.equals(PieceColor.BLACK)) {

			//white's turn
			game._gui.setMHturn();

			while(true) {

				String dest_str = null;
				
				System.out.println("Waiting For Message!");
				
				
				while (true) {
					ConsumerRecords<Long, String> consumerRecords = black_consumer.poll(1);
					if (consumerRecords.count() == 0) {
						System.out.println("NO message... trying to read from..." + WwritesBreads);
						TimeUnit.SECONDS.sleep(1);
						continue;
					}
					
					System.out.println("Black is reading from : " + WwritesBreads);
					
					for(ConsumerRecord<Long, String> record: consumerRecords) {
						dest_str = (String) record.value();
						System.out.println(dest_str);
						//JOptionPane.showMessageDialog(null, record.value());	
					}
					// commits the offset of record to broker.
					black_consumer.commitAsync();
					//black_consumer.close();
					break;
				}

				Gson gson = new Gson();
				Destination dest = gson.fromJson(dest_str, Destination.class);

				Piece selected = game._gui._game.get(dest.getInit_x(), dest.getInit_y());
				
			
				game._gui.repaintReceive(selected.makeValidMove(dest.getFin_x(), dest.getFin_y()));
				
				
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
						
						ProducerRecord<Long, String> record2 = new ProducerRecord(BwritesWreads , dest_str2);
						try {
							RecordMetadata metadata2 = black_producer.send(record2).get();
						} catch (InterruptedException | ExecutionException e) {

							e.printStackTrace();
						}
						game._gui.setFlag(0);
						break;
					}
					
				}
				
			
				//repeat the procedure
				game._gui.setMHturn();
				

			}
		}
		
		else if(pieceColor.equals(PieceColor.WHITE)) {
			
			this.white_consumer = ConsumerCreator.createConsumer(BwritesWreads);
			this.white_producer = ProducerCreator.createProducer();
			
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
						
						ProducerRecord<Long, String> record = new ProducerRecord(WwritesBreads , dest_str);
						try {
							RecordMetadata metadata = white_producer.send(record).get();
						} catch (InterruptedException | ExecutionException e) {

							e.printStackTrace();
						}
						game._gui.setFlag(0);
						break;
					}
					
				}
				
				//try to receive
				
				game._gui.setMHturn();
				
				System.out.println("Waiting For Message!");
				
				String dest_str2 = null;
				
				

				while(true){
					ConsumerRecords<Long, String> consumerRecords2 = white_consumer.poll(1);
					if (consumerRecords2.count() == 0) { 
						TimeUnit.SECONDS.sleep(1);
						System.out.println("NO message... trying to read from..." + BwritesWreads);
						continue;
					}
					
					System.out.println("White is reading from : " + BwritesWreads);
					
					for(ConsumerRecord<Long, String> record2: consumerRecords2) {
						dest_str2 = (String) record2.value();
						System.out.println(dest_str2);
						//JOptionPane.showMessageDialog(null, record.value());	
					}
					// commits the offset of record to broker.
					white_consumer.commitAsync();
					//white_consumer.close();
					break;
				}

				Gson gson = new Gson();
				Destination dest2 = gson.fromJson(dest_str2, Destination.class);

				Piece selected2 = game._gui._game.get(dest2.getInit_x(), dest2.getInit_y());
								
				game._gui.repaintReceive(selected2.makeValidMove(dest2.getFin_x(), dest2.getFin_y()));
				
				
				
				//repeat the procedure
				game._gui.setMHmouse();
				
				
			}

			

		}

	}



}