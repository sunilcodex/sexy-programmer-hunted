import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.regex.*;


public class SocketConnect{
	   private Socket socket = null;
	   private BufferedReader reader = null;
	   private BufferedWriter writer = null;
	   private InetAddress host;
	   private int port;

	   
	   public SocketConnect(String address, int port) throws IOException
	   {
		   
		  this.host = InetAddress.getByName(address);
		  this.port = port;
	      //socket = new Socket(host, this.port);
	      //reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	      //writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	   }

	   public void send(String msg) throws IOException
	   {
		   
		    socket = new Socket(host, this.port);		    
		    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	        writer.write(msg, 0, msg.length());
	        writer.flush();
	   }

	   public String recv() throws IOException
	   {
		   String userInput;
		   String receive = "";
			while ((userInput = reader.readLine()) != null) {
			    //System.out.println("echo: " + in.readLine());
				receive += userInput + "\n";
			}
			socket.close();

		   
	      return receive;
	   }
	   
	   
	   public String[] NewGame(SocketConnect client, String name) throws IOException{
	         client.send("GET\nNEW_GAME\nNICKNAME:"+name+"\n");
	         String response = client.recv();
	         System.out.println(response);
	         String[] tmp = response.split(":");
	         String[] tmp2 = tmp[1].split("\n");
	         String[] Id = tmp2[0].split(",");       
			return Id;

	   }
	   
	   
	   public String[] JoinGame(SocketConnect client, String name, String ID) throws IOException{
	         client.send("GET\nJOIN_GAME\nGROUP_ID:"+ID+"\nNICKNAME:"+name+"\n");
	         String response = client.recv();
	         String[] tmp = null;
	         
	        
        	 Pattern p = Pattern.compile("STATE_FREE\\nSESSION_ID:([0-9]+,[0-9]+)\\n");
        	 Matcher m = p.matcher(response);
        	 boolean b = m.matches();
        	 System.out.println(response);
        	// boolean b = Pattern.matches("^STATE_FREE\\nSESSION_ID:([0-9]+,[0-9]+)\\n", response);
        	 if (b){
        	
        		 String[] session_id = m.group(1).split(",");
        		 tmp = session_id;
        		 System.out.print("gid:"+ session_id[0] + "\n");
        		// return test;
        	 }

			return tmp;
	   }

	   
	   public String HostWaitChange(SocketConnect client,  String[] ID) throws IOException{
		   client.send("GET:"+ID[0]+","+ID[1] + "\n" + "USER:" + ID[1] + ",H\n");
		   
		   String response = client.recv();

	         
	        
		   Pattern p = Pattern.compile("^STATE_HOST_WAITING\\nGROUP_ID:([0-9]+)\\n.*",Pattern.DOTALL);
		   //STATE_HOST_WAITING\\nGROUP_ID:([0-9]+)\\nUSER:([0-9]+),([0-9a-zA-Z]+),[HP],[OX]\\nUSER:([0-9]+),([0-9a-zA-Z]+),[HP],[OX]\\n
		   Matcher m = p.matcher(response);
		   boolean b = m.matches();
		   // boolean b = Pattern.matches("^STATE_FREE\\nSESSION_ID:([0-9]+,[0-9]+)\\n", response);
		   System.out.println(response);
      	 	//String[] con = response.split(",");
      	 	//System.out.println(con[0]);
		  	if (b){/*
		  		 System.out.println(response);
		  		 String[] con = response.split("\n");
		  		 System.out.println(con[2]);
		  		 Pattern pattern2 = Pattern.compile("USER:([0-9]+),([0-9a-zA-Z]+),[HP],[OX]\\n");
		  		 for(int i=2;i<=con.length;i++){
		  			 Matcher matcher2 = pattern2.matcher(con[i]);
		  			 if(matcher2.matches()){
		  				 String[] session_id = matcher2.group(1).split(",");
		  			 }*/
		  		 }
		  		 
		  		 //System.out.print("gid:"+ session_id[0] + "\n");
		  	
		  		 return response;
		  	}



		//return wait_msg;
	   

	   
	   //Thread Running
	   public void Host_waiting(SocketConnect client, String[] ID) throws IOException{
		   client.send("GET:"+ID[0]+","+ID[1] + "\n");
		   //System.out.print("GET:"+id[0]+","+id[1]+"\n");
		   String response = client.recv();
			System.out.println("HOST wait:"+response);

		//return wait_msg;
	   
	   }
	   
	   public String HostStart(SocketConnect client, String[] ID) throws IOException{
	         client.send("GET:"+ID[0]+","+ID[1] + "\n" + "START\n");
	         String response = client.recv();
	         Pattern p = Pattern.compile("STATE_HOST_WAITING\\nGROUP_ID:([0-9]+)\\n.*",Pattern.DOTALL);
	         Matcher m = p.matcher(response);
	         boolean b = m.find();
	         if(b){
	        	 System.out.println("HOST START:"+"\n" + response);
		         return response;	        	 
	         }
	         else return "HostStart Failed"; 
	        		
	        	
	         
	   }
	   
	   public String HostCancel(SocketConnect client, String[] ID) throws IOException{
	         client.send("GET:"+ID[0]+","+ID[1] + "\n" + "CANCEL\n");
	         String response = client.recv();
	         Pattern p = Pattern.compile("^STATE_HOST_WAITING\\nGROUP_ID:([0-9]+)\\n.*",Pattern.DOTALL);
	         Matcher m = p.matcher(response);
	         boolean b = m.find();
	         if(b){
	        	 System.out.println("HostCancel:"+"\n"+response);
		         return response;	        	 
	         }
	         else return "HostCancel Failed"; 
	   }
	   
	   
	   public String ClientReady(SocketConnect client, String[] ID) throws IOException{
		   		client.send("GET:"+ID[0]+","+ID[1] + "\n" + "READY\n");
		   		String response  = client.recv();
		   		Pattern p = Pattern.compile("^STATE_PLAYER_WAITING\\n.*",Pattern.DOTALL);
		         Matcher m = p.matcher(response);
		         boolean b = m.find();
				   System.out.println(b);
		         if(b){
		        	 System.out.println("ClientReady:"+"\n"+response);
			         return response;	        	 
		         }
		         else return "ClientReady Failed"; 
	   }
	   
	   public String HunterInGame(SocketConnect client, String[] ID) throws IOException{
		   client.send("GET:"+ID[0]+","+ID[1] + "\n" + "GPS:123.0,456.0" +"\n");
		   String response  = client.recv();
		   Pattern p = Pattern.compile("^STATE_HUNTER_IN_GAME\\n.*",Pattern.DOTALL);
		   Matcher m = p.matcher(response);
		   boolean b = m.find();
		   System.out.println(b);
		   if(b){
			   System.out.println("HunterInGame:"+"\n"+response);
			   return response;   
	        }
		   else return "HunterInGame Failed"; 
	   }
	   public String HunterTimeOutQuit(SocketConnect client, String[] ID) throws IOException{
		   client.send("GET:"+ID[0]+","+ID[1] + "\n" + "QUIT" +"\n");
		   String response  = client.recv();
		   Pattern p = Pattern.compile("^STATE_HUNTER_TIMEOUT\\n.*",Pattern.DOTALL);
		   Matcher m = p.matcher(response);
		   boolean b = m.find();
		   System.out.println(b);
		   if(b){
			   System.out.println("HunterTimeOutQuit:"+"\n"+response);
			   return response;   
	        }
		   else return "HunterTimeOutQuit Failed"; 
	   }
	   
	   public String HunterFailedQuit(SocketConnect client, String[] ID) throws IOException{
		   client.send("GET:"+ID[0]+","+ID[1] + "\n" + "QUIT" +"\n");
		   String response  = client.recv();
		   Pattern p = Pattern.compile("^STATE_HUNTER_FAILED\\n.*",Pattern.DOTALL);
		   Matcher m = p.matcher(response);
		   boolean b = m.find();
		   System.out.println(b);
		   if(b){
			   System.out.println("HunterFailed:"+"\n"+response);
			   return response;   
	        }
		   else return "HunterFailed Failed"; 
	   }
	   
	   public String HunterWinQuit(SocketConnect client, String[] ID) throws IOException{
		   client.send("GET:"+ID[0]+","+ID[1] + "\n" + "QUIT" +"\n");
		   String response  = client.recv();
		   Pattern p = Pattern.compile("^STATE_HUNTER_WIN\\n.*",Pattern.DOTALL);
		   Matcher m = p.matcher(response);
		   boolean b = m.find();
		   System.out.println(b);
		   if(b){
			   System.out.println("HunterWin:"+"\n"+response);
			   return response;   
	        }
		   else return "HunterWin Failed"; 
	   }
	   
	   public String PlayerInGame(SocketConnect client, String[] ID, String gpsX, String gpsY ,boolean withdraw) throws IOException{
	       String response;	       
		   if(withdraw){
	    	   client.send("GET:"+ID[0]+","+ID[1] + "\nGPS:"+gpsX+","+gpsY+"\nWITHDREW\n");
		       response = client.recv();
		       System.out.println("HOST in game:\n"+response);  	    	   
	       }	       
	       else{	       
	    	   client.send("GET:"+ID[0]+","+ID[1] + "\nGPS:"+gpsX+","+gpsY+"\n");
	    	   
		       response = client.recv();
		       System.out.println("HOST in game:\n"+response); 	    	   
	       }
			return response;
	   }
	   
	   
	   public void Client_waiting(SocketConnect client, String[] ID) throws IOException{
		   		client.send("GET:"+ID[0]+","+ID[1]+"\n");
		   		String response  = client.recv();
				System.out.println(response);
  
	   }
	   
	   public String Player_Withdrew(SocketConnect client, String[] ID) throws IOException{
	   		client.send("GET:"+ID[0]+","+ID[1]+"\n");
	   		String response  = client.recv();
			System.out.println(response);
			return response;

	   }
	   public String Player_Quit(SocketConnect client, String[] ID) throws IOException{
	   		client.send("GET:"+ID[0]+","+ID[1]+"\nQUIT\n");
	   		String response  = client.recv();
			System.out.println(response);
			return response;
	   }

}
