package assignment2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Client.java
 * 
 * @author justine yap
 *
 *         Class for Client that sends requests to the intermediate host
 */
public class Client {

	private DatagramSocket socket;// datagram socket to use to both send and receive
	private DatagramPacket send, receive;// datagram packet either a "read request" or a "write request"
	private final static int PORT = 23;// sends packet to this port
	private final String filename = "test.txt";// filename to be sent to server
	private final String mode = "octet";// type of mode request to be sent to server

	/*
	 * client constructor
	 * 
	 * making sure socket creation is successful
	 */
	public Client() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 * creates and formats the request then sends the request to the server
	 * 
	 * @param x - accounts whether request is a read or write request
	 */
	private void createRequest(int x) {
		// create the request
		byte[] filenameBytes = filename.getBytes();
		byte[] modeBytes = mode.getBytes();

		// allocating request size
		byte[] req = new byte[4 + filenameBytes.length + modeBytes.length];

		// alternate between the read and write requests

		// formatting request
		if (x == 11) {
			req[0] = 0;
			req[1] = 4;

			// adding the filename in bytes to request
			for (int i = 0; i < filenameBytes.length; i++) {
				req[2 + i] = filenameBytes[i];
			}

			// 0 byte after filename
			req[2 + filenameBytes.length] = 1;

			// adding the mode in bytes to request
			for (int j = 0; j < modeBytes.length; j++) {
				req[3 + filenameBytes.length + j] = modeBytes[j];
			}

			// 0 byte after mode bytes
			req[3 + filenameBytes.length + modeBytes.length] = 3;
			req[3 + filenameBytes.length + modeBytes.length + 1] = 4;
			req[3 + filenameBytes.length + modeBytes.length + 2] = 5;
		} else {
			// read request
			if (x % 2 == 0) {
				// first two bytes are 0 and 1
				req[0] = 0;
				req[1] = 1;
				System.out.println("[Client] Creating a read request");
			} else { // read request
				// first two bytes are 0 and 2
				req[0] = 0;
				req[1] = 2;
				System.out.println("[Client] Creating a write request");

			}
			// adding the filename in bytes to request
			for (int i = 0; i < filenameBytes.length; i++) {
				req[2 + i] = filenameBytes[i];
			}

			// 0 byte after filename
			req[2 + filenameBytes.length] = 0;

			// adding the mode in bytes to request
			for (int j = 0; j < modeBytes.length; j++) {
				req[3 + filenameBytes.length + j] = modeBytes[j];
			}

			// 0 byte after mode bytes
			req[3 + filenameBytes.length + modeBytes.length] = 0;
		}

		System.out.println("[Client] Request #" + x + ": " + req);
		// create request packet to the server
		try {
			send = new DatagramPacket(req, (3 + filenameBytes.length + modeBytes.length + 1),
					InetAddress.getLocalHost(), PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("[Client] String Request #" + x + ": " + new String(send.getData(), 0, send.getLength()));

		// send packet to server
		try {
			socket.send(send);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("[Client] packet sent to server!");

	}

	/*
	 * receives data/response from server and prints out data
	 */
	private void receiveResponse() {
		// creating receive packet
		byte[] res = new byte[100];
		receive = new DatagramPacket(res, res.length);
		// receiving response from server
		try {
			socket.receive(receive);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("[Client] Byte Response: " + receive.getData());
		System.out.println("[Client] String Response: " + new String(res));

	}

	/*
	 * closes client datagram socket
	 */
	private void closeSocket() {
		socket.close();
		System.out.println("[Client] Socket closed!");
	}

	/**
	 * main func
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// create client
		Client client = new Client();
		for (int i = 0; i < 11; i++) {
			client.createRequest(i);
			client.receiveResponse();
			System.out.println();
		}
		client.closeSocket();// closing client socket
	}

}
