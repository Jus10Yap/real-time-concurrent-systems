package assignment2;

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

	private static int RECEIVE_PORT = 23;// port used for socket to receive
	private static int SEND_RECEIVE_PORT = 69;// port used for packet to send and receive

	/*
	 * host constructor
	 * 
	 * making sure send and receive sockets were created successfully
	 */
	public Host() {
		// create sockets
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(RECEIVE_PORT, InetAddress.getLocalHost());
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
		}
		InetAddress address = receivePacket.getAddress();
		int clientPort = receivePacket.getPort();

		System.out.println("[Host] Received (byte) request from client: " + receivePacket.getData());
		System.out.println("[Host] Received (String) request from client: " + new String(data));

		// sending packet to the server
		try {
			sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(),
					InetAddress.getLocalHost(), SEND_RECEIVE_PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[Host] Sent request to server!");

		byte[] res = new byte[500];
		receivePacket = new DatagramPacket(res, res.length);
		// receiving packet from server
		try {
			sendSocket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("[Host] Received (byte) request from server: " + receivePacket.getData());
		System.out.println("[Host] Received (String) request from server: " + new String(res));

		// sending packet to the client

		sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(), address, clientPort);

		try {

			sendSocket.send(sendPacket);
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[Host] Sent request to client!");

	}

	public static void main(String[] args) {
		Host host = new Host();
		while (true) {
			host.run();
		}
	}

}
