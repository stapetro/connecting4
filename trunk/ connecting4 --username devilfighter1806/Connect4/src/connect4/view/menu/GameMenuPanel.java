package connect4.view.menu;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JMenu;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JTree;
import javax.swing.JSlider;

/**
 * Represents game menu panel which visualize all menu items.
 * 
 * @author Stanislav Petrov
 */
public class GameMenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	/**
	 * Stores all menu and sub menu items.
	 */
	private final GameMenuItem[] ITEMS;
	/**
	 * Stores the current menu items to be presented.
	 */
	private JButton[] currMenuItems;

	/**
	 * Initializes current menu items.
	 */
	public GameMenuPanel() {
		super();
		ITEMS = GameMenuItem.values();
		initialize();
		initMenuItems();
	}

	/**
	 * Initializes current menu items and adds action listeners to each menu
	 * item.
	 */
	private void initMenuItems() {
		currMenuItems = new JButton[GameMenuItem.MENU_ITEMS_NUMBER];
		for (int i = 0; i < currMenuItems.length; i++) {
			currMenuItems[i] = new JButton(ITEMS[i].toString());
			currMenuItems[i]
					.addActionListener(new ItemActionListener(ITEMS[i]));
			this.add(currMenuItems[i]);
		}
	}

	/**
	 * Initializes game menu panel and its components.
	 */
	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.setRows(5);
		this.setLayout(gridLayout);
		this.setSize(150, 500);
	}

	/**
	 * Represents item action listener. Wraps all actions for all menu items.
	 * 
	 * @author Stanislav Petrov
	 */
	private class ItemActionListener implements ActionListener {

		/**
		 * Stores the menu item for which action should be performed.
		 */
		private GameMenuItem item;

		public ItemActionListener(GameMenuItem item) {
			this.item = item;
		}

		/**
		 * Implements item action.
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			addAcitonToItem(item);
		}

		/**
		 * Adds action to the specified menu item.
		 * 
		 * @param item
		 *            The specified menu item.
		 */
		private void addAcitonToItem(GameMenuItem item) {
			switch (item) {
			case SINGLE_PLAYER:
				break;
			case MULTI_PLAYER:
				break;
			case OPTIONS: {
				selectOptionsItem();
				break;
			}
			case CREDITS:
				break;
			case EXIT:
				System.exit(0);
				break;
			case BACK: {
				removeAll();
				updateUI();
				initMenuItems();
				break;
			}
			}
		}

		/**
		 * Selects options menu item, initializes the new current menu items and
		 * visualizes them on the game menu panel.
		 */
		private void selectOptionsItem() {
			removeAll();
			updateUI();
			currMenuItems = new JButton[item.getSubMenu().length];
			for (int i = 0; i < currMenuItems.length; i++) {
				currMenuItems[i] = new JButton(ITEMS[item.getSubMenu()[i]]
						.toString());
				currMenuItems[i].addActionListener(new ItemActionListener(
						ITEMS[item.getSubMenu()[i]]));
				add(currMenuItems[i]);
			}
		}
	}

} // @jve:decl-index=0:visual-constraint="10,18"
