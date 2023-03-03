package assignment3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Server.js
 * 
 * @author justine yap
 * 
 *         Class for Server that sends responses to the intermediate host
 */
public class Server {
	private DatagramPacket sendPacket, receivePacket; // send and receive packets
	private DatagramSocket receiveSocket; // receive sockets
	private final static int PORT = 69; // server port

	/*
	 * server constructor
	 * 
	 * making sure socket is created successfully
	 */
	public Server() {
		try {
			receiveSocket = new DatagramSocket(PORT, InetAddress.getLocalHost());
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
     * Function to send a packet to a specified address and port.
     * 
     * @param data The data to be sent
     * @param address The destination IP address
     * @param port The destination port number
     * @throws IOException if an I/O error occurs
     */
    private void rpc_send(byte[] data, InetAddress address, int port) throws IOException {
        try {
			receiveSocket= new DatagramSocket();
			sendPacket = new DatagramPacket(data, data.length, address, port);
			receiveSocket.send(sendPacket);
			 System.out.println("[Server] Sent (String) response: " + new String(sendPacket.getData()));
		} catch (SocketException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
       
        //sendSocket.close();
    }


	/*
	 * run function
	 * 
	 * receives packet from host then sends response back to host
	 */
	private void run() throws Exception {
		byte[] data = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		// receiving packet from host
		try {
			receiveSocket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] req = receivePacket.getData();
		String mode = "";
		String filename = "";
		// checking request validity
		if (req[0] != 0) {
			throw new Exception("[Server] this filename request format is invalid!");
		}
		if (req[1] != 2) {
			throw new Exception("[Server] this filename request format is invalid!");
		}

		int i = 2;
		while (data[i] != 0) {
			filename += (char) data[i++];
			if (i == req.length - 1) {
				throw new Exception("[Server] this filename request format is invalid!");
			}
		}

		if (filename.length() == 0) {
			throw new Exception("[Server] this filename request format is invalid!");
		}

		i++;
		int start = i;
		while (data[i] != 0) {
			i++;
			if (i == data.length - 1) {
				throw new Exception("[Server] this filename request format is invalid!");
			}
		}
		mode = new String(data, start, i - start, StandardCharsets.US_ASCII);
		if (!mode.equals("netascii") && !mode.equals("octet")) {
			throw new Exception("[Server] this filename request format is invalid!");
		}

		System.out.println("[Server] Received Write(String) request: " + new String(req));
		System.out.println("[Server] Received Write(byte) request: " + req);

		// preparing the response
		byte[] res = new byte[4];
		res[0] = 0;
		res[1] = 4;
		res[2] = 0;
		res[3] = 0;
		System.out.println("[Server] Sending (byte) response: " + Arrays.toString(res));
		// sending the response to the host
		InetAddress address = receivePacket.getAddress();
		int port = receivePacket.getPort();
	    rpc_send(res, address, port);

		
		
		System.out.println();

	}

	/*
	 * main function
	 * 
	 * creates a new instance of Server and runs the run function
	 */
	public static void main(String[] args) {
	    Server server = new Server();
	    try {
	        server.run();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	}
