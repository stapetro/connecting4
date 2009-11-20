package com.ngc4.connect4.controller;

import java.awt.Point;
import java.io.Serializable;

import com.ngc4.connect4.enums.Direction;


/**
 * Represents the protocol for communication between client and server.
 * 
 * @author Stanislav Petrov
 * 
 */
public class MultiPlayerProtocol implements Serializable {

	/**
	 * Stores the current player.
	 */
	private char player;
	/**
	 * Stores the row number of the board.
	 */
	private int row;
	/**
	 * Stores the column number of the board.
	 */
	private int col;
	/**
	 * Stores the direction of player when he drops a men on the board.
	 */
	private Direction direction;
	/**
	 * Stores row/column number from which player moves a man.
	 */
	private int position;
	/**
	 * Stores whether player wins the game, or not.
	 */
	private boolean isPlayerWin;
	/**
	 * Stores whether game is draw.
	 */
	private boolean isGameDraw;

	/**
	 * Stores wining path with 4 squares' coordinates.
	 */
	private Point[] winPath;

	/**
	 * Constructs the multi player protocol.
	 * 
	 * @param player
	 *            Player which is currently moved a man.
	 * @param row
	 *            Row number of the board.
	 * @param col
	 *            Column number of the board.
	 */
	public MultiPlayerProtocol(char player, int row, int col) {
		this.player = player;
		this.row = row;
		this.col = col;
	}

	public char getPlayer() {
		return player;
	}

	public void setPlayer(char player) {
		this.player = player;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String toString() {
		return player + " " + row + " " + col;
	}

	public void setPlayerWin(boolean win) {
		this.isPlayerWin = win;
	}

	public boolean isPlayerWin() {
		return this.isPlayerWin;
	}
	
	public boolean isGameDraw() {
		return isGameDraw;
	}

	public void setGameDraw(boolean isGameDraw) {
		this.isGameDraw = isGameDraw;
	}

	public Point[] getWinPath() {
		return winPath;
	}

	public void setWinPath(Point[] winPath) {
		this.winPath = winPath;
	}
}
