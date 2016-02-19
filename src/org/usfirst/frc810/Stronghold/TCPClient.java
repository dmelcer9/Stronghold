package org.usfirst.frc810.Stronghold;
import java.io.*;
import java.net.*;

public class TCPClient {
	final String commands[] = {
			"Goal Allign\n"
	};
	final String reads[] ={
			"Left",
			"Right",
			"Alligned"
			
	};
	InputStream in;
	String inputString;
	Socket sock;
	BufferedReader readFromServer;
	DataOutputStream out;
	public TCPClient() throws Exception{
		sock = new Socket("10.8.10.44",5805);
		readFromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new DataOutputStream(sock.getOutputStream());;
	}
	public void allignGoal()throws Exception{
		out.writeBytes(commands[0]);
		boolean alligned= false;
		while (!alligned){
		inputString = readFromServer.readLine();
		if(inputString.equals(reads[0])){
			//turn left
		}
		else if (inputString.equals(reads[1])){
			//turn right
		}
		else if(inputString.equals(reads[2])){
			//gooby stahp
			alligned = true;
		}
		}
		
		
		
	}
}
