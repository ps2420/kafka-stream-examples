package com.example.sg.fx.rate.db.examples;

import static com.example.sg.fx.rate.db.util.FxRateDbUtil.writeToJson;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.sg.fx.rate.db.model.FxRateModel;
import com.example.sg.fx.rate.db.util.FxRateDbUtil;

public class KafkaStreamConsumer extends KafkaSetup {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStreamConsumer.class);

	public static void main(String[] args) throws Exception {
		// kSteamExample();
		// kTableExample();
		// kStreamKTableJoinExample();
		globalKTableJoinExample();
		// windowingExample();
	}

	public static void kSteamExample() {
		final Properties consumerProp = createConsumerProp(DIR_STREAM);
		consumerProp.put(StreamsConfig.APPLICATION_ID_CONFIG, "consumeMsgWithRandomKey");
		final StreamsBuilder builder = new StreamsBuilder();
		final KStream<String, String> messages = builder.stream(TOPIC_NAME + "2");
		messages.foreach((key, value) -> {
			LOGGER.info("kSteam key:[{}], value:[{}] ", key, FxRateDbUtil.writeToJson(value));
		});
		startStream(builder, consumerProp);
	}

	/**
	 * vanilla ktable example, in this case Ktable internally (rocks-db) wait for a
	 * certain duration before producing unique records for that key
	 */
	public static void kTableExample() {
		final Properties consumerProp = createConsumerProp(DIR_STREAM);
		consumerProp.put(StreamsConfig.APPLICATION_ID_CONFIG, "consumeMsgKTableExample");
		final StreamsBuilder builder = new StreamsBuilder();

		final KTable<String, FxRateModel> ktable = builder.table(TOPIC_NAME);
		final KStream<String, FxRateModel> kStream = ktable.toStream();
		kStream.foreach((key, value) -> {
			LOGGER.info("ktable--> key:[{}], value:[{}] ", key, FxRateDbUtil.writeToJson(value));
		});
		startStream(builder, consumerProp);
	}

	/**
	 * kStream-kTable join example. join operation will occur when new message is
	 * posted on leftStream. It will match with latest KTableExample
	 */
	public static void kStreamKTableJoinExample() {
		final Properties sconsumerProp = createConsumerProp(DIR_STREAM);
		sconsumerProp.put(StreamsConfig.APPLICATION_ID_CONFIG, "kStreamKTableJoinExample");
		final StreamsBuilder sBuilder = new StreamsBuilder();

		final KStream<String, FxRateModel> left = sBuilder.stream(TOPIC_NAME);
		final KTable<String, FxRateModel> table = sBuilder.table(TOPIC_NAME + "2");

		left.join(table, (value1, value2) -> {
			value2.setTenor(value2.getTenor() + "-" + value1.getTenor());
			return value2;
		}).map((key, value) -> new KeyValue<String, FxRateModel>(key, value)).foreach((key, value) -> {
			LOGGER.info("Key-[{}], value-[{}]", key, FxRateDbUtil.writeToJson(value));
		});

		final KafkaStreams streams = new KafkaStreams(sBuilder.build(), sconsumerProp);
		streams.cleanUp();
		streams.start();
	}

	/**
	 * kStream-kTable join example. join operation will occur when new message is
	 * posted on leftStream. It will match with latest KTableExample
	 */
	public static void globalKTableJoinExample() {
		final Properties sconsumerProp = createConsumerProp(DIR_STREAM);
		sconsumerProp.put(StreamsConfig.APPLICATION_ID_CONFIG, java.util.UUID.randomUUID().toString());// "globalKTableJoinExample");
		final StreamsBuilder sBuilder = new StreamsBuilder();

		final KStream<String, FxRateModel> left = sBuilder.stream(TOPIC_NAME);
		final GlobalKTable<String, FxRateModel> globalKTable = sBuilder.globalTable(TOPIC_NAME + "2");

		left.join(globalKTable, (Key, lModel) -> {
			return lModel.getCurrency();
		}, (ldata, rdata) -> {
			return "left=" + writeToJson(ldata) + ", right = " + writeToJson(rdata);
		}).foreach((key, value) -> {
			LOGGER.info("Key:[{}], Value:[{}]", key, value);
		});

		final KafkaStreams streams = new KafkaStreams(sBuilder.build(), sconsumerProp);
		streams.cleanUp();
		streams.start();
	}

	/**
	 * Windowing operation example of KAFKA stream, reduce method --> compare two
	 * objects/merge and produce the output for-each() method --> calls the final
	 * output performed within 5 seconds operation
	 */
	public static void windowingExample() {
		final Properties consumerProp = createConsumerProp(DIR_STREAM);
		consumerProp.put(StreamsConfig.APPLICATION_ID_CONFIG, "consumeMsgWindowingExample");
		final StreamsBuilder builder = new StreamsBuilder();
		final KStream<String, FxRateModel> stream = builder.stream(TOPIC_NAME);

		stream.groupByKey().windowedBy(TimeWindows.of(TimeUnit.SECONDS.toMillis(5000))).reduce((value1, value2) -> {
			LOGGER.info("\nvalue1:[{}], value2:[{}]", value1.getTimestamp(), value2.getTimestamp());
			return value2;
		}).toStream().foreach((key, data) -> {
			LOGGER.info("==> " + key + "" + FxRateDbUtil.writeToJson(data));
		});
		startStream(builder, consumerProp);
	}

}
