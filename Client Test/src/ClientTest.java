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
	    	  
	    	//host waiting check connect
			   Thread thread1 = new Thread(new Runnable() {
				   public void run() {
					   int i = 0; 
					   while (i <= 10) {
					        // stop 1 sec to run
					        try {
					        	client.Host_waiting(client, newGroupId);
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
	    	  client.HostStart(client, newGroupId);
	    	  
	    	  
	    	  
	    	  
	    	  
	    	  
	    	  
	         
	      }
	      catch (IOException e) {
	         System.out.println("Caught Exception: " + e.toString());
	      }
	


}
}


