package connect4.view;

import java.awt.Graphics;
import java.awt.Point;

public class Arrow {

	private Direction myDirection;
	private Point startPoint;
	private int size;

	public Arrow(int size, int x, int y) {
		this.size = size;
		startPoint = new Point(x, y);
		myDirection = Direction.DOWN;
	}

	public void paint(Graphics g) {
		int x = startPoint.x;
		int y = startPoint.y;
		
		
		//points at 
		switch (myDirection) {
		case DOWN:
			g.drawLine(x, y, x, y + size);
			g.drawLine(x, y + size, x - size / 2, y + size / 2);
			g.drawLine(x, y + size, x + size / 2, y + size / 2);
			break;

		case UP:
			g.drawLine(x, y, x, y - size);
			g.drawLine(x, y - size, x - size / 2, y - size / 2);
			g.drawLine(x, y - size, x + size / 2, y - size / 2);
			break;

		case LEFT:
			g.drawLine(x, y, x - size, y);
			g.drawLine(x - size, y, x - size / 2, y - size / 2);
			g.drawLine(x - size, y, x - size / 2, y + size / 2);
			break;

		case RIGHT:
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
