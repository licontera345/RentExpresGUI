package com.pinguela.rentexpres.desktop.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Objects;

import static com.pinguela.rentexpres.desktop.util.AppIcons.*;

public abstract class AbstractSearchActionsView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final JButton btnNuevo;
	protected final JButton btnLimpiarFiltros;
	protected final JButton btnEliminar;

	public AbstractSearchActionsView() {
		setLayout(new FlowLayout(FlowLayout.LEFT));

		btnNuevo = new JButton("Nuevo", NEW);
		btnLimpiarFiltros = new JButton("Limpiar Filtros", CLEAR);
		btnEliminar = new JButton("Eliminar seleccionados", DELETE);

		makeFlat(btnNuevo, btnLimpiarFiltros, btnEliminar);

		add(btnNuevo);
		add(btnLimpiarFiltros);
		add(btnEliminar);
	}

	private void makeFlat(JButton... bs) {
		for (JButton b : bs) {
			b.setBorderPainted(false);
			b.setFocusPainted(false);
			b.setContentAreaFilled(false);
			if (b.getToolTipText() == null || b.getToolTipText().trim().isEmpty()) {
				b.setToolTipText(b.getText());
			}
		}
	}

	// Métodos públicos lambda-style (para controladores)
       public void onNuevo(Runnable r) {
               btnNuevo.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               Objects.requireNonNull(r).run();
                       }
               });
       }

       public void onLimpiar(Runnable r) {
               btnLimpiarFiltros.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               Objects.requireNonNull(r).run();
                       }
               });
       }

       public void onBorrarSeleccionados(Runnable r) {
               btnEliminar.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               Objects.requireNonNull(r).run();
                       }
               });
       }

	public JButton getBtnNuevo() {
		return btnNuevo;
	}

	public JButton getBtnLimpiarFiltros() {
		return btnLimpiarFiltros;
	}

	public JButton getBtnEliminar() {
		return btnEliminar;
	}
}
