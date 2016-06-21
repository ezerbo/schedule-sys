package com.rj.schedulesys.data;

import java.io.IOException;
import java.io.Serializable;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalTimeDeserializer extends StdDeserializer<LocalTime> implements Serializable{

	private static final long serialVersionUID = 1L;

	public LocalTimeDeserializer() {
		super(LocalTime.class);
	}
	
	@Override
	public LocalTime deserialize(JsonParser parser, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm");
		return LocalTime.parse(parser.getValueAsString(), formatter);
	}

}
