package com.pinguela.rentexpres.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import com.pinguela.rentexpres.desktop.dialog.StyledDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.pinguela.rentexpres.desktop.renderer.ProvLocRenderer;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.model.LocalidadDTO;
import com.pinguela.rentexpres.model.ProvinciaDTO;
import com.pinguela.rentexpres.service.LocalidadService;
import com.pinguela.rentexpres.service.ProvinciaService;
import com.pinguela.rentexpres.service.impl.LocalidadServiceImpl;
import com.pinguela.rentexpres.service.impl.ProvinciaServiceImpl;
import com.toedter.calendar.JDateChooser;

import net.miginfocom.swing.MigLayout;

public class ClienteCreateDialog extends StyledDialog implements ConfirmDialog<ClienteDTO> {
	private static final long serialVersionUID = 1L;

	protected final JTextField txtNombre = new JTextField(18);
	protected final JTextField txtAp1 = new JTextField(18);
	protected final JTextField txtAp2 = new JTextField(18);
	protected final JDateChooser dcNac = new JDateChooser();
	protected final JTextField txtEmail = new JTextField(22);
	protected final JTextField txtTelefono = new JTextField(12);

	protected final JTextField txtCalle = new JTextField(22);
	protected final JTextField txtNumero = new JTextField(6);

	protected final JComboBox<ProvinciaDTO> cmbProvincia = new JComboBox<>();
	protected final JComboBox<LocalidadDTO> cmbLocalidad = new JComboBox<>();

	protected final JButton btnGuardar = new JButton("Crear");
	private final JButton btnCancelar = new JButton("Cancelar");

        private final ProvinciaService provinciaSvc = new ProvinciaServiceImpl();
        public final LocalidadService localidadSvc = new LocalidadServiceImpl();

	private boolean confirmed = false;
	private ClienteDTO nuevo;

        public ClienteCreateDialog(Frame owner) {
                super(owner, "Nuevo Cliente", true);
                initUI();
                cargarProvincias();
                pack();
                setLocationRelativeTo(owner);
        }

        private void initUI() {
                setLayout(new BorderLayout(8, 8));

                JPanel form = createContentPanel();
                form.setLayout(new MigLayout("wrap 4", "[right]10[200:200:200]20[right]10[200:200:200]", ""));

		form.add(new JLabel("Nombre:"));
		form.add(txtNombre, "span 3,growx");
		form.add(new JLabel("1.º Apellido:"));
		form.add(txtAp1);
		form.add(new JLabel("2.º Apellido:"));
		form.add(txtAp2, "wrap");
		form.add(new JLabel("Fecha nac.:"));
		form.add(dcNac);
		form.add(new JLabel("E-mail:"));
		form.add(txtEmail, "span 2,growx,wrap");
		form.add(new JLabel("Teléfono:"));
		form.add(txtTelefono, "span 3,growx,wrap");

		form.add(new JLabel("Calle:"));
		form.add(txtCalle, "span 3,growx");
		form.add(new JLabel("Número:"));
		form.add(txtNumero, "wrap");
		form.add(new JLabel("Provincia:"));
		form.add(cmbProvincia, "span 3,growx,wrap");
		form.add(new JLabel("Localidad:"));
		form.add(cmbLocalidad, "span 3,growx,wrap");

                JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                stylePrimary(btnGuardar);
                styleCancel(btnCancelar);
                buttons.add(btnGuardar);
                buttons.add(btnCancelar);

                getContentPane().add(form, BorderLayout.CENTER);
                getContentPane().add(buttons, BorderLayout.SOUTH);

		// listeners
		cmbProvincia.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cargarLocalidades();
			}
		});
		btnGuardar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onGuardar();
			}
		});
		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cmbProvincia.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public java.awt.Component getListCellRendererComponent(JList<?> list, Object val, int i, boolean s,
					boolean f) {
				super.getListCellRendererComponent(list, val, i, s, f);
				if (val instanceof ProvinciaDTO) {
					ProvinciaDTO p = (ProvinciaDTO) val;
					setText(p.getNombre());
				} else if (val instanceof LocalidadDTO) {
					LocalidadDTO l = (LocalidadDTO) val;
					setText(l.getNombre());
				}
				return this;
			}
		});
	}

	private void cargarProvincias() {
		try {
			List<ProvinciaDTO> list = provinciaSvc.findAll();
			cmbProvincia.setModel(new DefaultComboBoxModel<>(list.toArray(new ProvinciaDTO[0])));
			cmbProvincia.setRenderer(new ProvLocRenderer<>());
			cargarLocalidades();
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error cargando provincias: " + ex.getMessage());
		}
	}

	private void cargarLocalidades() {
		cmbLocalidad.removeAllItems();
		ProvinciaDTO prov = (ProvinciaDTO) cmbProvincia.getSelectedItem();
		if (prov == null)
			return;
		try {
			List<LocalidadDTO> locs = localidadSvc.findByProvinciaId(prov.getId());
			cmbLocalidad.setModel(new DefaultComboBoxModel<>(locs.toArray(new LocalidadDTO[0])));
			cmbLocalidad.setRenderer(new ProvLocRenderer<>());
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error cargando localidades: " + ex.getMessage());
		}
	}

	private void onGuardar() {
		if (!validar())
			return;

		ClienteDTO dto = buildFromForm();
                nuevo = dto;
                confirmed = true;
                dispose();
	}

	protected ClienteDTO buildFromForm() {
		ClienteDTO c = new ClienteDTO();
		c.setNombre(txtNombre.getText().trim());
		c.setApellido1(txtAp1.getText().trim());
		c.setApellido2(txtAp2.getText().trim());
		if (dcNac.getDate() != null)
			c.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").format(dcNac.getDate()));
		c.setEmail(txtEmail.getText().trim());
		c.setTelefono(txtTelefono.getText().trim());
		c.setCalle(txtCalle.getText().trim());
		c.setNumero(txtNumero.getText().trim());

		LocalidadDTO loc = (LocalidadDTO) cmbLocalidad.getSelectedItem();
		ProvinciaDTO prov = (ProvinciaDTO) cmbProvincia.getSelectedItem();
		if (loc != null) {
			c.setNombreLocalidad(loc.getNombre());
			c.setIdDireccion(loc.getId());
		}
		if (prov != null) {
			c.setNombreProvincia(prov.getNombre());
		}
		return c;
	}

	private boolean validar() {
		if (txtNombre.getText().trim().isEmpty()) {
			SwingUtils.showWarning(this, "El nombre es obligatorio.");
			return false;
		}
		return true;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

        public ClienteDTO getCliente() {
                return nuevo;

        }

        @Override
        public ClienteDTO getValue() {
                return getCliente();
        }

}
