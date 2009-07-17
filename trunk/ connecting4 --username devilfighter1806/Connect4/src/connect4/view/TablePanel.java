package connect4.view;

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

import connect4.controller.Direction;

@SuppressWarnings("serial")
public class TablePanel extends JPanel {

	private Arrow arrow;
	private int arrowPos;

	private DrawMan[][] men;
	private int square_x_upLeftPoint;
	private int square_y_upLeftPoint;
	private int tableSize;
	private RotatableSquare square;

	private int size = DrawMan.SIZE;

	// arrow positions - initiated in constructor
	private Point mostleftUP;
	private Point mostleftDOWN;
	private Point mostupLEFT;
	private Point mostupRIGHT;

	private Color player1Color;

	public TablePanel(int wid, int hei, int tableSize, Color player1) {

		this.tableSize = tableSize;
		this.player1Color = player1;

		square_x_upLeftPoint = (wid - ((1 + tableSize) * size)) / 2;
		square_y_upLeftPoint = (hei - ((2 + tableSize) * size)) / 2;

		initVariables();
		initPoints();
		addMyMouseListener();
	}

	private void initVariables() {
		this.arrow = new Arrow(DrawMan.SIZE, square_x_upLeftPoint
				+ DrawMan.SIZE * tableSize / 2, square_y_upLeftPoint - 3
				* DrawMan.SIZE / 2);

		square = new RotatableSquare(square_x_upLeftPoint,
				square_y_upLeftPoint, tableSize * size + 3);

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
	 * color - different from the player1 and player2 colors.
	 * 
	 * @param winningMenPositions
	 *            - combo of x and y coordinates of each winning MAN. Uses
	 *            Point.
	 * @param color
	 *            - the winning men will be painted in this color
	 */
	public void displayWinningCombination(Point[] winningMenPositions,
			Color color) {
		for (Point manPoint : winningMenPositions) {
			men[manPoint.x][manPoint.y].setColor(color);
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
	 * the panel reacts to LEFT and RIGHT click left click moves the arrow to
	 * the LEFT or DOWN right click moves the arrow to the RIGHT or UP
	 * (depending on the position
	 */
	private void addMyMouseListener() {

		addMouseListener(new MouseListener() {

			private boolean animating;

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				action(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			private void action(MouseEvent e) {
				if (!animating) {
					animating = true;
				} else {
					return;
				}
				if (e.getButton() == MouseEvent.BUTTON1) {
					getGraphics().drawString("left button!", 10, 10);

					Direction dir = arrow.getDirection();
					switch (dir) {
					case VERTICAL_DOWN:
					case VERTICAL_UP:
						arrowMoveLeft();
						break;

					case HORIZONTAL_LEFT:
					case HORIZONTAL_RIGHT:
						arrowMoveDown();
						break;
					}

					// new Thread(new Runnable() {
					// @Override
					// public void run() {
					// for (int i = 0; i < 4; i++) {
					// square.rotateRight();
					// rotateBoardRight(i);
					// repaint();
					// try {
					// Thread.sleep(1000);
					// } catch (InterruptedException ex) {
					// ex.printStackTrace();
					// }
					// }

					try {
						Thread.sleep(50);
					} catch (Exception ex) {
					}
					animating = false;
					// }
					// }).start();

				} else if (e.getButton() == MouseEvent.BUTTON3) {
					getGraphics().drawString("right button!", 10, 10);

					// moveArrowTo(Direction.HORIZONTAL_RIGHT, 2);

					Random g = new Random(System.nanoTime());
					moveManTo(new Point(g.nextInt(tableSize), g
							.nextInt(tableSize)), acquireDirection(),
							Color.PINK);

					// Direction dir = arrow.getDirection();
					// switch (dir) {
					// case VERTICAL_DOWN:
					// case VERTICAL_UP:
					// arrowMoveRight();
					// break;
					//
					// case HORIZONTAL_LEFT:
					// case HORIZONTAL_RIGHT:
					// arrowMoveUp();
					// break;
					// }

					//					
					//					
					// // new Thread(new Runnable() {
					// // @Override
					// //// public void run() {
					// // for (int i = 0; i < 4; i++) {
					// // square.rotateLeft();
					// // rotateBoardLeft(i);
					// // repaint();
					// // try {
					// // Thread.sleep(1000);
					// // } catch (InterruptedException e) {
					// // e.printStackTrace();
					// // }
					// // }
					// //

					try {
						Thread.sleep(50);
					} catch (Exception ex) {
					}
					animating = false;

					// }
					// }).start();
				}

				try {
					Thread.sleep(50);
					repaint();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
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
	 * paints the square, the arrow and all MEN
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		square.paint(g);
		arrow.paint(g);

		for (DrawMan[] manRow : men) {
			for (DrawMan man : manRow) {
				man.drawMan(g);
			}
		}
	}

	@Deprecated
	private void rotate(boolean right, int step) {
		if (right) {

			switch (step) {
			case 0: // 22

				System.out.println("rotate 22");
				rotate22(right);
				break;

			case 1: // 0 System.out.println("rotate 0"); rotate0(right); break;

			case 2: // 77 System.out.println("rotate 77"); rotate77(right);
				// break;

			case 3: // 45 System.out.println("rotate 45"); rotate45(right);
				// break; }
			}
		} else {

			switch (step) {
			case 0: // 77 System.out.println("rotate 77");
				rotate77(right);
				break;

			case 1: // 0 System.out.println("rotate 0"); rotate0(right); break;

			case 2: // 22 System.out.println("rotate 22"); rotate22(right);
				// break;

			case 3: // 45 System.out.println("rotate 45"); rotate45(right);
				// break; }
			}
		}
	}

	@Deprecated
	private void rotate0(boolean right) {
		double sqr2 = Math.sqrt(2);

		Point temp = new Point(square.tempUpLeft);
		System.out.println(temp);
		System.out.println(square.keyPositions[1]);

		if (right) {
			for (int i = 0; i < tableSize; i++) {
				temp.x += 3 * (int) (DrawMan.SIZE * sqr2 / 2);
				temp.y -= (int) (DrawMan.SIZE * sqr2);

				for (int j = 0; j < tableSize; j++) {
					men[i][j].paintPoint.x = temp.x + DrawMan.SIZE / 2 + 3;
					men[i][j].paintPoint.y = temp.y + 5
							- (int) ((1 - sqr2) * DrawMan.SIZE / 2)
							+ DrawMan.SIZE / 4;

					temp.x += (int) (DrawMan.SIZE * sqr2 / 2);
					temp.y += (int) (DrawMan.SIZE * sqr2 / 2);

				}
				temp.x = (int) (square.tempUpLeft.x - (i + 1) * DrawMan.SIZE
						* sqr2 / 2) + 2;
				temp.y = (int) (square.tempUpLeft.y + (i + 1) * DrawMan.SIZE
						* sqr2 / 2) + 2;
			}

		} else {
			for (int i = 0; i < tableSize; i++) {
				temp.x += (int) (DrawMan.SIZE * sqr2);
				temp.y += (int) (DrawMan.SIZE * sqr2 / 2);

				for (int j = 0; j < tableSize; j++) {
					men[i][j].paintPoint.x = temp.x + DrawMan.SIZE / 2 + 4;
					men[i][j].paintPoint.y = temp.y + 7
							+ (int) ((1 - sqr2) * DrawMan.SIZE / 2)
							+ DrawMan.SIZE / 4;

					temp.x += (int) (DrawMan.SIZE * sqr2 / 2);
					temp.y -= (int) (DrawMan.SIZE * sqr2 / 2);

				}
				temp.x = (int) (square.tempUpLeft.x + (i + 1) * DrawMan.SIZE
						* sqr2 / 2) + 2;
				temp.y = (int) (square.tempUpLeft.y + (i + 1) * DrawMan.SIZE
						* sqr2 / 2) + 2;
			}
		}
	}

	// OSTAVA POSLEDNATA POZICIQ // parvi problemi - leko razminavane

	@Deprecated
	private void rotate22(boolean right) {
		Point temp = new Point(square.tempUpLeft);

		double sqr2 = Math.sqrt(2);
		int size = DrawMan.SIZE;
		int cos = (int) (size * Math.cos(Math.PI / 8));
		int sin = (int) (size * Math.sin(Math.PI / 8));

		if (right) {
			for (int i = 0; i < tableSize; i++) {
				temp.x += (int) (cos);
				temp.y -= 3 * (int) (sin);

				for (int j = 0; j < tableSize; j++) {
					men[i][j].paintPoint.x = (int) (temp.x - sin * sqr2 / 2
							- size * sqr2 / 2 - 5);
					men[i][j].paintPoint.y = (int) (temp.y + cos * sqr2 / 2
							+ size * sqr2 / 2 + 3);

					temp.x += (int) (cos);
					temp.y += (int) (sin);
				}
				temp.x = (int) (square.tempUpLeft.x - (i + 1) * sin) + 1;
				temp.y = (int) (square.tempUpLeft.y + (i + 1) * cos) + 1;
			}
		} else {
			for (int i = 0; i < tableSize; i++) {
				temp.x += (int) (8 * cos);
				temp.y += (int) (sin);

				for (int j = 0; j < tableSize; j++) {
					men[j][tableSize - i - 1].paintPoint.x = (int) (temp.x
							- sin * sqr2 / 2 - size * sqr2 / 2 - 2);
					men[j][tableSize - i - 1].paintPoint.y = (int) (temp.y
							+ cos * sqr2 / 2 + size * sqr2 / 2 + 12);

					temp.x += (int) (cos);
					temp.y += (int) (sin);
				}
				temp.x = (int) (square.tempUpLeft.x - (i + 1) * sin) + 1;
				temp.y = (int) (square.tempUpLeft.y + (i + 1) * cos) + 1;

			}
		}
	}

	// final position of men (NORMAL position
	@Deprecated
	private void rotate45(boolean right) {
		Color[][] colors = new Color[tableSize][tableSize];
		boolean[][] visibility = new boolean[tableSize][tableSize];

		for (int i = 0; i < tableSize; i++) {
			for (int j = 0; j < tableSize; j++) {
				men[i][j].restorePoint();
				colors[i][j] = men[i][j].getColor();
				visibility[i][j] = men[i][j].isVisible();
			}
		}

		for (int i = 0; i < tableSize; i++) {
			for (int j = 0; j < tableSize; j++) {
				int a = tableSize - j - 1;
				if (right) {
					men[i][a].setColor(colors[j][i]);
					men[i][a].setVisible(visibility[j][i]);
				} else {
					men[j][i].setColor(colors[i][a]);
					men[j][i].setVisible(visibility[i][a]);
				}
			}
		}
	}

	@Deprecated
	private void rotate77(boolean right) {
		Point temp = new Point(square.tempUpLeft);

		double sqr2 = Math.sqrt(2);
		int size = DrawMan.SIZE;
		int cos = (int) (size * Math.cos(Math.PI / 8));
		int sin = (int) (size * Math.sin(Math.PI / 8));

		if (!right) {
			System.out.println(temp);
			for (int i = 0; i < tableSize; i++) {
				temp.x -= (int) (cos);
				temp.y += (int) (sin);

				for (int j = 0; j < tableSize; j++) {
					men[tableSize - 1 - j][i].paintPoint.x = (int) (temp.x
							+ sin * sqr2 / 2 - size * sqr2 / 2);
					men[tableSize - 1 - j][i].paintPoint.y = (int) (temp.y
							- cos * sqr2 / 2 + size * sqr2 / 2);

					temp.x += (int) (cos);
					temp.y += (int) (sin);
				}
				temp.x = (int) (square.tempUpLeft.x - (i + 1) * sin) + 1;
				temp.y = (int) (square.tempUpLeft.y + (i + 1) * cos) + 1;
			}
		} else {
			// for (int i = 0; i < tableSize; i++) {
			// temp.x += (int) (8 * cos);
			// temp.y += (int) (sin);
			// for (int j = 0; j < tableSize; j++) { //
			// men[j][tableSize -
			// i - 1].paintPoint.x = (int) (temp.x // - sin * sqr2 / 2 - size *
			// sqr2
			// / 2
			// - 2); // men[j][tableSize - i - 1].paintPoint.y = (int) (temp.y
			// // +
			// cos
			// * sqr2 / 2 + size * sqr2 / 2 + 12); // // temp.x += (int) (cos);
			// //
			// temp.y += (int) (sin); // } // temp.x = (int)
			// (square.tempUpLeft.x -
			// (i +
			// 1) * sin) + 1; // temp.y = (int) (square.tempUpLeft.y + (i + 1) *
			// cos) +
			// 1; // // } }
		}
	}

	@Deprecated
	private void rotateBoardLeft(int step) {
		rotate(false, step);
	}

	@Deprecated
	private void rotateBoardRight(int step) {
		rotate(true, step);
	}

}
