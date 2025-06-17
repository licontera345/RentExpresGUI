package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import com.pinguela.rentexpres.desktop.dialog.ConfirmDialog;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;

/**
 * Generic action for dialogs that create entities.
 */
public abstract class AbstractCreateAction<T, D extends JDialog & ConfirmDialog<T>> extends AbstractAction {
    private static final long serialVersionUID = 1L;

    protected final Frame frame;
    private final ActionCallback afterCreate;

    protected AbstractCreateAction(Frame frame, ActionCallback afterCreate) {
        this(null, frame, afterCreate);
    }

    protected AbstractCreateAction(String name, Frame frame, ActionCallback afterCreate) {
        super(name);
        this.frame = frame;
        this.afterCreate = afterCreate;
    }

    /** Creates the dialog to obtain the DTO. */
    protected abstract D createDialog();

    /** Persists the new DTO. */
    protected abstract void save(T dto) throws RentexpresException;

    @Override
    public void actionPerformed(ActionEvent e) {
        D dlg = createDialog();
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            try {
                save(dlg.getValue());
            } catch (RentexpresException ex) {
                SwingUtils.showError(frame, "Error guardando: " + ex.getMessage());
            }
        }
        if (afterCreate != null) {
            afterCreate.execute();
        }
    }

    /**
     * For callers that used the old execute() method on concrete classes.
     */
    public void execute() throws RentexpresException {
        actionPerformed(null);
    }
}
