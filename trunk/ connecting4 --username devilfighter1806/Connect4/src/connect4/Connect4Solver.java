package connect4;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Connect4 game. Board should be NxN size, where N is odd number. Two players
 * are playing the game - white and black. Each player has (NxN -1)/2 men. Black
 * player always begins first in the middle of the board.
 * 
 * @author Stanislav Petrov
 * @date 28 June 2009
 */
public class Connect4Solver {
	/**
	 * Stores number of players in the game.
	 */
	public static final int PLAYERS = 2;
	/**
	 * Stores how many men should be connected for winning the game.
	 */
	public static final int CONNECT_NUMBER = 4;
	/**
	 * Sqaure state filled by the black player.
	 */
	public static final char BLACK = 'x';
	/**
	 * Square state filled by the white player.
	 */
	public static final char WHITE = 'o';
	/**
	 * Empty state of the square.
	 */
	public static final char EMPTY = '-';
	/**
	 * Stores the size of the board.
	 */
	private int boardSize;

	/**
	 * Playing board.
	 */
	private char[][] board;

	/**
	 * Stores which is current player.
	 */
	private char currentPlayer;
	/**
	 * Stores which player will be the bot in single player mode.
	 */
	private char botPlayer;
	/**
	 * Stores number of moves which are done for both players.
	 */
	private int[] numberMoves;
	/**
	 * Stores row numbers of each man of winning configuration.
	 */
	private int[] winPathRow;
	/**
	 * Stores column numbers of each man of winning configuration.
	 */
	private int[] winPathCol;
	/**
	 * Stores both players' moves.
	 */
	private ArrayList<Point>[] moves;
	/**
	 * Stores game board for printing to the standard output.
	 */
	private StringBuilder output;
	/**
	 * Stores mode of the game - single | multi player.
	 */
	private GameMode gameMode;

	/**
	 * General purpose contructor. Sets the number of wining paths to zero.
	 * 
	 * @param gameMode
	 *            Game mode to be set.
	 * @param size
	 *            Board size to be set.
	 */
	public Connect4Solver(GameMode gameMode, int size) {
		setBoardSize(size);
		board = new char[boardSize][boardSize];
		initializeBoard();
		initialiazeMovesList();
		this.gameMode = gameMode;
		winPathRow = new int[CONNECT_NUMBER];
		winPathCol = new int[CONNECT_NUMBER];
		output = new StringBuilder();
	}

	/**
	 * Initializes board with empty squares.
	 */
	private void initializeBoard() {
		for (int i = 0; i < getBoardSize(); i++) {
			Arrays.fill(board[i], '-');
		}
		board[boardSize / 2][boardSize / 2] = BLACK;
	}

	/**
	 * Initializes moves list for both players.
	 */
	@SuppressWarnings("unchecked")
	private void initialiazeMovesList() {
		numberMoves = new int[PLAYERS];
		moves = (ArrayList<Point>[]) new ArrayList[PLAYERS];
		for (int i = 0; i < moves.length; i++) {
			moves[i] = new ArrayList<Point>();
		}
		numberMoves[getMovesIndex(BLACK)] = 1;
		moves[getMovesIndex(BLACK)]
				.add(new Point(boardSize / 2, boardSize / 2));
	}

	/**
	 * Moves man from top of the board.
	 * 
	 * @param player
	 *            Current player.
	 * @param col
	 *            Column number.
	 * @return True - if move is valid, false - otherwise.
	 */
	private boolean moveManFromTop(char player, int col) {
		int i;
		for (i = 0; i < boardSize; i++) {
			if (board[i][col] != EMPTY) {
				break;
			}
		}
		if (i == boardSize || i == 0)
			return false;
		board[i - 1][col] = player;
		moves[getMovesIndex(player)].add(new Point(i - 1, col));
		numberMoves[getMovesIndex(player)]++;
		return true;
	}

	/**
	 * Moves man from bottom of the board.
	 * 
	 * @param player
	 *            Current player.
	 * @param col
	 *            Column number.
	 * @return True - if move is valid, false - otherwise.
	 */
	private boolean moveManFromBottom(char player, int col) {
		int i;
		for (i = boardSize - 1; i >= 0; i--) {
			if (board[i][col] != EMPTY) {
				break;
			}
		}
		if (i == -1 || i == boardSize - 1)
			return false;
		board[i + 1][col] = player;
		moves[getMovesIndex(player)].add(new Point(i + 1, col));
		numberMoves[getMovesIndex(player)]++;
		return true;
	}

	/**
	 * Moves man from left of the board.
	 * 
	 * @param player
	 *            Current player.
	 * @param row
	 *            Row number.
	 * @return True - if move is valid, false - otherwise.
	 */
	private boolean moveManFromLeft(char player, int row) {
		int j;
		for (j = 0; j < boardSize; j++) {
			if (board[row][j] != EMPTY)
				break;
		}
		if (j == boardSize || j == 0)
			return false;
		board[row][j - 1] = player;
		moves[getMovesIndex(player)].add(new Point(row, j - 1));
		numberMoves[getMovesIndex(player)]++;
		return true;
	}

	/**
	 * Moves man from right of the board.
	 * 
	 * @param player
	 *            Current player.
	 * @param row
	 *            Row number.
	 * @return True - if move is valid, false - otherwise.
	 */
	private boolean moveManFromRight(char player, int row) {
		int j;
		for (j = boardSize - 1; j >= 0; j--) {
			if (board[row][j] != EMPTY)
				break;
		}
		if (j == boardSize - 1 || j == -1)
			return false;
		board[row][j + 1] = player;
		moves[getMovesIndex(player)].add(new Point(row, j + 1));
		numberMoves[getMovesIndex(player)]++;
		return true;
	}

	/**
	 * Gets the index of the current player for fetching the data from number of
	 * moves array.
	 * 
	 * @param player
	 *            Current player.
	 * @return Index for pointing element from number of moves array.
	 */
	private int getMovesIndex(char player) {
		return (player == BLACK) ? 0 : 1;
	}

	/**
	 * Validates position in the board.
	 * 
	 * @param pos
	 *            Row or column in the board.
	 * @return True - if position is valid, false - otherwise.
	 */
	private boolean isPositionValid(int pos) {
		return (pos >= 0 && pos <= boardSize - 1) ? true : false;
	}

	/**
	 * Checks if player is winning in horizontal direction.
	 * 
	 * @param player
	 *            Current player.
	 * @param x
	 *            Row number of current player's position.
	 * @param y
	 *            Column number of current player's position.
	 * @return True - if player wins, false - otherwise.
	 */
	private boolean isPlayerWinHorizontally(char player, int x, int y) {
		int count = 0;
		int endCol = y + (CONNECT_NUMBER - 1);
		if (isPositionValid(endCol)) {
			for (int i = y + (CONNECT_NUMBER - 1); i >= y; i--) {
				if (board[x][i] != player)
					break;
				else {
					winPathRow[count] = x;
					winPathCol[count] = i;
					count++;
				}
			}
			if (count == CONNECT_NUMBER)
				return true;
			count = 0;
		}
		endCol = y - (CONNECT_NUMBER - 1);
		if (isPositionValid(endCol)) {
			for (int i = y - (CONNECT_NUMBER - 1); i <= y; i++) {
				if (board[x][i] != player)
					break;
				else {
					winPathRow[count] = x;
					winPathCol[count] = i;
					count++;
				}
			}
			if (count == CONNECT_NUMBER)
				return true;
		}
		return false;
	}

	/**
	 * Checks if player is winning in vertical direction.
	 * 
	 * @param player
	 *            Current player.
	 * @param x
	 *            Row number of current player's position.
	 * @param y
	 *            Column number of current player's position.
	 * @return True - if player wins, false - otherwise.
	 */
	private boolean isPlayerWinVertically(char player, int x, int y) {
		int count = 0;
		int endRow = x - (CONNECT_NUMBER - 1);
		if (isPositionValid(endRow)) {
			for (int i = x - (CONNECT_NUMBER - 1); i <= x; i++) {
				if (board[i][y] != player)
					break;
				else {
					winPathRow[count] = i;
					winPathCol[count] = y;
					count++;
				}
			}
			if (count == CONNECT_NUMBER)
				return true;
			count = 0;
		}
		endRow = x + (CONNECT_NUMBER - 1);
		if (isPositionValid(endRow)) {
			for (int i = x + (CONNECT_NUMBER - 1); i >= x; i--) {
				if (board[i][y] != player)
					break;
				else {
					winPathRow[count] = i;
					winPathCol[count] = y;
					count++;
				}
			}
			if (count == CONNECT_NUMBER)
				return true;
		}
		return false;
	}

	/**
	 * Checks if player wins on the 4 diagonals.
	 * 
	 * @param player
	 *            Current player.
	 * @param x
	 *            Row number of current player's position.
	 * @param y
	 *            Column number of current player's position.
	 * @return True - if player wins, false - otherwise.
	 */
	private boolean isPlayerWinDiagonally(char player, int x, int y) {
		//TODO TO be implemented.
		return (isPlayerWinDiaognalUp(player, x, y) || isPlayerWinDiagonalDown(
				player, x, y));
	}

	/**
	 * Checks if player wins on the 2 diagonals in left-upper and right-upper
	 * direction.
	 * 
	 * @param player
	 *            Current player.
	 * @param x
	 *            Row number of current player's position.
	 * @param y
	 *            Column number of current player's position.
	 * @return True - if player wins, false - otherwise.
	 */
	//TODO TO be splitted in two methods and tests if it works.
	private boolean isPlayerWinDiaognalUp(char player, int x, int y) {
		int count = 0;		
		int endRow = x - (CONNECT_NUMBER - 1);
		int endCol = y - (CONNECT_NUMBER - 1);
		if (isPositionValid(endRow) && isPositionValid(endCol)) {
			do {
				if (board[endRow][endCol] != player)
					break;
				else {
					winPathRow[count] = endRow;
					winPathCol[count] = endCol;
					count++;
					endRow++;
					endCol++;
				}
			} while (count < CONNECT_NUMBER);
			if(count == CONNECT_NUMBER){
				return true;
			}
			count = 0;
		}
		endRow = x - (CONNECT_NUMBER - 1);
		endCol = y + (CONNECT_NUMBER - 1);
		if (isPositionValid(endRow) && isPositionValid(endCol)) {
			do {
				if (board[endRow][endCol] != player)
					break;
				else {
					winPathRow[count] = endRow;
					winPathCol[count] = endCol;
					count++;
					endRow++;
					endCol--;
				}
			} while (count < CONNECT_NUMBER);
			if(count == CONNECT_NUMBER){
				return true;
			}
			count = 0;
		}
		return false;
	}

	/**
	 * Checks if player wins on the 2 diagonals in left-down and right-down
	 * direction.
	 * 
	 * @param player
	 *            Current player.
	 * @param x
	 *            Row number of current player's position.
	 * @param y
	 *            Column number of current player's position.
	 * @return True - if player wins, false - otherwise.
	 */
	private boolean isPlayerWinDiagonalDown(char player, int x, int y) {
		//TODO To be implemented.
		return false;
	}

	/**
	 * Board size setter.
	 * 
	 * @param boardSize
	 *            Board size to be set.
	 */
	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	/**
	 * Board size getter.
	 * 
	 * @return Board size.
	 */
	public int getBoardSize() {
		return boardSize;
	}

	/**
	 * Sets bot player state when game is in multiplayer mode.
	 */
	public void setBotPlayer() {
		botPlayer = (currentPlayer == BLACK) ? WHITE : BLACK;
	}

	/**
	 * Moves next man.
	 * 
	 * @param player
	 *            Current player.
	 * @param direction
	 *            Direction from which user drop a man.
	 * @param y
	 *            Row|Column of current direction.
	 * @return True - if move is valid, false - otherwise.
	 */
	public boolean moveMan(char player, Direction direction, int position) {

		switch (direction) {
		case TOP:
			return (moveManFromTop(player, position));
		case BOTTOM:
			return moveManFromBottom(player, position);
		case LEFT:
			return moveManFromLeft(player, position);
		case RIGHT:
			return moveManFromRight(player, position);
		}
		return false;
	}

	/**
	 * Checks whether player has won the game.
	 * 
	 * @param currentPlayer
	 *            Current player.
	 * @return True - if game has won, false - otherwise.
	 */
	public boolean isPlayerWin(char currentPlayer) {
		if (numberMoves[getMovesIndex(currentPlayer)] >= CONNECT_NUMBER) {
			Point p;
			for (int i = numberMoves[getMovesIndex(currentPlayer)] - 1; i >= 0; i--) {
				p = moves[getMovesIndex(currentPlayer)].get(i);
				if (isPlayerWinHorizontally(currentPlayer, p.x, p.y)) {
					return true;
				} else if (isPlayerWinVertically(currentPlayer, p.x, p.y)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Prints board on the standard output.
	 */
	public void printBoard() {
		output.delete(0, output.length());
		output.append("  ");
		int count = 0;
		while (count < getBoardSize()) {
			output.append(count);
			output.append((count == getBoardSize() - 1) ? '\n' : ' ');
			count++;
		}
		count = 0;
		for (int i = 0; i < getBoardSize(); i++) {
			output.append((count) + " ");
			for (int j = 0; j < getBoardSize(); j++) {
				output.append(board[i][j]);
				output.append((j == getBoardSize() - 1) ? '\n' : ' ');
			}
			count++;
		}
		System.out.print(output);
	}

	/**
	 * Prints the winning path.
	 */
	public void printWinPaths() {
		for (int i = 0; i < CONNECT_NUMBER; i++) {
			System.out.print("( " + winPathRow[i] + ", " + winPathCol[i] + ")");
			if (i == CONNECT_NUMBER - 1)
				System.out.println();
			else
				System.out.print(", ");
		}
	}
}
