package org.usfirst.frc810.Stronghold;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class TCPClient {

	private final String pingMessage = "A\n";
	private final int timeOut = 250;// ms

	private ScheduledThreadPoolExecutor tcpPool = new ScheduledThreadPoolExecutor(5); 
	private Future<ClientWorker> worker; //Will hold the tcpclient when it is created

	public TCPClient() {
		worker = tcpPool.submit(() -> new ClientWorker("10.8.10.44", 5805)); //Create worker
	}

	class ClientWorker {
		private Socket sock;
		private BufferedReader reader;
		private DataOutputStream output;

		public String readLine() throws IOException {
			return reader.readLine();
		}

		public void outputCommand(String line) throws IOException {
			output.writeBytes(line);
		}

		public ClientWorker(String IP, int port) {
			boolean created = false;
			//Keep trying until created
			while (!created) {

				try {

					sock = new Socket(IP, port);
					sock.setSoTimeout(timeOut);
					System.out.println("Socket created");

					reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
					System.out.println("Buffered Reader created");
					output = new DataOutputStream(sock.getOutputStream());
					;
					System.out.println("DataOutputStream created");
					created = true;

				} catch (Exception e) {
					//Try again in a second
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						//This shouldn't happen
						e1.printStackTrace();
					}
				}
			}
		}
	}

	public Future<Double> getAngleCorrection() {
		if (worker.isDone())
			try {
				return tcpPool.submit(new AngleCorrectionTask(worker.get()));
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

		return tcpPool.submit(() -> Double.NaN);
	}

	public class AngleCorrectionTask implements Callable<Double> {
		ClientWorker worker;

		public AngleCorrectionTask(ClientWorker worker) {
			this.worker = worker;
		}

		@Override
		public Double call() {
			try {
				worker.outputCommand(pingMessage);
				String result = worker.readLine();

				Double d = Double.parseDouble(result);

				return d;
			} catch (IOException | NumberFormatException | NullPointerException e) {
				// Timeout
				return Double.NaN;
			}
		}
	}
}
