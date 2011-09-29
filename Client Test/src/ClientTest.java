import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class ClientTest {	
	static String serveraddress = "140.113.166.42";
	static new_connect client;
	static boolean withdraw = true;
	   public static void main(String[] args)
	   {
	      try {
	         
	    	  //set socket net
	    	  client = new new_connect(serveraddress, 5050);
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
	    	  String[] joinGroupId = client.JoinGame(client, name, newGroupId[0]);
	    	  //lient.JoinGame(client, name, ID);
	    	  System.out.println(joinGroupId[0]);
	    	  ///////////////////////////////////////////
	    	  ArrayList<String[]> Host_waiting = client.Host_waiting_get(client, newGroupId);
	    	  //client.HostStart(client, newGroupId);  
	    	  System.out.println(Host_waiting.get(1)[0]);
	    	  
	    	  /*
	    	//check connect
			   Thread thread1 = new Thread(new Runnable() {
				   public void run() {
					   int i = 0; 
					   String tmp;
					   while (i <= 25) {
					        // stop 1 sec to run
					        try {
					        	if(i > 5 && i < 12)
					        		tmp = client.PlayerInGame(client, newGroupId,"120","140",false);

					        	else if(i == 7){
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
	*/


	      }
	      catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	      }
	   }
	      
}


