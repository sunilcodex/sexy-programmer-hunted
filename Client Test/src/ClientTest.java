import java.lang.*;
import java.io.*;
import java.net.*;


public class ClientTest {	
	static String serveraddress = "140.113.166.42";
	static SocketConnect client;
	   public static void main(String[] args)
	   {
	      try {
	         
	    	  //set socket net
	    	  client = new SocketConnect(serveraddress, 5050);
	    	  /////////////////////////////
	    	  
	    	  //set name, Id
	    	  String name = "test";
	    	  String ID_test = "0";
	    	  ///////////////////////////////////
	    	  
	    	  
	    	  //new game Button--get Id and user Id
	    	  final String[] new_game = client.NewGame(client, name);
	    	  //System.out.println(newGroupId[0]);
	    	  //////////////////////////////////////
	    	  
	    	  
	    	//join game Button--get Id and user Id or "NOID" msg
	    	  final String[] join_game = client.JoinGame(client, name, new_game[0]);
	    	  //System.out.println(joinGroupId[0]);
	    	  
	    	  String host_waiting_set = client.HostWaitChange(client, new_game);
	    	  String client_waiting_ready = client.ClientReady(client, join_game);
	    	  String host_waiting_start = client.HostStart(client, new_game);
	    	  
	    	 // String hunter_play_in_game = client.HunterInGame(client, new_game);
	    	  
	    	  
	    	  ///////////////////////////////////////////
	    	  
	    	//host waiting check connect

			   Thread thread1 = new Thread(new Runnable() {
				   public void run() {
					   int i = 0; 
					   while (i <= 30) {
					        // stop 1 sec to run
					        try {
					        	if(i <= 10){
						        	client.Host_waiting(client, new_game);
									i++;
									Thread.sleep(1000);
					        	}
					        	else if(i > 10 && i < 21 ){
					        		String hunter_play_in_game = client.HunterInGame(client, new_game);
					        		i++;
									Thread.sleep(1000);
					        	}
					        	else{
					        		
					        		client.Host_waiting(client, new_game);
					        		i++;
									Thread.sleep(1000);
					        	}
					        	

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
	    	//  client.HostStart(client, newGroupId);
	    	  
	    	  
	    	  
	    	  
	    	  
	    	  
	    	  
	         
	      }
	      catch (IOException e) {
	         System.out.println("Caught Exception: " + e.toString());
	      }
	


}
}


