package com.zoey.talkytalky;

import java.io.StringWriter;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class AppEncoder implements Encoder.Text<Message> {

	@Override
	public void destroy() {}

	@Override
	public void init(EndpointConfig arg0) {}

	public String encode(Message msg) throws EncodeException {
		return Json.createObjectBuilder().add("content", msg.getContent()).build().toString();
	}


}
