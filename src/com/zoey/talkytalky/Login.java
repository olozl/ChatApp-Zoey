package com.zoey.talkytalky;

import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.*;

public class Login implements Servlet {
	ServerSocket s = new ServerSocket();
	public void init(ServletConfig config){}
	public ServletConfig getServletConfig(){
		return null;
	}
	public void service(ServletRequest request, ServletResponse response){
		response.setContentType("text/html");  
		try{
			PrintWriter out = response.getWriter();
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			if(contains(username, password)){
				request.setAttribute("username", username);
				request.getRequestDispatcher("./chat.jsp").forward(request,  response);
				out.close();  
			} else {
				out.println("Invalid password. Try again!");
				out.println("<html><form action=./login.jsp><input type=submit value=back /></form></html>");
				out.close();
			}
		} catch(IOException | ServletException e){
			e.getStackTrace();
		}
	}
	public String getServletInfo(){
		return null;
	}
	public void destroy() {} 
	public boolean contains(String username, String password) {
		boolean result=false;
		if(s.contains(username, password)){
			result = true;
		}
		return result;
	}

}
