package assignment3;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Server.js
 * 
 * @author justine yap
 * 
 *         Class for Server that sends responses to the intermediate host
 */
public class Server extends UDPEntity {
	private final static int PORT = 69; // server port
	private static int RECEIVE_PORT = 24; // port used for socket to receive from server


	/*
	 * server constructor
	 * 
	 * making sure socket is created successfully
	 */
	public Server() {
		super(PORT);
		System.out.println("[Server] Created Sockets");
	}

	/*
	 * run function
	 * 
	 * receives packet from host then sends response back to host
	 */
	private void run() throws Exception {
		// send a request to the Host asking for data sent by client
		rpcSend(RECEIVE_PORT);

		
		String mode = "";
		String filename = "";
		byte[] req = new byte[getReceivePacket().getLength()];
		System.arraycopy(getReceivePacket().getData(), 0, req, 0, getReceivePacket().getLength());
	
		// checking request validity
		if (!new String(req).equals("DATA")) {

			if (req[0] != 0) {
				throw new Exception("[Server] this filename request format is invalid!");
			}
			if (req[1] != 1 && req[1] != 2) {
				throw new Exception("[Server] this filename request format is invalid!");
			}

			int i = 2;
			while (req[i] != 0) {
				filename += (char) req[i++];
				if (i == req.length - 1) {
					throw new Exception("[Server] this filename request format is invalid!");
				}
			}

			if (filename.length() == 0) {
				throw new Exception("[Server] this filename request format is invalid!");
			}

			i++;
			int start = i;
			while (req[i] != 0) {
				i++;
				
				if (i == req.length) {
					throw new Exception("[Server] this filename request format is invalid!");
				}
			}
			mode = new String(req, start, i - start, StandardCharsets.US_ASCII);
			if (!mode.equals("netascii") && !mode.equals("octet")) {
				throw new Exception("[Server] this filename request format is invalid!");
			}
		}
		

		// preparing the response
		System.out.println("[Server] Preparing response");
		byte[] res = new byte[4];
		res[0] = 0;
		if (req[1] == 1) {// 301 for read
			res[1] = 3;
			res[2] = 0;
			res[3] = 1;
		} else if (req[1] == 2) {// 400 for write
			res[1] = 4;
			res[2] = 0;
			res[3] = 0;
		}

		// send the response back to host
		rpcSend(res, RECEIVE_PORT);
		System.out.println("[Server] Sent (String) processed response: " + new String(res));
		System.out.println("[Server] Sent (byte) processed response: " + Arrays.toString(res));
		
		System.out.println();

	}

	/*
	 * main function
	 * 
	 */
	public static void main(String[] args) throws Exception {
		Server server = new Server();
		while (true) {
			try {
				server.run();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);

			}
		}
	}
}
