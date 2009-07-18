package connect4.view.gameplay;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import connect4.enums.Direction;
import connect4.enums.KeyStrokes;

/**
 * This class can create objects that can handle KeyBindings events and can be
 * used with the ActionMap.put method.
 * 
 * Here it's used to catch keyPressings of the keys defined in MyKeyStrokes.
 * 
 * 
 * @author Leni
 * 
 */
public class KeyAbstractAction extends AbstractAction {

	private KeyStrokes keyStroke;
	private TablePanel panel;

	public KeyAbstractAction(KeyStrokes key, TablePanel panel) {
		this.keyStroke = key;
		this.panel = panel;
	}

	/**
	 * Handles the ActionEvent and the event has his own Listener and that's why
	 * no methods of event 'e' are invoked.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (keyStroke) {
		case UP:
			processUp();
			break;
		case DOWN:
			processDown();
			break;
		case LEFT:
			processLeft();
			break;
		case RIGHT:
			processRight();
			break;
		case ENTER:
			processEnter();
			break;
		case ESCAPE:
			processEscape();
			break;
		}
		panel.repaint();
	}

	/**
	 * What happens if the UP button is pressed. The action depends on the
	 * direction
	 */
	private void processUp() {
		switch (panel.acquireDirection()) {
		case VERTICAL_UP:
			return;
		case VERTICAL_DOWN:
			panel.moveArrowTo(Direction.VERTICAL_UP, panel.acqurePosition());
			break;
		case HORIZONTAL_LEFT:
		case HORIZONTAL_RIGHT:
			panel.arrowMoveUp();
			break;
		}
	}

	/**
	 * What happens if the DOWN button is pressed. The action depends on the
	 * direction
	 */
	private void processDown() {
		switch (panel.acquireDirection()) {
		case VERTICAL_DOWN:
			return;
		case VERTICAL_UP:
			panel.moveArrowTo(Direction.VERTICAL_DOWN, panel.acqurePosition());
			break;
		case HORIZONTAL_LEFT:
		case HORIZONTAL_RIGHT:
			panel.arrowMoveDown();
			break;
		}
	}

	/**
	 * What happens if the LEFT button is pressed. The action depends on the
	 * direction
	 */
	private void processLeft() {
		switch (panel.acquireDirection()) {
		case HORIZONTAL_LEFT:
			return;
		case HORIZONTAL_RIGHT:
			panel
					.moveArrowTo(Direction.HORIZONTAL_LEFT, panel
							.acqurePosition());
			break;
		case VERTICAL_DOWN:
		case VERTICAL_UP:
			panel.arrowMoveLeft();
			break;
		}
	}

	/**
	 * What happens if the RIGHT button is pressed. The action depends on the
	 * direction
	 */
	private void processRight() {
		switch (panel.acquireDirection()) {
		case HORIZONTAL_RIGHT:
			return;
		case HORIZONTAL_LEFT:
			panel.moveArrowTo(Direction.HORIZONTAL_RIGHT, panel
					.acqurePosition());
			break;
		case VERTICAL_DOWN:
		case VERTICAL_UP:
			panel.arrowMoveRight();
			break;
		}
	}

	/**
	 * What happens if the ENTER button is pressed. The synchronization for
	 * processing the new MOVE. Another thread is notified and taking care of
	 * acquiring the needed information for continuing the game/calculations.
	 */

	private void processEnter() {
		synchronized (panel) {
			panel.notify();
		}
	}

	/**
	 * Shuts down the Java Application when pressed ESCAPE while playing the
	 * game (not including the menu)
	 */
	private void processEscape() {
		System.exit(0);
	}
}