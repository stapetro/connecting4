package connect4.view;

import java.awt.Color;
import java.awt.Graphics;

public class DrawMan {

	public static int SIZE = 25;

	private int x;
	private int y;
	private Color color;
	private boolean visible;

	public DrawMan(int x, int y) {
		this.x = x;
		this.y = y;

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
		g.fillOval(x, y, SIZE, SIZE);
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
}
