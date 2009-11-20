package com.ngc4.connect4.view.menu;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.ngc4.connect4.controller.GamePlay;
import com.ngc4.connect4.controller.menu.MenuController;
import com.ngc4.connect4.enums.GameMode;
import com.ngc4.connect4.enums.GamePlayers;
import com.ngc4.connect4.enums.GameProperties;
import com.ngc4.connect4.enums.MenuItem;
import com.ngc4.connect4.view.gameplay.StatusBarPanel;
import com.ngc4.connect4.view.gameplay.TablePanel;
import com.ngc4.connect4.view.multiplayer.HostGamePanel;
import com.ngc4.connect4.view.multiplayer.JoinGamePanel;


/**
 * Represents game menu panel which visualize all menu items.
 * 
 * @author Stanislav Petrov
 */
public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	/**
	 * Stores configuration information from the menu panel.
	 */
	private GamePlay gamePlay;
	/**
	 * Stores all menu and sub menu items.
	 */
	private final MenuItem[] ITEMS;
	/**
	 * Stores supported board sizes.
	 */
	private final GameProperties[] BOARD_SIZES;
	/**
	 * Stores the current menu items to be presented.
	 */
	private JButton[] currMenuItems;
	/**
	 * Stores reference to the menu controller.
	 */
	private MenuController menuController;
	private TablePanel tablePnl;
	private ConfigurationPanel configurationPnl;
	private HostGamePanel hostGamePnl;
	private JoinGamePanel joinGamePnl;
	private StatusBarPanel statusBarPanel;
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

	/**
	 * Initializes game menu panel and its components.
	 */
	private void initialize() {
		statusBarPanel = new StatusBarPanel();
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
		private MenuItem item;

		public ItemActionListener(MenuItem item) {
			this.item = item;
		}

		/**
		 * Implements item action.
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			addActionToItem(item);
		}

		/**
		 * Adds action to the specified menu item.
		 * 
		 * @param item
		 *            The specified menu item.
		 */
		private void addActionToItem(MenuItem item) {
			switch (item) {
			case SINGLE_PLAYER:
				startGame(GameMode.SINGLE_PLAYER, item);
				break;
			case HOT_SEAT: {
				startGame(GameMode.HOT_SEAT, item);
				break;
			}
			case MULTI_PLAYER:
				selectItem();
				break;
			case OPTIONS: {
				selectItem();
				break;
			}
			case HELP:
				break;
			case CONFIGURE_GAME: {
				gamePlay.setGameMode(GameMode.SINGLE_PLAYER);
				configurationPnl = new ConfigurationPanel(gamePlay);
				menuController.setContent(configurationPnl);
				break;
			}
			case INTERNET: {
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
				startGame(GameMode.INTERNET, item);
				break;
			}
			case JOIN_GAME: {
				selectItem();
				joinGamePnl = new JoinGamePanel(gamePlay);
				menuController.setContent(joinGamePnl);
				break;
			}
			case JOIN: {
				startGame(GameMode.INTERNET, item);
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
		 * Starts game in the specified game mode from the specified menu item.
		 * 
		 * @param gameMode
		 *            Game mode to be specified.
		 * @param item
		 *            Menu item to be chosen.
		 */
		private void startGame(GameMode gameMode, MenuItem item) {
			gamePlay.setGameMode(gameMode);
			switch (item) {
			case SINGLE_PLAYER: {
				if (configurationPnl != null) {
					gamePlay.setPlayer(GamePlayers.values()[configurationPnl
							.getPlayerComboBoxSelectedIndex()].getPlayer());
				} else {
					gamePlay.setPlayer(GamePlayers.SECOND_PLAYER.getPlayer());
				}
				break;
			}
			case HOT_SEAT: {
				gamePlay.setPlayer(GamePlayers.FIRST_PLAYER.getPlayer());
				break;
			}
			case HOST: {
				gamePlay.setPlayer(GamePlayers.values()[hostGamePnl
						.getPlayerComboBoxSelectedIndex()].getPlayer());
				break;
			}
			case JOIN: {
				gamePlay.setServerAddress(joinGamePnl.getIPAddress());
				gamePlay.setPlayer(GamePlayers.values()[joinGamePnl
						.getPlayerComboBoxSelectedIndex()].getPlayer());
				break;
			}
			}
			tablePnl = new TablePanel(500, 500, gamePlay.getBoardSize(),
					Color.BLUE);
			gamePlay.setTablePanel(tablePnl);
			gamePlay.setStatusBarPanel(statusBarPanel);
			gamePlay.start();
			menuController.addContentToContainer(tablePnl, statusBarPanel);
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

		/**
		 * Gets name of the game.
		 * 
		 * @return The game name.
		 */
		public String getGameName() {
			return gamePlay.getGameName();
		}
	}

} // @jve:decl-index=0:visual-constraint="10,18"
