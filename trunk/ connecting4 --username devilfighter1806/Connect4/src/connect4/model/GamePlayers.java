package connect4.model;

import java.awt.Color;

/**
 * Represents players properties.
 * @author Stanislav Petrov
 *
 */
public enum GamePlayers {

	FIRST_PLAYER('x', Color.BLUE, "1st player"), SECOND_PLAYER('o', Color.RED,
			"2nd player"), EMPTY('-', Color.GRAY, null);

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
	private final String description;

	private GamePlayers(char player, Color c, String desc) {
		this.player = player;
		this.color = c;
		this.description = desc;
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

	public String getDescription() {
		return this.description;
	}
}
