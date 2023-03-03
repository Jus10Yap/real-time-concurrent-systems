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
public class Host {

	private DatagramSocket sendSocket, receiveSocket; // send and receive sockets
	private DatagramPacket sendPacket, receivePacket; // send and receive packets

	private static int RECEIVE_PORT_CLIENT_TO_HOST = 23; // port used for socket to receive from client
	private static int RECEIVE_PORT_SERVER_TO_HOST = 24; // port used for socket to receive from server
	private static int SEND_RECEIVE_PORT = 69; // port used for packet to send and receive

	/*
	 * host constructor
	 * 
	 * making sure send and receive sockets were created successfully
	 */
	public Host() {
		// create sockets
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(RECEIVE_PORT_CLIENT_TO_HOST, InetAddress.getLocalHost());
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (UnknownHostException h) {
			h.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 * Receive packet from Client Send packet to the Server Receive packet from
	 * Server Send packet to the Client
	 */

	private void run() {
		byte[] data = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		// receiving packet from client
		try {
			receiveSocket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		InetAddress address = receivePacket.getAddress();
		int clientPort = receivePacket.getPort();

		System.out.println("[Host] Received (byte) request from client: " + receivePacket.getData());
		System.out.println("[Host] Received (String) request from client: " + new String(data));

		// sending packet to the server
		byte[] requestData = receivePacket.getData();
		byte[] responseData = new byte[500];
		try {
			rpc_send(requestData, responseData, InetAddress.getLocalHost(), SEND_RECEIVE_PORT, RECEIVE_PORT_SERVER_TO_HOST);
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
			System.exit(1);
		}

		System.out.println("[Host] Received (byte) request from server: " + responseData);
		System.out.println("[Host] Received (String) request from server: " + new String(responseData));

		// sending packet to the client
		sendPacket = new DatagramPacket(responseData, responseData.length, address, clientPort);

		try {
			sendSocket.send(sendPacket);
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[Host] Sent response to client!");

	}

	/*
	 * Method that sends the request to the server and receives the response
	 */
	private void rpc_send(byte[] requestData, byte[] responseData, InetAddress address, int sendPort, int receivePort) {
		sendPacket = new DatagramPacket(requestData, requestData.length, address, sendPort);
		try {
			sendSocket.send(sendPacket);
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		receivePacket = new DatagramPacket(responseData, responseData.length);

		try {
			receiveSocket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("[Host] Received (byte) response from server: " + receivePacket.getData());
		System.out.println("[Host] Received (String) response from server: " + new String(responseData));

	}

	/*
	 * main method to execute
	 */
	public static void main(String[] args) {
		Host host = new Host();
		while (true) {
			host.run();
		}
	}
}
