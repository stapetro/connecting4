package connect4.view;

import java.awt.Color;
import java.awt.Graphics;

public class DrawMan {

	public static int SIZE = 25;

//	private TablePanel drawPanel;
	private int x;
	private int y;
	private Color color;
	private boolean visible;

	public DrawMan(/*TablePanel panel,*/ int x, int y) {
//		drawPanel = panel;
		this.x = x;
		this.y = y;

		visible = true;

		if (System.nanoTime() % 2 == 0) {
			color = Color.RED;
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
}
