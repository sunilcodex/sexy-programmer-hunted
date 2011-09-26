import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SocketConnect{
	   private Socket socket = null;
	   private BufferedReader reader = null;
	   private BufferedWriter writer = null;
	   private InetAddress host;
	   private int port;
	   private SocketConnect client_tmp;

	   
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
	         //System.out.println(response);
	         String[] tmp = response.split(":");
	         String[] Id = tmp[1].split(",");       
			return Id;

	   }
	   
	   
	   public String[] JoinGame(SocketConnect client, String name, String ID) throws IOException{
	         client.send("GET\nJOIN_GAME\nGROUP_ID:"+ID+"\nNICKNAME:"+name+"\n");
	         String response = client.recv();
	         if (response == ""){
	        	 String[] failed = {"NOID", ""};
	        	 return failed;  
	         }
	         else{
	        	 String[] tmp = response.split(":");
	        	 String[] Id = tmp[1].split(",");
	        	 return Id;  
	         }
	   }
	   
	   
	   public void Host_waiting(SocketConnect client, String[] ID) throws IOException{
		   client.send("GET:"+ID[0]+","+ID[1]);
		   //System.out.print("GET:"+id[0]+","+id[1]+"\n");
		   String response = client.recv();
			System.out.println("HOST wait:"+response);

		//return wait_msg;
	   
	   }
	   
	   public void HostStart(SocketConnect client, String[] ID) throws IOException{
	         client.send("GET:"+ID[0]+","+ID[1] + "START\n");
	         String response = client.recv();
	         System.out.println("HOST START:"+response);
	   }
	   
	   
	   
	   
	   public void Client_waiting(SocketConnect client, String[] ID) throws IOException{
		   		client.send("GET:"+ID[0]+","+ID[1]);
		   		String response  = client.recv();
				System.out.println(response);
  
	   }
	   
	   

}
