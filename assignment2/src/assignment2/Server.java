package assignment2;

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
		if (req[1] != 1 && req[1] != 2) {
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

		System.out.println("[Server] Received (String) request: " + new String(req));
		System.out.println("[Server] Received (byte) request: " + req);

		// preparing the response
		byte[] res = new byte[4];
		res[0] = 0;
		if (req[1] == 1) {// 301 for read
			res[1] = 3;
			res[2] = 0;
			res[3] = 1;
		} else {// 400 for write
			res[1] = 4;
			res[2] = 0;
			res[3] = 0;
		}

		// send the response back to host
		DatagramSocket sendSocket = new DatagramSocket();
		InetAddress address = receivePacket.getAddress();
		int port = receivePacket.getPort();

		try {
			sendPacket = new DatagramPacket(res, res.length, address, port);
			sendSocket.send(sendPacket);
			sendSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("[Server] Sending (String) response: " + new String(res));
		System.out.println("[Server] Sending (byte) response: " + Arrays.toString(res));
		System.out.println();

	}

	/*
	 * main function
	 * 
	 */
	public static void main(String[] args) throws Exception {
		Server server = new Server();
		while (true) {
			server.run();
		}
	}
}
