package connect4.view.gameplay;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Status information.
 * @author Leni Kirilov
 */
public class StatusBarPanel extends JPanel {

    /**
     * Represents status information.
     */
    private JLabel statusLabel;

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
        statusLabel = new JLabel();
        statusLabel.setFont(new java.awt.Font("Verdana", 1, 14));
        add(statusLabel);
    }

    /**
     * Status setter.
     * @param text Status text to be set.
     */
    public void setStatus(String text,Color color) {
        statusLabel.setText(text);
        statusLabel.setForeground(color);
    }
}
