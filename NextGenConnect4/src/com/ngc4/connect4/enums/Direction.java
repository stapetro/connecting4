package com.ngc4.connect4.enums;

/**
 * Represents directions from which user can move man and directions for
 * checking wining paths(4 men).
 * 
 * @author Stanislav Petrov
 * 
 */
public enum Direction {

	/**
	 * Represents vertical up direction connect 4 men and move direction(top)
	 * for user from top side of the board.
	 */
	VERTICAL_UP(0, 0),
	/**
	 * Represents vertical down direction for connect 4 men and move
	 * direction(bottom) for user from bottom side of the board.
	 */
	VERTICAL_DOWN(1, 6),
	/**
	 * Represents horizontal left direction for connect 4 men and move
	 * direction(left) for user from left side of the board.
	 */
	HORIZONTAL_LEFT(2, 0),
	/**
	 * Represents horizontal left direction for connect 4 men and move
	 * direction(left) for user from right side of the board.
	 */
	HORIZONTAL_RIGHT(3, 6),
	/**
	 * Represents diagonal down/up left/right diagonal directions for checking
	 * for a wining 4. Position has no meaning in the diagonal directions below.
	 */
	DIOAGONAL_UP_LEFT(4, -1), DIOAGONAL_UP_RIGHT(5, -1), DIOAGONAL_DOWN_LEFT(6,
			-1), DIOAGONAL_DOWN_RIGHT(7, -1);
	/**
	 * Stores the number of all directions for checking wining configurations.
	 */
	public static final int DIRECTIONS_MOVES_NUMBER = 8;
	/**
	 * Stores moves for x-coordinate(row offsets) in the board.
	 */
	public static final int[] COORD_X = { -1, 1, 0, 0, -1, -1, 1, 1 };
	/**
	 * Stores moves for y-coordinate(column offsets) in the board.
	 */
	public static final int[] COORD_Y = { 0, 0, -1, 1, -1, 1, -1, 1 };
	/**
	 * Stores sequential number of the direction.
	 */
	private final int number;
	/**
	 * Stores board position of the direction for only first four
	 * directions(vertical/horizontal up/down)
	 */
	private final int position;

	/**
	 * General purpose constructor.
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
	 * Direction2 position getter only for horizontal/vertical directions.
	 * 
	 * @return Row or column of the relevant position.
	 */
	public int getPosition() {
		return this.position;
	}

	/**
	 * Gets sequenatial number of direction.
	 * 
	 * @return Direction2 number used for indexing in arrays.
	 */
	public int getIndex() {
		return this.number;
	}
}
