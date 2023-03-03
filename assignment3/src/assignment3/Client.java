package assignment3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

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
	 * sends a read request to the server via the Intermediate task using
	 * synchronous RPCs
	 * 
	 * @param x - accounts whether request is a read or write request
	 */
	private byte[] createRequest(int x) {

		// create the request
		byte[] filenameBytes = filename.getBytes();
		byte[] modeBytes = mode.getBytes();

		// allocate request size
		byte[] req = new byte[4 + filenameBytes.length + modeBytes.length];

		// formatting request
		req[0] = 0;
		req[1] = 2;

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

		System.out.println("[Client] Sending write request: " + req);

		// send the request to the Intermediate task via RPC
		byte[] result = rpc_send(req);

		System.out.println("[Client] Received write response: " + result);

		return result;
	}

	/*
	 * sends a write request to the server via the Intermediate task using
	 * synchronous RPCs
	 */
	private byte[] receiveResponse() {
		// creating receive packet
		byte[] res = new byte[100];
		receive = new DatagramPacket(res, res.length);
		// receiving response from server
		try {
			// wait to receive data from the server
			socket.receive(receive);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("[Client] Byte Response: " + receive.getData());
		System.out.println("[Client] String Response: " + new String(res));

		return res;

	}

	/*
	 * sends a request to the Intermediate task via RPC
	 * 
	 * @param req - the request to be sent
	 */
	private byte[] rpc_send(byte[] req) {

		// send the request to the Intermediate task
		try {
			send = new DatagramPacket(req, req.length, InetAddress.getLocalHost(), PORT);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			socket.send(send);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("[Client] String Write Request: " + new String(send.getData(), 0, send.getLength()));

		// receive the response from the Intermediate task
		byte[] res = new byte[100];
		receive = new DatagramPacket(res, res.length);
		try {
			socket.receive(receive);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// return the response
		byte[] response = Arrays.copyOfRange(receive.getData(), 0, receive.getLength());
		return response;
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

		// send a read request
		client.createRequest(1);

		// receive response from server
		client.receiveResponse();

		// send a write request
		client.createRequest(2);

		// receive response from server
		client.receiveResponse();

		client.closeSocket();// closing client socket
	}

}
