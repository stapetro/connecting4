package connect4.view.menu;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

/**
 * Connects menu panel with menu content panel, or represents game panels.
 * 
 * @author Stanislav Petrov
 * 
 */
/*
 * Only this panel should be added to the Main window. This panel holds all
 * child panels, i.e. table panel, etc.
 */
public class ContainerPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private MenuPanel menuPanel;
	private MenuContentPanel menuContentPanel;

	/**
	 * This is the default constructor
	 */
	public ContainerPanel() {
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
		this.setLayout(new BorderLayout());
		this.setFocusable(false);
		menuContentPanel = new MenuContentPanel();
		menuPanel = new MenuPanel(menuContentPanel, this);
		this.add(menuPanel, BorderLayout.WEST);
		this.add(menuContentPanel, BorderLayout.EAST);
	}

}
