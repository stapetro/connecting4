package connect4.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Class that represents a MAN that is to be displayed. The MAN is unique with
 * it's position defined by a POINT. The MAN has a color and boolean variable
 * visible that should be checked when displayed.
 * 
 * @author Leni
 * 
 */
public class DrawMan {

	public static int SIZE = 25;

	/**
	 * stores secure and original position of MAN
	 */
	private Point upLeft;
	/**
	 * paintPoint is working copy of upLeft point
	 */
	public Point paintPoint;
	private Color color;
	private boolean visible;

	/**
	 * added value to reduce the radius of the
	 */
	private int offset = 0;

	public DrawMan(int x, int y) {
		color = color.BLACK;
		upLeft = new Point(x, y);
		paintPoint = new Point(upLeft);
	}

	/**
	 * Draws the MAN in component
	 * 
	 * @param g
	 *            - graphics object used for painting the man
	 */
	public void drawMan(Graphics g) {
		if (visible) {
			Color lastColor = g.getColor();
			g.setColor(color);
			g.fillOval(paintPoint.x, paintPoint.y, SIZE - offset, SIZE - offset);
			g.setColor(lastColor);
		}
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

	/**
	 * restores the point of the MAN with the originally defined point RECOVERY
	 * METHOD - old value of POINT is lost!
	 */
	public void restorePoint() {
		paintPoint.x = upLeft.x;
		paintPoint.y = upLeft.y;
	}

	/**
	 * Changes the offset that makes the radius smaller for the winning DrawMan.
	 * 
	 * @param offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
		paintPoint.x += offset / 2;
		paintPoint.y += offset / 2;
	}
}
