package connect4.enums;

/**
 * Reperesents status messages during the game play.
 * 
 * @author Stanislav Petrov
 * 
 */
public enum StatusMessages {

	WAITING_CONNECTION("Waiting for connection"), CONNECTION_ESTABLISHED(
			"Connection is established."), WIN("!!! WIN !!!"), CURRENT_PLAER(
			"You are player "), TURN("Your turn."), NOT_YOUR_TURN(
			"Waiting for your turn"), DRAW_GAME("Draw game!");

	/**
	 * Stores the content of the relevant status message.
	 */
	private final String message;

	StatusMessages(String msg) {
		this.message = msg;
	}

	public String toString() {
		return this.message;
	}
}
