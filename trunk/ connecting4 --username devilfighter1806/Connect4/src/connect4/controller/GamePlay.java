package connect4.controller;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import connect4.view.ManCombo;
import connect4.view.TablePanel;

/**
 * Controls game play.
 * 
 * @author Stanislav Petrov
 * 
 */
public class GamePlay extends Thread {
	/**
	 * Stores game mode - single|multi player.
	 */
	private GameMode gameMode;
	private String gameName;

	/**
	 * Stores which is current player.
	 */
	private char currentPlayer;
	/**
	 * Stores connect4 solving algorithms.
	 */
	private Connect4Solver connect4;
	/**
	 * Stores multi player reference.
	 */
	private MultiPlayer mp;
	/**
	 * Stores multi player protocol reference.
	 */
	private MultiPlayerProtocol protocol;
	/**
	 * Stores whether player is win or not, flag for finishing the game loop.
	 */
	private boolean isPlayerWin;
	/**
	 * Stores reference to input stream for client/server communication.
	 */
	private ObjectInputStream input;
	/**
	 * Stores reference to output stream for client/srever communication.
	 */
	private ObjectOutputStream output;
	/**
	 * Stores the server IP address.
	 */
	private String serverAddress;
	/**
	 * Reads from standard input.
	 */
	private Scanner stdin;
	/**
	 * Stores reference to the table panel.
	 */
	private TablePanel tablePnl;

	/**
	 * Constructs the game play.
	 * 
	 * @param gameMode
	 *            Game mode to be set.
	 * @param boardSize
	 *            Size of the board.
	 */
	public GamePlay(GameMode gameMode, int boardSize) {
		isPlayerWin = false;
		stdin = new Scanner(System.in);
		this.gameMode = gameMode;
		connect4 = new Connect4Solver(gameMode, boardSize);
	}

	/**
	 * Switches current player's turn in multiplayer mode.
	 * 
	 * @param currentPlayer
	 *            Current player which will be switched.
	 * @return Current player's turn.
	 */
	private char switchPlayer() {
		if (currentPlayer == Connect4Solver.BLACK) {
			currentPlayer = Connect4Solver.WHITE;
		} else {
			currentPlayer = Connect4Solver.BLACK;
		}
		return currentPlayer;
	}

	/**
	 * Moves man from the specified position and row/column number.
	 * 
	 * @param position
	 *            Position from which user drops a man.
	 * @param num
	 *            Row/column number in the board.
	 * @return True - if the move is valid, false - otherwise.
	 */
	private boolean moveMan(char player, int position, int num) {
		boolean validMove = false;
		switch (position) {
		case 1:
			validMove = connect4.moveMan(player, Direction.VERTICAL_UP, num);
			break;
		case 2:
			validMove = connect4.moveMan(player, Direction.VERTICAL_DOWN, num);
			break;
		case 3:
			validMove = connect4
					.moveMan(player, Direction.HORIZONTAL_LEFT, num);
			break;
		case 4:
			validMove = connect4.moveMan(player, Direction.HORIZONTAL_RIGHT,
					num);
			break;
		}
		return validMove;
	}

	/**
	 * Creates connection between client and server, and the communication
	 * protocol. Gets streams from the client socket.
	 */
	private void createConnection() {
		if (serverAddress == null) {
			mp = new MultiPlayer(true, null);
		} else {
			mp = new MultiPlayer(false, serverAddress);
		}
		input = mp.getInputStream();
		output = mp.getOutputStream();
		protocol = new MultiPlayerProtocol(currentPlayer, 0, 0);
	}

	/**
	 * Sends data through the protocol. The information contains - player who
	 * made the last move, filled sqaure's coordinates on the board, if player
	 * wins and the win path.
	 */
	private void sendData() {
		Point p = connect4.getLastMove(currentPlayer);
		MultiPlayerProtocol prot1 = new MultiPlayerProtocol(currentPlayer, p.x,
				p.y);
		prot1.setPlayerWin(isPlayerWin);
		prot1.setWinPath(connect4.getWinPath());
		try {
			output.writeObject(prot1);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Receives data through the protocol. The information contains - player who
	 * made the last move, filled sqaure's coordinates on the board, if player
	 * wins and the win path.
	 * 
	 * @return Player who sends the data.
	 */
	private char receiveData() {
		Object obj = null;
		while (obj == null) {
			try {
				obj = input.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		protocol = (MultiPlayerProtocol) obj;
		isPlayerWin = protocol.isPlayerWin();
		connect4.setSquare(protocol.getPlayer(), protocol.getRow(), protocol
				.getCol());
		fillSquare(new Point(protocol.getRow(), protocol.getCol()), protocol.getPlayer());
		return protocol.getPlayer();
	}

	/**
	 * Configures new game.
	 * 
	 * @param boardSize
	 *            Board size to be set.
	 * @param gameMode
	 *            Game mode to be set.
	 */
	public void configureGame(int boardSize, GameMode gameMode) {
		// TODO To be implemented.
	}

	/**
	 * Game mode setter.
	 * 
	 * @param gameMode
	 *            Game mode to be set.
	 */
	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * User chooses which player to be and current player becomes oponent.
	 */
	public void choosePlayer() {
		System.out.println("Choose player (x-black, o - white):");
		this.currentPlayer = stdin.nextLine().charAt(0);
		connect4.setPlayer(currentPlayer);
	}

	/**
	 * Sets current player who will be the first one.
	 * 
	 * @param player
	 */
	public void setPlayer(char player) {
		this.currentPlayer = player;
		connect4.setPlayer(currentPlayer);
	}

	/**
	 * Gets the server IP address.
	 * 
	 * @return The server IP address.
	 */
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * Sets the server IP address.
	 * 
	 * @param serverAddress
	 *            Server's IP address to be set.
	 */
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	/**
	 * Determines the game mode and loops the relevant game play.
	 */
	public void loopGame() {
		switch (gameMode) {
		case SINGLE_PLAYER: {
			playSinglePlayer();
			break;
		}
		case HOT_SEED: {
			playHotSeed();
			break;
		}
		case TCP_CONNECTION: {
			playClientServer();
			break;
		}
		}
	}

	private void fillSquare(Point p, char player) {
		tablePnl.moveManTo(new ManCombo(p.x, p.y), tablePnl.acquireDirection(),
				player == 'x' ? Color.BLUE : Color.RED);
	}

	/**
	 * Controls the game play in single player mode.
	 */
	public void playSinglePlayer() {
		int position, num;
		connect4.printBoard();
		if (currentPlayer == Connect4Solver.BLACK) {
			connect4.nextBotMove();
			connect4.printBoard();
		}
		while (!isPlayerWin) {
			do {
				System.out.println("Current player is: " + currentPlayer);
				System.out
						.println("Position(1-Top, 2-Bottom, 3-Left, 4-Right:");
				position = stdin.nextInt();
				num = stdin.nextInt();
			} while (!(moveMan(currentPlayer, position, num)));
			fillSquare(connect4.getLastMove(currentPlayer), currentPlayer);
			connect4.printBoard();
			System.out.println("Bot move..........");
			if ((isPlayerWin = connect4.nextBotMove())) {
				connect4.printBoard();
				connect4.printWinPaths();
			}
			fillSquare(connect4.getLastMove(connect4.getBot()), connect4.getBot());
			if (!isPlayerWin
					&& (isPlayerWin = connect4.isPlayerWin(currentPlayer))) {
				System.out.println("WIN!!!");
				connect4.printWinPaths();
			}
			connect4.printBoard();
		}
	}

	/**
	 * Controls the game play in multiplayer hot seed mode, i.e. two players
	 * play on a single computer.
	 */
	public void playHotSeed() {
		int position, num;
		connect4.printBoard();
		switchPlayer();
		while (!isPlayerWin) {
			do {
				System.out.println("Current player is: " + currentPlayer);
				System.out
						.println("Position(1-Top, 2-Bottom, 3-Left, 4-Right:");
				position = stdin.nextInt();
				num = stdin.nextInt();
			} while (!(moveMan(currentPlayer, position, num)));
			fillSquare(connect4.getLastMove(currentPlayer), currentPlayer);
			if ((isPlayerWin = connect4.isPlayerWin(currentPlayer))) {
				System.out.println("WIN!!!");
				connect4.printWinPaths();
			}
			connect4.printBoard();
			switchPlayer();
		}
	}

	/**
	 * Controls the game play in multi player game mode over the network.
	 */
	public void playClientServer() {
		createConnection();
		char player;
		int position = 0, num = 0;
		connect4.printBoard();
		connect4.printBoard();
		if (currentPlayer == 'o') {
			do {
				System.out.println("Current player is: " + currentPlayer);
				System.out
						.println("Position(1-Top, 2-Bottom, 3-Left, 4-Right:");
				position = stdin.nextInt();
				num = stdin.nextInt();
			} while (!(moveMan(currentPlayer, position, num)));
			fillSquare(connect4.getLastMove(currentPlayer), currentPlayer);
			connect4.printBoard();
			sendData();
		}
		while (!isPlayerWin) {
			player = receiveData();
			connect4.printBoard();
			if (isPlayerWin) {
				System.out.println("Player : " + player + " WIN!");
				for (int i = 0; i < protocol.getWinPath().length; i++) {
					System.out.print("(" + protocol.getWinPath()[i].x + ", "
							+ protocol.getWinPath()[i].y + "), ");
				}
				break;
			}
			do {
				System.out.println("Current player is: " + currentPlayer);
				System.out
						.println("Position(1-Top, 2-Bottom, 3-Left, 4-Right:");
				position = stdin.nextInt();
				num = stdin.nextInt();
			} while (!(moveMan(currentPlayer, position, num)));
			fillSquare(connect4.getLastMove(currentPlayer), currentPlayer);
			connect4.printBoard();
			if ((isPlayerWin = connect4.isPlayerWin(currentPlayer))) {
				System.out.println("WIN!!!");
				connect4.printWinPaths();
			}
			sendData();
		}
	}

	/**
	 * Runs the game loop.
	 */
	@Override
	public void run() {
		loopGame();
	}

	/**
	 * Sets table panel reference. Used for visualizing the players' moves.
	 * 
	 * @param tablePnl
	 *            Table panel object.
	 */
	public void setTablePanel(TablePanel tablePnl) {
		this.tablePnl = tablePnl;
	}

	/**
	 * Starts game loop.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// configure new game.
		GamePlay game = new GamePlay(GameMode.TCP_CONNECTION, 7);
		// choose which player to be.
		game.choosePlayer();
		if (args.length > 0) {
			game.setServerAddress(args[0]);
		}
		// game loop.
		game.start();
	}
}