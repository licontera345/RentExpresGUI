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
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.model.CityDTO;
import com.pinguela.rentexpres.model.ProvinceDTO;
import com.pinguela.rentexpres.service.CityService;
import com.pinguela.rentexpres.service.ProvinceService;
import com.pinguela.rentexpres.service.impl.CityServiceImpl;
import com.pinguela.rentexpres.service.impl.ProvinceServiceImpl;
import com.toedter.calendar.JDateChooser;

import net.miginfocom.swing.MigLayout;

public class CustomerCreateDialog extends StyledDialog implements ConfirmDialog<CustomerDTO> {
	private static final long serialVersionUID = 1L;

	protected final JTextField txtName = new JTextField(18);
	protected final JTextField txtAp1 = new JTextField(18);
	protected final JTextField txtAp2 = new JTextField(18);
	protected final JDateChooser dcNac = new JDateChooser();
	protected final JTextField txtEmail = new JTextField(22);
	protected final JTextField txtPhone = new JTextField(12);

	protected final JTextField txtStreet = new JTextField(22);
	protected final JTextField txtStreetNumber = new JTextField(6);

	protected final JComboBox<ProvinceDTO> cmbProvince = new JComboBox<>();
	protected final JComboBox<CityDTO> cmbCity = new JComboBox<>();

	protected final JButton btnGuardar = new JButton("Crear");
	private final JButton btnCancelar = new JButton("Cancelar");

        private final ProvinceService provinceSvc = new ProvinceServiceImpl();
        public final CityService citySvc = new CityServiceImpl();

	private boolean confirmed = false;
	private CustomerDTO nuevo;

        public CustomerCreateDialog(Frame owner) {
                super(owner, "Nuevo Customer", true);
                initUI();
                cargarProvinces();
                pack();
                setLocationRelativeTo(owner);
        }

        private void initUI() {
                setLayout(new BorderLayout(8, 8));

                JPanel form = createContentPanel();
                form.setLayout(new MigLayout("wrap 4", "[right]10[200:200:200]20[right]10[200:200:200]", ""));

		form.add(new JLabel("Name:"));
		form.add(txtName, "span 3,growx");
		form.add(new JLabel("1.º Apellido:"));
		form.add(txtAp1);
		form.add(new JLabel("2.º Apellido:"));
		form.add(txtAp2, "wrap");
		form.add(new JLabel("Fecha nac.:"));
		form.add(dcNac);
		form.add(new JLabel("E-mail:"));
		form.add(txtEmail, "span 2,growx,wrap");
		form.add(new JLabel("Teléfono:"));
		form.add(txtPhone, "span 3,growx,wrap");

		form.add(new JLabel("Street:"));
		form.add(txtStreet, "span 3,growx");
		form.add(new JLabel("Número:"));
		form.add(txtStreetNumber, "wrap");
		form.add(new JLabel("Province:"));
		form.add(cmbProvince, "span 3,growx,wrap");
		form.add(new JLabel("City:"));
		form.add(cmbCity, "span 3,growx,wrap");

                JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                stylePrimary(btnGuardar);
                styleCancel(btnCancelar);
                buttons.add(btnGuardar);
                buttons.add(btnCancelar);

                getContentPane().add(form, BorderLayout.CENTER);
                getContentPane().add(buttons, BorderLayout.SOUTH);

		// listeners
		cmbProvince.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cargarCities();
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
		cmbProvince.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public java.awt.Component getListCellRendererComponent(JList<?> list, Object val, int i, boolean s,
					boolean f) {
				super.getListCellRendererComponent(list, val, i, s, f);
				if (val instanceof ProvinceDTO) {
					ProvinceDTO p = (ProvinceDTO) val;
					setText(p.getName());
				} else if (val instanceof CityDTO) {
					CityDTO l = (CityDTO) val;
					setText(l.getName());
				}
				return this;
			}
		});
	}

	private void cargarProvinces() {
		try {
			List<ProvinceDTO> list = provinceSvc.findAll();
			cmbProvince.setModel(new DefaultComboBoxModel<>(list.toArray(new ProvinceDTO[0])));
			cmbProvince.setRenderer(new ProvLocRenderer<>());
			cargarCities();
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error cargando provinces: " + ex.getMessage());
		}
	}

	private void cargarCities() {
		cmbCity.removeAllItems();
		ProvinceDTO prov = (ProvinceDTO) cmbProvince.getSelectedItem();
		if (prov == null)
			return;
		try {
			List<CityDTO> locs = citySvc.findByProvinceId(prov.getId());
			cmbCity.setModel(new DefaultComboBoxModel<>(locs.toArray(new CityDTO[0])));
			cmbCity.setRenderer(new ProvLocRenderer<>());
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error cargando cities: " + ex.getMessage());
		}
	}

	private void onGuardar() {
		if (!validar())
			return;

		CustomerDTO dto = buildFromForm();
                nuevo = dto;
                confirmed = true;
                dispose();
	}

	protected CustomerDTO buildFromForm() {
		CustomerDTO c = new CustomerDTO();
		c.setName(txtName.getText().trim());
		c.setLastName(txtAp1.getText().trim());
		c.setSecondLastName(txtAp2.getText().trim());
		if (dcNac.getDate() != null)
			c.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").format(dcNac.getDate()));
		c.setEmail(txtEmail.getText().trim());
		c.setPhone(txtPhone.getText().trim());
		c.setStreet(txtStreet.getText().trim());
		c.setStreetNumber(txtStreetNumber.getText().trim());

		CityDTO loc = (CityDTO) cmbCity.getSelectedItem();
		ProvinceDTO prov = (ProvinceDTO) cmbProvince.getSelectedItem();
		if (loc != null) {
			c.setCityName(loc.getName());
			c.setAddressId(loc.getId());
		}
		if (prov != null) {
			c.setProvinceName(prov.getName());
		}
		return c;
	}

	private boolean validar() {
		if (txtName.getText().trim().isEmpty()) {
			SwingUtils.showWarning(this, "El name es obligatorio.");
			return false;
		}
		return true;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

        public CustomerDTO getCustomer() {
                return nuevo;

        }

        @Override
        public CustomerDTO getValue() {
                return getCustomer();
        }

}
