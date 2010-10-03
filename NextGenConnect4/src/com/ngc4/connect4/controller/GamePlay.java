package com.ngc4.connect4.controller;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Scanner;

import javax.swing.KeyStroke;

import com.ngc4.connect4.enums.Direction;
import com.ngc4.connect4.enums.GameMode;
import com.ngc4.connect4.enums.GamePlayers;
import com.ngc4.connect4.enums.KeyStrokes;
import com.ngc4.connect4.enums.StatusMessages;
import com.ngc4.connect4.view.gameplay.KeyAbstractAction;
import com.ngc4.connect4.view.gameplay.StatusBarPanel;
import com.ngc4.connect4.view.gameplay.TablePanel;

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
    private char currentPlayer;
    private GameSolver connect4Solver;
    private MultiPlayer mp;
    private MultiPlayerProtocol protocol;
    /**
     * Stores whether player is win or not, flag for finishing the game loop.
     */
    private boolean isPlayerWin;
    private boolean isGameDraw;
    /**
     * Stores reference to input stream for client/server communication.
     */
    private ObjectInputStream input;
    /**
     * Stores reference to output stream for client/server communication.
     */
    private ObjectOutputStream output;
    private String serverIPAddress;
    private Scanner stdin;
    private TablePanel tablePanel;
    private StatusBarPanel statusBarPanel;
    /**
     * Stores key handler references, prevents them from garbage collection.
     */
    private HashSet<KeyAbstractAction> abstractActions;

    public GamePlay(GameMode gameMode, int boardSize) {
        isPlayerWin = false;
        stdin = new Scanner(System.in);
        this.gameMode = gameMode;
        connect4Solver = new GameSolver(gameMode, boardSize);
        this.abstractActions = new HashSet<KeyAbstractAction>();
    }

    /**
     * Switches current player's turn in multiplayer mode.
     * @return Current player's turn.
     */
    //TODO should use enum and not Char for player
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
        return connect4Solver.moveMan(player, direction, position);
    }

    /**
     * Creates connection between client and server, and the communication
     * protocol. Gets streams from the client socket.
     */
    private void connect() {
        if (serverIPAddress == null) {
            mp = new MultiPlayer(true, null);
        } else {
            mp = new MultiPlayer(false, serverIPAddress);
        }
        input = mp.getInputStream();
        output = mp.getOutputStream();
        protocol = new MultiPlayerProtocol(currentPlayer, 0, 0);
    }

    /**
     * Sends data through the protocol. The information contains - player who
     * made the last move, filled square`s coordinates on the board, if player
     * wins and the win path.
     */
    private void sendData() {
        Point p = connect4Solver.getLastMove(currentPlayer);
        MultiPlayerProtocol prot1 = new MultiPlayerProtocol(currentPlayer, p.x, p.y);
        prot1.setPlayerWin(isPlayerWin);
        prot1.setGameDraw(isGameDraw);
        prot1.setWinPath(connect4Solver.getWinPath());
        prot1.setDirection(tablePanel.acquireDirection());

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
        connect4Solver.setSquare(protocol.getPlayer(), protocol.getRow(), protocol.getCol());
        fillSquare(new Point(protocol.getRow(), protocol.getCol()), protocol.getDirection(), protocol.getPlayer());
        return protocol.getPlayer();
    }

    /**
     * Configures new game.
     */
    public void configureGame(int boardSize, GameMode gameMode) {
        // TODO To be implemented.
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Gets the name of the game, when connection is made in multi player mode
     * over the network.
     *
     * @return The game name.
     */
    //TODO is this method invoked only at multiplayer mode?
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
     * User chooses which player to be and current player becomes opponent.
     */
    public void choosePlayer() {
        System.out.println("Choose player (x-black, o - white):");
        this.currentPlayer = stdin.nextLine().charAt(0);
        connect4Solver.setPlayer(currentPlayer);
    }

    /**
     * Sets current player who will be the first one.
     *
     * @param player
     */
    //TODO hard to understand the javadoc - needs improvement
    public void setPlayer(char player) {
        this.currentPlayer = player;
        connect4Solver.setPlayer(currentPlayer);
    }

    public String getServerIPAddress() {
        return serverIPAddress;
    }

    public void setServerIPAddress(String serverAddress) {
        this.serverIPAddress = serverAddress;
    }

    /**
     * Determines the game mode and loops the relevant game play.
     */
    //TODO check whether this switch-case is found in other places. consider using polymorphism
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

        KeyAbstractAction temp = null;
        String doKey = null;
        for (KeyStrokes key : KeyStrokes.values()) {
            temp = new KeyAbstractAction(key, tablePanel);
            abstractActions.add(temp);

            doKey = "do" + key.toString();

            tablePanel.getInputMap().put(KeyStroke.getKeyStroke(key.toString()), doKey);
            tablePanel.getActionMap().put(doKey, temp);
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
        tablePanel.moveManTo(p, direction, getPlayerColor(player));
    }

    /**
     * Gets the relevant color of the specified player.
     *
     * @param player
     *            Player to be specified.
     * @return The player's color.
     */
    private Color getPlayerColor(char player) {
        return (player == GamePlayers.FIRST_PLAYER.getPlayer()) ? Color.BLUE : Color.RED;
    }

    /**
     * Controls the game play in single player mode.
     */
    //TODO playClientServer() similar to playHotSeat, playComputer() - refactor + polymorphism wanted
    public void playSinglePlayer() {
        Direction direction;
        int position;
        if (currentPlayer == GameSolver.BLACK) {
            connect4Solver.nextBotMove();
            fillSquare(connect4Solver.getLastMove(connect4Solver.getBot()), tablePanel.acquireDirection(), connect4Solver.getBot());

            statusBarPanel.setStatus("Bot moved to ("
                    + (connect4Solver.getLastMove(connect4Solver.getBot()).x + 1) + ", "
                    + (connect4Solver.getLastMove(connect4Solver.getBot()).y + 1) + ").",
                    getPlayerColor(connect4Solver.getBot()));
        }
        while (!isPlayerWin) {
            do {
                synchronized (tablePanel) {
                    try {
                        tablePanel.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                direction = tablePanel.acquireDirection();
                position = tablePanel.acqurePosition();
            } while (!(moveMan(currentPlayer, direction, position)));

            fillSquare(connect4Solver.getLastMove(currentPlayer), tablePanel.acquireDirection(), currentPlayer);
            if (!isPlayerWin && (isPlayerWin = connect4Solver.isPlayerWin(currentPlayer))) {
                tablePanel.displayWinningCombination(connect4Solver.getWinPath(), Color.YELLOW);
                statusBarPanel.setStatus(StatusMessages.WIN.toString(), getPlayerColor(currentPlayer));
                break;
            } else if (connect4Solver.isGameDraw()) {
                statusBarPanel.setStatus(StatusMessages.DRAW_GAME.toString(), getPlayerColor(currentPlayer));
                break;
            }
            isPlayerWin = connect4Solver.nextBotMove();
            fillSquare(connect4Solver.getLastMove(connect4Solver.getBot()), tablePanel.acquireDirection(), connect4Solver.getBot());

            statusBarPanel.setStatus("Bot moved to ("
                    + (connect4Solver.getLastMove(connect4Solver.getBot()).x + 1) + ", "
                    + (connect4Solver.getLastMove(connect4Solver.getBot()).y + 1) + ").",
                    getPlayerColor(connect4Solver.getBot()));
            if (isPlayerWin) {
                tablePanel.displayWinningCombination(connect4Solver.getWinPath(), Color.YELLOW);
                statusBarPanel.setStatus(StatusMessages.WIN.toString(), getPlayerColor(connect4Solver.getBot()));
                break;
            }
        }
    }

    /**
     * Controls the game play in multiplayer hot seed mode, i.e. two players
     * play on a single computer.
     */
    //TODO playClientServer() similar to playHotSeat, playComputer() - refactor + polymorphism wanted
    public void playHotSeat() {
        Direction direction;
        int position;
        switchPlayer();
        while (!isPlayerWin) {
            do {
                synchronized (tablePanel) {
                    try {
                        tablePanel.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                direction = tablePanel.acquireDirection();
                position = tablePanel.acqurePosition();
            } while (!(moveMan(currentPlayer, direction, position)));

            fillSquare(connect4Solver.getLastMove(currentPlayer), tablePanel.acquireDirection(), currentPlayer);
            if ((isPlayerWin = connect4Solver.isPlayerWin(currentPlayer))) {
                tablePanel.displayWinningCombination(connect4Solver.getWinPath(), Color.YELLOW);
            } else if (connect4Solver.isGameDraw()) {
                statusBarPanel.setStatus(StatusMessages.DRAW_GAME.toString(), getPlayerColor(currentPlayer));
                break;
            }
            if (!isPlayerWin) {
                switchPlayer();
                statusBarPanel.setStatus(StatusMessages.TURN.toString(), getPlayerColor(currentPlayer));
            } else {
                statusBarPanel.setStatus(StatusMessages.WIN.toString(), getPlayerColor(currentPlayer));
            }
        }
    }

    /**
     * Controls the game play in multi player game mode over the network.
     */
    //TODO playClientServer() similar to playHotSeat, playComputer() - refactor + polymorphism wanted
    public void playClientServer() {
        statusBarPanel.setStatus(StatusMessages.WAITING_CONNECTION.toString(), getPlayerColor(currentPlayer));
        connect();
        statusBarPanel.setStatus(StatusMessages.CONNECTION_ESTABLISHED.toString(), getPlayerColor(currentPlayer));
        Direction direction;
        int position = 0;

        if (currentPlayer == GamePlayers.SECOND_PLAYER.getPlayer()) {
            do {
                synchronized (tablePanel) {
                    try {
                        tablePanel.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                direction = tablePanel.acquireDirection();
                position = tablePanel.acqurePosition();
            } while (!(moveMan(currentPlayer, direction, position)));
            fillSquare(connect4Solver.getLastMove(currentPlayer), tablePanel.acquireDirection(), currentPlayer);
            sendData();
        }

        while (!isPlayerWin && !isGameDraw) {
            statusBarPanel.setStatus(StatusMessages.NOT_YOUR_TURN.toString(), getPlayerColor(currentPlayer));
            receiveData();
            statusBarPanel.setStatus(StatusMessages.TURN.toString(), getPlayerColor(currentPlayer));

            if (isPlayerWin) {
                tablePanel.displayWinningCombination(protocol.getWinPath(), Color.YELLOW);
                statusBarPanel.setStatus(StatusMessages.WIN.toString(), getPlayerColor(protocol.getPlayer()));
                break;
            } else if (isGameDraw) {
                statusBarPanel.setStatus(StatusMessages.DRAW_GAME.toString(), getPlayerColor(protocol.getPlayer()));
                break;
            }

            do {
                synchronized (tablePanel) {
                    try {
                        tablePanel.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                direction = tablePanel.acquireDirection();
                position = tablePanel.acqurePosition();
            } while (!(moveMan(currentPlayer, direction, position)));

            fillSquare(connect4Solver.getLastMove(currentPlayer), direction,
                    currentPlayer);
            if ((isPlayerWin = connect4Solver.isPlayerWin(currentPlayer))) {
                tablePanel.displayWinningCombination(connect4Solver.getWinPath(), Color.YELLOW);
                statusBarPanel.setStatus(StatusMessages.WIN.toString(), getPlayerColor(currentPlayer));
            } else if (connect4Solver.isGameDraw()) {
                statusBarPanel.setStatus(StatusMessages.DRAW_GAME.toString(), getPlayerColor(currentPlayer));
                isGameDraw = true;
            }
            sendData();
        }
        mp.closeConnection();
    }

    /**
     * Sets table panel reference. Used for visualizing the players` moves.
     *
     * @param tablePanel
     *            Table panel object.
     */
    public void setTablePanel(TablePanel tablePanel) {
        this.tablePanel = tablePanel;
        addKeyHandler();
    }

    public void setStatusBarPanel(StatusBarPanel statusBarPanel) {
        this.statusBarPanel = statusBarPanel;
    }

    public void setBoardSize(int size) {
        connect4Solver.setBoardSize(size);
    }

    public int getBoardSize() {
        return connect4Solver.getBoardSize();
    }

    /**
     * Runs the game loop.
     */
    @Override
    public void run() {
        loopGame();
    }
}
