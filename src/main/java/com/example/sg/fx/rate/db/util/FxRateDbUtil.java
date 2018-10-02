package com.example.sg.fx.rate.db.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FxRateDbUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FxRateDbUtil.class);

	private static ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static ObjectMapper objectMapper() {
		return objectMapper;
	}

	public static String writeToJson(final Object data) {
		String jsonData = "";
		try {
			jsonData = FxRateDbUtil.objectMapper.writeValueAsString(data);
		} catch (final Exception ex) {
			LOGGER.warn("Failed to write data into json format for class: [{}] " + data.getClass());
		}
		return jsonData;
	}

	public static <T> T readData(final byte[] data, final Class<T> clazz) {
		T value = null;
		try {
			value = FxRateDbUtil.objectMapper.readValue(data, clazz);
		} catch (final Exception ex) {
			LOGGER.error("Error in converting to class:[{}]", clazz);
		}
		return value;
	}
}
