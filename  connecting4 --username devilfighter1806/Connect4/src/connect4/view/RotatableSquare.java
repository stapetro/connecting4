package connect4.view;

import java.awt.Graphics;
import java.awt.Point;

public class RotatableSquare {

	private Point upLeft;
	private Point mid;
	private Position position;

	public RotatableSquare(int x, int y) {
		upLeft = new Point(x, y);
		position = Position.DEGREE_45;
		mid = new Point(x + DrawMan.SIZE / 2, y + DrawMan.SIZE / 2);
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
		int cos, sin;

		switch (position.i) {
		case 0:
			int sqr2 = (int) (DrawMan.SIZE * Math.sqrt(2) / 2);

			g.drawLine(x + sqr2, y, x, y + sqr2);
			g.drawLine(x, y + sqr2, x - sqr2, y);
			g.drawLine(x - sqr2, y, x, y - sqr2);
			g.drawLine(x, y - sqr2, x + sqr2, y);
			break;
		case 1:
			cos = (int) ((DrawMan.SIZE * Math.cos(22.5) * Math.sqrt(2)) / 2);
			sin = (int) ((DrawMan.SIZE * Math.sin(22.5) * Math.sqrt(2)) / 2);

			g.drawLine(x + cos, y + sin, x - sin, y + cos);
			g.drawLine(x - sin, y + cos, x - cos, y - sin);
			g.drawLine(x - cos, y - sin, x + sin, y - cos);
			g.drawLine(x + sin, y - cos, x + cos, y + sin);
			break;
		case 2:
			g.drawRect(upLeft.x, upLeft.y, DrawMan.SIZE + 2, DrawMan.SIZE + 2);
			break;

		case 3:
			cos = (int) ((DrawMan.SIZE * Math.cos(22.5) * Math.sqrt(2)) / 2);
			sin = (int) ((DrawMan.SIZE * Math.sin(22.5) * Math.sqrt(2)) / 2);

			g.drawLine(x + sin, y + cos, x - cos, y + sin);
			g.drawLine(x - cos, y + sin, x - sin, y - cos);
			g.drawLine(x - sin, y - cos, x + cos, y - sin);
			g.drawLine(x + cos, y - sin, x + sin, y + cos);
			break;
		}
	}
}
