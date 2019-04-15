package kafka_consumer_producer;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;



public class TestConsumer {

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public static void main(String[] args) {
		
		Consumer<Long, String> consumer = ConsumerCreator.createConsumer("demo_topic_2");

		while (true) {

			ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);
			
			 if (consumerRecords.count() == 0) {
				 
				 continue;
			 }
			
			 
			for(ConsumerRecord record: consumerRecords) {
				System.out.println("Record Key " + record.key());

				System.out.println("Record value " + record.value());

				System.out.println("Record partition " + record.partition());

				System.out.println("Record offset " + record.offset());
			}
			

			// commits the offset of record to broker. 

			consumer.commitAsync();
			
			//break;

		}

		//consumer.close();

	}

}
