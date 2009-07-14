package connect4.view.menu;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;

/**
 * Tests the game menu.
 * @author User
 *
 */
public class TestGameMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private MenuPanel menuPanel;
	private MenuContentPanel menuContent;
	private ContainerPanel containerPnl;

	/**
	 * This is the default constructor
	 */
	public TestGameMenu() {
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
		this.setTitle("Test game menu");
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
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TestGameMenu gameMenuWindow = new TestGameMenu();
				gameMenuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				gameMenuWindow.setLocationRelativeTo(null);
				gameMenuWindow.setVisible(true);
			}
		});
	}

}
