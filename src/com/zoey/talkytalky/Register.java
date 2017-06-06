package com.zoey.talkytalky;

import java.io.*;  

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;  
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
  
public class Register implements Servlet {
	ServerSocket s = new ServerSocket();
	
	public void destroy() {}
	public ServletConfig getServletConfig() {
		return null;
	}
	public String getServletInfo() {
		return null;
	}
	public void init(ServletConfig arg0) throws ServletException {}
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();  
		String username = request.getParameter("username");  
		String password = request.getParameter("password"); 
		boolean result=true;
		
		Set<String> keys = s.getHashtable().keySet();
		Iterator<String> itr = keys.iterator();
		while(itr.hasNext()){
			if(username.equals(itr.next())){
				result = false;
				break;
			}
		}
		if(username=="" || password==""){
			out.println("Not a valid username/password. Try different.");
			out.println("<html><form action=./register.jsp><input type=submit value=back /></form></html>");
			out.close();  
		}
		if(result){
			s.setUserlist(username, password);
			out.println("Registered succesfully! Please login!");
			out.println("<html><form action=./login.jsp><input type=submit value=login /></form></html>");
			out.close();  
		} else{
			out.println("Existing username. Try different.");
			out.println("<html><form action=./register.jsp><input type=submit value=back /></form></html>");
			out.close();  
		} 
		
	}
}  

