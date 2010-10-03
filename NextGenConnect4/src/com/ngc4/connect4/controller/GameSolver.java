package com.ngc4.connect4.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import com.ngc4.connect4.enums.BotTactics;
import com.ngc4.connect4.enums.Direction;
import com.ngc4.connect4.enums.GameMode;

/**
 * Connect4 game. Board should be NxN size, where N is odd number. Two players
 * are playing the game - white and black. Each player has (NxN -1)/2 men. Black
 * player always begins first in the middle of the board. Man is color which
 * fills square of the board, square is empty in the playing board.
 * 
 * @author Stanislav Petrov
 * @date 28 June 2009
 */
public class GameSolver {

    public static final int PLAYERS_COUNT = 2;
    /**
     * Stores how many men should be connected for winning the game.
     */
    public static final int CONNECT_NUMBER = 4;
    /**
     * Square state filled by the black player.
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
    private int boardSize;
    private char[][] board;
    private char currentPlayer;
    private char computerPlayer;
    private int[] numberMovesDone;
    /**
     * Stores all moves which are made.
     */
    private int movesCounter = 0;
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
    private StringBuilder output = new StringBuilder(256);
    /**
     * Stores mode of the game - single | multi player.
     */
    private GameMode gameMode;
    /**
     * Stores all bot statistics from traversing the board in the game.
     */
    private ArrayList<Point>[] botStatistics;
    /**
     * Stores in which direction's index current square of the board has maximum
     * neighbors for both players. Gives information when bot inspects the board
     * for choosing bot's next move.
     */
    private int[] neighborsDirection;

    /**
     * General purpose constructor. Sets the number of wining paths to zero.
     */
    public GameSolver(GameMode gameMode, int size) {
        this.gameMode = gameMode;
        setBoardSize(size);
        initializeBoard();
        initialiazeMoves();
        initBotProperties();
        winPathRow = new int[CONNECT_NUMBER];
        winPathCol = new int[CONNECT_NUMBER];
    }

    /**
     * Initializes board with empty squares.
     */
    private void initializeBoard() {
        board = new char[boardSize][boardSize];
        for (int i = 0; i < getBoardSize(); i++) {
            Arrays.fill(board[i], EMPTY);
        }

        //the center square is set to BLACK
        board[boardSize / 2][boardSize / 2] = BLACK;

        lastMove = new Point[PLAYERS_COUNT];

        for (int i = 0; i < lastMove.length; i++) {
            lastMove[i] = new Point();
        }

        movesCounter++;
    }

    /**
     * Initializes moves number of both players.
     */
    private void initialiazeMoves() {
        numberMovesDone = new int[PLAYERS_COUNT];
        numberMovesDone[getMovesIndex(BLACK)] = 1;
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
        neighborsDirection = new int[PLAYERS_COUNT];
    }

    /**
     * Clears all bot statistics on every move of the bot during the game.
     */
    private void clearBotStatistics() {
        for (int i = 0; i < botStatistics.length; i++) {
            botStatistics[i].clear();
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
        if (i == boardSize || i == 0) {
            return false;
        }
        board[i - 1][col] = player;
        lastMove[getMovesIndex(player)].x = i - 1;
        lastMove[getMovesIndex(player)].y = col;
        numberMovesDone[getMovesIndex(player)]++;
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
        if (i == -1 || i == boardSize - 1) {
            return false;
        }
        board[i + 1][col] = player;
        lastMove[getMovesIndex(player)].x = i + 1;
        lastMove[getMovesIndex(player)].y = col;
        numberMovesDone[getMovesIndex(player)]++;
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
            if (board[row][j] != EMPTY) {
                break;
            }
        }
        if (j == boardSize || j == 0) {
            return false;
        }
        board[row][j - 1] = player;
        lastMove[getMovesIndex(player)].x = row;
        lastMove[getMovesIndex(player)].y = j - 1;
        numberMovesDone[getMovesIndex(player)]++;
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
            if (board[row][j] != EMPTY) {
                break;
            }
        }
        if (j == boardSize - 1 || j == -1) {
            return false;
        }
        board[row][j + 1] = player;
        lastMove[getMovesIndex(player)].x = row;
        lastMove[getMovesIndex(player)].y = j + 1;
        numberMovesDone[getMovesIndex(player)]++;
        movesCounter++;
        return true;
    }

    /**
     * Gets the index of the current player for last move array.
     *
     * @param player
     *            Current player.
     * @return Index for pointing element from last move array.
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
     * Checks whether specified square is filled in the playing board.
     *
     * @param x
     *            Row number of the man.
     * @param y
     *            Column number of the man.
     * @return True - if square is filled, false - otherwise.
     */
    private boolean isSquareFilled(int x, int y) {
        return (board[x][y] == EMPTY) ? false : true;
    }

    /**
     * Checks whether player wins.
     *
     * @param player
     *            Current player of the game.
     * @param x
     *            Row number of the board.
     * @param y
     *            Column number of the board.
     * @return True - if current player wins, false - otherwise.
     */
    private boolean isPlayerWin(char player, int x, int y) {
        if (isPositionValid(x) && isPositionValid(y) && board[x][y] == player) {
            int cnt = 0;
            int row = 0;
            int col = 0;
            for (int i = 0; i < Direction.DIRECTIONS_MOVES_NUMBER; i++) {
                cnt = 0;
                row = x;
                col = y;
                winPathRow[cnt] = row;
                winPathCol[cnt] = col;
                row += Direction.COORD_X[i];
                col += Direction.COORD_Y[i];
                while (isPositionValid(row) && isPositionValid(col)) {
                    if (board[row][col] != player) {
                        break;
                    } else {
                        cnt++;
                        winPathRow[cnt] = row;
                        winPathCol[cnt] = col;
                        if (cnt == CONNECT_NUMBER - 1) {
                            return true;
                        }
                        row += Direction.COORD_X[i];
                        col += Direction.COORD_Y[i];
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks whether move is valid for bot player.
     *
     * @param x
     *            Row number of the man.
     * @param y
     *            Column number of the man.
     * @param directions
     *            Directions for all wining paths on the board.
     * @return True - if move is valid, false - otherwise.
     */
    private boolean isMoveValid(int x, int y, Direction[] directions) {
        //TODO substitute with easier to read boolean expression
        if (!isPositionValid(x) || !isPositionValid(y) || isSquareFilled(x, y)) {
            return false;
        }
        for (int i = 0; i < Direction.DIRECTIONS_MOVES_NUMBER / 2; i++) {
            if (moveMan(computerPlayer, directions[i], (i < 2) ? y : x)) {
                board[lastMove[getMovesIndex(computerPlayer)].x][lastMove[getMovesIndex(computerPlayer)].y] = EMPTY;
                if (lastMove[getMovesIndex(computerPlayer)].x == x
                        && lastMove[getMovesIndex(computerPlayer)].y == y) {
                    return true;
                }
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
     * @param directions
     *            Directions for all wining paths on the board.
     * @return 0 if the given man has no neighbors, otherwise - max number of
     *         man's neighbors.
     */
    private int getMaxNeighbors(char player, int x, int y) {
        int maxNeighbors = 0;
        int cnt = 0;
        int row = 0;
        int col = 0;
        for (int i = 0; i < Direction.DIRECTIONS_MOVES_NUMBER; i++) {
            cnt = 0;
            row = x + Direction.COORD_X[i];
            col = y + Direction.COORD_Y[i];
            while (isPositionValid(row) && isPositionValid(col)) {
                if (board[row][col] == player) {
                    cnt++;
                    row += Direction.COORD_X[i];
                    col += Direction.COORD_Y[i];
                } else {
                    break;
                }
            }
            if (maxNeighbors < cnt) {
                maxNeighbors = cnt;
                neighborsDirection[getMovesIndex(player)] = i;
            }
        }
        return maxNeighbors;
    }

    /**
     * Bot tries to win, i.e. checks whether he can win(3 consecutive men), or
     * prevent user from wining when user has 3 consecutive men. Coordinates
     * should be valid, otherwise - throws exception.
     *
     * @param x
     *            X-coordinate of the square(row number).
     * @param y
     *            Y-coordinate of the square(column number).
     * @param botNeighbors
     *            Maximum number of bot neighbors for specified square.
     * @param currPlayerNeighbors
     *            Maximum number of current player neighbors for the specified
     *            square.
     */
    private void tryBotWin(int x, int y, int botNeighbors,
            int currPlayerNeighbors) {
        if (botNeighbors == CONNECT_NUMBER - 1) {
            botStatistics[BotTactics.BOT_WIN.getIndex()].add(new Point(x, y));
        } else if (currPlayerNeighbors == CONNECT_NUMBER - 1) {
            botStatistics[BotTactics.NOT_USER_WIN.getIndex()].add(new Point(x, y));
        }
    }

    /**
     * Prevents user threats in the game.
     *
     * @param x
     *            Row number of the board.
     * @param y
     *            Column number of the board.
     * @param directions
     *            Directions for all wining paths on the board.
     */
    // TODO To be implemented for all threats.
    private void preventPlayerThreats(int x, int y, Direction[] directions) {
        int neighborsCurrPlayer = getMaxNeighbors(currentPlayer, x, y);
        if (neighborsCurrPlayer == CONNECT_NUMBER - 2) {
            isDoubleThreat(x, y, directions);
        }

        if ((isPositionValid(x - 1) && board[x - 1][y] == currentPlayer
                && isPositionValid(y + 1) && board[x][y + 1] == currentPlayer)
                || (isPositionValid(y - 1) && board[x][y - 1] == currentPlayer
                && isPositionValid(x + 1) && board[x + 1][y] == currentPlayer)
                || (isPositionValid(y + 1) && board[x][y + 1] == currentPlayer
                && isPositionValid(x + 1) && board[x + 1][y] == currentPlayer)
                || (isPositionValid(y - 1) && board[x][y - 1] == currentPlayer
                && isPositionValid(x - 1) && board[x - 1][y] == currentPlayer)) {

            botStatistics[BotTactics.NOT_USER_THREAT.getIndex()].add(0, new Point(x, y));
        }
    }

    /**
     * Prevents from player's double threats.
     *
     * @param x
     *            Row number of the board.
     * @param y
     *            Column number of the board.
     * @param directions
     *            Directions for all wining paths on the board.
     */
    private boolean isDoubleThreat(int x, int y, Direction[] directions) {
        boolean result = false;
        for (int i = 0; i < Direction.DIRECTIONS_MOVES_NUMBER; i++) {
            int calculatedX = x + Direction.COORD_X[neighborsDirection[getMovesIndex(currentPlayer)]] * (CONNECT_NUMBER - 1);
            int calucaltedY = y + Direction.COORD_Y[neighborsDirection[getMovesIndex(currentPlayer)]] * (CONNECT_NUMBER - 1);
            
            if (isMoveValid(calculatedX, calucaltedY, directions)) {
                botStatistics[BotTactics.NOT_USER_THREAT.getIndex()].add(0, new Point(x, y));
                result = true;
                
                // this check might be redundant.
                calculatedX = x + Direction.COORD_X[neighborsDirection[getMovesIndex(currentPlayer)]] * (CONNECT_NUMBER);
                calucaltedY = y + Direction.COORD_Y[neighborsDirection[getMovesIndex(currentPlayer)]] * (CONNECT_NUMBER);

                if (isMoveValid(calculatedX, calucaltedY, directions)) {
                    botStatistics[BotTactics.NOT_USER_THREAT.getIndex()].add(0,new Point(x, y));
                }
            }
        }
        return result;
    }

    /**
     * Bot makes some tactics during his move.
     *
     * @param x
     *            Row number of the board.
     * @param y
     *            Column number of the board.
     * @param directions
     *            Directions for all wining paths on the board.
     */
    private void goBotTactics(int x, int y) {
        botStatistics[BotTactics.TACTIC.getIndex()].add(0, new Point(x, y));
    }

    /**
     * Chooses next move of the bot.
     *
     * @return Current position which bot player puts his color in the playing
     *         board on.
     */
    private Point chooseBotMove() {
        int maxBotNeighbors = 0;
        int botNeighbors = 0;
        int currPlayerNeighbors = 0;
        Point p = new Point(-1, -1);
        Direction[] directions = Direction.values();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (isMoveValid(i, j, directions)) {
                    botNeighbors = getMaxNeighbors(computerPlayer, i, j);
                    currPlayerNeighbors = getMaxNeighbors(currentPlayer, i, j);
                    tryBotWin(i, j, botNeighbors, currPlayerNeighbors);
                    preventPlayerThreats(i, j, directions);

                    if (maxBotNeighbors < botNeighbors) {
                        maxBotNeighbors = botNeighbors;
                        goBotTactics(i, j);
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
        clearBotStatistics();
        return p;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        initializeBoard();
        initialiazeMoves();
        initBotProperties();
    }

    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Sets first player(only first player if it's multiplayer), and bot player
     * if game mode is single player.
     *
     * @param player
     *            First player to be set.
     */
    public void setPlayer(char player) {
        this.currentPlayer = player;
        if (gameMode == GameMode.SINGLE_PLAYER) {
            computerPlayer = (currentPlayer == BLACK) ? WHITE : BLACK;
        }
    }

    public char getBot() {
        return this.computerPlayer;
    }

    /**
     * Moves next man.
     *
     * @param player
     *            Current player.
     * @param direction
     *            Direction2 from which user drop a man.
     * @param position
     *            Row|Column of current direction.
     * @return True - if move is valid, false - otherwise.
     */
    public boolean moveMan(char player, Direction direction, int position) {

        switch (direction) {
            case VERTICAL_UP:
                return moveManFromTop(player, position);
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
        int row = lastMove[getMovesIndex(currentPlayer)].x;
        int col = lastMove[getMovesIndex(currentPlayer)].y;

        if (isPlayerWin(currentPlayer, row, col)) {
            return true;
        }

        for (int i = 0; i < Direction.DIRECTIONS_MOVES_NUMBER; i++) {
            if (isPlayerWin(currentPlayer, row + Direction.COORD_X[i], col + Direction.COORD_Y[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether game is a draw,i.e. if none of both players wins and there
     * is no valid move in the playing board.
     *
     * @return True - if game is over with no winner, false - otherwise.
     */
    public boolean isGameDraw() {
        Point currentPlayerLastMove;
        Point oldLastMove;
        Direction[] directions = Direction.values();
        oldLastMove = new Point(getLastMove(currentPlayer));

        for (int i = 0; i < Direction.DIRECTIONS_MOVES_NUMBER / 2; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (moveMan(currentPlayer, directions[i], j)) {
                    currentPlayerLastMove = getLastMove(currentPlayer);
                    board[currentPlayerLastMove.x][currentPlayerLastMove.y] = EMPTY;
                    this.lastMove[getMovesIndex(currentPlayer)] = oldLastMove;
                    return false;
                }
            }//inner for
        }//outer for
        return true;
    }

    /**
     * Indicates next move on bot player over the playing board.
     *
     * @return True - if bot wins with this move, false - otherwise.
     */
    public boolean nextBotMove() {
        Point move = chooseBotMove();
        board[move.x][move.y] = computerPlayer;
        lastMove[getMovesIndex(computerPlayer)].x = move.x;
        lastMove[getMovesIndex(computerPlayer)].y = move.y;
        numberMovesDone[getMovesIndex(computerPlayer)]++;

        if (isPlayerWin(computerPlayer)) {
            return true;
        }
        return false;
    }

    /**
     * Gets last move made from the specified player.
     *
     * @param player
     *            Player to be specified.
     * @return The last move made by the specified player.
     */
    public Point getLastMove(char player) {
        return lastMove[getMovesIndex(player)];
    }

    /**
     * Sets square from the board with value. Used in client/server
     * communication.
     *
     * @param player
     *            Player to be set on the board.
     * @param row
     *            Row number of the board.
     * @param col
     *            Column number of the board.
     */
    public void setSquare(char player, int row, int col) {
        if (isPositionValid(row) && isPositionValid(col)) {
            board[row][col] = player;
        }
    }

    /**
     * Gets the winning path, used in client/server communication.
     *
     * @return Array with the wining path which contains 4 squares` coordinates.
     */
    public Point[] getWinPath() {
        Point[] winPath = new Point[winPathRow.length];
        for (int i = 0; i < winPath.length; i++) {
            winPath[i] = new Point(winPathRow[i], winPathCol[i]);
        }
        return winPath;
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
            output.append((count)).append(" ");
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
            if (i == CONNECT_NUMBER - 1) {
                System.out.println();
            } else {
                System.out.print(", ");
            }
        }
    }
}