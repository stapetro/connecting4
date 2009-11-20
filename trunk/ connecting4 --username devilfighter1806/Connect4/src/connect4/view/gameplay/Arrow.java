package connect4.view.gameplay;

import connect4.enums.Direction;

import java.awt.Graphics;
import java.awt.Point;

/**
 * An 2D-arrow pointing at all directions Has states - defining the pointing
 * direction.
 * 
 * @author Leni Kirilov
 * 
 */
public class Arrow {

	/**
	 * myDirection - the direction the arrow points at
	 */
	private Direction myDirection;
	private Point startPoint;
	private int size;

	public Arrow(int size, int x, int y) {
		this.size = size;
		startPoint = new Point(x, y);
		myDirection = Direction.VERTICAL_DOWN;
	}

	public void paint(Graphics g) {
		int x = startPoint.x;
		int y = startPoint.y;

		// the arrow points at
		switch (myDirection) {
		case VERTICAL_DOWN:
			g.drawLine(x, y, x, y + size);
			g.drawLine(x, y + size, x - size / 2, y + size / 2);
			g.drawLine(x, y + size, x + size / 2, y + size / 2);
			break;

		case VERTICAL_UP:
			g.drawLine(x, y, x, y - size);
			g.drawLine(x, y - size, x - size / 2, y - size / 2);
			g.drawLine(x, y - size, x + size / 2, y - size / 2);
			break;

		case HORIZONTAL_LEFT:
			g.drawLine(x, y, x - size, y);
			g.drawLine(x - size, y, x - size / 2, y - size / 2);
			g.drawLine(x - size, y, x - size / 2, y + size / 2);
			break;

		case HORIZONTAL_RIGHT:
			g.drawLine(x, y, x + size, y);
			g.drawLine(x + size, y, x + size / 2, y - size / 2);
			g.drawLine(x + size, y, x + size / 2, y + size / 2);
			break;
		}

	}

	/**
	 * Reset new start point for the arrow . That way the arrow moves across the
	 * field.
	 * 
	 * @param point
	 */
	public void setStartPoint(Point point) {
		startPoint.x = point.x;
		startPoint.y = point.y;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * Change the direction the arrow points at. Important for the paint method
	 * 
	 * @param dir
	 */
	public void setDirection(Direction dir) {
		myDirection = dir;
	}

	public Direction getDirection() {
		return myDirection;
	}
}
