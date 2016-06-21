package com.rj.schedulesys.data;

import java.io.IOException;
import java.io.Serializable;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LocalTimeSerializer extends StdSerializer<LocalTime> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LocalTimeSerializer() {
		super(LocalTime.class);
	}
	

	@Override
	public void serialize(LocalTime localTime, JsonGenerator generator, SerializerProvider arg2) throws IOException {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm");
		generator.writeString(localTime.toString(formatter));
	}

	

}
