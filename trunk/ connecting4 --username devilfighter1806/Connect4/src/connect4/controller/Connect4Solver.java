package connect4.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Connect4 game. Board should be NxN size, where N is odd number. Two players
 * are playing the game - white and black. Each player has (NxN -1)/2 men. Black
 * player always begins first in the middle of the board. Man is color which
 * fills square of the board, square is empty in the playing board.
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
	 * Empty square state.
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
	 * Stores all moves which are made.
	 */
	private int movesCounter;
	/**
	 * Stores row numbers of each man of winning configuration.
	 */
	private int[] winPathRow;
	/**
	 * Stores column numbers of each man of winning configuration.
	 */
	private int[] winPathCol;
	/**
	 * Stores the last move regardless of the current player.
	 */
	private Point lastMove;
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
		initializeBoard();
		initialiazeMoves();
		this.gameMode = gameMode;
		winPathRow = new int[CONNECT_NUMBER];
		winPathCol = new int[CONNECT_NUMBER];
		output = new StringBuilder();
	}

	/**
	 * Initializes board with empty squares.
	 */
	private void initializeBoard() {
		board = new char[boardSize][boardSize];
		for (int i = 0; i < getBoardSize(); i++) {
			Arrays.fill(board[i], EMPTY);
		}
		board[boardSize / 2][boardSize / 2] = BLACK;
		lastMove = new Point(boardSize / 2, boardSize / 2);
		movesCounter = 1;
	}

	/**
	 * Initializes moves number of both players.
	 */
	private void initialiazeMoves() {
		numberMoves = new int[PLAYERS];
		numberMoves[getMovesIndex(BLACK)] = 1;
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
		lastMove.x = i - 1;
		lastMove.y = col;
		numberMoves[getMovesIndex(player)]++;
		movesCounter++;
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
		lastMove.x = i + 1;
		lastMove.y = col;
		numberMoves[getMovesIndex(player)]++;
		movesCounter++;
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
		lastMove.x = row;
		lastMove.y = j - 1;
		numberMoves[getMovesIndex(player)]++;
		movesCounter++;
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
		lastMove.x = row;
		lastMove.y = j + 1;
		numberMoves[getMovesIndex(player)]++;
		movesCounter++;
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
	 * Validates position(row or column) in the board.
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
		return (isPlayerWinDiaognal(player, x, y, true) || isPlayerWinDiaognal(
				player, x, y, false));
	}

	/**
	 * Checks if player wins on the 2 diagonals in left and right direction.
	 * 
	 * @param player
	 *            Current player.
	 * @param x
	 *            Row number of current player's position.
	 * @param y
	 *            Column number of current player's position.
	 * @param up
	 *            True - when checks diagonals in up direction, false - in down
	 *            direction.
	 * @return True - if player wins, false - otherwise.
	 */
	private boolean isPlayerWinDiaognal(char player, int x, int y, boolean up) {
		int count = 0;
		int endRow = (up) ? (x - (CONNECT_NUMBER - 1))
				: (x + (CONNECT_NUMBER - 1));
		int endCol = y - (CONNECT_NUMBER - 1);
		if (isPositionValid(endRow) && isPositionValid(endCol)) {
			do {
				if (board[endRow][endCol] != player)
					break;
				else {
					winPathRow[count] = endRow;
					winPathCol[count] = endCol;
					count++;
					endRow = (up) ? (endRow + 1) : (endRow - 1);
					endCol++;
				}
			} while (count < CONNECT_NUMBER);
			if (count == CONNECT_NUMBER) {
				return true;
			}
			count = 0;
		}
		endCol = y + (CONNECT_NUMBER - 1);
		if (isPositionValid(endRow) && isPositionValid(endCol)) {
			do {
				if (board[endRow][endCol] != player)
					break;
				else {
					winPathRow[count] = endRow;
					winPathCol[count] = endCol;
					count++;
					endRow = (up) ? (endRow + 1) : (endRow - 1);
					endCol--;
				}
			} while (count < CONNECT_NUMBER);
			if (count == CONNECT_NUMBER) {
				return true;
			}
			count = 0;
		}
		return false;
	}

	/**
	 * Checks whether specified man is filled in the playing board.
	 * 
	 * @param x
	 *            Row number of the man.
	 * @param y
	 *            Column number of the man.
	 * @return True - if man is filled, false - otherwise.
	 */
	private boolean isSquareFilled(int x, int y) {
		if (isPositionValid(x) && isPositionValid(y)) {
			return (board[x][y] == BLACK || board[x][y] == WHITE) ? true
					: false;
		} else
			return false;
	}

	/**
	 * Checks whether move is valid for bot player, not for human being player.
	 * 
	 * @param x
	 *            Row number of the man.
	 * @param y
	 *            Column number of the man.
	 * @return True - if move is valid, false - otherwise.
	 */
	private boolean isMoveValid(int x, int y) {
		return (isSquareFilled(x, y - 1) && !isSquareFilled(x, y + 1))
				|| (!isSquareFilled(x, y - 1) && isSquareFilled(x, y + 1))
				|| (isSquareFilled(x + 1, y) && !isSquareFilled(x - 1, y))
				|| (!isSquareFilled(x + 1, y) && isSquareFilled(x - 1, y));
	}

	/**
	 * Gets max number of the specified man's neighbors in vertical direction.
	 * This is a helper method.
	 * 
	 * @param x
	 *            Row number of the given man.
	 * @param y
	 *            Row number of the given man.
	 * @param player
	 *            Player whose neighbors are counted.
	 * @return 0 if the given man has no neighbors , max number of man's
	 *         neighbors vertically.
	 */
	private int getMaxNeighborsVertically(char player, int x, int y) {
		int maxNeigbors = 0;
		int countNeighbors = 0;
		int row = 0;
		row = x + 1;
		while (isPositionValid(row) && countNeighbors < CONNECT_NUMBER) {
			if (board[row][y] == player) {
				countNeighbors++;
				row++;
			} else
				break;
		}
		maxNeigbors = Math.max(maxNeigbors, countNeighbors);
		countNeighbors = 0;
		row = x - 1;
		while (isPositionValid(row) && countNeighbors < CONNECT_NUMBER) {
			if (board[row][y] == player) {
				countNeighbors++;
				row--;
			} else
				break;
		}
		maxNeigbors = Math.max(maxNeigbors, countNeighbors);
		return maxNeigbors;
	}

	/**
	 * Gets max number of the specified man's neighbors in horizontal direction.
	 * This is a helper method.
	 * 
	 * @param x
	 *            Row number of the given man.
	 * @param y
	 *            Row number of the given man.
	 * @param player
	 *            Player whose neighbors are counted.
	 * @return 0 if the given man has no neighbors , max number of man's
	 *         neighbors horizontally.
	 */
	private int getMaxNeighborsHorizontally(char player, int x, int y) {
		int maxNeigbors = 0;
		int countNeighbors = 0;
		int col;
		col = y + 1;
		while (isPositionValid(col) && countNeighbors < CONNECT_NUMBER) {
			if (board[x][col] == player) {
				countNeighbors++;
				col++;
			} else {
				break;
			}
		}
		maxNeigbors = Math.max(maxNeigbors, countNeighbors);
		countNeighbors = 0;
		col = y - 1;
		while (isPositionValid(col) && countNeighbors < CONNECT_NUMBER) {
			if (board[x][col] == player) {
				countNeighbors++;
				col--;
			} else {
				break;
			}
		}
		maxNeigbors = Math.max(maxNeigbors, countNeighbors);
		return maxNeigbors;
	}

	/**
	 * Gets max number of the specified man's neighbors in all diagonals
	 * direction. This is a helper method.
	 * 
	 * @param x
	 *            Row number of the given man.
	 * @param y
	 *            Row number of the given man.
	 * @param player
	 *            Player whose neighbors are counted.
	 * @return 0 if the given man has no neighbors , max number of man's
	 *         neighbors diagonally.
	 */
	private int getMaxNeighborsDiagonally(char player, int x, int y) {
		int maxNeighbors = 0;
		maxNeighbors = Math.max(getMaxNeighborsDiagonal(player, x, y, true),
				getMaxNeighborsDiagonal(player, x, y, false));
		return maxNeighbors;
	}

	/**
	 * Gets max number of the specified man's neighbors in two diagonals in
	 * specified direction. This is a helper method.
	 * 
	 * @param x
	 *            Row number of the given man.
	 * @param y
	 *            Row number of the given man.
	 * @param player
	 *            Player whose neighbors are counted.
	 * @param up
	 *            True when traverses diagonals in up direction, false -
	 *            diagonals in down direction.
	 * @return 0 if the given man has no neighbors , max number of man's
	 *         neighbors in the 2 diagonals.
	 */
	private int getMaxNeighborsDiagonal(char player, int x, int y, boolean up) {
		int maxNeighbors = 0;
		int countNeigbors = 0;
		int row = (up) ? x - 1 : x + 1;
		int col = y - 1;
		col = y - 1;
		while (isPositionValid(row) && isPositionValid(col)
				&& countNeigbors < CONNECT_NUMBER) {
			if (board[row][col] == player) {
				countNeigbors++;
				row = (up) ? (row - 1) : (row + 1);
				col--;
			} else
				break;
		}
		maxNeighbors = Math.max(maxNeighbors, countNeigbors);
		countNeigbors = 0;
		row = (up) ? x - 1 : x + 1;
		col = y + 1;
		while (isPositionValid(row) && isPositionValid(col)
				&& countNeigbors < CONNECT_NUMBER) {
			if (board[row][col] == player) {
				countNeigbors++;
				row = (up) ? (row - 1) : (row + 1);
				col++;
			} else
				break;
		}
		maxNeighbors = Math.max(maxNeighbors, countNeigbors);
		return maxNeighbors;
	}

	/**
	 * Gets max number of the specified man's neighbors.
	 * 
	 * @param x
	 *            Row number of the given man.
	 * @param y
	 *            Row number of the given man.
	 * @param player
	 *            Player whose neighbors are counted.
	 * @return 0 if the given man has no neighbors, max number of man's
	 *         neighbors.
	 */
	private int getMaxNeighbors(char player, int x, int y) {
		int maxNeigbors = 0;
		if (board[x][y] == EMPTY) {
			maxNeigbors = Math.max(maxNeigbors, getMaxNeighborsVertically(
					player, x, y));
			maxNeigbors = Math.max(maxNeigbors, getMaxNeighborsHorizontally(
					player, x, y));
			maxNeigbors = Math.max(maxNeigbors, getMaxNeighborsDiagonally(
					player, x, y));
		}
		return maxNeigbors;
	}

	/**
	 * Checks whether this man is threaten from the current player. Threaten man
	 * means that this man has 3 men in any of the its winning paths.
	 * 
	 * @param x
	 *            Row number of the possibly threaten man.
	 * @param y
	 *            Column number of the possibly threaten man.
	 * @param player
	 *            The player who threats the opponent.
	 * @return True - if man is threaten, false - otherwise.
	 */
	private boolean isSquareThreaten(char player, int x, int y) {
		if (board[x][y] == EMPTY) {
			return (getMaxNeighbors(player, x, y) == CONNECT_NUMBER - 1) ? true
					: false;
		}
		return false;
	}

	/**
	 * Chooses next move of the bot.
	 * 
	 * @return Current position which bot player puts his color in the playing
	 *         board on.
	 */
	private Point chooseBotMove() {
		Point p = new Point(-1, -1);
		int maxNeighbours = -1;
		int neighbours = 0;
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (isMoveValid(i, j)) {
					if (isSquareThreaten(currentPlayer, i, j)) {
						p.x = i;
						p.y = j;
						return p;
					} else {
						neighbours = getMaxNeighbors(botPlayer, i, j);
						if (neighbours > maxNeighbours) {
							maxNeighbours = neighbours;
							p.x = i;
							p.y = j;
						}
					}
				}
			}
		}
		return p;
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
	 * Sets first player and bot player if game mode is single player.
	 */
	public void setPlayer(char player) {
		this.currentPlayer = player;
		if (gameMode == GameMode.SINGLE_PLAYER) {
			botPlayer = (currentPlayer == BLACK) ? WHITE : BLACK;
		}
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
			if (isPlayerWinHorizontally(currentPlayer, lastMove.x, lastMove.y)) {
				return true;
			} else if (isPlayerWinVertically(currentPlayer, lastMove.x,
					lastMove.y)) {
				return true;
			} else if (isPlayerWinDiagonally(currentPlayer, lastMove.x,
					lastMove.y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether game is a draw,i.e. if none of both players wins and there
	 * is no empty men in the playing board.
	 * 
	 * @return True - if game is over with no winner, false - otherwise.
	 */
	public boolean isGameDraw() {
		return (movesCounter == boardSize * boardSize - 1) ? true : false;
	}

	/**
	 * Indicates next move on bot player over the playing board.
	 */
	public void nextBotMove() {
		Point move = chooseBotMove();
		System.out.println("Bot pos: " + move);
		board[move.x][move.y] = botPlayer;
		if (isPlayerWin(botPlayer))
			System.out.println("Bot win!");
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
