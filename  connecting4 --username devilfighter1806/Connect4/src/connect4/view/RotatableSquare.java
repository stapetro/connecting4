package connect4.view;

import java.awt.Graphics;
import java.awt.Point;

public class RotatableSquare {

	private Point upLeft;
	private Point mid;
	public Point tempUpLeft;
	private Position position;
	private int size;

	public RotatableSquare(int x, int y, int size) {
		this.size = size;
		upLeft = new Point(x, y);
		position = Position.DEGREE_45;
		mid = new Point(x + size / 2, y + size / 2);
		tempUpLeft = new Point(upLeft);
	}

	public void rotateLeft() {
		position.i = (position.i + 1) % 4;
	}

	public void rotateRight() {
		position.i = (position.i - 1) % 4;
		if (position.i < 0) {
			position.i = 3;
		}
	}

	public void paint(Graphics g) {
		int x = mid.x;
		int y = mid.y;
		int cos = (int) ((size * Math.cos(22.5) * Math.sqrt(2)) / 2);
		int sin = (int) ((size * Math.sin(22.5) * Math.sqrt(2)) / 2);
		int sqr2 = (int) (size * Math.sqrt(2) / 2);

		switch (position.i) {

		// 0
		case 0:
			System.out.println("case 0");
			tempUpLeft.x = 0;
			tempUpLeft.y = 0;

			g.drawLine(x + sqr2, y, x, y + sqr2);
			g.drawLine(x, y + sqr2, x - sqr2, y);
			g.drawLine(x - sqr2, y, x, y - sqr2);
			g.drawLine(x, y - sqr2, x + sqr2, y);
			break;

		// 22
		case 1:

			System.out.println("case 1");
			tempUpLeft.x = upLeft.x + (int) (sqr2)/2;
			tempUpLeft.y = upLeft.y - (int) (sqr2)/2;

			g.drawLine(x + sin, y + cos, x - cos, y + sin);
			g.drawLine(x - cos, y + sin, x - sin, y - cos);
			g.drawLine(x - sin, y - cos, x + cos, y - sin);
			g.drawLine(x + cos, y - sin, x + sin, y + cos);
			break;

		// 45
		case 2:

			System.out.println("case 2");
			tempUpLeft.x = 0;
			tempUpLeft.y = 0;

			g.drawRect(upLeft.x, upLeft.y, size + 2, size + 2);
			break;

		// 77
		case 3:

			System.out.println("case 3");

			tempUpLeft.x = upLeft.x - (int) (sqr2)/2;
			tempUpLeft.y = upLeft.y + (int) (sqr2)/2;

			g.drawLine(x + cos, y + sin, x - sin, y + cos);
			g.drawLine(x - sin, y + cos, x - cos, y - sin);
			g.drawLine(x - cos, y - sin, x + sin, y - cos);
			g.drawLine(x + sin, y - cos, x + cos, y + sin);
			break;

		}
	}

	public int getSize() {
		return size;
	}
}
