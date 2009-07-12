package connect4.controller;

/**
 * Represents bot actions during the game.
 * 
 * @author Stanislav Petrov
 */
public enum BotTactics {

	/**
	 * Bot checks whether he's wining.
	 */
	BOT_WIN(0),
	/**
	 * Bot checks board for user not to win.
	 */
	NOT_USER_WIN(1),
	/**
	 * Bot checks board for user threats.
	 */
	NOT_USER_THREAT(2),
	/**
	 * Bot tactics for playing.
	 */
	TACTIC(3);

	/**
	 * Stores the number of bot tactics.
	 */
	public static final int TACTICS_NUMBER = 4;

	/**
	 * Stores an index of the specified bot tactics.
	 */
	private final int index;

	BotTactics(int index) {
		this.index = index;
	}

	/**
	 * Gets bot tactic index.
	 * 
	 * @return Index of the tactic used in data structure.
	 */
	public int getIndex() {
		return this.index;
	}

}
