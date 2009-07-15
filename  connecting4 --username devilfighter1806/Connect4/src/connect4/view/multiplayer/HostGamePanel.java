package connect4.view.multiplayer;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JButton;

import connect4.model.GameProperties;

public class HostGamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel gameNameLbl = null;
	private JTextField gameNameTxt = null;
	private JLabel jLabel = null;
	private JComboBox playersComboBox = null;
	private JButton hostGameBtn = null;

	/**
	 * This is the default constructor
	 */
	public HostGamePanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setVgap(20);
		jLabel = new JLabel();
		jLabel.setText("Choose player: ");
		jLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		jLabel.setPreferredSize(new Dimension(150, 30));
		gameNameLbl = new JLabel();
		gameNameLbl.setText("Enter game name:  ");
		gameNameLbl.setPreferredSize(new Dimension(150, 30));
		gameNameLbl.setFont(new Font("Dialog", Font.BOLD, 14));
		this.setLayout(flowLayout);
		this.setSize(350, 500);
		this.setPreferredSize(new Dimension(350, 500));
		this.add(gameNameLbl, null);
		this.add(getGameNameTxt(), null);
		this.add(jLabel, null);
		this.add(getPlayersComboBox(), null);
		this.add(getHostGameBtn(), null);
	}

	/**
	 * This method initializes gameNameTxt	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getGameNameTxt() {
		if (gameNameTxt == null) {
			gameNameTxt = new JTextField();
			gameNameTxt.setText("MultiPlayer1");
			gameNameTxt.setPreferredSize(new Dimension(150, 30));
		}
		return gameNameTxt;
	}

	/**
	 * This method initializes playersComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getPlayersComboBox() {
		if (playersComboBox == null) {
			playersComboBox = new JComboBox(GameProperties.PLAYERS);
			playersComboBox.setPreferredSize(new Dimension(150, 30));
		}
		return playersComboBox;
	}

	/**
	 * This method initializes hostGameBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getHostGameBtn() {
		if (hostGameBtn == null) {
			hostGameBtn = new JButton();
			hostGameBtn.setPreferredSize(new Dimension(100, 30));
			hostGameBtn.setText("host game");
		}
		return hostGameBtn;
	}

}  //  @jve:decl-index=0:visual-constraint="7,46"
