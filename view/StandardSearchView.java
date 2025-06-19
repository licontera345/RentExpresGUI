package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.pinguela.rentexpres.desktop.util.PaginationPanel;

/**
 * Generic view providing standard layout for search screens:
 * filter panel on top, actions right below,
 * the table in the center and pagination panel at the bottom.
 */
public class StandardSearchView<F extends JPanel, A extends AbstractSearchActionsView, T extends JPanel>
        extends JPanel {
    private static final long serialVersionUID = 1L;

    protected final F filter;
    protected final A actions;
    protected final T table;
    protected final PaginationPanel pager;

    public StandardSearchView(F filter, A actions, T table) {
        this.filter = filter;
        this.actions = actions;
        this.table = table;
        this.pager = new PaginationPanel();

        JPanel north = new JPanel(new BorderLayout(0, 4));
        north.add(filter, BorderLayout.NORTH);
        north.add(actions, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(table);

        setLayout(new BorderLayout(8, 8));
        add(north, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(pager, BorderLayout.SOUTH);
    }

    public F getFilter() { return filter; }
    public A getActions() { return actions; }
    public T getTable() { return table; }
    public PaginationPanel getPager() { return pager; }
}
