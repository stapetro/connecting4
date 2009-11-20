package com.ngc4.connect4.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.ngc4.connect4.view.menu.ContainerPanel;
import com.ngc4.connect4.view.menu.MenuContentPanel;
import com.ngc4.connect4.view.menu.MenuPanel;


/**
 * Starts the application.
 * @author Stanislav Petrov
 *
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private MenuPanel menuPanel;
	private MenuContentPanel menuContent;
	private ContainerPanel containerPnl;

	/**
	 * This is the default constructor
	 */
	public MainWindow() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(500, 500);
		this.setContentPane(getJContentPane());
		this.setTitle("NextGen Connect4");
		this.setResizable(false);
		menuContent = new MenuContentPanel();
		menuContent.setPreferredSize(new Dimension(350, 500));
		containerPnl = new ContainerPanel();
		add(containerPnl);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
		}
		return jContentPane;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainWindow gameMenuWindow = new MainWindow();
				gameMenuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				gameMenuWindow.setLocationRelativeTo(null);
				gameMenuWindow.setVisible(true);
			}
		});
	}

}
