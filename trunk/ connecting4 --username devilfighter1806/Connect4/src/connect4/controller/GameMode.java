package connect4.controller;

/**
 * Represents different game modes.
 * 
 * @author Stanislav Petrov
 * 
 */
public enum GameMode {

	/**
	 * Indicates single player game mode.
	 */
	SINGLE_PLAYER,
	/**
	 * Indicates multi player game over the network.
	 */
	INTERNET,
	/**
	 * Indicates multi player game on the local computer.
	 */
	HOT_SEAT;
}
