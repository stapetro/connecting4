package connect4.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class MyFrame extends JFrame {

	public int hei;
	public int wid;
	private TablePanel drawPanel;
	private StatusBarPanel statusBar;

	public MyFrame(int w, int h) {
		statusBar = new StatusBarPanel(getWidth(), getHeight() / 5);
		hei = h;
		wid = w;
		drawPanel = new TablePanel(wid, hei, 11);
		init();
	}

	private void init() {
		setLayout(new BorderLayout());
		add(drawPanel, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);

		setTitle("NextGen Connect4");
		setSize(hei, wid);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
