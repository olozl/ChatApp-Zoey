<html>
	 <head>
        <title>Chat</title>
    </head>
    <body>
        <table>
            <tr>
                <td colspan="2">
                    <form action="./login.jsp">
                    <b id="online"></b><b>'s chat room</b>
						<input type="submit" value="logout" />
					</form>
                </td>
            </tr>
            <tr>
                <td>
                    <textarea readonly="true" rows="20" cols="80" id="log" style="resize:none"></textarea>
                </td>
            </tr>
            <tr>
                <td>
                    <textarea rows="1" cols="75" id="msg" placeholder="Enter message here" style="resize:none"></textarea>
                   	<input type="button" onclick="send();" value="Send"/>
                </td>
            </tr>
        </table>
        <script type="text/javascript" >
    	  var wsUri = "ws://localhost:1992/ChatApp/chat/";
    	  var online = document.getElementById("online");
	  	  var log = document.getElementById("log");
	  	  var u = '${username}';
	  
	  	  websocket = new WebSocket(wsUri+u);
	  	  websocket.onopen = function(evt) {
	  		  if(evt.data === undefined){
		  		  online.innerHTML+=u;
		  		  return;
	  		  }
	  			  
	  		  log.innerHTML+=evt.data;
	  	  };
	  	  websocket.onmessage = function(evt) {
	            log.innerHTML+=evt.data;
	        };
       
        function send() {
        	var d = new Date();
              var content = document.getElementById("msg").value;
              var sendinfo = u+" : "+content + " ( "+ d.toLocaleTimeString()+" )\n";
              websocket.send(sendinfo);
        }
  
        </script>
    </body>

</html>

