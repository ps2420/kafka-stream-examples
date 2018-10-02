package com.example.sg.fx.rate.db.serde;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.example.sg.fx.rate.db.model.FxRateModel;

public class KafkaStreamJsonSerDe implements Serde<FxRateModel> {

	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey) {
	}

	@Override
	public void close() {
	}

	@Override
	public Serializer<FxRateModel> serializer() {
		return new KafkaJsonSerDe();
	}

	@Override
	public Deserializer<FxRateModel> deserializer() {
		return new KafkaJsonSerDe();
	}

}
