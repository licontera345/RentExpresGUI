package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Color;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.pinguela.rentexpres.desktop.util.AppTheme;

/**
 * Base dialog applying consistent RentExpres style.
 */
public abstract class StyledDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    protected StyledDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        getContentPane().setBackground(AppTheme.DIALOG_BG);
    }

    /**
     * Returns a content panel with default padding and opaque false.
     */
    protected JPanel createContentPanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(15, 15, 15, 15));
        return p;
    }

    /**
     * Styles a button as primary according to the theme.
     */
    protected void stylePrimary(JButton btn) {
        btn.setBackground(AppTheme.PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    /**
     * Styles a button as cancel according to the theme.
     */
    protected void styleCancel(JButton btn) {
        btn.setBackground(AppTheme.CANCEL);
        btn.setForeground(AppTheme.CANCEL_FG);
        btn.setFocusPainted(false);
    }
}
