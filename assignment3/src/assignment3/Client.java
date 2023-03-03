package assignment3;

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

    private DatagramSocket sendSocket, receiveSocket; // send and receive sockets
    private DatagramPacket sendPacket, receivePacket; // send and receive packets
    private InetAddress intermediateAddress;
    private int intermediatePort;

    public Client(InetAddress intermediateAddress, int intermediatePort) {
        this.intermediateAddress = intermediateAddress;
        this.intermediatePort = intermediatePort;
        // create sockets
        try {
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void rpc_send(byte[] data, byte[] reply) {
        sendPacket = new DatagramPacket(data, data.length, intermediateAddress, intermediatePort);
        receivePacket = new DatagramPacket(reply, reply.length);
        try {
            // send data to intermediate host
            sendSocket.send(sendPacket);
            // wait for reply from intermediate host
            receiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void close() {
        sendSocket.close();
        receiveSocket.close();
    }

    public static void main(String[] args) {
        InetAddress intermediateAddress;
        int intermediatePort;
        try {
            intermediateAddress = InetAddress.getLocalHost();
            intermediatePort = 69;
            Client client = new Client(intermediateAddress, intermediatePort);
            byte[] request = "Hello, Intermediate Host".getBytes();
            byte[] reply = new byte[100];
            client.rpc_send(request, reply);
            System.out.println("[Client] Received reply: " + new String(reply));
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}