package connect4.view.menu;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import connect4.controller.GameMode;
import connect4.controller.GamePlay;
import connect4.controller.menu.MenuController;
import connect4.model.GamePlayers;
import connect4.model.GameProperties;
import connect4.view.TablePanel;
import connect4.view.multiplayer.HostGamePanel;
import connect4.view.multiplayer.JoinGamePanel;

/**
 * Represents game menu panel which visualize all menu items.
 * 
 * @author Stanislav Petrov
 */
public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private GamePlay gamePlay;
	/**
	 * Stores all menu and sub menu items.
	 */
	private final MenuItem[] ITEMS;
	private final GameProperties[] BOARD_SIZES;
	/**
	 * Stores the current menu items to be presented.
	 */
	private JButton[] currMenuItems;
	private JButton[] previousMenuItems;
	/**
	 * Stores reference to the menu controller.
	 */
	private MenuController menuController;
	private TablePanel tablePnl;
	private ConfigurationPanel configurationPnl;
	private HostGamePanel hostGamePnl;
	private JoinGamePanel joinGamePnl;
	private int boardSize;

	/**
	 * Initializes current menu items.
	 */
	public MenuPanel(MenuContentPanel contentPnl, ContainerPanel containerPnl) {
		super();
		gamePlay = new GamePlay(GameMode.SINGLE_PLAYER, GameProperties.SIZE_9
				.getSize());
		BOARD_SIZES = GameProperties.values();
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
			currMenuItems[i] = new JButton((ITEMS[i]).toString());
			currMenuItems[i]
					.addActionListener(new ItemActionListener(ITEMS[i]));
			this.add(currMenuItems[i]);
		}
	}

	// TODO Fix the problem with back item.
	private void constructPreviousMenuItems() {
		currMenuItems = previousMenuItems;
		for (int i = 0; i < currMenuItems.length; i++) {
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
				gamePlay.setGameMode(GameMode.SINGLE_PLAYER);
				gamePlay.setPlayer(GamePlayers.SECOND_PLAYER.getPlayer());
				tablePnl = new TablePanel(500, 500, GameProperties.SIZE_9
						.getSize(), Color.BLUE, Color.RED);
				gamePlay.setTablePanel(tablePnl);
				gamePlay.start();
				menuController.addContentToContainer(tablePnl);
				break;
			case MULTI_PLAYER:
				// previousMenuItems = currMenuItems;
				selectItem();
				break;
			case OPTIONS: {
				// previousMenuItems = currMenuItems;
				selectItem();
				break;
			}
			case CREDITS:
				break;
			case CONFIGURE_GAME: {
				gamePlay.setGameMode(GameMode.SINGLE_PLAYER);
				configurationPnl = new ConfigurationPanel(gamePlay);
				menuController.setContent(configurationPnl);
				break;
			}
			case TCP_CONNECTION: {
				previousMenuItems = currMenuItems;
				selectItem();
				break;
			}
			case HOST_GAME: {
				selectItem();
				hostGamePnl = new HostGamePanel(gamePlay);
				menuController.setContent(hostGamePnl);
				break;
			}
			case HOST: {
				gamePlay.setGameMode(GameMode.TCP_CONNECTION);
				gamePlay.setPlayer(GamePlayers.values()[hostGamePnl
						.getPlayerComboBoxSelectedIndex()].getPlayer());
				tablePnl = new TablePanel(500, 500, GameProperties.SIZE_9
						.getSize(), Color.BLUE, Color.RED);
				gamePlay.setTablePanel(tablePnl);
				gamePlay.start();
				menuController.addContentToContainer(tablePnl);
				break;
			}
			case JOIN_GAME: {
				selectItem();
				joinGamePnl = new JoinGamePanel(gamePlay);
				menuController.setContent(joinGamePnl);
				break;
			}
			case JOIN: {
				gamePlay.setGameMode(GameMode.TCP_CONNECTION);
				gamePlay.setServerAddress(joinGamePnl.getIPAddress());
				gamePlay.setPlayer(GamePlayers.values()[joinGamePnl
						.getPlayerComboBoxSelectedIndex()].getPlayer());
				tablePnl = new TablePanel(500, 500, GameProperties.SIZE_9
						.getSize(), Color.BLUE, Color.RED);
				gamePlay.setTablePanel(tablePnl);
				gamePlay.start();
				menuController.addContentToContainer(tablePnl);
				break;
			}
			case EXIT:
				System.exit(0);
				break;
			case BACK: {
				removeAll();
				menuController.clearContent();
				// constructPreviousMenuItems();
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
			currMenuItems = new JButton[item.getSubMenu().length];
			for (int i = 0; i < currMenuItems.length; i++) {
				currMenuItems[i] = new JButton(ITEMS[item.getSubMenu()[i]]
						.toString());
				currMenuItems[i].addActionListener(new ItemActionListener(
						ITEMS[item.getSubMenu()[i]]));
				add(currMenuItems[i]);
			}
			updateUI();
		}
	}

} // @jve:decl-index=0:visual-constraint="10,18"