package com.pinguela.rentexpres.desktop.view;

import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.pinguela.rentexpres.desktop.util.AppTheme;
import com.pinguela.rentexpres.desktop.util.AppIcons;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.ReservationStatusDTO;
import com.toedter.calendar.JDateChooser;

import net.miginfocom.swing.MigLayout;

import java.awt.Color;

/**
 * Panel de filtros para la búsqueda de Reservations. Incluye spinners, date-picker,
 * sliders y combos; 100 % compatibles con ReservationCriteria.
 *
 * Ahora incorpora la bandera 'suppressEvents' para que, durante clear(), no se
 * disparen múltiples búsquedas automáticas.
 */
public class ReservationFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/* ───── componentes ───── */
	private final JSpinner spnReservationId = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JSpinner spnVehicleId = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JSpinner spnCustomerId = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

	private final JDateChooser dcInicio = new JDateChooser();
	private final JDateChooser dcFin = new JDateChooser();

	private final JComboBox<ReservationStatusDTO> cmbEstado = new JComboBox<>();
	private final JComboBox<String> cmbMake = new JComboBox<>();
	private final JComboBox<String> cmbModel = new JComboBox<>();

	private final JSlider sldDailyPrice = new JSlider(0, 500, 500);

        private final JTextField txtName = new JTextField();
        private final JTextField txtLastName = new JTextField();
        private final JTextField txtPhone = new JTextField();

	/* callbacks */
        private ActionCallback onChange;
        private ActionCallback toggleListener;
        private Consumer<String> onMakeChange;

	/** Nueva bandera para suprimir eventos mientras se limpia el panel */
        private boolean suppressEvents = false;

        private JLabel lbl(String t) {
                JLabel l = new JLabel(t);
                l.setForeground(AppTheme.LABEL_FG);
                return l;
        }

        public ReservationFilterPanel() {
                /* ───── apariencia ───── */
                setBackground(AppTheme.FILTER_BG);
                setBorder(BorderFactory.createTitledBorder("Filtros de Reservation"));
		setLayout(new MigLayout("wrap 4,insets 8", "[right]10[150!]20[right]10[150!][]", "[]8[]8[]8[]8[]8[]8[]"));
                dcFin.setDateFormatString("yyyy-MM-dd");

                txtName.putClientProperty("JTextField.placeholderText", "Name");
                txtLastName.putClientProperty("JTextField.placeholderText", "Apellido");
                txtPhone.putClientProperty("JTextField.placeholderText", "Teléfono");

                sldDailyPrice.setMajorTickSpacing(100);
                sldDailyPrice.setPaintTicks(true);
		sldDailyPrice.setPaintLabels(true);

		int r = 0;
		// Primera línea: ID Reservation y ID Vehicle
                add(lbl("ID Reservation:"), "cell 0 " + r);
                add(spnReservationId, "cell 1 " + r);
                JLabel lblIdVeh = lbl("ID Vehicle:");
                add(lblIdVeh, "flowx,cell 4 0");

		r++;
		// Segunda línea: ID Customer y Fecha Inicio
                add(lbl("ID Customer:"), "cell 0 " + r);
                add(spnCustomerId, "cell 1 " + r);
                JLabel lblFInicio = lbl("Fecha Inicio:");
                add(lblFInicio, "flowx,cell 4 1");

		r++;
		// Tercera línea: Fecha Fin y Estado
                add(lbl("Fecha Fin:"), "cell 0 " + r);
                add(dcFin, "cell 1 " + r);
                JLabel lblEstado = lbl("Estado:");
                add(lblEstado, "flowx,cell 4 2");

		r++;
		// Cuarta línea: Make y Model
                add(lbl("Make:"), "cell 0 " + r);
                add(cmbMake, "cell 1 " + r);
                JLabel lblModel = lbl("Model:");
                add(lblModel, "flowx,cell 4 3");

		r++;
		// Quinta línea: Precio/Día
                add(lbl("Precio/Día ≤"), "cell 0 " + r);
                add(sldDailyPrice, "cell 1 " + r + " 3");

		r++;
		// Sexta línea: Name y Apellido
                add(lbl("Name:"), "cell 0 " + r);
                add(txtName, "cell 1 " + r);
                add(lbl("Apellido:"), "cell 2 " + r);
                add(txtLastName, "cell 3 " + r);

		r++;
		// Séptima línea: Teléfono y botón Seleccionar
                add(lbl("Teléfono:"), "cell 0 " + r);
                add(txtPhone, "cell 1 " + r);
                JButton btnSel = SwingUtils.button("Seleccionar", new ActionCallback() {
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
                SwingUtils.addDocumentListener(new ActionCallback() {
                        @Override
                        public void execute() {
                                fire();
                        }
                }, txtName, txtLastName, txtPhone);

		// Agregamos componentes de la columna derecha
		add(spnVehicleId, "cell 4 0");
		dcInicio.setDateFormatString("yyyy-MM-dd");
		add(dcInicio, "cell 4 1");
		add(cmbEstado, "cell 4 2");
		add(cmbModel, "cell 4 3");

		// Listeners que deben disparar fire() si no estamos en modo clear()
               cmbModel.addActionListener(new ActionListener() {
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
               spnVehicleId.addChangeListener(new ChangeListener() {
                       @Override
                       public void stateChanged(ChangeEvent e) {
                               fire();
                       }
               });
               sldDailyPrice.addChangeListener(new ChangeListener() {
                       @Override
                       public void stateChanged(ChangeEvent e) {
                               fire();
                       }
               });
               spnReservationId.addChangeListener(new ChangeListener() {
                       @Override
                       public void stateChanged(ChangeEvent e) {
                               fire();
                       }
               });
               spnCustomerId.addChangeListener(new ChangeListener() {
                       @Override
                       public void stateChanged(ChangeEvent e) {
                               fire();
                       }
               });
               cmbMake.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               fire();
                               if (onMakeChange != null) {
                                       onMakeChange.accept(getMake());
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
	public Integer getReservationId() {
		return zeroToNull(spnReservationId);
	}

	public Integer getVehicleId() {
		return zeroToNull(spnVehicleId);
	}

	public Integer getCustomerId() {
		return zeroToNull(spnCustomerId);
	}

	public String getStartDate() {
		return dateToString(dcInicio);
	}

	public String getEndDate() {
		return dateToString(dcFin);
	}

	public String getMake() {
		return (String) cmbMake.getSelectedItem();
	}

	public String getModel() {
		return (String) cmbModel.getSelectedItem();
	}

	public BigDecimal getDailyPrice() {
		return BigDecimal.valueOf(sldDailyPrice.getValue());
	}

	public String getName() {
		return txtName.getText().trim();
	}

	public String getLastName() {
		return txtLastName.getText().trim();
	}

	public String getPhone() {
		return txtPhone.getText().trim();
	}

	public ReservationStatusDTO getEstadoSeleccionado() {
		return (ReservationStatusDTO) cmbEstado.getSelectedItem();
	}

	/* ───── control externo (el controlador conecta aquí) ───── */
        public void setOnChange(ActionCallback r) {
                this.onChange = r;
       }

        public void setToggleListener(ActionCallback r) {
                this.toggleListener = r;
       }

	public void setOnMakeChange(Consumer<String> r) {
		this.onMakeChange = r;
	}

	/* ───── método para limpiar todos los campos ───── */
	public void clear() {
		// 1) Ponemos la bandera para suprimir eventos durante el clear()
		suppressEvents = true;

		// 2) Restauramos todos los componentes a sus valores por defecto
		spnReservationId.setValue(0);
		spnVehicleId.setValue(0);
		spnCustomerId.setValue(0);

		dcInicio.setDate(null);
		dcFin.setDate(null);

		cmbEstado.setSelectedIndex(0);
		cmbMake.setSelectedIndex(-1);
		cmbModel.setSelectedIndex(-1);

		sldDailyPrice.setValue(sldDailyPrice.getMaximum());

		txtName.setText("");
		txtLastName.setText("");
		txtPhone.setText("");

		// 3) Desactivamos la supresión de eventos
		suppressEvents = false;
	}

	public JComboBox<String> getCmbMake() {
		return cmbMake;
	}

	public JComboBox<String> getCmbModel() {
		return cmbModel;
	}

	public JComboBox<ReservationStatusDTO> getCmbEstado() {
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
