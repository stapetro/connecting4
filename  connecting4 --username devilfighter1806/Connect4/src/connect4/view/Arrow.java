package connect4.view;

import connect4.controller.Direction;
import java.awt.Graphics;
import java.awt.Point;

/**
 * An 2D-arrow pointing at all directions Has states - defining the pointing
 * direction.
 * 
 * @author Leni
 * 
 */
public class Arrow {

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

		// points at
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

	public void setStartPoint(Point point) {
		startPoint.x = point.x;
		startPoint.y = point.y;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setDirection(Direction dir) {
		myDirection = dir;
	}

	public Direction getDirection() {
		return myDirection;
	}
}
