package connect4.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TablePanel extends JPanel {

	private int w;
	private int h;

	private DrawMan[][] men;
	private int sidewaysBuffer;
	private int upDownBuffer;
	private int tableSize;
	private RotatableSquare square;

	public TablePanel(int wid, int hei, int tableSize) {
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

					new Thread(new Runnable() {
						@Override
						public void run() {
							for (int i = 0; i < 4; i++) {
								square.rotateRight();
								rotateBoardRight(i);
								repaint();
								try {
									Thread.sleep(100);
								} catch (InterruptedException ex) {
									ex.printStackTrace();
								}
							}
							animating = false;
						}
					}).start();

				} else if (e.getButton() == MouseEvent.BUTTON3) {
					getGraphics().drawString("right button!", 10, 10);

					new Thread(new Runnable() {
						@Override
						public void run() {
							for (int i = 0; i < 4; i++) {
								square.rotateLeft();
								rotateBoardLeft(i);
								repaint();
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

							animating = false;
						}
					}).start();
				}

				try {
					Thread.sleep(50);
					repaint();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});

		this.tableSize = tableSize;

		w = wid;
		h = hei;

		men = new DrawMan[tableSize][tableSize];
		sidewaysBuffer = (w - ((1 + tableSize) * DrawMan.SIZE)) / 2;
		upDownBuffer = (h - ((2 + tableSize) * DrawMan.SIZE)) / 2;

		square = new RotatableSquare(sidewaysBuffer, upDownBuffer, tableSize
				* DrawMan.SIZE + 3);

		for (int i = 0; i < tableSize; i++) {
			for (int j = 0; j < tableSize; j++) {
				men[i][j] = new DrawMan(sidewaysBuffer + j * DrawMan.SIZE + 3,
						upDownBuffer + i * DrawMan.SIZE + 3);

				// marks the MIDDLE man
				if (i == j && j == (tableSize / 2)) {
					DrawMan man = men[i][j];
					man.setColor(Color.BLACK);
					man.setVisible(true);
				}
			}
		}
	}

	private void rotate(boolean right, int step) {
		if (right) {

			switch (step) {
			case 0: // 22

				System.out.println("rotate 22");
				rotate22(right);
				break;

			case 1: // 0
				System.out.println("rotate 0");
				rotate0(right);
				break;

			case 2: // 77
				System.out.println("rotate 77");
				rotate77();
				break;

			case 3: // 45
				System.out.println("rotate 45");
				rotate45(right);
				break;
			}
		} else {

			switch (step) {
			case 0: // 77
				System.out.println("rotate 77");
				rotate77();
				break;

			case 1: // 0
				System.out.println("rotate 0");
				rotate0(right);
				break;

			case 2: // 22
				System.out.println("rotate 22");
				rotate22(right);
				break;

			case 3: // 45
				System.out.println("rotate 45");
				rotate45(right);
				break;
			}
		}
	}

	private void rotate0(boolean right) {
		double sqr2 = Math.sqrt(2);

		Point temp = new Point(square.tempUpLeft);

		if (right) {
			for (int i = 0; i < tableSize; i++) {
				temp.x += (int) (DrawMan.SIZE * sqr2 / 2);
				temp.y += (int) (DrawMan.SIZE * sqr2);

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

	// OSTAVA V POSOKA NALQVO I POSLEDNATA POZICIQ
	// parvi problemi - leko razminavane v aglite na zavartane...
	// ne se vijda pri visoka skorost na animaciq, no bavno - da
	private void rotate22(boolean right) {
		Point temp = new Point(square.tempUpLeft);

		double sqr2 = Math.sqrt(2);
		int size = DrawMan.SIZE;
		int cos = (int) (size * Math.cos(Math.PI / 8));
		int sin = (int) (size * Math.sin(Math.PI / 8));

		if (right) {
			for (int i = 0; i < tableSize; i++) {
				System.out.println(temp);
				temp.x += (int) (cos);
				temp.y -= 3 * (int) (sin);

				for (int j = 0; j < tableSize; j++) {
					men[i][j].paintPoint.x = (int) (temp.x - sin * sqr2 / 2
							- size * sqr2 / 2 - 4);
					men[i][j].paintPoint.y = (int) (temp.y + cos * sqr2 / 2
							+ size * sqr2 / 2 + 4);

					temp.x += (int) (cos);
					temp.y += (int) (sin);
				}
				temp.x = (int) (square.tempUpLeft.x - (i + 1) * sin) + 1;
				temp.y = (int) (square.tempUpLeft.y + (i + 1) * cos) + 1;
			}
		} else {

		}
	}

	// final position of men (NORMAL position
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

	private void rotate77() {

	}

	private void rotateBoardLeft(int step) {
		rotate(false, step);
	}

	private void rotateBoardRight(int step) {
		rotate(true, step);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		square.paint(g);

		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		// g.drawRect(sidewaysBuffer, upDownBuffer, tableSize * DrawMan.SIZE +
		// 5,
		// tableSize * DrawMan.SIZE + 5);
		g.setColor(new Color(0, 150, 100));

		for (DrawMan[] manRow : men) {
			for (DrawMan man : manRow) {
				if (man.isVisible()) {
					man.drawMan(g);
				}
			}
		}
	}
}
