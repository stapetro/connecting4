package connect4.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class StatusBarPanel extends JPanel {

	public StatusBarPanel(int x, int y) {
		setMinimumSize(new Dimension(x, y));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);

		g.setColor(Color.RED);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g.drawString("STATUS BAR", getWidth() / 2, 10);
	}
}
