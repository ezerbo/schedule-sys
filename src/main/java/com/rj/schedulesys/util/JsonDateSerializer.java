package com.rj.schedulesys.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonDateSerializer extends JsonSerializer<Date> {
	
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void serialize(Date date, JsonGenerator generator, SerializerProvider serializers) throws IOException,
			JsonProcessingException {
		
		String formattedDate = simpleDateFormat.format(date);
		generator.writeString(formattedDate);
	}
}
