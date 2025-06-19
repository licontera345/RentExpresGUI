package com.pinguela.rentexpres.desktop.view;

import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.AppTheme;

import com.pinguela.rentexpres.model.CategoriaVehiculoDTO;
import com.pinguela.rentexpres.model.EstadoVehiculoDTO;

import net.miginfocom.swing.MigLayout;

import java.awt.Color;

public class VehiculoFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;

       private final JComboBox<String> cmbMarca = new JComboBox<>();
       private final JComboBox<String> cmbModelo = new JComboBox<>();
       private final JFormattedTextField ftfAnioDesde;
       private final JFormattedTextField ftfAnioHasta;
       private final JFormattedTextField ftfPrecioMax;

	
       public final JComboBox<EstadoVehiculoDTO> cmbEstado = new JComboBox<>();
       public final JComboBox<CategoriaVehiculoDTO> cmbCategoria = new JComboBox<>();

       private ActionCallback onChange;
       private ActionCallback toggleListener;
       private Consumer<String> onMarcaChange;
       private boolean suppressEvents = false;

        private JLabel lbl(String t) {
                JLabel l = new JLabel(t);
                l.setForeground(AppTheme.LABEL_FG);
                return l;
        }

        public VehiculoFilterPanel() {
                setBorder(new TitledBorder("Filtros de Vehículo"));
                setLayout(new MigLayout("wrap 4", "[right]10[150!]20[right]10[150!][][]", "[]8[]8[]8[]8[]"));
                setBackground(AppTheme.FILTER_BG);

		NumberFormat intFormat = NumberFormat.getIntegerInstance();
		NumberFormat doubleFormat = NumberFormat.getNumberInstance();

               ftfAnioDesde = new JFormattedTextField(intFormat);
               ftfAnioHasta = new JFormattedTextField(intFormat);
               ftfPrecioMax = new JFormattedTextField(doubleFormat);

               ftfAnioDesde.putClientProperty("JTextField.placeholderText", "Desde");
               ftfAnioHasta.putClientProperty("JTextField.placeholderText", "Hasta");
               ftfPrecioMax.putClientProperty("JTextField.placeholderText", "Máximo");

		// Fila 0: Marca | Modelo
               add(lbl("Marca:"), "cell 0 0");
               add(cmbMarca, "cell 1 0, growx");
               add(lbl("Modelo:"), "cell 2 0");
               add(cmbModelo, "cell 3 0, growx");

		// Fila 1: Año Desde | Año Hasta
                add(lbl("Año Desde:"), "cell 0 1");
                add(ftfAnioDesde, "cell 1 1, growx");
                add(lbl("Año Hasta:"), "cell 2 1");
                add(ftfAnioHasta, "cell 3 1, growx");

		// Fila 2: Precio Máximo | Estado
                add(lbl("Precio Máximo:"), "cell 0 2");
                add(ftfPrecioMax, "cell 1 2, growx");
                add(lbl("Estado:"), "cell 2 2");
                add(cmbEstado, "cell 3 2, growx");

		// Fila 3: Categoría
                add(lbl("Categoría:"), "cell 0 3");
                add(cmbCategoria, "cell 1 3, growx");

		// Fila 4: Botón "Seleccionar"
               JButton btnToggleSel = new JButton("Seleccionar");
               btnToggleSel.setBackground(AppTheme.PRIMARY);
               btnToggleSel.setForeground(Color.WHITE);
               btnToggleSel.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               fireToggleSelect();
                       }
               });
               add(btnToggleSel, "cell 0 4 2 1, alignx right");

               // DocumentListeners para notificar cambios
               ftfAnioDesde.getDocument().addDocumentListener(new SimpleDocumentListener());
               ftfAnioHasta.getDocument().addDocumentListener(new SimpleDocumentListener());
               ftfPrecioMax.getDocument().addDocumentListener(new SimpleDocumentListener());
               cmbMarca.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               fire();
                               if (onMarcaChange != null) {
                                       onMarcaChange.accept(getMarca());
                               }
                       }
               });
               cmbModelo.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               fire();
                       }
               });
               cmbEstado.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               fire();
                       }
               });
               cmbCategoria.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               fire();
                       }
               });
	}

	/**
	 * Getter para que el controlador pueda hacer: getCbEstado().removeAllItems()
	 */
	public JComboBox<EstadoVehiculoDTO> getCbEstado() {
		return cmbEstado;
	}

	/**
	 * Getter para que el controlador pueda hacer: getCbCategoria().removeAllItems()
	 */
        public JComboBox<CategoriaVehiculoDTO> getCbCategoria() {
                return cmbCategoria;
        }

       public JComboBox<String> getCmbMarca() {
               return cmbMarca;
       }

       public JComboBox<String> getCmbModelo() {
               return cmbModelo;
       }

       public String getMarca() {
               return (String) cmbMarca.getSelectedItem();
       }

       public String getModelo() {
               return (String) cmbModelo.getSelectedItem();
       }

	public Integer getAnioDesde() {
		try {
			Object value = ftfAnioDesde.getValue();
			return (value == null) ? null : Integer.valueOf(value.toString());
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	public Integer getAnioHasta() {
		try {
			Object value = ftfAnioHasta.getValue();
			return (value == null) ? null : Integer.valueOf(value.toString());
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	public Double getPrecioMax() {
		try {
			Object value = ftfPrecioMax.getValue();
			return (value == null) ? null : Double.valueOf(value.toString());
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	public EstadoVehiculoDTO getEstadoSeleccionado() {
		return (EstadoVehiculoDTO) cmbEstado.getSelectedItem();
	}

	public CategoriaVehiculoDTO getCategoriaSeleccionada() {
		return (CategoriaVehiculoDTO) cmbCategoria.getSelectedItem();
	}

       public void clear() {
               suppressEvents = true;

               cmbMarca.setSelectedIndex(-1);
               cmbModelo.setSelectedIndex(-1);
               ftfAnioDesde.setValue(null);
               ftfAnioHasta.setValue(null);
               ftfPrecioMax.setValue(null);
               cmbEstado.setSelectedIndex(-1);
               cmbCategoria.setSelectedIndex(-1);

               suppressEvents = false;
               fire();
       }

        public void setOnChange(ActionCallback r) {
                this.onChange = r;
       }

       public void setToggleListener(ActionCallback r) {
               this.toggleListener = r;
       }

       public void setOnMarcaChange(Consumer<String> r) {
               this.onMarcaChange = r;
       }

       private void fire() {
               if (suppressEvents)
                       return;
               if (onChange != null)
                       onChange.execute();
       }

        private void fireToggleSelect() {
                if (toggleListener != null)
                        toggleListener.execute();
        }

	private class SimpleDocumentListener implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			fire();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			fire();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			fire();
		}
	}
}
