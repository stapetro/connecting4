package connect4.view.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Represents content associated with the relevant menu item.
 * @author Stanislav Petrov
 */
//TODO To be impelemented.
public class MenuContentPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel welcomeLbl;
	private JLabel gameNameLbl;

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
		gameNameLbl = new JLabel("    Next Gen Connect 4");
		gameNameLbl.setPreferredSize(new Dimension(getWidth(), getHeight()));
        gameNameLbl.setFont(new java.awt.Font("Georgia", 1, 26));
		this.add(gameNameLbl, BorderLayout.CENTER);
	}

}
