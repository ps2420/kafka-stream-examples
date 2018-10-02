package com.example.sg.fx.rate.db.examples;

import static java.util.UUID.randomUUID;

import java.util.stream.IntStream;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.sg.fx.rate.db.model.FxRateModel;
import com.example.sg.fx.rate.db.util.FxRateDbUtil;

public class KafkaStreamProducer extends KafkaSetup {

	// https://dzone.com/articles/kafka-producer-and-consumer-example
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStreamProducer.class);

	public static void main(String[] args) throws Exception {
		// createMessageWithRandomKey();
		createMessageWithUniqueKey();
		//createMessageForGlobalTable();
	}

	public static void createMessageWithRandomKey() {
		final KafkaProducer<String, FxRateModel> kafkaProducer = createProducer();
		final FxRateModel model = mockFxRateModel();
		final ProducerRecord<String, FxRateModel> record = new ProducerRecord<String, FxRateModel>(TOPIC_NAME,
				randomUUID().toString(), model);
		kafkaProducer.send(record);
		kafkaProducer.close();
		LOGGER.debug("Messages has been sent successfully to kafka topic..");
	}

	public static void createMessageWithUniqueKey() {
		final KafkaProducer<String, FxRateModel> kafkaProducer = createProducer();
		IntStream.range(1, 2).forEach((tracker) -> {
			try {
				final FxRateModel model = mockFxRateModel();
				model.setTenor(TOPIC_NAME);
				final ProducerRecord<String, FxRateModel> record = new ProducerRecord<String, FxRateModel>(TOPIC_NAME,
						model.getCurrency(), model);
				kafkaProducer.send(record);
				LOGGER.info("Message[{}] - [{}]", tracker, FxRateDbUtil.writeToJson(model));
				Thread.sleep(1000l);
			} catch (final Exception ex) {
				LOGGER.error("Error in sending messages.." + ex, ex);
			}
		});
		kafkaProducer.close();
		LOGGER.debug("Messages has been sent successfully to kafka topic..");
	}

	public static void createMessageForGlobalTable() {
		final KafkaProducer<String, FxRateModel> kafkaProducer = createProducer();
		IntStream.range(1, 2).forEach((tracker) -> {
			try {
				final FxRateModel gModel = FxRateDbUtil
						.readData(FxRateDbUtil.writeToJson(mockFxRateModel()).getBytes(), FxRateModel.class);
				gModel.setTenor(TOPIC_NAME + 2);
				final ProducerRecord<String, FxRateModel> record = new ProducerRecord<String, FxRateModel>(
						TOPIC_NAME + 2, gModel.getCurrency(), gModel);
				kafkaProducer.send(record);
				LOGGER.info("Message[{}] - [{}]", tracker, FxRateDbUtil.writeToJson(gModel));
				Thread.sleep(1000l);
			} catch (final Exception ex) {
				LOGGER.error("Error in sending messages.." + ex, ex);
			}
		});
		kafkaProducer.close();
		LOGGER.debug("Messages has been sent successfully to kafka topic..");
	}

}
