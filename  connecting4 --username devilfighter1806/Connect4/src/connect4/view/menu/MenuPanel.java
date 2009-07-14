package connect4.view.menu;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

import connect4.controller.menu.MenuController;
import connect4.view.MyFrame;
import connect4.view.TablePanel;

/**
 * Represents game menu panel which visualize all menu items.
 * 
 * @author Stanislav Petrov
 */
public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	/**
	 * Stores all menu and sub menu items.
	 */
	private final MenuItem[] ITEMS;
	/**
	 * Stores the current menu items to be presented.
	 */
	private JButton[] currMenuItems;
	/**
	 * Stores reference to the menu controller.
	 */
	private MenuController menuController;

	/**
	 * Initializes current menu items.
	 */
	public MenuPanel(MenuContentPanel contentPnl, ContainerPanel containerPnl) {
		super();
		ITEMS = MenuItem.values();
		menuController = new MenuController(contentPnl, containerPnl);
		initialize();
		initMenuItems();
	}

	/**
	 * Initializes current menu items and adds action listeners to each menu
	 * item.
	 */
	private void initMenuItems() {
		currMenuItems = new JButton[MenuItem.MENU_ITEMS_NUMBER];
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
	public class ItemActionListener implements ActionListener {

		/**
		 * Stores the menu item for which action should be performed.
		 */
		private MenuItem item;

		public ItemActionListener(MenuItem item) {
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
		private void addAcitonToItem(MenuItem item) {
			switch (item) {
			case SINGLE_PLAYER:
				menuController.addContentToContainer(new TablePanel(500, 500, 11));
				break;
			case MULTI_PLAYER:
				selectItem();
				break;
			case OPTIONS: {
				selectItem();
				break;
			}
			case CREDITS:
				break;
			case CONFIGURE_GAME: {
				menuController.setContent(new ConfigurationPanel());
				break;
			}
			case EXIT:
				System.exit(0);
				break;
			case BACK: {
				removeAll();
				menuController.clearContent();
				initMenuItems();
				updateUI();
				break;
			}
			}
		}

		/**
		 * Selects options menu item, initializes the new current menu items and
		 * visualizes them on the game menu panel.
		 */
		private void selectItem() {
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
