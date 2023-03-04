package assignment3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Host.java
 * 
 * @author justine yap
 *
 *         Class for Host that sends requests on to the server/client
 */
public class Host extends UDPEntity implements Runnable {

	private DatagramSocket serverReceiveSocket; // send and receive sockets

	private static int RECEIVE_PORT_CLIENT = 23; // port used for socket to receive from client
	private static int RECEIVE_PORT_SERVER = 24; // port used for socket to receive from server
	private static int SEND_PORT_SERVER = 69; // port used for packet to send from server

	private byte[] req; // request received from Client
	private byte[] res; // response from Server
	
	 private boolean clientRequestReceived = false;
	 private boolean serverRequestReceived = false;
	/*
	 * host constructor
	 * 
	 * making sure send and receive sockets were created successfully
	 */
	public Host() {
		super(RECEIVE_PORT_CLIENT);
		try {
			serverReceiveSocket = new DatagramSocket(RECEIVE_PORT_SERVER);
			System.out.println("[Host] Created sockets");
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public synchronized void handleClient() {
	
		req = receiveData();
		// receiving request from client
		InetAddress address = getReceivePacket().getAddress();
		int clientPort = getReceivePacket().getPort();
		System.out.println(getReceivePacket().getPort());
		System.out.println(getReceivePacket().getAddress());
		System.out.println("[Host] Received Byte request from client: " + req);
		System.out.println("[Host] Received String request from client: " + new String(req));

		notifyAll();

		// Notify client that request has been received
		System.out.println("[Host] Acknowledging Request from client");
		sendData(clientPort);

		
		// Set client request received flag to true
        clientRequestReceived = true;
        serverRequestReceived = false;
		// If there is no reply from the server yet, wait
		System.out.println("[Host] Waiting for Server Data Request");
		while (!serverRequestReceived) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		// Send reply to client from server
		System.out.println("[Host] Sending for Server processed response to client");
		sendData(res,clientPort);
		
		res = null;
	}

	public synchronized void handleServer() {
		// Receive data request from the server
		byte[] data = receiveData(serverReceiveSocket);
		// receiving request from client
		

		System.out.println("[Host] Received Byte request from server: " + data);
		System.out.println("[Host] Received String request from server: " + new String(data));
		
		// Set server request received flag to true
        clientRequestReceived = false;
        serverRequestReceived = true;
		// If there is no request from the client yet, wait
		System.out.println("[Host] Waiting for Client  Request");
		while (!clientRequestReceived) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		// Send client's request to server
		System.out.println("[Host] Sending client request to server");
		sendData(req, SEND_PORT_SERVER);

		System.out.println("[Host] Sending Byte request to server: " + getSendPacket().getData());
		System.out.println("[Host] Sending String Request to server: " + new String(getSendPacket().getData()));


		req = null;

		// Receive reply from server
		
		res = receiveData(serverReceiveSocket);
		notifyAll();

		System.out.println("[Host] Received Byte reply from server: " + res);
		System.out.println("[Host] Received String reply from server: " + new String(res));

		

		// Notify server that reply has been received
		System.out.println("[Host] Acknowledging Request from Server");
		sendData(SEND_PORT_SERVER);
	}

	/*
	 * main method to execute
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		Host host = new Host();
		Thread client = new Thread(host, "Client");
		client.start();
		
		while (true) {
		
			
			host.handleServer();
			
	
		}
	}

	@Override
	public void run() {
		while (true) {
		
			handleClient();
				
			
		}

	}
}
