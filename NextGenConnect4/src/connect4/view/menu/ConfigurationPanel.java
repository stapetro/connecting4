package connect4.view.menu;

import java.awt.GridBagLayout;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JComboBox;

import connect4.controller.GamePlay;
import connect4.enums.GamePlayers;
import connect4.enums.GameProperties;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * Represents interface for game configuration.
 * 
 * @author Stanislav Petrov
 * 
 */
public class ConfigurationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Stores board size user prompt.
	 */
	private JLabel boardSizelbl = null;
	private JComboBox boardSizeComboBox = null;
	/**
	 * Stores board sizes constants.
	 */
	private final GameProperties[] BOARD_SIZES;
	private JLabel styleLbl = null;
	private JColorChooser colorChooser;
	private JButton colorChooseBtn = null;
	private JComboBox playersComboBox = null;
	/**
	 * Stores the chosen size of the board.
	 */
	private int boardSize;
	/**
	 * Stores all entered data from the configuration panel.
	 */
	private GamePlay gamePlay;

	/**
	 * This is the default constructor
	 */
	public ConfigurationPanel(GamePlay gamePlay) {
		super();
		BOARD_SIZES = GameProperties.values();
		this.gamePlay = gamePlay;
		initialize();
	}

	/**
	 * This method initializes the configuration panel.
	 * 
	 * @return void
	 */
	private void initialize() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setVgap(30);
		styleLbl = new JLabel();
		styleLbl.setText("      Choose colors for players:");
		styleLbl.setPreferredSize(new Dimension(300, 24));
		styleLbl.setFont(new Font("Dialog", Font.BOLD, 18));
		boardSizelbl = new JLabel();
		boardSizelbl.setText("     Choose size of the board:");
		boardSizelbl.setPreferredSize(new Dimension(300, 24));
		boardSizelbl.setFont(new Font("Dialog", Font.BOLD, 18));
		this.setLayout(flowLayout);
		colorChooser = new JColorChooser(Color.BLUE);
		this.setSize(350, 500);
		this.setPreferredSize(new Dimension(350, 150));
		this.add(boardSizelbl, null);
		this.add(getBoardSizeComboBox(), null);
		this.add(styleLbl, null);
		this.add(getPlayersComboBox(), null);
		this.add(getColorChooseBtn(), null);
	}

	/**
	 * This method initializes boardSizeComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getBoardSizeComboBox() {
		if (boardSizeComboBox == null) {
			boardSizeComboBox = new JComboBox(GameProperties.SIZES);
			boardSizeComboBox.setPreferredSize(new Dimension(300, 24));
			boardSizeComboBox
					.addItemListener(new java.awt.event.ItemListener() {
						public void itemStateChanged(java.awt.event.ItemEvent e) {
							if (e.getStateChange() == 1) {
								boardSize = BOARD_SIZES[boardSizeComboBox
										.getSelectedIndex()].getSize();
								gamePlay.setBoardSize(boardSize);
							}
						}
					});
		}
		return boardSizeComboBox;
	}

	/**
	 * This method initializes colorChooseBtn
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getColorChooseBtn() {
		if (colorChooseBtn == null) {
			colorChooseBtn = new JButton();
			colorChooseBtn.setPreferredSize(new Dimension(90, 30));
			colorChooseBtn.setText("Colors");
			colorChooseBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					JColorChooser.showDialog(ConfigurationPanel.this,
							"Choose color for player", Color.blue);
				}
			});
		}
		return colorChooseBtn;
	}

	/**
	 * This method initializes playersComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getPlayersComboBox() {
		if (playersComboBox == null) {
			playersComboBox = new JComboBox(GameProperties.PLAYERS);
			playersComboBox.setPreferredSize(new Dimension(100, 25));
		}
		return playersComboBox;
	}

	/**
	 * Gets selected index from the players combo box.
	 * 
	 * @return The selected index from the players' combo box.
	 */
	public int getPlayerComboBoxSelectedIndex() {
		return this.playersComboBox.getSelectedIndex();
	}
}
