package connect4.controller;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

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
	/**
	 * Stores which is current player.
	 */
	private char currentPlayer;
	/**
	 * Stores connect4 solving algorithms.
	 */
	private Connect4Solver connect4;
	/**
	 * Reads from standard input.
	 */
	private Scanner stdin;
	private MultiPlayer mp;
	private MultiPlayerProtocol protocol;
	/**
	 * Stores reference to input stream for client/server communication.
	 */
	private ObjectInputStream input;
	/**
	 * Stores reference to output stream for client/srever communication.
	 */
	private ObjectOutputStream output;
	private String serverAddress;

	public GamePlay(GameMode gameMode, int boardSize) {
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

	/**
	 * Controls the game play in single player mode.
	 */
	public void playSinglePlayer() {
		int position, num;
		boolean validMove = true;
		connect4.printBoard();
		if (currentPlayer == Connect4Solver.BLACK) {
			connect4.nextBotMove();
			connect4.printBoard();
		}
		while (true) {
			System.out.println("Current player is: " + currentPlayer);
			System.out.println("Position(1-Top, 2-Bottom, 3-Left, 4-Right:");
			position = stdin.nextInt();
			num = stdin.nextInt();
			if (position == -1 && num == -1)
				break;
			validMove = moveMan(currentPlayer, position, num);
			connect4.printBoard();
			System.out.println("Bot move..........");
			if (connect4.nextBotMove()) {
				connect4.printBoard();
				connect4.printWinPaths();
				break;
			}
			connect4.printBoard();
			if (!validMove)
				System.out.println("Invalid move!");
			else if (connect4.isPlayerWin(currentPlayer)) {
				System.out.println("WIN!!!");
				connect4.printWinPaths();
				break;
			}
		}
	}

	/**
	 * Controls the game play in multiplayer hot seed mode, i.e. two players
	 * play on a single computer.
	 */
	public void playHotSeed() {
		int position, num;
		boolean validMove = true;
		connect4.printBoard();
		switchPlayer();
		while (true) {
			System.out.println("Current player is: " + currentPlayer);
			System.out.println("Position(1-Top, 2-Bottom, 3-Left, 4-Right:");
			position = stdin.nextInt();
			num = stdin.nextInt();
			if (position == -1 && num == -1)
				break;
			validMove = moveMan(currentPlayer, position, num);
			connect4.printBoard();
			if (!validMove)
				System.out.println("Invalid move!");
			else if (connect4.isPlayerWin(currentPlayer)) {
				System.out.println("WIN!!!");
				connect4.printWinPaths();
				break;
			}
			switchPlayer();
		}
	}

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
	 * Controls the game play in multi player game mode over the network.
	 */
	// TODO To be implemented.
	public void playClientServer() {
		createConnection();
		int position = 0, num = 0;
		boolean validMove = true;
		Point p;
		connect4.printBoard();
		MultiPlayerProtocol prot;
		connect4.printBoard();
		if (currentPlayer == 'o') {
			do {
				System.out.println("Current player is: " + currentPlayer);
				System.out
						.println("Position(1-Top, 2-Bottom, 3-Left, 4-Right:");
				position = stdin.nextInt();
				num = stdin.nextInt();
			} while (!(validMove = moveMan(currentPlayer, position, num)));
			connect4.printBoard();
			p = connect4.getLastMove(currentPlayer);
			MultiPlayerProtocol prot1 = new MultiPlayerProtocol(currentPlayer,
					p.x, p.y);
			try {
				output.writeObject(prot1);
				output.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while (true) {
			Object obj = null;
			while (obj == null) {
				try {
					obj = input.readObject();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			prot = (MultiPlayerProtocol) obj;
			connect4.setSquare(prot.getPlayer(), prot.getRow(), prot.getCol());
			connect4.printBoard();
			do {
				System.out.println("Current player is: " + currentPlayer);
				System.out
						.println("Position(1-Top, 2-Bottom, 3-Left, 4-Right:");
				position = stdin.nextInt();
				num = stdin.nextInt();
			} while (!(validMove = moveMan(currentPlayer, position, num)));
			connect4.printBoard();
			p = connect4.getLastMove(currentPlayer);
			MultiPlayerProtocol prot1 = new MultiPlayerProtocol(currentPlayer,
					p.x, p.y);
			try {
				output.writeObject(prot1);
				output.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!validMove)
				System.out.println("Invalid move!");
			else if (connect4.isPlayerWin(currentPlayer)) {
				System.out.println("WIN!!!");
				connect4.printWinPaths();
				break;
			}
		}
	}

	@Override
	public void run() {
		loopGame();
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
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