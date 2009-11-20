package connect4.view.gameplay;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import connect4.enums.Direction;

/**
 * The major JPanel which offers rich interface for handling the array of men,
 * grid and arrow to be displayed.
 * 
 * @author Leni Kirilov
 * 
 */

@SuppressWarnings("serial")
public class TablePanel extends JPanel {

	private Arrow arrow;
	private int arrowPos;

	private DrawMan[][] men;
	private int square_x_upLeftPoint;
	private int square_y_upLeftPoint;
	private int tableSize;
	private Square square;

	private int size = DrawMan.SIZE;

	// arrow positions - initiated in constructor
	private Point mostleftUP;
	private Point mostleftDOWN;
	private Point mostupLEFT;
	private Point mostupRIGHT;

	private Color player1Color;
	private DrawMan[] winningMen;

	public TablePanel(int wid, int hei, int tableSize, Color player1) {

		this.tableSize = tableSize;
		this.player1Color = player1;

		square_x_upLeftPoint = (wid - ((1 + tableSize) * size)) / 2;
		square_y_upLeftPoint = (hei - ((2 + tableSize) * size)) / 2;

		initVariables();
		initPoints();
	}

	private void initVariables() {
		this.arrow = new Arrow(DrawMan.SIZE, square_x_upLeftPoint
				+ DrawMan.SIZE * tableSize / 2, square_y_upLeftPoint - 3
				* DrawMan.SIZE / 2);

		square = new Square(square_x_upLeftPoint, square_y_upLeftPoint,
				tableSize * size + 3);

		arrowPos = tableSize / 2;

		/**
		 * creating men for all positions initially they are all invisible but
		 * the middle one
		 */
		men = new DrawMan[tableSize][tableSize];
		for (int i = 0; i < tableSize; i++) {
			for (int j = 0; j < tableSize; j++) {
				men[i][j] = new DrawMan(square_x_upLeftPoint + j * size + 3,
						square_y_upLeftPoint + i * size + 3);

				// marks the MIDDLE man
				if (i == j && j == (tableSize / 2)) {
					DrawMan man = men[i][j];
					man.setColor(player1Color);
					man.setVisible(true);
				}
			}
		}
	}

	private void initPoints() {
		mostleftUP = new Point(square_x_upLeftPoint + size / 2,
				square_y_upLeftPoint - 3 * size / 2);
		mostleftDOWN = new Point(mostleftUP.x, mostleftUP.y + size
				* (tableSize + 3));

		mostupLEFT = new Point(square_x_upLeftPoint - 3 * size / 2,
				square_y_upLeftPoint + size / 2);
		mostupRIGHT = new Point(mostupLEFT.x + size * (tableSize + 3),
				mostupLEFT.y);
	}

	/**
	 * This method makes the MAN that is located in the position pointed with
	 * the ManCombo object with the color specified. The Direction is used for
	 * the relocation of the arrow so that the next user sees where it was last
	 * played. The MAN becomes visible.
	 * 
	 * WARNING: No check for already VISIBLE man - the user must check that
	 * himself.
	 * 
	 * @param manPoint
	 *            - contains the position of the newly positioned man
	 * @param direction
	 *            - the direction from where the man came. The arrow is
	 *            relocated due to this parameter.
	 * @param color
	 *            - the man will be painted in this color.
	 */
	public void moveManTo(Point manPoint, Direction direction, Color color) {
		men[manPoint.x][manPoint.y].setColor(color);
		men[manPoint.x][manPoint.y].setVisible(true);

		int position = -1;

		switch (direction) {
		case HORIZONTAL_LEFT:
		case HORIZONTAL_RIGHT:
			position = manPoint.x;
			break;
		case VERTICAL_DOWN:
		case VERTICAL_UP:
			position = manPoint.y;
			break;
		}

		moveArrowTo(direction, position);
		repaint();
	}

	/**
	 * 
	 * Replaces the arrow to the new location. Position is index in array and
	 * the direction defines one of the four arrays.
	 * 
	 * @param direction
	 *            - on which side the arrow is
	 * @param position
	 *            - the exact position of the arrow
	 */
	public void moveArrowTo(Direction direction, int position) {
		if (position == -1) {
			System.out.println("BOOOOOM!");
		}

		switch (direction) {
		case HORIZONTAL_LEFT:
			arrowPos = tableSize - 1 - position;
			arrow.setStartPoint(new Point(mostupLEFT.x, mostupLEFT.y + size
					* position));
			arrow.setDirection(Direction.HORIZONTAL_RIGHT);
			break;
		case HORIZONTAL_RIGHT:
			arrowPos = tableSize - 1 - position;
			arrow.setStartPoint(new Point(mostupRIGHT.x, mostupRIGHT.y + size
					* position));
			arrow.setDirection(Direction.HORIZONTAL_LEFT);
			break;

		case VERTICAL_DOWN:
			arrowPos = position;
			arrow.setStartPoint(new Point(mostleftDOWN.x + size * position,
					mostleftDOWN.y));
			arrow.setDirection(Direction.VERTICAL_UP);
			break;
		case VERTICAL_UP:
			arrowPos = position;
			arrow.setStartPoint(new Point(mostleftUP.x + size * position,
					mostleftUP.y));
			arrow.setDirection(Direction.VERTICAL_DOWN);
			break;
		}
		repaint();
	}

	/**
	 * Method which shows the winning 4 consecutive MEN painting them in another
	 * color style - different from the player1 and player2 colors.
	 * 
	 * @param winningMenPositions
	 *            - combo of x and y coordinates of each winning MAN. Uses
	 *            Point.
	 * @param color
	 *            - the winning men will be painted in this color
	 */
	public void displayWinningCombination(Point[] winningMenPositions,
			Color color) {
		winningMen = new DrawMan[winningMenPositions.length];

		for (int i = 0; i < winningMenPositions.length; i++) {
			winningMen[i] = new DrawMan(
					men[winningMenPositions[i].x][winningMenPositions[i].y]
							.getUpLeft().x,
					men[winningMenPositions[i].x][winningMenPositions[i].y]
							.getUpLeft().y);
			winningMen[i].setOffset(4);
			winningMen[i].setVisible(true);
			winningMen[i].setColor(color);
		}
		repaint();
	}

	/**
	 * return position of arrow
	 * 
	 * @return int position the position in a grid that starts with 0,0
	 */
	public int acqurePosition() {
		if (arrow.getDirection() == Direction.HORIZONTAL_LEFT
				|| arrow.getDirection() == Direction.HORIZONTAL_RIGHT) {
			return tableSize - arrowPos - 1;
		} else {
			return arrowPos;
		}
	}

	/**
	 * returns transformed direction left -> right right -> left up -> down down
	 * ->up
	 * 
	 * UNSAFE if the direction of arrow is not one of the following (hard to
	 * happend but may be possible)
	 * 
	 * @return Direction direction of arrow
	 */
	public Direction acquireDirection() {
		switch (arrow.getDirection()) {
		case HORIZONTAL_LEFT:
			return Direction.HORIZONTAL_RIGHT;
		case HORIZONTAL_RIGHT:
			return Direction.HORIZONTAL_LEFT;
		case VERTICAL_DOWN:
			return Direction.VERTICAL_UP;
		case VERTICAL_UP:
			return Direction.VERTICAL_DOWN;
		default:
			// unsafe
			return arrow.getDirection();
		}
	}

	/**
	 * moves arrow to the left if it reaches the END - it changes position
	 * recalculates position based on the beginning of the grid with MEN and the
	 * size of a MAN
	 */
	public void arrowMoveLeft() {

		if (arrowPos == 0) {
			if (arrow.getDirection() == Direction.VERTICAL_DOWN) {
				arrowPos = tableSize - 1;
				arrow.setStartPoint(new Point(square_x_upLeftPoint - 3 * size
						/ 2, square_y_upLeftPoint + size / 2));
			} else {
				arrowPos = 0;
				arrow.setStartPoint(new Point(square_x_upLeftPoint - 3 * size
						/ 2, square_y_upLeftPoint + (tableSize - 1) * size
						+ size / 2));
			}
			arrow.setDirection(Direction.HORIZONTAL_RIGHT);
			return;
		}
		arrowPos--;
		arrow.setStartPoint(new Point(arrow.getStartPoint().x - size, arrow
				.getStartPoint().y));
	}

	/**
	 * moves arrow to the right the same as arrowMoveLeft
	 */
	public void arrowMoveRight() {
		if (arrowPos == tableSize - 1) {
			if (arrow.getDirection() == Direction.VERTICAL_DOWN) {
				arrowPos = tableSize - 1;
				arrow.setStartPoint(new Point(square_x_upLeftPoint + size
						* tableSize + size * 3 / 2, square_y_upLeftPoint + size
						/ 2));
			} else {
				arrowPos = 0;
				arrow.setStartPoint(new Point(square_x_upLeftPoint + size
						* tableSize + size * 3 / 2, square_y_upLeftPoint
						+ (tableSize - 1) * size + size / 2));
			}
			arrow.setDirection(Direction.HORIZONTAL_LEFT);
			return;
		}
		arrowPos++;
		arrow.setStartPoint(new Point(arrow.getStartPoint().x + size, arrow
				.getStartPoint().y));
	}

	/**
	 * moves the arrow UP
	 */
	public void arrowMoveUp() {
		if (arrowPos == tableSize - 1) {
			if (arrow.getDirection() == Direction.HORIZONTAL_LEFT) {
				arrowPos = tableSize - 1;
				arrow.setStartPoint(new Point(square_x_upLeftPoint
						+ (tableSize - 1) * size + size / 2,
						square_y_upLeftPoint - 3 * size / 2));
			} else {
				arrowPos = 0;
				arrow.setStartPoint(new Point(square_x_upLeftPoint + size / 2,
						square_y_upLeftPoint - 3 * size / 2));
			}
			arrow.setDirection(Direction.VERTICAL_DOWN);
			return;
		}
		arrowPos++;
		arrow.setStartPoint(new Point(arrow.getStartPoint().x, arrow
				.getStartPoint().y
				- size));
	}

	/**
	 * moves the arrow DOWN
	 */
	public void arrowMoveDown() {
		if (arrowPos == 0) {
			if (arrow.getDirection() == Direction.HORIZONTAL_LEFT) {
				arrowPos = tableSize - 1;
				arrow.setStartPoint(new Point(square_x_upLeftPoint
						+ (tableSize) * size - size / 2, square_y_upLeftPoint
						+ (tableSize) * size + 3 * size / 2));
			} else {
				arrowPos = 0;
				arrow.setStartPoint(new Point(square_x_upLeftPoint + size / 2,
						square_y_upLeftPoint + (tableSize) * size + 3 * size
								/ 2));
			}
			arrow.setDirection(Direction.VERTICAL_UP);
			return;
		}
		arrowPos--;
		arrow.setStartPoint(new Point(arrow.getStartPoint().x, arrow
				.getStartPoint().y
				+ size));
	}

	/**
	 * Painting numbers for grid for table. They move away from arrow as arrow
	 * moves around the table.
	 * 
	 * @param g
	 *            - graphics object to paint the numbers
	 */
	public void paintNumbers(Graphics g) {
		for (int i = 0; i < tableSize; i++) {
			if (arrow.getDirection() != Direction.VERTICAL_DOWN) {
				g.drawString(String.valueOf(i + 1), mostleftUP.x + i * size,
						mostleftUP.y);
			} else {
				g.drawString(String.valueOf(i + 1), mostleftDOWN.x + i * size,
						mostleftDOWN.y);
			}
			if (arrow.getDirection() != Direction.HORIZONTAL_RIGHT) {
				g.drawString(String.valueOf(i + 1), mostupLEFT.x, mostupLEFT.y
						+ i * size);
			} else {
				g.drawString(String.valueOf(i + 1), mostupRIGHT.x,
						mostupRIGHT.y + i * size);
			}
		}
	}

	/**
	 * paints the square, the arrow and all MEN
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		square.paint(g);
		arrow.paint(g);
		paintNumbers(g);

		for (DrawMan[] manRow : men) {
			for (DrawMan man : manRow) {
				man.drawMan(g);
			}
		}

		if (winningMen != null) {
			for (DrawMan man : winningMen) {
				man.drawMan(g);
			}
		}

	}

}
