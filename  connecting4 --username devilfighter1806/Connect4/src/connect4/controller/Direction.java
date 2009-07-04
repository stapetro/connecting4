package connect4.controller;

/**
 * Represents directions from which user can move man.
 * 
 * @author Stanislav Petrov
 * 
 */
public enum Direction {

	TOP(1, 0), BOTTOM(2, 6), LEFT(3, 0), RIGHT(4, 6);
	/**
	 * Stores the number of all directions of a man.
	 */
	public static final int DIRECTION_NUMBER = 8;
	/**
	 * Stores sequential number of the direction.
	 */
	private final int number;
	/**
	 * Stores position of the direction.
	 */
	private final int position;

	/**
	 * General purpose constructor.
	 * 
	 * @param num
	 *            Sequential number to be set.
	 * @param position
	 *            Position to be set.
	 */
	Direction(int num, int position) {
		this.number = num;
		this.position = position;
	}

	/**
	 * Direction position getter.
	 * 
	 * @return Row or column of the relevant position.
	 */
	public int getPosition() {
		return this.position;
	}
}
