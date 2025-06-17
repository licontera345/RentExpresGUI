package com.pinguela.rentexpres.desktop.dialog;

/**
 * Simple interface for dialogs that return a value when confirmed.
 */
public interface ConfirmDialog<T> {
    /** Whether the dialog was accepted by the user. */
    boolean isConfirmed();

    /** Returns the created or edited value. */
    T getValue();
}
