package connect4.view.menu;

import java.awt.GridBagLayout;
import javax.swing.JPanel;

public class GameMenuContainerPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private GameMenuPanel menuPanel;
	private GameMenuContentPanel menuContentPanel;

	/**
	 * This is the default constructor
	 */
	public GameMenuContainerPanel() {
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
		this.setLayout(new GridBagLayout());
		menuPanel = new GameMenuPanel();
		menuContentPanel = new GameMenuContentPanel();
		add(menuPanel);
		add(menuContentPanel);
	}

}
