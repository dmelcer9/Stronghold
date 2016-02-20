package org.usfirst.frc810.Stronghold;
import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TCPClient {
	final String commands[] = {
			"G\n",
			"S\n"
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
	public TCPClient(){
		boolean created = false;
		while(!created){
			try{
				sock = new Socket("10.8.10.44",5805);
				System.out.println("Socket created");
				readFromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				System.out.println("Buffered Reader created");
				out = new DataOutputStream(sock.getOutputStream());;
				System.out.println("DataOutPutStream created");
				created = true;
			}catch(Exception e){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	public AtomicBoolean cancelled = new AtomicBoolean(false);
	
	public void allignGoal() throws Exception{
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
		SmartDashboard.putString("allignment status", inputString);
		if(SmartDashboard.getBoolean("Cancel")){
			out.writeBytes(commands[1]);
			SmartDashboard.putString("PiOutput", commands[1]);
			cancelled.set(false);
			break;
		}
		else
			out.writeBytes(commands[0]);
			SmartDashboard.putString("PiOutput", commands[0]);
		}
		
		
		
	}
}
