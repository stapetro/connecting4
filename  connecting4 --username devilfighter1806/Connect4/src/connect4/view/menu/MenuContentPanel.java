package connect4.view.menu;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Represents content associated with the relevant menu item.
 * @author Stanislav Petrov
 */
//TODO To be impelemented.
public class MenuContentPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public MenuContentPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(350, 500);
		this.setLayout(new BorderLayout());
	}

}
