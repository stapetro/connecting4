package com.ngc4.connect4.view.gameplay;

import java.awt.Graphics;
import java.awt.Point;

/**
 * 
 * Square that represents the frame of the game board.
 * The limits where the player can reach.
 * 
 * @author Leni
 * 
 */
public class Square {

	private Point upLeft;
	private int size;

	public Point[] keyPositions;

	public Square(int x, int y, int size) {
		this.size = size;
		upLeft = new Point(x, y);
		keyPositions = new Point[4];
	}

	/**
	 * Paints the square.
	 * 
	 * @param g-Graphics object that paints the square
	 */
	public void paint(Graphics g) {
		g.drawRect(upLeft.x, upLeft.y, size + 2, size + 2);
	}
}
