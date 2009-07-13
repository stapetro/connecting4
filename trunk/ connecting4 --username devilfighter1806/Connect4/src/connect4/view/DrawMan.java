package connect4.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class DrawMan {

	public static int SIZE = 25;

	private Point upLeft;
	public Point paintPoint;
	private Color color;
	private boolean visible;

	public DrawMan(int x, int y) {
		upLeft = new Point(x, y);
		paintPoint = new Point(upLeft);

		if (System.nanoTime() % 2 == 0) {
			color = Color.RED;
		}
		if (System.nanoTime() % 2 == 0) {
			visible = true;
		}
	}

	public void drawMan(Graphics g) {
		Color lastColor = g.getColor();
		g.setColor(color);
		g.fillOval(paintPoint.x, paintPoint.y, SIZE, SIZE);
		g.setColor(lastColor);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean b) {
		visible = b;
	}

	public void setColor(Color c) {
		color = c;
	}

	public Color getColor() {
		return color;
	}

	public Point getUpLeft() {
		return upLeft;
	}

	public void restorePoint() {
		paintPoint.x = upLeft.x;
		paintPoint.y = upLeft.y;
	}
}
