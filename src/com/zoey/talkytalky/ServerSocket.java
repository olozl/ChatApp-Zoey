package com.zoey.talkytalky;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;



@ServerEndpoint(value="/chat/{username}", decoders = {AppDecoder.class}, encoders = {AppEncoder.class})
public class ServerSocket {
	private Session session;
	private String username;
	private String password;
	private static final Set<ServerSocket> endpoints = new CopyOnWriteArraySet<>();
	private static Hashtable<String, String> userlist = new Hashtable<>();
	private static final Set<String> online = new HashSet<String>();
	private static final Set<String> history = new HashSet<String>(); // change it to hashtable for username and message
	
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public void setUserlist(String username, String password){
		userlist.put(username, password);
	}
	public Hashtable<String, String> getHashtable(){
		return userlist;
	}
	public boolean contains(String username, String password){
		if(userlist.keySet().contains(username) && userlist.values().contains(password)){
			return true;
		}
		return false;
	}

	@OnOpen
	public void handleOpen(Session session, @PathParam("username") String username){
	       this.session = session;
	       this.username = username;
	       endpoints.add(this);
	       userlist.put(session.getId(), username);
	       online.add(username);
	       for(ServerSocket pt : endpoints){
				synchronized(pt){
					try {
						pt.session.getBasicRemote().sendText(getOnlineUsersList());
						
						if(!pt.session.equals(session.getOpenSessions()))
							pt.session.getBasicRemote().sendText("server : "+username+" logged in!\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} 
	}
	
	
	@OnMessage
	public void handleMessage(Session session, String msg){
		history.add(msg);
		for(ServerSocket pt : endpoints){
			synchronized(pt){
				try {
					if(!pt.session.equals(session.getOpenSessions()))
						pt.session.getBasicRemote().sendText(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		/*
		if(this.session.getId().equals(session.getId())){
		try {
			session.getBasicRemote().sendText(getHistory());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		*/
	}
	
	@OnError
	public void handleError(Session session, Throwable error){
		if(session!=null)
			try {
				session.close();
			} catch (IOException e) {
				e.getCause();
			}
	}
	
	@OnClose
	public void handleClose(Session session, CloseReason reason) {
	    this.session = session;
		endpoints.remove(this);
		online.remove(username);
		for(ServerSocket pt : endpoints){
			synchronized(pt){
				try {
					
					if(!pt.session.equals(session.getOpenSessions())){
						pt.session.getBasicRemote().sendText("server : "+username+" logged out!\n");
						pt.session.getBasicRemote().sendText(getOnlineUsersList());
					}
				} catch (IOException e) {
					e.getCause();
				}
			}
		} 
	}
	
	private String getHistory() {
		String historyList = new String();
		Set<String> keys = history;
		Iterator<String> itr = keys.iterator();
		
		while(itr.hasNext()){
			String iUser = itr.next();
			if(historyList.toLowerCase().contains(iUser.toLowerCase())){
				continue;
			}else{
				historyList += iUser+" \n ";
			}
		}
		return historyList;
	}
	  private String getOnlineUsersList() {
		  
		  String usersList = new String();
		  Set<String> keys = online;
		  Iterator<String> itr = keys.iterator();
		  
		  for(ServerSocket pt : endpoints){
				synchronized(pt){
					if(!pt.session.equals(session.getOpenSessions())){
						  while(itr.hasNext()){
								String iUser = itr.next();
								if(usersList.toLowerCase().contains(iUser.toLowerCase())){
									  continue;
								  }else{
									  usersList += iUser+" | ";
								  }
							}
						  }
				}
			} 
		  usersList = "\t\t\t >> ONLINE USERS : "+usersList+"\n";
		  return usersList;
	  }
	  
//	    private static void sendMessageToOneUser(Message message) {
//	        for (ServerSocket endpoint : endpoints) {
//	            synchronized(endpoint) {
//	                if (endpoint.session.getId().equals(getSessionId(message.getTo()))) {
//	                    try {
//							endpoint.session.getBasicRemote().sendObject(message);
//						} catch (IOException | EncodeException e) {
//							e.getCause();
//						}
//	                }
//	            }
//	        }
//	    }

	    private static String getSessionId(String to) {
	        if (userlist.containsValue(to)) {
	            for (String sessionId: userlist.keySet()) {
	                if (userlist.get(sessionId).equals(to)) {
	                    return sessionId;
	                }
	            }
	        }
	        return null;
	    }

}