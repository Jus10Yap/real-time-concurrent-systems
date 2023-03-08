/**
 * 
 */
package assignment3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * UDPEntity.java code copied from Iteration 2
 * 
 * provides functionality for sending and receiving UDP packets.
 * 
 * @author Justine Yap
 *
 */
public class UDPEntity {
	private DatagramSocket sendSocket, receiveSocket; // send and receive sockets
	private DatagramPacket sendPacket, receivePacket; // send and receive packets

	// constructors
	/**
	 * UDPEntity constructor
	 * 
	 * Creates a new UDPEntity object with send and receive sockets.
	 * 
	 * @throws SocketException if there is an error creating the sockets
	 */
	public UDPEntity() {
		// create sockets
		try {
			receiveSocket = new DatagramSocket();
			sendSocket = receiveSocket;
			
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * UDPEntity constructor
	 * 
	 * Creates a new UDPEntity object with a specified receivePort.
	 * 
	 * @param port - the port to receive packets on
	 * @throws SocketException - if there is an error creating the sockets
	 */
	public UDPEntity(int port) {
		try {
			receiveSocket = new DatagramSocket(port);
			sendSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	// getters and setters

	/**
	 * Get the DatagramPacket object used for sending data.
	 * 
	 * @return the send packet
	 */
	DatagramPacket getSendPacket() {
		return sendPacket;
	}

	/**
	 * Get the DatagramPacket object used for receiving data.
	 * 
	 * @return the receive packet
	 */
	DatagramPacket getReceivePacket() {
		return receivePacket;
	}

	/**
	 * Send a request to a remote host and receive a response.
	 * 
	 * @param port - the port to send the request to
	 * @return the response received from the remote host
	 * @throws UnknownHostException if the host is unknown
	 * @throws IOException          if there is an error sending or receiving data
	 */
	public byte[] rpcSend(int port) {
		// send a data request
		byte[] req = "DATA".getBytes();
		try {
			sendPacket = new DatagramPacket(req, req.length, InetAddress.getLocalHost(), port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[Server] Byte Request to Host: " + Arrays.toString(req));
        System.out.println("[Server] String Request to Host: " + new String(req));
		// receive response from host
		byte[] res = new byte[100];
		receivePacket = new DatagramPacket(res, res.length);
		try {
			receiveSocket.receive(receivePacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] result = new byte[receivePacket.getLength()];
		System.arraycopy(res, 0, result, 0, receivePacket.getLength());
	
		System.out.println("[Server] Received String Request from Host: " + new String(result));
        System.out.println("[Server] Received Byte Request from Host: " + Arrays.toString(result));
        
		// print packet information
        System.out.println();
		System.out.println("----- [PACKET INFO] -----");
		System.out.println("[Address] " + receivePacket.getAddress());
		System.out.println("[Port] " + receivePacket.getPort());
		System.out.println("[Size] " + receivePacket.getLength());
		System.out.println("[Contents] " +new String(receivePacket.getData(), 0, receivePacket.getLength()));
		System.out.println();
		return result;
	}

	/**
	 * 
	 * Send data to a remote host and receive an acknowledgement.
	 * 
	 * @param data the data to send
	 * @param port the port to send the data to
	 * @throws UnknownHostException if the host is unknown
	 * @throws IOException          if there is an error sending or receiving data
	 */
	public void rpcSend(byte[] data, int port) {
		// create a packet with the data to be sent
		try {
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), port);
		} catch (UnknownHostException e1) {
		
			e1.printStackTrace();
		}

		// send the packet to the remote host
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			System.err.println("An error occurred while sending data to the host.");
			e.printStackTrace();
			System.exit(1);
		}

		// print packet information
		System.out.println();
		System.out.println("----- [PACKET INFO] -----");
		System.out.println("[Address] " + sendPacket.getAddress());
		System.out.println("[Port] " + sendPacket.getPort());
		System.out.println("[Size] " + sendPacket.getLength());
		System.out.println("[Contents] " +new String(sendPacket.getData(), 0, sendPacket.getLength()));
		System.out.println();
		
		// receive acknowledgement from the remote host
		byte[] ack = new byte[12];
		receivePacket = new DatagramPacket(ack, ack.length);
		
		try {
			receiveSocket.receive(receivePacket);
			System.out.println("Acknowledgement Received!");
			System.out.println(new String(receivePacket.getData()));
		} catch (IOException e) {
			System.err.println("An error occurred while receiving acknowledgement from the remote host.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * 
	 * Send acknowledgement packet to specified port.
	 * 
	 * @param port the port to send the acknowledgement to
	 */
	public void sendData(int port) {
		// Create acknowledgement data
		byte[] data = "ACKNOWLEDGED".getBytes();

		try {
			// Create packet with acknowledgement data and destination address/port
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), port);
		} catch (UnknownHostException e) {
			// Handle exception if destination address is unknown
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			// Send acknowledgement packet
			sendSocket.send(sendPacket);
			System.out.println("[Host] Sent Acknowledgement");
			// print packet information
			System.out.println();
			System.out.println("----- [PACKET INFO] -----");
			System.out.println("[Address] " + sendPacket.getAddress());
			System.out.println("[Port] " + sendPacket.getPort());
			System.out.println("[Size] " + sendPacket.getLength());
			System.out.println("[Contents] " +new String(sendPacket.getData()));
			System.out.println();
			
		} catch (IOException e) {
			// Handle exception if there is an error sending the packet
			e.printStackTrace();
			System.exit(1);
		}
	}
	


	/**
	 * Send data to a remote host using a DatagramPacket and receive an
	 * acknowledgement using a different socket.
	 *
	 * @param data   the data to send
	 * @param port   the port to send the data to
	 * @param source the source port to receive acknowledgement
	 * @throws UnknownHostException if the host is unknown
	 * @throws IOException          if there is an error sending or receiving data
	 */
	public void sendData(byte[] data, int port) {
		// Create and send a packet using sendSocket
		try {
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println();
		System.out.println("----- [PACKET INFO] -----");
		System.out.println("[Address] " + sendPacket.getAddress());
		System.out.println("[Port] " + sendPacket.getPort());
		System.out.println("[Size] " + sendPacket.getLength());
		System.out.println("[Contents] " +new String(sendPacket.getData()));
		System.out.println();
		
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}

	/**
	 * Receive data from a remote host using the receiveSocket and return the
	 * received data.
	 *
	 * @return received data
	 */
	public byte[] receiveData() {
		byte[] data = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		try {
			// Receive data from remote host
			receiveSocket.receive(receivePacket);
			byte[] result = new byte[receivePacket.getLength()];
			System.arraycopy(data, 0, result, 0, receivePacket.getLength());
			
			System.out.println();
			System.out.println("----- [PACKET INFO] -----");
			System.out.println("[Address] " + receivePacket.getAddress());
			System.out.println("[Port] " + receivePacket.getPort());
			System.out.println("[Size] " + receivePacket.getLength());
			System.out.println("[Contents] " +new String(receivePacket.getData(), 0, receivePacket.getLength()));
			System.out.println("[Array] " + Arrays.toString(receivePacket.getData()));
			System.out.println();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	/**
	 * 
	 * Receive data from a DatagramSocket and return the received bytes as an array.
	 * 
	 * @param socket the DatagramSocket to receive the data from
	 * 
	 * @return a byte array containing the received data
	 */
	public byte[] receiveData(DatagramSocket socket) {
		byte[] data = new byte[100];
		DatagramPacket receivePacket = new DatagramPacket(data, data.length);
		
		try {
			socket.receive(receivePacket);
			byte[] result = new byte[receivePacket.getLength()];
			System.arraycopy(data, 0, result, 0, receivePacket.getLength());
			
			System.out.println();
			System.out.println("----- [PACKET INFO] -----");
			System.out.println("[Address] " + receivePacket.getAddress());
			System.out.println("[Port] " + receivePacket.getPort());
			System.out.println("[Size] " + receivePacket.getLength());
			System.out.println("[Contents] " + new String(receivePacket.getData(), 0, receivePacket.getLength()));
			System.out.println("[Array] " + Arrays.toString(result));

			System.out.println();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public void closeSocket() {
		receiveSocket.close();
		sendSocket.close();
		
	}

}
