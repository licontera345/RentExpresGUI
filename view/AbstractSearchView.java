package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.pinguela.rentexpres.desktop.util.PaginationPanel;

/**
 * Clase base que evita cargar componentes o datos más de una vez.
 */
public abstract class AbstractSearchView<FILTER extends JPanel, ACTIONS extends AbstractSearchActionsView, TABLE extends JPanel>
		extends JPanel {

	private static final long serialVersionUID = 1L;

	protected FILTER filterPanel;
	protected ACTIONS actionsPanel;
	protected TABLE tablePanel;
	protected PaginationPanel pager;
	protected JScrollPane scrollPane;
	private boolean initialized = false;

	public AbstractSearchView() {
		setLayout(new BorderLayout());
	}

	/**
	 * Llama a initComponents() y loadData() una sola vez.
	 */
	public void initIfNeeded() {
		if (!initialized) {
			initComponents();
			loadData();
			initialized = true;
		}
	}

	protected abstract void initComponents();

	protected abstract void loadData();

	public ACTIONS getActions() {
		return actionsPanel;
	}

	public TABLE getTable() {
		return tablePanel;
	}

	public PaginationPanel getPager() {
		return pager;
	}
}
