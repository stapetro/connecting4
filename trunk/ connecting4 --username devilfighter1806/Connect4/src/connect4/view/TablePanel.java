package connect4.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TablePanel extends JPanel {

	private DrawMan[][] men;
	private MyFrame frame;
	private int sidewaysBuffer;
	private int upDownBuffer;
	private int tableSize;
	private RotatableSquare square;

	public TablePanel(MyFrame frame, int tableSize) {
		addMouseListener(new MouseListener() {

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
				if (e.getButton() == MouseEvent.BUTTON1) {
					getGraphics().drawString("left button!", 10, 10);
					rotateBoardRight();
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							for (int i = 0; i < 4; i++) {
								square.rotateRight();
								repaint();
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}).start();
					
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					getGraphics().drawString("right button!", 10, 10);
					rotateBoardLeft();
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							for (int i = 0; i < 4; i++) {
								square.rotateLeft();
								repaint();
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
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
		
		square = new RotatableSquare(30, 30);

		this.frame = frame;
		this.tableSize = tableSize;

		men = new DrawMan[tableSize][tableSize];
		sidewaysBuffer = (frame.wid - ((1 + tableSize) * DrawMan.SIZE)) / 2;
		upDownBuffer = (frame.hei - ((2 + tableSize) * DrawMan.SIZE)) / 2;

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

	private void rotate(boolean right) {
		Color[][] colors = new Color[tableSize][tableSize];
		boolean[][] visibility = new boolean[tableSize][tableSize];

		for (int i = 0; i < tableSize; i++) {
			for (int j = 0; j < tableSize; j++) {
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

	private void rotateBoardLeft() {
		rotate(false);
	}

	private void rotateBoardRight() {
		rotate(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		square.paint(g);

		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g.drawRect(sidewaysBuffer, upDownBuffer, tableSize * DrawMan.SIZE + 5,
				tableSize * DrawMan.SIZE + 5);
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
