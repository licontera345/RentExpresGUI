package com.pinguela.rentexpres.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import com.pinguela.rentexpres.desktop.util.CatalogCache;
import com.pinguela.rentexpres.service.VehicleService;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pinguela.rentexpres.desktop.util.FileService;
import com.pinguela.rentexpres.desktop.util.AppConfig;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;
import com.pinguela.rentexpres.model.VehicleStatusDTO;
import com.pinguela.rentexpres.model.VehicleDTO;

public class VehicleEditDialog extends JDialog {
	private static final long serialVersionUID = 1L;

        private JComboBox<String> cmbMake;
        private JComboBox<String> cmbModel;
        private JTextField txtAnio;
	private JTextField txtDailyPrice;
	private JTextField txtLicensePlate;
	private JTextField txtVin;
	private JTextField txtKilometraje;
	private JComboBox<VehicleStatusDTO> cbEstado;
	private JComboBox<VehicleCategoryDTO> cbCategory;
	private JLabel lblImagenPreview;
	private JButton btnSeleccionarImagen;

	private boolean confirmed = false;
	private String imagenSeleccionada;

        private final FileService fileService;
        private final VehicleService vehicleService;
	private final VehicleDTO originalDto;

        public VehicleEditDialog(Frame owner, VehicleDTO dto, List<VehicleCategoryDTO> categorys,
                        List<VehicleStatusDTO> estados, VehicleService vehicleService) throws RentexpresException {
                super(owner, "Editar Vehicle #" + dto.getId(), true);
                this.originalDto = dto;
                this.vehicleService = vehicleService;

                try {
                        fileService = new FileService(AppConfig.getImageDir("vehicles"));
                } catch (IOException e) {
                        throw new RentexpresException("No se pudo inicializar FileService: " + e.getMessage(), e);
                }

		initComponents(categorys, estados);
		rellenarCamposConDto(dto);
		pack();
		setLocationRelativeTo(owner);
	}

	private void initComponents(List<VehicleCategoryDTO> categorys, List<VehicleStatusDTO> estados) {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;

                gbc.gridx = 0;
                gbc.gridy = 0;
                panel.add(new JLabel("Make:"), gbc);
                cmbMake = new JComboBox<>();
                gbc.gridx = 1;
                panel.add(cmbMake, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
                panel.add(new JLabel("Model:"), gbc);
                cmbModel = new JComboBox<>();
                gbc.gridx = 1;
                panel.add(cmbModel, gbc);

                try {
                        cargarMakes();
                        cargarModelsPorMake(null);
                } catch (Exception ex) {
                        // ignore
                }

                cmbMake.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                try {
                                        cargarModelsPorMake((String) cmbMake.getSelectedItem());
                                } catch (Exception ex) {
                                        // ignore
                                }
                        }
                });

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Año Fabricación:"), gbc);
		txtAnio = new JTextField(6);
		gbc.gridx = 1;
		panel.add(txtAnio, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Precio/Día:"), gbc);
		txtDailyPrice = new JTextField(10);
		gbc.gridx = 1;
		panel.add(txtDailyPrice, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("LicensePlate:"), gbc);
		txtLicensePlate = new JTextField(12);
		gbc.gridx = 1;
		panel.add(txtLicensePlate, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Nº Bastidor:"), gbc);
		txtVin = new JTextField(20);
		gbc.gridx = 1;
		panel.add(txtVin, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Kilometraje Actual:"), gbc);
		txtKilometraje = new JTextField(10);
		gbc.gridx = 1;
		panel.add(txtKilometraje, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Estado Vehicle:"), gbc);
		cbEstado = new JComboBox<VehicleStatusDTO>(estados.toArray(new VehicleStatusDTO[0]));
		gbc.gridx = 1;
		panel.add(cbEstado, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Categoría Vehicle:"), gbc);
		cbCategory = new JComboBox<VehicleCategoryDTO>(categorys.toArray(new VehicleCategoryDTO[0]));
		gbc.gridx = 1;
		panel.add(cbCategory, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Imagen:"), gbc);
		btnSeleccionarImagen = new JButton("Seleccionar Imagen");
		lblImagenPreview = new JLabel();
		lblImagenPreview.setPreferredSize(new java.awt.Dimension(120, 90));

		btnSeleccionarImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
						"Imágenes (*.jpg, *.jpeg, *.png, *.gif)", "jpg", "jpeg", "png", "gif"));
				int resp = chooser.showOpenDialog(VehicleEditDialog.this);
				if (resp == JFileChooser.APPROVE_OPTION) {
					File selected = chooser.getSelectedFile();
					try {
						String rutaRel = fileService.store(selected);
						imagenSeleccionada = rutaRel;
						ImageIcon ico = new ImageIcon(new ImageIcon(selected.getAbsolutePath()).getImage()
								.getScaledInstance(120, 90, java.awt.Image.SCALE_SMOOTH));
						lblImagenPreview.setIcon(ico);
					} catch (IOException ex) {
						SwingUtils.showError(VehicleEditDialog.this,
								"No se pudo guardar la imagen: " + ex.getMessage());
					}
				}
			}
		});

		JPanel imgPanel = new JPanel(new BorderLayout());
		imgPanel.add(btnSeleccionarImagen, BorderLayout.WEST);
		imgPanel.add(lblImagenPreview, BorderLayout.CENTER);
		gbc.gridx = 1;
		panel.add(imgPanel, gbc);

		JPanel pnlButtons = new JPanel();
		JButton btnOk = new JButton("Guardar");
		JButton btnCancel = new JButton("Cancelar");
		pnlButtons.add(btnOk);
		pnlButtons.add(btnCancel);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		panel.add(pnlButtons, gbc);

		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onOk();
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				confirmed = false;
				setVisible(false);
			}
		});

		getContentPane().add(panel, BorderLayout.CENTER);
	}

	private void rellenarCamposConDto(VehicleDTO dto) {
                try {
                        cargarMakes();
                        cargarModelsPorMake(dto.getMake());
                        cmbMake.setSelectedItem(dto.getMake());
                        cmbModel.setSelectedItem(dto.getModel());
                } catch (Exception ex) {
                        cmbMake.setSelectedItem(dto.getMake());
                        cmbModel.setSelectedItem(dto.getModel());
                }
                txtAnio.setText(dto.getManufactureYear() != null ? dto.getManufactureYear().toString() : "");
		txtDailyPrice.setText(dto.getDailyPrice() != null ? dto.getDailyPrice().toString() : "");
		txtLicensePlate.setText(dto.getLicensePlate());
		txtVin.setText(dto.getVin());
		txtKilometraje.setText(dto.getCurrentMileage() != null ? dto.getCurrentMileage().toString() : "");

		for (int i = 0; i < cbEstado.getItemCount(); i++) {
			VehicleStatusDTO e = cbEstado.getItemAt(i);
			if (e.getId().equals(dto.getVehicleStatusId())) {
				cbEstado.setSelectedIndex(i);
				break;
			}
		}
		for (int i = 0; i < cbCategory.getItemCount(); i++) {
			VehicleCategoryDTO c = cbCategory.getItemAt(i);
			if (c.getId().equals(dto.getCategoryId())) {
				cbCategory.setSelectedIndex(i);
				break;
			}
		}
                if (dto.getImagePath() != null) {
                        Path imgFile = AppConfig.getImageDir("vehicles").resolve(dto.getImagePath());
			if (Files.exists(imgFile)) {
				ImageIcon ico = new ImageIcon(new ImageIcon(imgFile.toString()).getImage().getScaledInstance(120, 90,
						java.awt.Image.SCALE_SMOOTH));
				lblImagenPreview.setIcon(ico);
				imagenSeleccionada = dto.getImagePath();
			}
		}
	}

	private void onOk() {
                if (cmbMake.getSelectedItem() == null || ((String)cmbMake.getSelectedItem()).trim().isEmpty()) {
                        SwingUtils.showError(this, "La make es obligatoria.");
                        return;
                }
                if (cmbModel.getSelectedItem() == null || ((String)cmbModel.getSelectedItem()).trim().isEmpty()) {
                        SwingUtils.showError(this, "El model es obligatorio.");
                        return;
                }
		try {
			Integer.parseInt(txtAnio.getText().trim());
		} catch (NumberFormatException e) {
			SwingUtils.showError(this, "El año debe ser un número entero.");
			return;
		}
		try {
			Double.parseDouble(txtDailyPrice.getText().trim());
		} catch (NumberFormatException e) {
			SwingUtils.showError(this, "El precio/día debe ser un número.");
			return;
		}
		if (txtLicensePlate.getText().trim().isEmpty()) {
			SwingUtils.showError(this, "La licensePlate es obligatoria.");
			return;
		}
		if (txtVin.getText().trim().isEmpty()) {
			SwingUtils.showError(this, "El número de bastidor es obligatorio.");
			return;
		}
		try {
			Integer.parseInt(txtKilometraje.getText().trim());
		} catch (NumberFormatException e) {
			SwingUtils.showError(this, "El kilometraje debe ser un número entero.");
			return;
		}
		if (cbEstado.getSelectedItem() == null) {
			SwingUtils.showError(this, "Debe seleccionar un estado.");
			return;
		}
		if (cbCategory.getSelectedItem() == null) {
			SwingUtils.showError(this, "Debe seleccionar una categoría.");
			return;
		}

		confirmed = true;
		setVisible(false);
	}

	public boolean isConfirmed() {
		return confirmed;
	}

        public VehicleDTO getVehicle() {
                if (!confirmed)
                        return null;

		VehicleDTO dto = new VehicleDTO();
		dto.setId(originalDto.getId());
                dto.setMake((String) cmbMake.getSelectedItem());
                dto.setModel((String) cmbModel.getSelectedItem());
		dto.setManufactureYear(Integer.parseInt(txtAnio.getText().trim()));
		dto.setDailyPrice(Double.parseDouble(txtDailyPrice.getText().trim()));
		dto.setLicensePlate(txtLicensePlate.getText().trim());
		dto.setVin(txtVin.getText().trim());
		dto.setCurrentMileage(Integer.parseInt(txtKilometraje.getText().trim()));
		VehicleStatusDTO estado = (VehicleStatusDTO) cbEstado.getSelectedItem();
		dto.setVehicleStatusId(estado.getId());
		VehicleCategoryDTO category = (VehicleCategoryDTO) cbCategory.getSelectedItem();
		dto.setCategoryId(category.getId());
                dto.setImagePath(imagenSeleccionada);

                return dto;
        }

        private void cargarMakes() throws RentexpresException {
                cmbMake.removeAllItems();
                java.util.Set<String> makes = new java.util.HashSet<>();
                for (VehicleDTO v : CatalogCache.getVehicles(vehicleService)) {
                        makes.add(v.getMake());
                }
                java.util.List<String> lista = new java.util.ArrayList<>(makes);
                java.util.Collections.sort(lista);
                for (String m : lista) {
                        cmbMake.addItem(m);
                }
                cmbMake.setSelectedIndex(-1);
        }

        private void cargarModelsPorMake(String make) throws RentexpresException {
                cmbModel.removeAllItems();
                java.util.Set<String> models = new java.util.HashSet<>();
                for (VehicleDTO v : CatalogCache.getVehicles(vehicleService)) {
                        if (make == null || make.equals(v.getMake())) {
                                models.add(v.getModel());
                        }
                }
                java.util.List<String> lista = new java.util.ArrayList<>(models);
                java.util.Collections.sort(lista);
                for (String m : lista) {
                        cmbModel.addItem(m);
                }
                cmbModel.setSelectedIndex(-1);
        }
}
