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
	 * Stores the last move for both players.
	 */
	private Point[] lastMove;
	/**
	 * Stores game board for printing to the standard output.
	 */
	private StringBuilder output;
	/**
	 * Stores mode of the game - single | multi player.
	 */
	private GameMode gameMode;
	/**
	 * Stores max number of neighbors when checking for threaten square.
	 */
	private int maxNeighbors;
	/**
	 * Stores all bot statistics from traversing the board in the game.
	 */
	private ArrayList<Point>[] botStatistics;
	/**
	 * Stores in which direction current square of the board has maximum
	 * neighbors. Gives information when bot inspects the board for choosing
	 * bot's next move.
	 */
	private Direction maxNeighborsDirection;

	/**
	 * General purpose contructor. Sets the number of wining paths to zero.
	 * 
	 * @param gameMode
	 *            Game mode to be set.
	 * @param size
	 *            Board size to be set.
	 */
	public Connect4Solver(GameMode gameMode, int size) {
		this.gameMode = gameMode;
		setBoardSize(size);
		initializeBoard();
		initialiazeMoves();
		initBotProperties();
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
		lastMove = new Point[PLAYERS];
		for(int i = 0; i < lastMove.length; i++){
			lastMove[i] = new Point();
		}
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
	 * Initialize the data structure which stores the bot statistics.
	 */
	@SuppressWarnings("unchecked")
	private void initBotProperties() {
		if (gameMode != GameMode.SINGLE_PLAYER) {
			return;
		}
		botStatistics = new ArrayList[BotTactics.TACTICS_NUMBER];
		for (int i = 0; i < botStatistics.length; i++) {
			botStatistics[i] = new ArrayList<Point>();
		}
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
		lastMove[getMovesIndex(player)].x = i - 1;
		lastMove[getMovesIndex(player)].y = col;
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
		lastMove[getMovesIndex(player)].x = i + 1;
		lastMove[getMovesIndex(player)].y = col;
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
		lastMove[getMovesIndex(player)].x = row;
		lastMove[getMovesIndex(player)].y = j - 1;
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
		lastMove[getMovesIndex(player)].x = row;
		lastMove[getMovesIndex(player)].y = j + 1;
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
	 * Checks whether specified square is filled in the playing board.
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
	 * Checks whether move is valid for bot player.
	 * 
	 * @param x
	 *            Row number of the man.
	 * @param y
	 *            Column number of the man.
	 * @return True - if move is valid, false - otherwise.
	 */
	private boolean isMoveValid(int x, int y, Direction[] directions) {
		if (!isPositionValid(x) || !isPositionValid(y)) {
			return false;
		}
		for (int i = 0; i < Direction.DIRECTIONS_MOVE_NUMBERS / 2; i++) {
			if (moveMan(botPlayer, directions[i], (i < 2) ? y : x)) {
				System.out.println("Last move: " + x + ", " + y);
				board[lastMove[getMovesIndex(botPlayer)].x][lastMove[getMovesIndex(botPlayer)].y] = EMPTY;
				return (lastMove[getMovesIndex(botPlayer)].x == x && lastMove[getMovesIndex(botPlayer)].y == y) ? true
						: false;
			}
		}
		return false;
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
	 * @return 0 if the given man has no neighbors, otherwise - max number of
	 *         man's neighbors.
	 */
	private int getMaxNeighbors(char player, int x, int y,
			Direction[] directions) {
		int maxNeighbors = 0;
		int cnt = 0;
		int row = x;
		int col = y;
		for (Direction d : directions) {
			cnt = 0;
			row += Direction.COORD_X[d.getIndex()];
			col += Direction.COORD_Y[d.getIndex()];
			while (isPositionValid(row) && isPositionValid(col)) {
				if (board[row][col] == player) {
					cnt++;
					row += Direction.COORD_X[d.getIndex()];
					col += Direction.COORD_Y[d.getIndex()];
				} else
					break;
			}
			if (maxNeighbors < cnt) {
				maxNeighbors = cnt;
				maxNeighborsDirection = d;
			}
		}
		return maxNeighbors;
	}

	/**
	 * Checks whether opponent double threats the bot, i.e. player can win from
	 * two sides in one win configuration.
	 * 
	 * @param x
	 *            Row number.
	 * @param y
	 *            Column number.
	 * @return True - if this square is part from the double threat, false -
	 *         otherwise.
	 */
	private boolean isDoubleThreaten(int x, int y) {
		// TODO To be implemented - checks whether move is valid from the two
		// sides.
		return false;
	}

	/**
	 * Bot tries tow win, i.e. checks whether he can win(3 consecutive men), or
	 * prevent user from wining when user has 3 consecutive men. Coordinates
	 * should be valid, otherwise - throws exception.
	 * 
	 * @param x
	 *            X-coordinate of the square(row number).
	 * @param y
	 *            Y-coordinate of the square(column number).
	 */
	private void tryBotWin(int x, int y, Direction[] directions) {
		int neighborsBot = 0;
		int neighborsCurrPlayer = 0;
		neighborsBot = getMaxNeighbors(botPlayer, x, y, directions);
		neighborsCurrPlayer = getMaxNeighbors(currentPlayer, x, y, directions);
		if (neighborsBot == CONNECT_NUMBER - 1) {
			botStatistics[BotTactics.BOT_WIN.getIndex()].add(new Point(x, y));
		} else if (neighborsCurrPlayer == CONNECT_NUMBER - 1) {
			botStatistics[BotTactics.NOT_USER_WIN.getIndex()].add(new Point(x,
					y));
		}
	}

	/**
	 * Prevents user threats in the game.
	 */
	private void preventPlayerThreats(int x, int y, Direction[] directions) {
		int neighborsCurrPlayer = getMaxNeighbors(currentPlayer, x, y,
				directions);
		if (neighborsCurrPlayer == CONNECT_NUMBER - 2) {
			if (isMoveValid(x
					+ Direction.COORD_X[maxNeighborsDirection.getIndex()]
					* CONNECT_NUMBER, y
					+ Direction.COORD_Y[maxNeighborsDirection.getIndex()]
					* CONNECT_NUMBER, directions)) {
				botStatistics[BotTactics.NOT_USER_THREAT.getIndex()]
						.add(new Point(x, y));
			}
		} else if (isPositionValid(x - 1) && board[x - 1][y] == currentPlayer
				&& isPositionValid(y + 1) && board[x][y + 1] == currentPlayer) {
			botStatistics[BotTactics.NOT_USER_THREAT.getIndex()].add(new Point(
					x, y));
		}
	}

	private void botTactics(int x, int y, Direction[] directions) {

	}

	/**
	 * Chooses next move of the bot.
	 * 
	 * @return Current position which bot player puts his color in the playing
	 *         board on.
	 */
	private Point chooseBotMove() {
		int maxBotNeighbors = 0;
		int tempBotNeighbors = 0;
		Point p = new Point(-1, -1);
		Direction[] directions = Direction.values();
		for (int i = 0; i < boardSize; i++) {
			System.out.println("Valid moves");
			for (int j = 0; j < boardSize; j++) {
				if (isMoveValid(i, j, directions)) {
					System.out.println(i + ",  " + j);
					tryBotWin(i, j, directions); // bot win, not user win
					preventPlayerThreats(i, j, directions); // not user threats
					tempBotNeighbors = getMaxNeighbors(botPlayer, i, j,
							directions);
					if (maxBotNeighbors < tempBotNeighbors) {
						maxBotNeighbors = tempBotNeighbors;
						botStatistics[BotTactics.TACTIC.getIndex()].add(0,
								new Point(i, j));
					} else {
						p.x = i;
						p.y = j;
					}
				}
			}
		}
		for (int i = 0; i < botStatistics.length; i++) {
			if (botStatistics[i].size() > 0) {
				p = botStatistics[i].get(0);
				break;
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
		case VERTICAL_UP:
			return (moveManFromTop(player, position));
		case VERTICAL_DOWN:
			return moveManFromBottom(player, position);
		case HORIZONTAL_LEFT:
			return moveManFromLeft(player, position);
		case HORIZONTAL_RIGHT:
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
			if (isPlayerWinHorizontally(currentPlayer,
					lastMove[getMovesIndex(currentPlayer)].x,
					lastMove[getMovesIndex(currentPlayer)].y)) {
				return true;
			} else if (isPlayerWinVertically(currentPlayer,
					lastMove[getMovesIndex(currentPlayer)].x,
					lastMove[getMovesIndex(currentPlayer)].y)) {
				return true;
			} else if (isPlayerWinDiagonally(currentPlayer,
					lastMove[getMovesIndex(currentPlayer)].x,
					lastMove[getMovesIndex(currentPlayer)].y)) {
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
