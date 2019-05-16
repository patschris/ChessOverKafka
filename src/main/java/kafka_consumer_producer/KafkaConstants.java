package kafka_consumer_producer;

public interface KafkaConstants {

    public static String KAFKA_BROKERS = "x.x.x.x:9092";

    public static Integer MESSAGE_COUNT=1000;

    public static String CLIENT_ID="client1";

    public static String TOPIC_NAME="demo_topic";

    public static String GROUP_ID_CONFIG="consumerGroup1";

    public static String OFFSET_LATEST="latest";

    public static String OFFSET_EARLIEST="earliest";

    public static Integer MAX_POLL_RECORDS=1;

}