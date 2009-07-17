package connect4.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class MyAbstractAction extends AbstractAction {

	private MyKeyStrokes keyStroke;

	public MyAbstractAction(MyKeyStrokes key) {
		this.keyStroke = key;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (keyStroke) {
		case UP:
			// moveUP
			break;
		case DOWN:
			// moveDown
			break;
		case LEFT:
			// moveleft
			break;
		case RIGHT:
			// moveRight
			break;
		case ENTER:
			// placeMAN
			break;
		case ESC:
			// leaveToMenu
			break;
		default:
			// do nothing
		}
		System.out.println(e.getActionCommand());
	}

	// private void processUP(){
	// switch (key) {
	// case value:
	//			
	// break;
	//
	// default:
	// break;
	// }
	// }
}