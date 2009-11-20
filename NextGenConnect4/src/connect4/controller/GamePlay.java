package connect4.controller;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Scanner;

import javax.swing.KeyStroke;

import connect4.enums.Direction;
import connect4.enums.GameMode;
import connect4.enums.GamePlayers;
import connect4.enums.KeyStrokes;
import connect4.enums.StatusMessages;
import connect4.view.gameplay.KeyAbstractAction;
import connect4.view.gameplay.StatusBarPanel;
import connect4.view.gameplay.TablePanel;

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
	 * Stores name of the game.
	 */
	private String gameName;
	/**
	 * Stores which is current player.
	 */
	private char currentPlayer;
	/**
	 * Stores connect4 solving algorithms.
	 */
	private GameSolver connect4;
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
	 * Stores whether game is draw.
	 */
	private boolean isGameDraw;
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
	 * Stores status bar reference.
	 */
	private StatusBarPanel statusBarPanel;
	/**
	 * Stores key handler references, prevents them from garbage collection.
	 */
	private HashSet<KeyAbstractAction> abstractActions;

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
		connect4 = new GameSolver(gameMode, boardSize);
		this.abstractActions = new HashSet<KeyAbstractAction>();
	}

	/**
	 * Switches current player's turn in multiplayer mode.
	 * 
	 * @param currentPlayer
	 *            Current player which will be switched.
	 * @return Current player's turn.
	 */
	private char switchPlayer() {
		if (currentPlayer == GameSolver.BLACK) {
			currentPlayer = GameSolver.WHITE;
		} else {
			currentPlayer = GameSolver.BLACK;
		}
		return currentPlayer;
	}

	/**
	 * Moves man from the specified position and row/column number.
	 * 
	 * @param direction
	 *            Position from which user drops a man.
	 * @param position
	 *            Row/column number in the board.
	 * @return True - if the move is valid, false - otherwise.
	 */
	private boolean moveMan(char player, Direction direction, int position) {
		return connect4.moveMan(player, direction, position);
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
		prot1.setGameDraw(isGameDraw);
		prot1.setWinPath(connect4.getWinPath());
		prot1.setDirection(tablePnl.acquireDirection());
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
		isGameDraw = protocol.isGameDraw();
		connect4.setSquare(protocol.getPlayer(), protocol.getRow(), protocol
				.getCol());
		fillSquare(new Point(protocol.getRow(), protocol.getCol()), protocol
				.getDirection(), protocol.getPlayer());
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

	/**
	 * Gets the name of the game, when connection is made in multi player mode
	 * over the network.
	 * 
	 * @return The game name.
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * Sets the name of the game, when connection is made in multi player mode
	 * over the network.
	 * 
	 * @param gameName
	 */
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
		case HOT_SEAT: {
			playHotSeat();
			break;
		}
		case INTERNET: {
			playClientServer();
			break;
		}
		}
	}

	/**
	 * For every key event in the enum type MyKeyStrokes, adds the relevant key
	 * handler.
	 */
	private void addKeyHandler() {

		KeyAbstractAction temp;
		String doKey;
		for (KeyStrokes key : KeyStrokes.values()) {
			temp = new KeyAbstractAction(key, tablePnl);
			abstractActions.add(temp);

			doKey = "do" + key.toString();

			tablePnl.getInputMap().put(KeyStroke.getKeyStroke(key.toString()),
					doKey);
			tablePnl.getActionMap().put(doKey, temp);
		}
	}

	/**
	 * Fills square in the table panel with the specified color.
	 * 
	 * @param p
	 *            Position of the square to be filled in the board.
	 * @param direction
	 *            Direction of the move according to the board.
	 * @param player
	 *            Player who moved the man.
	 */
	private void fillSquare(Point p, Direction direction, char player) {
		tablePnl.moveManTo(p, direction, getPlayerColor(player));
	}

	/**
	 * Gets the relevant color of the specified player.
	 * 
	 * @param player
	 *            Player to be specified.
	 * @return The player's color.
	 */
	private Color getPlayerColor(char player) {
		return (player == GamePlayers.FIRST_PLAYER.getPlayer()) ? Color.BLUE
				: Color.RED;
	}

	/**
	 * Controls the game play in single player mode.
	 */
	public void playSinglePlayer() {
		Direction direction;
		int position;
		if (currentPlayer == GameSolver.BLACK) {
			connect4.nextBotMove();
			fillSquare(connect4.getLastMove(connect4.getBot()), tablePnl
					.acquireDirection(), connect4.getBot());
			statusBarPanel.setStatus("Bot moved to ("
					+ (connect4.getLastMove(connect4.getBot()).x + 1) + ", "
					+ (connect4.getLastMove(connect4.getBot()).y + 1) + ").",
					getPlayerColor(connect4.getBot()));
		}
		while (!isPlayerWin) {
			do {
				synchronized (tablePnl) {
					try {
						tablePnl.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				direction = tablePnl.acquireDirection();
				position = tablePnl.acqurePosition();
			} while (!(moveMan(currentPlayer, direction, position)));
			fillSquare(connect4.getLastMove(currentPlayer), tablePnl
					.acquireDirection(), currentPlayer);
			if (!isPlayerWin
					&& (isPlayerWin = connect4.isPlayerWin(currentPlayer))) {
				tablePnl.displayWinningCombination(connect4.getWinPath(),
						Color.YELLOW);
				statusBarPanel.setStatus(StatusMessages.WIN.toString(),
						getPlayerColor(currentPlayer));
				break;
			}else if(connect4.isGameDraw()){
				statusBarPanel.setStatus(StatusMessages.DRAW_GAME.toString(),
						getPlayerColor(currentPlayer));
				break;
			}
			isPlayerWin = connect4.nextBotMove();
			fillSquare(connect4.getLastMove(connect4.getBot()), tablePnl
					.acquireDirection(), connect4.getBot());
			statusBarPanel.setStatus("Bot moved to ("
					+ (connect4.getLastMove(connect4.getBot()).x + 1) + ", "
					+ (connect4.getLastMove(connect4.getBot()).y + 1) + ").",
					getPlayerColor(connect4.getBot()));
			if (isPlayerWin) {
				tablePnl.displayWinningCombination(connect4.getWinPath(),
						Color.YELLOW);
				statusBarPanel.setStatus(StatusMessages.WIN.toString(),
						getPlayerColor(connect4.getBot()));
				break;
			}
		}
	}

	/**
	 * Controls the game play in multiplayer hot seed mode, i.e. two players
	 * play on a single computer.
	 */
	public void playHotSeat() {
		Direction direction;
		int position;
		switchPlayer();
		while (!isPlayerWin) {
			do {
				synchronized (tablePnl) {
					try {
						tablePnl.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				direction = tablePnl.acquireDirection();
				position = tablePnl.acqurePosition();
			} while (!(moveMan(currentPlayer, direction, position)));
			fillSquare(connect4.getLastMove(currentPlayer), tablePnl
					.acquireDirection(), currentPlayer);
			if ((isPlayerWin = connect4.isPlayerWin(currentPlayer))) {
				tablePnl.displayWinningCombination(connect4.getWinPath(),
						Color.YELLOW);
			}else if(connect4.isGameDraw()){
				statusBarPanel.setStatus(StatusMessages.DRAW_GAME.toString(),
						getPlayerColor(currentPlayer));
				break;
			}
			if (!isPlayerWin) {
				switchPlayer();
				statusBarPanel.setStatus(StatusMessages.TURN.toString(),
						getPlayerColor(currentPlayer));
			} else {
				statusBarPanel.setStatus(StatusMessages.WIN.toString(),
						getPlayerColor(currentPlayer));
			}
		}
	}

	/**
	 * Controls the game play in multi player game mode over the network.
	 */
	public void playClientServer() {
		statusBarPanel.setStatus(StatusMessages.WAITING_CONNECTION.toString(),
				getPlayerColor(currentPlayer));
		createConnection();
		statusBarPanel.setStatus(StatusMessages.CONNECTION_ESTABLISHED
				.toString(), getPlayerColor(currentPlayer));
		char player;
		Direction direction;
		int position = 0;
		if (currentPlayer == GamePlayers.SECOND_PLAYER.getPlayer()) {
			do {
				synchronized (tablePnl) {
					try {
						tablePnl.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				direction = tablePnl.acquireDirection();
				position = tablePnl.acqurePosition();
			} while (!(moveMan(currentPlayer, direction, position)));
			fillSquare(connect4.getLastMove(currentPlayer), tablePnl
					.acquireDirection(), currentPlayer);
			sendData();
		}
		while (!isPlayerWin && !isGameDraw) {
			statusBarPanel.setStatus(StatusMessages.NOT_YOUR_TURN.toString(),
					getPlayerColor(currentPlayer));
			player = receiveData();
			statusBarPanel.setStatus(StatusMessages.TURN.toString(),
					getPlayerColor(currentPlayer));
			if (isPlayerWin) {
				tablePnl.displayWinningCombination(protocol.getWinPath(),
						Color.YELLOW);
				statusBarPanel.setStatus(StatusMessages.WIN.toString(),
						getPlayerColor(protocol.getPlayer()));
				break;
			}else if(isGameDraw){
				statusBarPanel.setStatus(StatusMessages.DRAW_GAME.toString(),
						getPlayerColor(protocol.getPlayer()));
				break;
			}
			do {
				synchronized (tablePnl) {
					try {
						tablePnl.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				direction = tablePnl.acquireDirection();
				position = tablePnl.acqurePosition();
			} while (!(moveMan(currentPlayer, direction, position)));
			fillSquare(connect4.getLastMove(currentPlayer), direction,
					currentPlayer);
			if ((isPlayerWin = connect4.isPlayerWin(currentPlayer))) {
				tablePnl.displayWinningCombination(connect4.getWinPath(),
						Color.YELLOW);
				statusBarPanel.setStatus(StatusMessages.WIN.toString(),
						getPlayerColor(currentPlayer));
			}else if(connect4.isGameDraw()){
				statusBarPanel.setStatus(StatusMessages.DRAW_GAME.toString(),
						getPlayerColor(currentPlayer));
				isGameDraw = true;
			}
			sendData();
		}
		mp.closeConnection();
	}

	/**
	 * Sets table panel reference. Used for visualizing the players' moves.
	 * 
	 * @param tablePnl
	 *            Table panel object.
	 */
	public void setTablePanel(TablePanel tablePnl) {
		this.tablePnl = tablePnl;
		addKeyHandler();
	}

	/**
	 * Sets the status bar panel reference for manipulating tha status bar
	 * messages.
	 * 
	 * @param statusBarPanel
	 *            Status bar panel reference to set.
	 */
	public void setStatusBarPanel(StatusBarPanel statusBarPanel) {
		this.statusBarPanel = statusBarPanel;
	}

	/**
	 * Sets the size of the board.
	 * 
	 * @param size
	 *            Board size to be set.
	 */
	public void setBoardSize(int size) {
		connect4.setBoardSize(size);
	}

	/**
	 * Gets the size of the board.
	 * @return The board size.
	 */
	public int getBoardSize() {
		return connect4.getBoardSize();
	}

	/**
	 * Runs the game loop.
	 */
	@Override
	public void run() {
		loopGame();
	}
}