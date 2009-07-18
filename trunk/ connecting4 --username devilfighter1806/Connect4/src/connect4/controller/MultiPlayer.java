package connect4.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

/**
 * Represents client or server of the game.
 * 
 * @author Stanislav Petrov
 * 
 */
public class MultiPlayer {

	/**
	 * Port for establishing connection between the players.
	 */
	public static final int PORT = 6666;
	/**
	 * Stores flag that indicates if the player is server(true) or
	 * client(false).
	 */
	private boolean isServer;
	/**
	 * Stores the socket on which server listens for connections.
	 */
	private ServerSocket serverSocket;
	/**
	 * Stores socket on which client connects to the server.
	 */
	private Socket clientSocket;
	/**
	 * Stores reference to input stream for client/server communication.
	 */
	private ObjectInputStream input;
	/**
	 * Stores reference to output stream for client/srever communication.
	 */
	private ObjectOutputStream output;
	/**
	 * Stores the IP address of the server of the game, i.e. the player who
	 * hosts the game.
	 */
	private String serverAddress;
	/**
	 * Stores reference to the communication protocol between client-server.
	 */
	private MultiPlayerProtocol protocol;

	/**
	 * 
	 * @param isServer
	 *            True - if multi player hosts a game(he's the server), false -
	 *            if the player joins the game(he's the client).
	 */
	public MultiPlayer(boolean isServer, String serverAddr) {
		this.isServer = isServer;
		this.serverAddress = serverAddr;
		initMultiPlayer();
	}

	/**
	 * Initializes connections for the client or the server.
	 */
	private void initMultiPlayer() {
		if (isServer) {
			listenConnection();
			System.out.println("Server: receives connection");
		} else {
			createConnection();
			System.out.println("Client: created connection");
		}
		getStreams();
	}

	/**
	 * Server starts to listen on server socket.
	 */
	private void listenConnection() {
		try {
			serverSocket = new ServerSocket(PORT, 1);
			clientSocket = serverSocket.accept();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Client creates connection to the server.
	 */
	private void createConnection() {
		try {
			clientSocket = new Socket(serverAddress, PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets streams from the client socket.
	 */
	private void getStreams() {
		try {
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			output.flush();
			input = new ObjectInputStream(clientSocket.getInputStream());
			System.out.println("Streams gotten");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes sockets and associated streams with them.
	 */
	public void closeConnection() {
		try {
			input.close();
			output.close();
			clientSocket.close();
			if (isServer) {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the input stream from server(host)/client(join).
	 * 
	 * @return Associated input stream with the given server/client socket.
	 */
	public ObjectInputStream getInputStream() {
		return input;
	}

	/**
	 * Gets the output stream from server(host)/client(join).
	 * 
	 * @return Associated output stream with the given server/client socket.
	 */
	public ObjectOutputStream getOutputStream() {
		return output;
	}
}
