package com.zoey.talkytalky;

public class Message{
	ServerSocket s = new ServerSocket();
	private String from;
	private String to;
	private String content;
	
	public String toString(){
		return super.toString();
	}
	
	public String getFrom(){
		return from;
	}
	
	public String getTo(){
		return to;
	}
	
	public String getContent(){
		return content;
	}
	
	public void setFrom(String from){
		this.from = from;
	}
	
	public void setTo(String to){
		this.to = to;
	}
	
	public void setContent(String content){
		this.content = content;
	}
}
