package com.example.sg.fx.rate.db.examples;

import java.util.Calendar;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;

import com.example.sg.fx.rate.db.BaseSetup;
import com.example.sg.fx.rate.db.model.FxRateModel;
import com.example.sg.fx.rate.db.serde.KafkaJsonSerDe;
import com.example.sg.fx.rate.db.serde.KafkaStreamJsonSerDe;

public class KafkaSetup extends BaseSetup {

	/**
	 * checkout README.cmd on https://github.com/AmarisAI/Kafka-connectors create
	 * two topics test-topic and test-topic2 and experiment
	 */

	protected static String BOOTSTRAP_SERVERS = "localhost:9092";
	protected static String TOPIC_NAME = "test-topic";
	protected static String OFFSET_EARLIEST = "earliest"; // earliest, //latest

	protected static String DIR_STREAM = "stream";
	protected static String DIR_GTABLE = "gtable";

	protected static FxRateModel mockFxRateModel() {
		final FxRateModel model = new FxRateModel();
		model.setAsk(3.43d);
		model.setBid(4.54d);
		model.setCurrency("USD");
		model.setTenor("T+1");
		model.setTimestamp(Calendar.getInstance().getTime());
		return model;
	}

	protected static KafkaProducer<String, FxRateModel> createProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, java.util.UUID.randomUUID().toString());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerDe.class.getName());
		// props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,
		// CustomPartitioner.class.getName());
		return new KafkaProducer<>(props);
	}

	protected static Properties createGTableConsumerProp(final String suffix) {
		final Properties props = new Properties();
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3); // assuming three partitions
		props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka/" + suffix); // System.getProperty("java.io.tmpdir"));

		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OFFSET_EARLIEST);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 2000);
		return props;
	}

	protected static Properties createConsumerProp(final String suffix) {
		final Properties props = new Properties();
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, KafkaStreamJsonSerDe.class.getName()); //KafkaStreamJsonSerDe.class.getName());
		props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3);
		props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka/" + suffix); // System.getProperty("java.io.tmpdir"));

		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OFFSET_EARLIEST);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 2000);
		return props;
	}

	protected static void startStream(final StreamsBuilder builder, final Properties consumerProp) {
		final KafkaStreams streams = new KafkaStreams(builder.build(), consumerProp);
		streams.cleanUp();
		streams.start();
	}

}
