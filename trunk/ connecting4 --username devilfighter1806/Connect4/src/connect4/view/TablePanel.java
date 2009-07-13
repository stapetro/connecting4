package connect4.view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TablePanel extends JPanel {

	private Collection<DrawMan> men;
	private MyFrame frame;

	public TablePanel(MyFrame frame, int tableSize) {
		this.frame = frame;

		men = new ArrayList<DrawMan>();
		int sidewaysBuffer = (frame.wid - ((1 + tableSize) * DrawMan.SIZE)) / 2;
		int upDownBuffer = (frame.hei - ((2 + tableSize) * DrawMan.SIZE)) / 2;

		for (int i = 0; i < tableSize; i++) {
			for (int j = 0; j < tableSize; j++) {
				men.add(new DrawMan(/*this,*/
						sidewaysBuffer + j * DrawMan.SIZE + 3, upDownBuffer + i
								* DrawMan.SIZE + 3));
				
			// marks the MIDDLE man
				if(i==j && j==(tableSize/2)){
					DrawMan man = null;
					for(DrawMan man2: men){
					man = man2;
					}
					man.setColor(Color.BLACK);
				}
			
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(new Color(0, 150, 100));
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		for (DrawMan man : men) {
			if (man.isVisible()) {
				man.drawMan(g);
			}
		}
	}

}
