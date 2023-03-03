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

	private DatagramSocket clientServerSocket; // socket to receive data from client
    private DatagramSocket serverClientSocket; // socket to send data to client
    private DatagramSocket serverIntermediateSocket; // socket to receive data from server
    private DatagramSocket intermediateServerSocket; // socket to send data to server
    
    private static int CLIENT_SERVER_PORT = 23; // port used for socket to receive data from client
    private static int SERVER_INTERMEDIATE_PORT = 69; // port used for socket to receive data from server
    private static int INTERMEDIATE_SERVER_PORT = 68; // port used for socket to send data to server
    private static int SERVER_CLIENT_PORT = 70; // port used for socket to send data to client
	    

	/*
	 * host constructor
	 * 
	 * making sure send and receive sockets were created successfully
	 */
	public Host() {
		// create sockets
        try {
            clientServerSocket = new DatagramSocket(CLIENT_SERVER_PORT);
            serverClientSocket = new DatagramSocket();
            serverIntermediateSocket = new DatagramSocket(SERVER_INTERMEDIATE_PORT);
            intermediateServerSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
	}

	/*
	 * Receive packet from Client Send packet to the Server Receive packet from
	 * Server Send packet to the Client
	 */

	private void run() {
		byte[] data = new byte[100];
        DatagramPacket clientRequestPacket = new DatagramPacket(data, data.length);
        // receive packet from client
        try {
            clientServerSocket.receive(clientRequestPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InetAddress clientAddress = clientRequestPacket.getAddress();
        int clientPort = clientRequestPacket.getPort();
        System.out.println("[Intermediate] Received (byte) request from client: " + clientRequestPacket.getData());
        System.out.println("[Intermediate] Received (String) request from client: " + new String(data));
        
        // send packet to the server
        try {
            DatagramPacket serverRequestPacket = new DatagramPacket(clientRequestPacket.getData(), clientRequestPacket.getLength(),
                    InetAddress.getLocalHost(), INTERMEDIATE_SERVER_PORT);
            intermediateServerSocket.send(serverRequestPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[Intermediate] Sent request to server!");
        
        byte[] responseData = new byte[500];
        DatagramPacket serverResponsePacket = new DatagramPacket(responseData, responseData.length);
        // receive packet from server
        try {
            serverIntermediateSocket.receive(serverResponsePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[Intermediate] Received (byte) response from server: " + serverResponsePacket.getData());
        System.out.println("[Intermediate] Received (String) response from server: " + new String(responseData));
        
        // send packet to the client
        DatagramPacket clientResponsePacket = new DatagramPacket(serverResponsePacket.getData(), serverResponsePacket.getLength(), clientAddress, clientPort);
        try {
            serverClientSocket.send(clientResponsePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[Intermediate] Sent response to Client");

	}

	public static void main(String[] args) {
	    Host host = new Host();
	    System.out.println("[Intermediate] Host started!");
	    while (true) {
	        host.run(); // run the host
	    }
	}

}