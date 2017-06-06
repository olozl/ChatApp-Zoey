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
//		JAXBContext jax = null;
//		StringWriter st = null;
//		try{
//			jax = JAXBContext.newInstance(Message.class);
//			Marshaller ma = jax.createMarshaller();
//			st = new StringWriter();
//			ma.marshal(msg, st);
//		} catch(Exception e){
//			e.printStackTrace();
//		}
//		return st.toString();
		return Json.createObjectBuilder().add("content", msg.getContent()).build().toString();
	}


}
