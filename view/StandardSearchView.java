package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import com.pinguela.rentexpres.desktop.util.PaginationPanel;

/**
 * Generic view providing standard layout for search screens:
 * toolbar with actions, filter panel on top, table in the center
 * and pagination panel at the bottom.
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

        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.add(actions);

        JPanel main = new JPanel(new BorderLayout());
        main.add(filter, BorderLayout.NORTH);
        main.add(new JScrollPane(table), BorderLayout.CENTER);
        main.add(pager, BorderLayout.SOUTH);

        setLayout(new BorderLayout(8, 8));
        add(bar, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
    }

    public F getFilter() { return filter; }
    public A getActions() { return actions; }
    public T getTable() { return table; }
    public PaginationPanel getPager() { return pager; }
}
