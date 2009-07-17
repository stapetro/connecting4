package connect4.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Class that will display messages that will notify the player of the current
 * state of the game.
 * 
 * @author Leni
 * 
 */
public class StatusBarPanel extends JPanel {

	private String message;

	public StatusBarPanel(int x, int y) {
		setMinimumSize(new Dimension(x, y));
		message = "";
	}

	/**
	 * Sets new 
	 * @param newMessage
	 */
	public void showStatus(String newMessage) {
		message = newMessage;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);

		g.drawLine(0, 0, getWidth(), 0);
		g.setColor(Color.RED);
		g.drawString(message, getWidth() / 2 - message.length() * 2, 10);
	}
}
