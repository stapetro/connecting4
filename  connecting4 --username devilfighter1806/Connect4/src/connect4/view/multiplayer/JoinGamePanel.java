package connect4.view.multiplayer;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JButton;

import connect4.controller.GamePlay;
import connect4.model.GamePlayers;
import connect4.model.GameProperties;

public class JoinGamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private GamePlay gamePlay;
	private JLabel ipAddrLbl = null;
	private JTextField ipAddrTxt = null;
	private JLabel choosePlayerLbl = null;
	private  JComboBox playerComboBox = null;

	/**
	 * This is the default constructor
	 */
	public JoinGamePanel(GamePlay gamePlay) {
		super();
		this.gamePlay = gamePlay;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setVgap(30);
		flowLayout.setHgap(10);
		choosePlayerLbl = new JLabel();
		choosePlayerLbl.setText("Choose player: ");
		choosePlayerLbl.setFont(new Font("Dialog", Font.BOLD, 14));
		choosePlayerLbl.setPreferredSize(new Dimension(150, 30));
		ipAddrLbl = new JLabel();
		ipAddrLbl.setText("IP address: ");
		ipAddrLbl.setFont(new Font("Dialog", Font.BOLD, 14));
		ipAddrLbl.setPreferredSize(new Dimension(100, 30));
		this.setLayout(flowLayout);
		this.setSize(350, 500);
		this.setPreferredSize(new Dimension(350, 500));
		this.add(ipAddrLbl, null);
		this.add(getIpAddrTxt(), null);
		this.add(choosePlayerLbl, null);
		this.add(getPlayerComboBox(), null);
	}

	/**
	 * This method initializes ipAddrTxt
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getIpAddrTxt() {
		if (ipAddrTxt == null) {
			ipAddrTxt = new JTextField();
			ipAddrTxt.setPreferredSize(new Dimension(170, 30));
		}
		return ipAddrTxt;
	}

	/**
	 * This method initializes playerComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getPlayerComboBox() {
		if (playerComboBox == null) {
			playerComboBox = new JComboBox(GameProperties.PLAYERS);
			gamePlay.setPlayer(GamePlayers.values()[playerComboBox
					.getSelectedIndex()].getPlayer());
			playerComboBox.setPreferredSize(new Dimension(120, 30));
		}
		return playerComboBox;
	}
	
	public String getIPAddress(){
		return ipAddrTxt.getText();
	}
	
	public int getPlayerComboBoxSelectedIndex(){
		return this.playerComboBox.getSelectedIndex();
	}

} // @jve:decl-index=0:visual-constraint="12,-9"
