package connect4.model;

/**
 * Represents all game properties.
 * 
 * @author Stanislav Petrov
 * 
 */
public enum GameProperties {

	SIZE_9(9, 0), SIZE_13(13, 1);

	public static  final String[] SIZES = {"9x9", "13x13"};
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
