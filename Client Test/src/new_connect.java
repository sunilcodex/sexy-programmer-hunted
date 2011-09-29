

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.*;

public class new_connect {
	public static new_connect Instance;
	public static String[] SessionID;

	public final static String DefaultIP = "140.113.166.42";
	public final static int DefaultPort = 5050;

	private Socket socket = null;
	private BufferedReader reader = null;
	private BufferedWriter writer = null;
	private InetAddress host;
	private int port;

	public static HashMap<String, Object> parse(String value) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String[] lines = value.split("\n");
		String[] values = new String[lines.length];

		for (int i = 0; i < values.length; i++) {
			String[] kv = lines[i].split(":");

			if (kv[0].equals("USER"))
			{
				// User data
				
				HashMap<String, String[]> userMap;
				if(!map.containsKey("USER"))
					map.put("USER", new HashMap<String, String[]>());
				
				userMap = (HashMap<String, String[]>)map.get("USER");
					
				String[] tmp = kv[1].split(",");
				userMap.put(tmp[0], tmp);
			}
			else
			{
				if (kv.length == 2)
					map.put(kv[0], kv[1]);
				else if (kv.length == 1)
					map.put(kv[0], "");
			}
		}

		return map;
	}

	public new_connect(String address, int port) throws IOException {
		List<String[]> list = new ArrayList<String[]>();
		
		String[] a = {"1","nnn","p","X"};
		list.add(a);
		
		
		this.host = InetAddress.getByName(address);
		this.port = port;
		// socket = new Socket(host, this.port);
		// reader = new BufferedReader(new
		// InputStreamReader(socket.getInputStream()));
		// writer = new BufferedWriter(new
		// OutputStreamWriter(socket.getOutputStream()));
	}

	public void send(String msg) throws IOException {

		socket = new Socket(host, this.port);
		reader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		writer.write(msg, 0, msg.length());
		writer.flush();
	}

	public String recv() throws IOException {
		String userInput;
		String receive = "";
		while ((userInput = reader.readLine()) != null) {
			// System.out.println("echo: " + in.readLine());
			receive += userInput + "\n";
		}
		socket.close();

		return receive;
	}

	public String[] NewGame(new_connect client, String name)
			throws IOException {
		client.send("GET\nNEW_GAME\nNICKNAME:" + name + "\n");
		String response = client.recv();
		System.out.println(response);
		String[] tmp = response.split(":");
		String[] tmp2 = tmp[1].split("\n");
		String[] Id = tmp2[0].split(",");
		return Id;

	}

	public String[] JoinGame(new_connect client, String name, String ID)
			throws IOException {
		client.send("GET\nJOIN_GAME\nGROUP_ID:" + ID + "\nNICKNAME:" + name
				+ "\n");
		String response = client.recv();
		String[] tmp = null;

		Pattern p = Pattern
				.compile("STATE_FREE\\nSESSION_ID:([0-9]+,[0-9]+)\\n");
		Matcher m = p.matcher(response);
		boolean b = m.matches();
		System.out.println(response);
		// boolean b =
		// Pattern.matches("^STATE_FREE\\nSESSION_ID:([0-9]+,[0-9]+)\\n",
		// response);
		if (b) {

			String[] session_id = m.group(1).split(",");
			tmp = session_id;
			//System.out.print("gid:" + session_id[0] + "\n");
			// return test;
		}

		return tmp;
	}

	public ArrayList<String[]> HostWaitChange(new_connect client, String[] ID)throws IOException {
		//List<String[]> list = new ArrayList<String[]>();
		List<String[]> list = new ArrayList<String[]>();

		client.send("GET:" + ID[0] + "," + ID[1] + "\n" + "USER:1,H\n");

		String response = client.recv();

		Pattern p = Pattern.compile("^STATE_HOST_WAITING\\nGROUP_ID:([0-9]+)\\n.*", Pattern.DOTALL);
		Matcher m = p.matcher(response);

		if( m.matches() ){
			
			String[] packet_info = response.split("\n");
			//System.out.println(packet_info[4]);
			
			Pattern user_pattern = Pattern.compile("USER:([0-9]+),([0-9a-zA-Z]+),[HP],[OX].*", Pattern.DOTALL);
			for(int i=2;i < packet_info.length;i++){ 
				
				Matcher user_matcher = user_pattern.matcher(packet_info[i]);
				
				if( user_matcher.matches()){
					System.out.println("HOST wait:" + response);
					String[] user_info = user_matcher.group(0).split(":");
					user_info = user_info[1].split(",");
					
					list.add(user_info);

					}
				}
			}
	
		return (ArrayList<String[]>) list; 
	}


	public ArrayList<String[]> InfoParser(String response, String state)throws IOException{
		List<String[]> list = new ArrayList<String[]>();
		//Pattern p = Pattern.compile("^STATE_HOST_WAITING\\nGROUP_ID:([0-9]+)\\n.*", Pattern.DOTALL);
		Pattern p = Pattern.compile(state + "GROUP_ID:([0-9]+)\\n.*", Pattern.DOTALL);
		Matcher m = p.matcher(response);

		if( m.matches() ){
			
			String[] packet_info = response.split("\n");
			//System.out.println(packet_info[4]);
			
			Pattern user_pattern = Pattern.compile("USER:([0-9]+),([0-9a-zA-Z]+),[HP],[OX].*", Pattern.DOTALL);
			for(int i=2;i < packet_info.length;i++){ 
				
				Matcher user_matcher = user_pattern.matcher(packet_info[i]);
				
				if( user_matcher.matches()){
					System.out.println("HOST wait:" + response);
					String[] user_info = user_matcher.group(0).split(":");
					user_info = user_info[1].split(",");
					
					list.add(user_info);

					}
				}
			}
		return (ArrayList<String[]>) list;
	}
	
	// Thread Running
	public ArrayList<String[]> Host_waiting_get(new_connect client, String[] ID)throws IOException {
		//List<String[]> list = new ArrayList<String[]>();
		List<String[]> list = new ArrayList<String[]>();

		client.send("GET:" + ID[0] + "," + ID[1] + "\n");

		String response = client.recv();

		list = InfoParser(response, "STATE_HOST_WAITING\\n");
		return (ArrayList<String[]>) list; 

}
		

	public  ArrayList<String[]> HostStart(new_connect client, String[] ID)throws IOException {
		List<String[]> list = new ArrayList<String[]>();

		client.send("GET:" + ID[0] + "," + ID[1] + "\n" + "START\n");
		String response = client.recv();

		list = InfoParser(response, "STATE_HOST_WAITING\\n");
	
		return (ArrayList<String[]>) list; 
	}

	public  ArrayList<String[]> HostCancel(new_connect client, String[] ID)throws IOException {
		List<String[]> list = new ArrayList<String[]>();

		client.send("GET:" + ID[0] + "," + ID[1] + "\n" + "CANCEL\n");
		String response = client.recv();
		list = InfoParser(response, "STATE_HOST_WAITING\\n");
	
		return (ArrayList<String[]>) list; 
	}

	public ArrayList<String[]> Client_waiting_get(new_connect client, String[] ID)throws IOException {
			//List<String[]> list = new ArrayList<String[]>();
			List<String[]> list = new ArrayList<String[]>();

			client.send("GET:" + ID[0] + "," + ID[1] + "\n");

			String response = client.recv();

			list = InfoParser(response, "STATE_PLAYER_WAITING\\n");
			return (ArrayList<String[]>) list; 

	}
	public ArrayList<String[]> Client_get_ready(new_connect client, String[] ID)throws IOException {
		//List<String[]> list = new ArrayList<String[]>();
		List<String[]> list = new ArrayList<String[]>();

		client.send("GET:" + ID[0] + "," + ID[1] + "\n" + "READY\n");

		String response = client.recv();

		list = InfoParser(response, "STATE_PLAYER_WAITING\\n");
		return (ArrayList<String[]>) list; 
}
	public ArrayList<String[]> Client_cancel(new_connect client, String[] ID)throws IOException {
		//List<String[]> list = new ArrayList<String[]>();
		List<String[]> list = new ArrayList<String[]>();

		client.send("GET:" + ID[0] + "," + ID[1] + "\n" + "READY\n");

		String response = client.recv();

		list = InfoParser(response, "STATE_PLAYER_WAITING\\n");
		return (ArrayList<String[]>) list; 
}
	public String HunterInGame(new_connect client, String[] ID, String gpsX, String gpsY)
			throws IOException {
		client.send("GET:" + ID[0] + "," + ID[1] + "\n" + "GPS:" + gpsX + "," + gpsY
				+ "\n");
		String response = client.recv();
		Pattern p = Pattern.compile("^STATE_HUNTER_IN_GAME\\n.*",
				Pattern.DOTALL);
		Matcher m = p.matcher(response);
		boolean b = m.find();
		System.out.println(b);
		if (b) {
			System.out.println("HunterInGame:" + "\n" + response);
			return response;
		} else
			return "HunterInGame Failed";
	}

	public String HunterTimeOutQuit(new_connect client, String[] ID)
			throws IOException {
		client.send("GET:" + ID[0] + "," + ID[1] + "\n" + "QUIT" + "\n");
		String response = client.recv();
		Pattern p = Pattern.compile("^STATE_HUNTER_TIMEOUT\\n.*",
				Pattern.DOTALL);
		Matcher m = p.matcher(response);
		boolean b = m.find();
		System.out.println(b);
		if (b) {
			System.out.println("HunterTimeOutQuit:" + "\n" + response);
			return response;
		} else
			return "HunterTimeOutQuit Failed";
	}

	public String HunterFailedQuit(new_connect client, String[] ID)
			throws IOException {
		client.send("GET:" + ID[0] + "," + ID[1] + "\n" + "QUIT" + "\n");
		String response = client.recv();
		Pattern p = Pattern
				.compile("^STATE_HUNTER_FAILED\\n.*", Pattern.DOTALL);
		Matcher m = p.matcher(response);
		boolean b = m.find();
		System.out.println(b);
		if (b) {
			System.out.println("HunterFailed:" + "\n" + response);
			return response;
		} else
			return "HunterFailed Failed";
	}

	public String HunterWinQuit(new_connect client, String[] ID)
			throws IOException {
		client.send("GET:" + ID[0] + "," + ID[1] + "\n" + "QUIT" + "\n");
		String response = client.recv();
		Pattern p = Pattern.compile("^STATE_HUNTER_WIN\\n.*", Pattern.DOTALL);
		Matcher m = p.matcher(response);
		boolean b = m.find();
		System.out.println(b);
		if (b) {
			System.out.println("HunterWin:" + "\n" + response);
			return response;
		} else
			return "HunterWin Failed";
	}

	public String PlayerInGame(new_connect client, String[] ID, String gpsX,
			String gpsY, boolean withdraw) throws IOException {
		String response;
		if (withdraw) {
			client.send("GET:" + ID[0] + "," + ID[1] + "\nGPS:" + gpsX + ","
					+ gpsY + "\nWITHDREW\n");
			response = client.recv();
			System.out.println("HOST in game:\n" + response);
		} else {
			client.send("GET:" + ID[0] + "," + ID[1] + "\nGPS:" + gpsX + ","
					+ gpsY + "\n");

			response = client.recv();
			System.out.println("HOST in game:\n" + response);
		}
		return response;
	}

	public void Client_waiting(new_connect client, String[] ID)
			throws IOException {
		client.send("GET:" + ID[0] + "," + ID[1] + "\n");
		String response = client.recv();
		System.out.println(response);

	}

	public String Player_Withdrew(new_connect client, String[] ID)
			throws IOException {
		client.send("GET:" + ID[0] + "," + ID[1] + "\n");
		String response = client.recv();
		System.out.println(response);
		return response;

	}

	public String Player_Quit(new_connect client, String[] ID)
			throws IOException {
		client.send("GET:" + ID[0] + "," + ID[1] + "\nQUIT\n");
		String response = client.recv();
		System.out.println(response);
		return response;
	}

}

