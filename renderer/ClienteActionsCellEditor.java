package com.pinguela.rentexpres.desktop.renderer;

import static com.pinguela.rentexpres.desktop.util.AppIcons.DELETE;
import static com.pinguela.rentexpres.desktop.util.AppIcons.EDIT;
import static com.pinguela.rentexpres.desktop.util.AppIcons.VIEW;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.pinguela.rentexpres.desktop.dialog.ClienteDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.ClienteEditDialog;
import com.pinguela.rentexpres.desktop.model.ClienteSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.service.ClienteService;

public class ClienteActionsCellEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = 1L;

	private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
	private final JButton btnView = iconButton(VIEW, "Ver");
	private final JButton btnEdit = iconButton(EDIT, "Editar");
	private final JButton btnDel = iconButton(DELETE, "Borrar");

	private final Frame frame;
	private ClienteDTO clienteActual;

	public ClienteActionsCellEditor(Frame owner, ClienteService service) {
		this.frame = owner;
		panel.add(btnView);
		panel.add(btnEdit);
		panel.add(btnDel);

		btnView.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (clienteActual != null) {
					new ClienteDetailDialog(frame, clienteActual).setVisible(true);
				}
				fireEditingStopped();
			}
		});

		btnEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (clienteActual != null) {
					ClienteEditDialog dlg = new ClienteEditDialog(frame, clienteActual);
					dlg.setVisible(true);
					if (dlg.isConfirmed()) {
						try {
							service.update(dlg.getCliente());
						} catch (RentexpresException ex) {
							SwingUtils.showError(frame, ex.getMessage());
						}
					}
				}
				fireEditingStopped();
			}
		});

		btnDel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (clienteActual != null
						&& SwingUtils.showConfirm(frame, "¿Eliminar cliente " + clienteActual.getId() + "?",
								"Confirmar borrado") == JOptionPane.YES_OPTION) {
					try {
						service.delete(clienteActual.getId());
					} catch (RentexpresException ex) {
						SwingUtils.showError(frame, ex.getMessage());
					}
				}
				fireEditingStopped();
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (table.getModel() instanceof ClienteSearchTableModel) {
			ClienteSearchTableModel model = (ClienteSearchTableModel) table.getModel();
			clienteActual = model.getClienteAt(row);
		}
		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	@Override
	public boolean isCellEditable(EventObject e) {
		return true;
	}

	private static JButton iconButton(ImageIcon ico, String tip) {
		JButton b = new JButton(ico);
		b.setToolTipText(tip);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		return b;
	}
}
