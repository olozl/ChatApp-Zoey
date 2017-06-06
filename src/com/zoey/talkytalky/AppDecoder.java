package com.zoey.talkytalky;

import java.io.StringReader;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
 
public class AppDecoder implements Decoder.Text<Message>{

	@Override
	public void destroy() {}

	@Override
	public void init(EndpointConfig arg0) {}

	@Override
	public Message decode(String content) throws DecodeException {
//	
		
		JsonObject obj = Json.createReader(new StringReader(content)).readObject();
		Message message = new Message();
		message.setContent(obj.getString("content"));
		return message;
	}

	@Override
	public boolean willDecode(String s) {
		return (s!=null);
	}
	
}