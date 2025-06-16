package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import com.pinguela.rentexpres.model.Results;

/**
 * Vista base genérica para búsquedas paginadas. Subclases deben implementar el
 * filtro y la búsqueda.
 */
public abstract class SearchView<T> extends JPanel {
	private static final long serialVersionUID = 1L;

	protected final JTable table;
	protected final JToolBar toolbar;
	protected final JPanel paginador;
	protected final JLabel lblPagina;
	protected final JButton btnAnterior, btnSiguiente;
	protected int paginaActual = 1;
	protected final int tamanioPagina;

	protected SearchView(int tamanioPagina) {
		this.tamanioPagina = tamanioPagina;
		setLayout(new BorderLayout());

		// 1) Toolbar superior
		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		add(toolbar, BorderLayout.NORTH);

		// 2) Tabla de resultados
		table = new JTable();
		add(new JScrollPane(table), BorderLayout.CENTER);

		// 3) Paginador inferior
		paginador = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		lblPagina = new JLabel("Página 1");
		btnAnterior = new JButton("◀");
		btnSiguiente = new JButton("▶");
		paginador.add(btnAnterior);
		paginador.add(lblPagina);
		paginador.add(btnSiguiente);
		add(paginador, BorderLayout.SOUTH);

		btnAnterior.addActionListener(e -> {
			if (paginaActual > 1) {
				paginaActual--;
				buscar();
			}
		});
		btnSiguiente.addActionListener(e -> {
			paginaActual++;
			buscar();
		});
	}

	/** Agrega un botón con icono al toolbar. */
	protected final JButton createToolBarButton(String iconName, String tooltip, ActionListener l) {
		URL url = getClass().getClassLoader().getResource("icons/" + iconName);
		JButton btn = (url != null) ? new JButton(new ImageIcon(url)) : new JButton(tooltip);
		btn.setToolTipText(tooltip);
		btn.addActionListener(l);
		toolbar.add(btn);
		return btn;
	}

	/**
	 * Realiza la búsqueda en la página actual.
	 */
	public void buscar() {
		buscarResultados(paginaActual, tamanioPagina);
	}

	/**
	 * Implementado por subclases: ejecuta la consulta con paginación.
	 */
	protected abstract void buscarResultados(int pagina, int tamanio);

	/**
	 * Actualiza los resultados en la tabla y paginador.
	 */
	protected final void setResultados(Results<T> resultados, AbstractTableModel modelo) {
		table.setModel(modelo);
		lblPagina.setText("Página " + paginaActual + " de " + resultados.getTotalPages());
		btnAnterior.setEnabled(paginaActual > 1);
		btnSiguiente.setEnabled(paginaActual < resultados.getTotalPages());
	}

	/**
	 * Subclases pueden sobrescribir este método para limpiar sus filtros.
	 */
	public void limpiarFiltros() {
		
	}
}
