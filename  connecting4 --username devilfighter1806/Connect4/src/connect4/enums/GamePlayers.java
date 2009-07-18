package connect4.enums;

import java.awt.Color;

/**
 * Represents players properties.
 * 
 * @author Stanislav Petrov
 * 
 */
public enum GamePlayers {

	FIRST_PLAYER('x', Color.BLUE, 1), SECOND_PLAYER('o', Color.RED, 2), EMPTY(
			'-', Color.GRAY, 0);

	/**
	 * Stores player's value in the board in the working logic.
	 */
	private final char player;
	/**
	 * Stores color of the player.
	 */
	private Color color;
	/**
	 * Stores the player's description.
	 */
	private final int number;

	/**
	 * Construct game player.
	 * 
	 * @param player
	 *            Player value to be set.
	 * @param c
	 *            Color of the player to be set.
	 * @param num
	 *            Number identifier of the player(square's state).
	 */
	private GamePlayers(char player, Color c, int num) {
		this.player = player;
		this.color = c;
		this.number = num;
	}

	public char getPlayer() {
		return this.player;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public Color getColor() {
		return this.color;
	}

	public int getNumber() {
		return this.number;
	}
}
