import java.lang.*;
import java.io.*;
import java.net.*;


public class ClientTest {	
	static String serveraddress = "140.113.166.42";
	static SocketConnect client;
	static boolean withdraw = true;
	   public static void main(String[] args)
	   {
	      try {
	         
	    	  //set socket net
	    	  client = new SocketConnect(serveraddress, 5050);
	    	  /////////////////////////////
	    	  
	    	  //set name, Id
	    	  String name = "test";
	    	  String ID = "6";
	    	  
	    	  
	    	  ///////////////////////////////////
	    	  
	    	  
	    	  //new game Button--get Id and user Id
	    	  final String[] newGroupId = client.NewGame(client, name);
	    	  System.out.println(newGroupId[0]);
	    	  //////////////////////////////////////
	    	  
	    	  
	    	//join game Button--get Id and user Id or "NOID" msg
	    	  String[] joinGroupId = client.JoinGame(client, name, ID);
	    	  System.out.println(joinGroupId[0]);
	    	  ///////////////////////////////////////////
	    	 
	    	  client.HostStart(client, newGroupId);  
	    	  
	    	  
	    	//check connect
			   Thread thread1 = new Thread(new Runnable() {
				   public void run() {
					   int i = 0; 
					   String tmp;
					   while (i <= 25) {
					        // stop 1 sec to run
					        try {
					        	if(i > 10 && i <21)
					        		tmp = client.PlayerInGame(client, newGroupId,"120","140",false);
					        	/*
					        	if(i == 12){
					        		System.out.println("withdraw!!!!:");
					        		tmp = client.PlayerInGame(client, newGroupId,"120.0","140.0",true);
					        	}
					        	else if(i == 11)
					        		tmp = client.PlayerInGame(client, newGroupId,"120","140",false);
					        	else if(i == 14){
					        		tmp = client.Player_Quit(client, newGroupId);
					        		System.out.println("Quit Game!!!!:");
					        	}
					        	*/
					        	else if(i == 22){
					        		tmp = client.Player_Quit(client, newGroupId);
					        		System.out.println("Quit Game!!!!:");
					        	}
					        	else
					        		tmp = client.Host_waiting(client, newGroupId);
								i++;
								Thread.sleep(1000);
							} 
	                          catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					        
					      }  
				   }
			   });
			thread1.start();
			/////////////////////////////////////////////
			
			
			
			//Host start Button -- go to count down
	    	 // client.HostStart(client, newGroupId);
	    	  
	    	//Player in Game   
			 //client.PlayerInGame(client, newGroupId); 
	    	  

	    	  
	         
	      }
	      catch (IOException e) {
	         System.out.println("Caught Exception: " + e.toString());
	      }
	


}
}


