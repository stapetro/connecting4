package com.ngc4.connect4.controller.menu;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.ngc4.connect4.view.gameplay.StatusBarPanel;
import com.ngc4.connect4.view.menu.ConfigurationPanel;
import com.ngc4.connect4.view.menu.ContainerPanel;
import com.ngc4.connect4.view.menu.MenuContentPanel;


/**
 * Handles with changes in content and controller panels when user chooses a
 * menu item.
 * 
 * @author Stanislav Petrov
 * 
 */
public class MenuController {

	/**
	 * Stores reference to the menu content panel.
	 */
	private MenuContentPanel menuContentPnl;
	/**
	 * Stores reference to the container panel.
	 */
	private ContainerPanel containerPnl;
	/**
	 * Stores reference to the configuraiton panel.
	 */
	private ConfigurationPanel configurationPnl;

	/**
	 * Initializes all data necessary for the controller.
	 * 
	 * @param contentPnl
	 *            Content panel to be set.
	 * @param containerPnl
	 *            Container panel to be set.
	 */
	public MenuController(MenuContentPanel contentPnl,
			ContainerPanel containerPnl) {
		this.menuContentPnl = contentPnl;
		this.containerPnl = containerPnl;
	}

	/**
	 * Sets content to the content panel.
	 * 
	 * @param contentPnl
	 *            Panel with content to be added to the content panel.
	 */
	public void setContent(JPanel contentPnl) {
		if (contentPnl instanceof ConfigurationPanel) {
			this.configurationPnl = (ConfigurationPanel) contentPnl;
		}
		menuContentPnl.removeAll();
		menuContentPnl.add(contentPnl);
		menuContentPnl.updateUI();
	}

	/**
	 * Clears the content from the content panel.
	 */
	public void clearContent() {
		menuContentPnl.removeAll();
	}

	/**
	 * Adds contetn to the container panel, remove the menu.
	 * 
	 * @param component
	 *            Component to be added to the whole container panel.
	 */
	public void addContentToContainer(JComponent component, StatusBarPanel statusBarPanel) {
		containerPnl.removeAll();
		containerPnl.add(component, BorderLayout.CENTER);
		containerPnl.add(statusBarPanel, BorderLayout.SOUTH);
		component.requestFocusInWindow();
		containerPnl.updateUI();
	}
}
