package connect4.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import connect4.controller.Direction;

public class MyAbstractAction extends AbstractAction {

	private MyKeyStrokes keyStroke;
	private TablePanel panel;

	public MyAbstractAction(MyKeyStrokes key, TablePanel panel) {
		this.keyStroke = key;
		this.panel = panel;
	}

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
			// placeMAN
			break;
		case ESCAPE:
			// leaveToMenu
			break;
		default:
			// do nothing
		}
		panel.repaint();
		System.out.println(e.getActionCommand());
	}

	private void processUp() {
		switch (panel.acquireDirection()) {
		case VERTICAL_DOWN:
			panel.moveArrowTo(Direction.VERTICAL_UP, panel.acqurePosition());
			break;
		case VERTICAL_UP:
			return;
		case HORIZONTAL_LEFT:
		case HORIZONTAL_RIGHT:
			panel.arrowMoveUp();
			break;
		}
	}

	private void processDown() {
		switch (panel.acquireDirection()) {
		case VERTICAL_UP:
			panel.moveArrowTo(Direction.VERTICAL_DOWN, panel.acqurePosition());
			break;
		case HORIZONTAL_LEFT:
		case HORIZONTAL_RIGHT:
			panel.arrowMoveDown();
			break;
		case VERTICAL_DOWN:
			return;
		}
	}

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

	private void processEnter(){
		//unimplemented
	}
}