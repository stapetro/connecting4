package connect4.enums;

/**
 * Represents all game properties.
 * 
 * @author Stanislav Petrov
 * 
 */
public enum GameProperties {

	SIZE_9(9, 0), SIZE_11(11, 1), SIZE_13(13, 2), SIZE_15(15, 3);

	public static  final String[] SIZES = {"9x9", "11x11", "13x13", "15x15"};
	public static final String[] PLAYERS = {"1st player", "2nd player"};
	private final int size;
	private final int nameId;

	private GameProperties(int size, int nameId) {
		this.size = size;
		this.nameId = nameId;
	}
	
	public int getSize(){
		return this.size;
	}
	
	public String getName(){
		return this.SIZES[this.nameId];
	}
}
