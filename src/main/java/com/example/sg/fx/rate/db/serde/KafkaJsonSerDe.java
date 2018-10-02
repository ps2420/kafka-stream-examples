package com.example.sg.fx.rate.db.serde;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import com.example.sg.fx.rate.db.model.FxRateModel;
import com.example.sg.fx.rate.db.util.FxRateDbUtil;

public class KafkaJsonSerDe implements Serializer<FxRateModel>, Deserializer<FxRateModel> {

	@Override
	public FxRateModel deserialize(final String topic, final byte[] data) {
		return FxRateDbUtil.readData(data, FxRateModel.class);
	}

	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey) {

	}

	@Override
	public byte[] serialize(final String topic, final FxRateModel data) {
		return FxRateDbUtil.writeToJson(data).getBytes();
	}

	@Override
	public void close() {
	}

}
