package connect4.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

/**
 * test class
 * 
 * @author Leni
 * 
 */
public class MyFrame extends JFrame {

	public int hei;
	public int wid;
	private TablePanel drawPanel;
	private StatusBarPanel statusBar;

	public MyFrame(int w, int h) {
		statusBar = new StatusBarPanel();
		hei = h;
		wid = w;
		drawPanel = new TablePanel(wid, hei, 17, Color.RED);
		init();
	}

	private void init() {
//		setLayout(new BorderLayout());
		add(drawPanel, BorderLayout.CENTER);
//		drawPanel.setLayout(new BorderLayout());
		add(statusBar, BorderLayout.SOUTH);

		setTitle("NextGen Connect4");
		setSize(hei, wid);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		statusBar.setStatus("bla", Color.cyan);
	}

}
