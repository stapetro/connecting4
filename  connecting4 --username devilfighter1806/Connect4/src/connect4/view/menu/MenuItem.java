package connect4.view.menu;

/**
 * Represents and stores game menu items.
 * 
 * @author Stanislav Petrov
 */
public enum MenuItem {

	SINGLE_PLAYER(0, null), MULTI_PLAYER(1, new int[] { 7, 8, 13 }), OPTIONS(2,
			new int[] { 5, 6, 13 }), CREDITS(3, null), EXIT(4, null), CONFIGURE_GAME(
			5, null), CONTROLS(6, null), HOT_SEAT(7, null), TCP_CONNECTION(8,
			new int[] { 9, 10, 13 }), HOST_GAME(9, new int[] { 11, 13 }), JOIN_GAME(
			10, new int[] { 12, 13 }), HOST(11, null), JOIN(12, null), BACK(13,
			null);

	/**
	 * Stores the number of parent menu items.
	 */
	public static final int MENU_ITEMS_NUMBER = 5;
	/**
	 * Stores the index of the menu item.
	 */
	private final int index;
	/**
	 * Stores the parent idnex of the current item index.
	 */
	private int parentIndex;
	/**
	 * Stores all sub menu item indices for the current menu item.
	 */
	private final int[] subMenuItem;

	MenuItem(int index, int[] subMenuItems) {
		this.index = index;
		this.subMenuItem = subMenuItems;
	}

	public void setParentIndex(int parentInd) {
		this.parentIndex = parentInd;
	}

	public int getParentIdnex() {
		return this.parentIndex;
	}

	/**
	 * Gets an index of the menu item.
	 * 
	 * @return Menu item's index.
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * Gets sub menu items of the relevant menu item.
	 * 
	 * @return Array of sub menus' indices.
	 */
	public int[] getSubMenu() {
		return this.subMenuItem;
	}

}
