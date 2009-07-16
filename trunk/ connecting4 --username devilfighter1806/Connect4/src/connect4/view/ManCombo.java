package connect4.view;

public class ManCombo {
	private int positionX;
	private int positionY;

	/**
	 * 
	 * @param posX
	 *            X position of winning MAN
	 * @param posY
	 *            Y position of winning MAN
	 */
	public ManCombo(int posX, int posY) {
		this.positionX = posX;
		this.positionY = posY;
	}

	public int getPositionX() {
		return positionX;
	}

	public int getPositionY() {
		return positionY;
	}
}
