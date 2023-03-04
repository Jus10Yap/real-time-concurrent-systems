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
public class Client extends UDPEntity {

	private final static int PORT = 23;// sends packet to this port
	private final String filename = "test.txt";// filename to be sent to server
	private final String mode = "octet";// type of mode request to be sent to server
	private final static int MAX_REQUESTS = 11; // maximum number of requests/data/packets client can send to host
	/*
	 * client constructor
	 * 
	 */
	public Client() {
		super();
	}

	/**
	 * Creates and formats the request then sends the request to the server.
	 * 
	 * @param x the type of request: even number for read, odd for write,
	 *          MAX_REQUESTS for invalid.
	 * 
	 */
	private byte[] createRequest(int x) {
		// create the request
		byte[] filenameBytes = filename.getBytes();
		byte[] modeBytes = mode.getBytes();
		int reqLength = 4 + filenameBytes.length + modeBytes.length;

		// allocating request size
		byte[] req = new byte[reqLength];

		// alternate between the read and write requests

		// formatting request
		if (x == MAX_REQUESTS) {// invalid request
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
		// send request to host
	
		
		return req;

	}

	public void run() {
		byte [] req;
		for(int i = 0; i < MAX_REQUESTS; i++) {
			 // create a request and send it to the host
			req = createRequest(i);
			System.out.println("[Client] Created Byte request: " + req);
			System.out.println("[Client] Created String request: " + new String(req));
			// send a request to the server for processed response data
			rpcSend(req,PORT);
		}
 
        // close the socket
		closeSocket();
		System.out.println("[Client] Sockets closed!");
    }

	/**
	 * main function
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) {
		// create client
		Client client = new Client();
		client.run();
	}

}
