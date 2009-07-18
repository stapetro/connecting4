package connect4.enums;

/**
 * Represents all game properties.
 * 
 * @author Stanislav Petrov
 * 
 */
public enum GameProperties {

	SIZE_9(9, 0), SIZE_11(11, 1), SIZE_13(13, 2), SIZE_15(15, 3);

	/**
	 * Stores descriptions of the board sizes used for combo box in the
	 * presentation layer.
	 */
	public static final String[] SIZES = { "9x9", "11x11", "13x13", "15x15" };
	/**
	 * Stores descriptions of the players types for the combo box int the
	 * presentation layer.
	 */
	public static final String[] PLAYERS = { "1st player", "2nd player" };
	/**
	 * Stores board size.
	 */
	private final int size;
	/**
	 * Stores the number of the index in the relevant combo box in the
	 * presentation layer.
	 */
	private final int nameId;

	/**
	 * Constructs board size constant flag.
	 * 
	 * @param size
	 *            Board size to be set.
	 * @param nameId
	 *            Index of the board size to be set.
	 */
	private GameProperties(int size, int nameId) {
		this.size = size;
		this.nameId = nameId;
	}

	public int getSize() {
		return this.size;
	}

	public String getName() {
		return this.SIZES[this.nameId];
	}
}
