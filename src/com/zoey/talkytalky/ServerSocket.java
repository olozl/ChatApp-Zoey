package com.zoey.talkytalky;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
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
	private static final Set<String> offline = new HashSet<String>();
	
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
	       offline.remove(username);
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
		int i=0;
		String from="";
		String to="";
		String send="";
		if(msg.contains("/to ")){
			for(int j=0; j<msg.length()-1; ++j){
				if(msg.charAt(j)=='/')
				{
					i=j;
					break;
				}
			}
			i+=4;
			while(msg.charAt(i)!= ':')
			{
				to+=msg.charAt(i);
				++i;
			}
			++i;
			while(msg.charAt(i)!='('){
				send+=msg.charAt(i);
				++i;
			}
			String to_id = null;
			for (String sessionId: userlist.keySet()) {
				if (userlist.get(sessionId).equals(to)) {
                    to_id= sessionId;
                    break;
	            }
	        }
			for(Map.Entry<String, String> s: userlist.entrySet()){
				if(s.getKey()==session.getId())
					from = s.getValue();
			}
			for(ServerSocket pt2: endpoints){
				try {
					if(pt2.session.getId().equals(to_id) || pt2.session.equals(session)){
						pt2.session.getBasicRemote().sendText("/from "+from+" to "+to+":"+send+"\n");
					}
				} catch (IOException e) {
					e.getCause();
				}
			}
		}else {
			for(ServerSocket pt : endpoints){
				synchronized(pt){
					try {
						if(!pt.session.equals(session.getOpenSessions()))
							pt.session.getBasicRemote().sendText(msg);
					} catch (IOException e) {
						e.getCause();
					}
				}
			}
		}
		 
	
		/*
		if(this.session.getId().equals(session.getId())){
		try {
			session.getBasicRemote().sendText(getHistory());
		} catch (IOException e) {
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
		online.remove(this.username);
		offline.add(this.username);
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
	
//	private String getHistory() {
//		String historyList = new String();
//		Set<String> keys = history;
//		Iterator<String> itr = keys.iterator();
//		
//		while(itr.hasNext()){
//			String iUser = itr.next();
//			if(historyList.toLowerCase().contains(iUser.toLowerCase())){
//				continue;
//			}else{
//				historyList += iUser+" \n ";
//			}
//		}
//		return historyList;
//	}
	  private String getOnlineUsersList() {
		  
		  String usersList = new String();
		  String usersList2 = new String();
		  Set<String> keys = online;
		  Iterator<String> itr = keys.iterator();
		  Set<String> keys2 = offline;
		  Iterator<String> itr2 = keys2.iterator();
		  
		  for(ServerSocket pt : endpoints){
				synchronized(pt){
					while(itr2.hasNext()){
						String iUser2 = itr2.next();
						if(usersList2.toLowerCase().contains(iUser2.toLowerCase())){
							  continue;
						  }else{
							  usersList2 += iUser2+" | ";
						  }
					}
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
		  usersList += "\t\t\t >> OFFLINE USERS : "+usersList2+"\n";
		  return usersList;
	  }
}