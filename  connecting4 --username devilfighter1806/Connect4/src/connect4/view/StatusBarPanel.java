package connect4.view;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Status information.
 * @author 
 */
public class StatusBarPanel extends JPanel {

    /**
     * Represents status information.
     */
    private JLabel statusModifyLbl;

    /**
     * General purpose constructor. Sets some of the
     * parameters and values of the used objects
     */
    public StatusBarPanel() {
        super();
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        initComponents();
    }

    /**
     * Initializes componenets in status panel.
     */
    private void initComponents() {
        setPreferredSize(new Dimension(150, 30));
        statusModifyLbl = new JLabel();
        statusModifyLbl.setFont(new java.awt.Font("Verdana", 1, 14));
        add(statusModifyLbl);
    }

    /**
     * Status setter.
     * @param text Status text to be set.
     */
    public void setStatus(String text,Color color) {
        statusModifyLbl.setText(text);
        statusModifyLbl.setForeground(color);
    }
}
