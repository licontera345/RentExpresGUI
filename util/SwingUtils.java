package com.pinguela.rentexpres.desktop.util;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Objects;

import com.pinguela.rentexpres.desktop.util.ActionCallback;

/**
 * Utilidades Swing reutilizables en toda la aplicación.
 */
public final class SwingUtils {
	public static int showConfirm(Component parent, String message, String title) {
		return JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
	}

	public static void showInfo(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "Información", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showWarning(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
	}

	public static void showError(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/** Centra el diálogo respecto a un componente padre */
        public static void center(Window dialog, Component parent) {
                dialog.setLocationRelativeTo(parent);
        }

        /** Wrapper for SwingUtilities.invokeLater using ActionCallback */
        public static void invokeLater(ActionCallback action) {
                Objects.requireNonNull(action);
                SwingUtilities.invokeLater(new ActionCallbackThread(action));
        }

	/** Centra el diálogo en la pantalla */
	public static void center(Window dialog) {
		dialog.setLocationRelativeTo(null);
	}

	/**
	 * Añade el mismo DocumentListener a todos los campos indicados.
	 * 
	 * @param fire   acción a ejecutar en cada cambio
	 * @param fields campos de texto (JTextField/JTextArea…) a escuchar
	 */
        public static void addDocumentListener(ActionCallback fire, JTextComponent... fields) {
                Objects.requireNonNull(fire);
                DocumentListener dl = new DocumentListener() {
                        private void changed() {
                                fire.execute();
                        }

			public void insertUpdate(DocumentEvent e) {
				changed();
			}

			public void removeUpdate(DocumentEvent e) {
				changed();
			}

			public void changedUpdate(DocumentEvent e) {
				changed();
			}
		};
		for (JTextComponent f : fields) {
			if (f != null)
				f.getDocument().addDocumentListener(dl);
		}
	}

	public static Integer toInteger(String text) {
		try {
			return text == null || text.trim().isEmpty() ? null : Integer.valueOf(text.trim());
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	public static Double toDouble(String text) {
		try {
			return text == null || text.trim().isEmpty() ? null : Double.valueOf(text.trim());
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	public static BigDecimal toBigDecimal(String text) {
		try {
			return text == null || text.trim().isEmpty() ? null : new BigDecimal(text.trim());
		} catch (NumberFormatException ex) {
			return null;
		}
	}

        public static JButton button(String text, ActionCallback action) {
               JButton b = new JButton(text);
               b.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               action.execute();
                       }
               });
               return b;
       }

	private SwingUtils() {
	}
}
