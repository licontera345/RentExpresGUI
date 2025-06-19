package com.pinguela.rentexpres.desktop.view;

import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.pinguela.rentexpres.desktop.util.AppTheme;
import com.pinguela.rentexpres.desktop.util.AppIcons;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.EstadoReservaDTO;
import com.toedter.calendar.JDateChooser;

import net.miginfocom.swing.MigLayout;

import java.awt.Color;

/**
 * Panel de filtros para la búsqueda de Reservas. Incluye spinners, date-picker,
 * sliders y combos; 100 % compatibles con ReservaCriteria.
 *
 * Ahora incorpora la bandera 'suppressEvents' para que, durante clear(), no se
 * disparen múltiples búsquedas automáticas.
 */
public class ReservaFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/* ───── componentes ───── */
	private final JSpinner spnIdReserva = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JSpinner spnIdVehiculo = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JSpinner spnIdCliente = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

	private final JDateChooser dcInicio = new JDateChooser();
	private final JDateChooser dcFin = new JDateChooser();

	private final JComboBox<EstadoReservaDTO> cmbEstado = new JComboBox<>();
	private final JComboBox<String> cmbMarca = new JComboBox<>();
	private final JComboBox<String> cmbModelo = new JComboBox<>();

	private final JSlider sldPrecioDia = new JSlider(0, 500, 500);

        private final JTextField txtNombre = new JTextField();
        private final JTextField txtApellido1 = new JTextField();
        private final JTextField txtTelefono = new JTextField();

	/* callbacks */
        private com.pinguela.rentexpres.desktop.util.ActionCallback onChange;
        private com.pinguela.rentexpres.desktop.util.ActionCallback toggleListener;
        private Consumer<String> onMarcaChange;

	/** Nueva bandera para suprimir eventos mientras se limpia el panel */
        private boolean suppressEvents = false;

        private JLabel lbl(String t) {
                JLabel l = new JLabel(t);
                l.setForeground(AppTheme.LABEL_FG);
                return l;
        }

        public ReservaFilterPanel() {
                /* ───── apariencia ───── */
                setBackground(AppTheme.FILTER_BG);
                setBorder(BorderFactory.createTitledBorder("Filtros de Reserva"));
		setLayout(new MigLayout("wrap 4,insets 8", "[right]10[150!]20[right]10[150!][]", "[]8[]8[]8[]8[]8[]8[]"));
                dcFin.setDateFormatString("yyyy-MM-dd");

                txtNombre.putClientProperty("JTextField.placeholderText", "Nombre");
                txtNombre.putClientProperty("JTextField.leadingIcon", AppIcons.CLIENTE);
                txtApellido1.putClientProperty("JTextField.placeholderText", "Apellido");
                txtApellido1.putClientProperty("JTextField.leadingIcon", AppIcons.CLIENTE);
                txtTelefono.putClientProperty("JTextField.placeholderText", "Teléfono");
                txtTelefono.putClientProperty("JTextField.leadingIcon", AppIcons.CLIENTE);

                sldPrecioDia.setMajorTickSpacing(100);
                sldPrecioDia.setPaintTicks(true);
		sldPrecioDia.setPaintLabels(true);

		int r = 0;
		// Primera línea: ID Reserva y ID Vehículo
                add(lbl("ID Reserva:"), "cell 0 " + r);
                add(spnIdReserva, "cell 1 " + r);
                JLabel lblIdVeh = lbl("ID Vehículo:");
                add(lblIdVeh, "flowx,cell 4 0");

		r++;
		// Segunda línea: ID Cliente y Fecha Inicio
                add(lbl("ID Cliente:"), "cell 0 " + r);
                add(spnIdCliente, "cell 1 " + r);
                JLabel lblFInicio = lbl("Fecha Inicio:");
                add(lblFInicio, "flowx,cell 4 1");

		r++;
		// Tercera línea: Fecha Fin y Estado
                add(lbl("Fecha Fin:"), "cell 0 " + r);
                add(dcFin, "cell 1 " + r);
                JLabel lblEstado = lbl("Estado:");
                add(lblEstado, "flowx,cell 4 2");

		r++;
		// Cuarta línea: Marca y Modelo
                add(lbl("Marca:"), "cell 0 " + r);
                add(cmbMarca, "cell 1 " + r);
                JLabel lblModelo = lbl("Modelo:");
                add(lblModelo, "flowx,cell 4 3");

		r++;
		// Quinta línea: Precio/Día
                add(lbl("Precio/Día ≤"), "cell 0 " + r);
                add(sldPrecioDia, "cell 1 " + r + " 3");

		r++;
		// Sexta línea: Nombre y Apellido
                add(lbl("Nombre:"), "cell 0 " + r);
                add(txtNombre, "cell 1 " + r);
                add(lbl("Apellido:"), "cell 2 " + r);
                add(txtApellido1, "cell 3 " + r);

		r++;
		// Séptima línea: Teléfono y botón Seleccionar
                add(lbl("Teléfono:"), "cell 0 " + r);
                add(txtTelefono, "cell 1 " + r);
                JButton btnSel = SwingUtils.button("Seleccionar", new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                        @Override
                        public void execute() {
                                fireToggleSelect();
                        }
                });
                btnSel.setBackground(AppTheme.PRIMARY);
                btnSel.setForeground(Color.WHITE);
                add(btnSel, "cell 3 " + r + ",alignx right");

		/* ───── listeners genéricos ───── */
		// Cada vez que un JTextField cambie, invocamos fire()
                SwingUtils.addDocumentListener(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                        @Override
                        public void execute() {
                                fire();
                        }
                }, txtNombre, txtApellido1, txtTelefono);

		// Agregamos componentes de la columna derecha
		add(spnIdVehiculo, "cell 4 0");
		dcInicio.setDateFormatString("yyyy-MM-dd");
		add(dcInicio, "cell 4 1");
		add(cmbEstado, "cell 4 2");
		add(cmbModelo, "cell 4 3");

		// Listeners que deben disparar fire() si no estamos en modo clear()
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
               dcInicio.getDateEditor().addPropertyChangeListener("date", new java.beans.PropertyChangeListener() {
                       @Override
                       public void propertyChange(java.beans.PropertyChangeEvent e) {
                               fire();
                       }
               });
               spnIdVehiculo.addChangeListener(new ChangeListener() {
                       @Override
                       public void stateChanged(ChangeEvent e) {
                               fire();
                       }
               });
               sldPrecioDia.addChangeListener(new ChangeListener() {
                       @Override
                       public void stateChanged(ChangeEvent e) {
                               fire();
                       }
               });
               spnIdReserva.addChangeListener(new ChangeListener() {
                       @Override
                       public void stateChanged(ChangeEvent e) {
                               fire();
                       }
               });
               spnIdCliente.addChangeListener(new ChangeListener() {
                       @Override
                       public void stateChanged(ChangeEvent e) {
                               fire();
                       }
               });
               cmbMarca.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               fire();
                               if (onMarcaChange != null) {
                                       onMarcaChange.accept(getMarca());
                               }
                       }
               });
               dcFin.getDateEditor().addPropertyChangeListener("date", new java.beans.PropertyChangeListener() {
                       @Override
                       public void propertyChange(java.beans.PropertyChangeEvent e) {
                               fire();
                       }
               });
	}

	/*
	 * ───── getters (devuelven null o cadena vacía si el filtro está “vacío”) ─────
	 */
	public Integer getIdReserva() {
		return zeroToNull(spnIdReserva);
	}

	public Integer getIdVehiculo() {
		return zeroToNull(spnIdVehiculo);
	}

	public Integer getIdCliente() {
		return zeroToNull(spnIdCliente);
	}

	public String getFechaInicio() {
		return dateToString(dcInicio);
	}

	public String getFechaFin() {
		return dateToString(dcFin);
	}

	public String getMarca() {
		return (String) cmbMarca.getSelectedItem();
	}

	public String getModelo() {
		return (String) cmbModelo.getSelectedItem();
	}

	public BigDecimal getPrecioDia() {
		return BigDecimal.valueOf(sldPrecioDia.getValue());
	}

	public String getNombre() {
		return txtNombre.getText().trim();
	}

	public String getApellido1() {
		return txtApellido1.getText().trim();
	}

	public String getTelefono() {
		return txtTelefono.getText().trim();
	}

	public EstadoReservaDTO getEstadoSeleccionado() {
		return (EstadoReservaDTO) cmbEstado.getSelectedItem();
	}

	/* ───── control externo (el controlador conecta aquí) ───── */
        public void setOnChange(com.pinguela.rentexpres.desktop.util.ActionCallback r) {
                this.onChange = r;
       }

        public void setToggleListener(com.pinguela.rentexpres.desktop.util.ActionCallback r) {
                this.toggleListener = r;
       }

	public void setOnMarcaChange(Consumer<String> r) {
		this.onMarcaChange = r;
	}

	/* ───── método para limpiar todos los campos ───── */
	public void clear() {
		// 1) Ponemos la bandera para suprimir eventos durante el clear()
		suppressEvents = true;

		// 2) Restauramos todos los componentes a sus valores por defecto
		spnIdReserva.setValue(0);
		spnIdVehiculo.setValue(0);
		spnIdCliente.setValue(0);

		dcInicio.setDate(null);
		dcFin.setDate(null);

		cmbEstado.setSelectedIndex(0);
		cmbMarca.setSelectedIndex(-1);
		cmbModelo.setSelectedIndex(-1);

		sldPrecioDia.setValue(sldPrecioDia.getMaximum());

		txtNombre.setText("");
		txtApellido1.setText("");
		txtTelefono.setText("");

		// 3) Desactivamos la supresión de eventos
		suppressEvents = false;
	}

	public JComboBox<String> getCmbMarca() {
		return cmbMarca;
	}

	public JComboBox<String> getCmbModelo() {
		return cmbModelo;
	}

	public JComboBox<EstadoReservaDTO> getCmbEstado() {
		return cmbEstado;
	}

	/* ───── helpers internos ───── */
	private void fire() {
		// Si estamos en modo clear(), no disparamos el onChange
		if (suppressEvents) {
			return;
		}
                if (onChange != null) {
                        onChange.execute();
                }
        }

        private void fireToggleSelect() {
                if (toggleListener != null) {
                        toggleListener.execute();
                }
        }

	private static Integer zeroToNull(JSpinner sp) {
		int v = (int) sp.getValue();
		return v == 0 ? null : v;
	}

	private static String dateToString(JDateChooser dc) {
		return dc.getDate() == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(dc.getDate());
	}
}
