package connect4.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * A square that rotates. 4 steps of animation. Remembers at what part of the
 * animation it is.
 * 
 * Currently the animation is UNUSED.
 * 
 * @author Leni
 * 
 */
public class RotatableSquare {

	private Point upLeft;
	public Point tempUpLeft;

	private Position position;
	private int size;

	public Point[] keyPositions;

	public RotatableSquare(int x, int y, int size) {
		this.size = size;
		upLeft = new Point(x, y);
		position = Position.DEGREE_45;
		tempUpLeft = new Point(upLeft);
		keyPositions = new Point[4];
	}

	@Deprecated
	public void rotateLeft() {
		position.i = (position.i + 1) % 4;
	}

	@Deprecated
	public void rotateRight() {
		position.i = (position.i - 1) % 4;
		if (position.i < 0) {
			position.i = 3;
		}
	}

	public void paint(Graphics g) {
		int x = upLeft.x + size / 2;
		int y = upLeft.y + size / 2;
		int cos = (int) ((size * Math.cos(Math.PI / 8) * Math.sqrt(2)) / 2);
		int sin = (int) ((size * Math.sin(Math.PI / 8) * Math.sqrt(2)) / 2);
		int sqr2 = (int) (size * Math.sqrt(2) / 2);

		for (int i = 0; i < 4; i++) {
			keyPositions[i] = new Point();
		}
		/*
		 * // 0 keyPositions[0].x = x + sqr2; keyPositions[0].y = y;
		 * 
		 * // 22 (nadqsno 1va stapka) keyPositions[1].x = x - sin;
		 * keyPositions[1].y = y - cos;
		 * 
		 * // 45 (nadqsno 2ra stapka) keyPositions[2].x = x; keyPositions[2].y =
		 * y - sqr2;
		 * 
		 * // 77 (nadqsno 3ta stapka) keyPositions[3].x = x + sin;
		 * keyPositions[3].y = y - cos;
		 */

		switch (position.i) {

		// 0
		case 0:
			// System.out.println("case 0");
			tempUpLeft.x = 0;
			tempUpLeft.y = 0;

			// g.drawString("case 0", 20, 20);

			g.setColor(Color.BLACK);
			g.drawLine(x + sqr2, y, x, y + sqr2);
			g.setColor(Color.RED);
			g.drawLine(x, y + sqr2, x - sqr2, y);
			g.setColor(Color.GREEN);
			g.drawLine(x - sqr2, y, x, y - sqr2);
			g.setColor(Color.BLUE);
			g.drawLine(x, y - sqr2, x + sqr2, y);
			break;

		// 22
		case 1:

			// g.drawString("case 1", 20, 20);

			// System.out.println("case 1");
			// tempUpLeft.x = upLeft.x + (int) (sqr2) / 2;
			// tempUpLeft.y = upLeft.y - (int) (sqr2) / 2;
			tempUpLeft.x = x - sin;
			tempUpLeft.y = x - cos;

			g.setColor(Color.BLACK);
			g.drawLine(x + sin, y + cos, x - cos, y + sin);
			g.setColor(Color.RED);
			g.drawLine(x - cos, y + sin, x - sin, y - cos);
			g.setColor(Color.GREEN);
			g.drawLine(x - sin, y - cos, x + cos, y - sin);
			g.setColor(Color.BLUE);
			g.drawLine(x + cos, y - sin, x + sin, y + cos);
			break;

		/**
		 * normal position - only this is used
		 * 
		 */
		// 45
		case 2:

			// g.drawString("case 2", 20, 20);

			// System.out.println("case 2");
			tempUpLeft.x = x - sin;
			tempUpLeft.y = y - cos;

			g.drawRect(upLeft.x, upLeft.y, size + 2, size + 2);
			break;

		// 77
		case 3:

			// System.out.println("case 3");

			tempUpLeft.x = upLeft.x - (int) (sqr2) / 2;
			tempUpLeft.y = upLeft.y + (int) (sqr2) / 2;

			g.drawString("case 3", 20, 20);

			g.setColor(Color.BLACK);
			g.drawLine(x + cos, y + sin, x - sin, y + cos);
			g.setColor(Color.RED);
			g.drawLine(x - sin, y + cos, x - cos, y - sin);
			g.setColor(Color.GREEN);
			g.drawLine(x - cos, y - sin, x + sin, y - cos);
			g.setColor(Color.BLUE);
			g.drawLine(x + sin, y - cos, x + cos, y + sin);
			break;
		}
	}

	public int getSize() {
		return size;
	}
}
