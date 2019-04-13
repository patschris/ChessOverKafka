package kafka_consumer_producer;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class TestProducer
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main( String[] args )
	{
		Producer<Long, String> producer = ProducerCreator.createProducer();

		for (int index = 0; index < 1; index++) {

			Destination dest = new Destination(1,1,2,2);
			
			Gson gson = new Gson();
			String dest_str = gson.toJson(dest);
					
			ProducerRecord<Long, String> record = new ProducerRecord("demo_topic" , dest_str);

			try {
				RecordMetadata metadata = producer.send(record).get();

				System.out.println("Record sent with key " + index + " to partition " + metadata.partition()

				+ " with offset " + metadata.offset());

			} catch (InterruptedException e) {

				System.out.println("Error in sending record");
				System.out.println(e);

			} catch (ExecutionException e) {

				System.out.println("Error in sending record");
				System.out.println(e);

			}

		}
	}
}
