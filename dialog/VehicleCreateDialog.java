package com.pinguela.rentexpres.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.pinguela.rentexpres.desktop.util.CatalogCache;
import com.pinguela.rentexpres.service.VehicleService;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.pinguela.rentexpres.desktop.util.FileService;
import com.pinguela.rentexpres.desktop.util.AppConfig;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;
import com.pinguela.rentexpres.model.VehicleStatusDTO;
import com.pinguela.rentexpres.model.VehicleDTO;

public class VehicleCreateDialog extends JDialog implements ConfirmDialog<VehicleDTO> {
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
	private String imagenSeleccionada = null;

        private final FileService fileService;
        private final VehicleService vehicleService;

        public VehicleCreateDialog(Frame owner, List<VehicleCategoryDTO> categorys,
                        List<VehicleStatusDTO> estados, VehicleService vehicleService)
                        throws RentexpresException {
                super(owner, "Crear Vehicle", true);
                try {
                        fileService = new FileService(AppConfig.getImageDir("vehicles"));
                        this.vehicleService = vehicleService;
                } catch (IOException e) {
                        throw new RentexpresException("No se pudo inicializar FileService: " + e.getMessage(), e);
                }
                initComponents(categorys, estados);
		pack();
		setLocationRelativeTo(owner);
	}

        private void initComponents(List<VehicleCategoryDTO> categorys, List<VehicleStatusDTO> estados) {
                JPanel panel = new JPanel(new net.miginfocom.swing.MigLayout(
                                "wrap 4", "[right]10[200:200:200]20[right]10[200:200:200]", ""));

                cmbMake = new JComboBox<>();
                cmbModel = new JComboBox<>();
                txtAnio = new JTextField(6);
                txtDailyPrice = new JTextField(10);
                txtLicensePlate = new JTextField(12);
                txtVin = new JTextField(20);
                txtKilometraje = new JTextField(10);
                cbEstado = new JComboBox<>(estados.toArray(new VehicleStatusDTO[0]));
                cbCategory = new JComboBox<>(categorys.toArray(new VehicleCategoryDTO[0]));
                btnSeleccionarImagen = new JButton("Seleccionar Imagen");
                lblImagenPreview = new JLabel();
                lblImagenPreview.setPreferredSize(new java.awt.Dimension(120, 90));

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

                panel.add(new JLabel("Make:"));
                panel.add(cmbMake, "growx");
                panel.add(new JLabel("Model:"));
                panel.add(cmbModel, "growx");

                panel.add(new JLabel("Año Fabricación:"));
                panel.add(txtAnio, "growx");
                panel.add(new JLabel("Precio/Día:"));
                panel.add(txtDailyPrice, "growx");

                panel.add(new JLabel("LicensePlate:"));
                panel.add(txtLicensePlate, "growx");
                panel.add(new JLabel("Nº Bastidor:"));
                panel.add(txtVin, "growx");

                panel.add(new JLabel("Kilometraje Actual:"));
                panel.add(txtKilometraje, "growx");
                panel.add(new JLabel("Estado Vehicle:"));
                panel.add(cbEstado, "growx");

                panel.add(new JLabel("Categoría Vehicle:"));
                panel.add(cbCategory, "growx");
                panel.add(new JLabel("Imagen:"));
                JPanel imgPanel = new JPanel(new BorderLayout());
                imgPanel.add(btnSeleccionarImagen, BorderLayout.WEST);
                imgPanel.add(lblImagenPreview, BorderLayout.CENTER);
                panel.add(imgPanel, "span 3, growx");

                JPanel pnlButtons = new JPanel();
                JButton btnOk = new JButton("Aceptar");
                JButton btnCancel = new JButton("Cancelar");
                pnlButtons.add(btnOk);
                pnlButtons.add(btnCancel);
                panel.add(pnlButtons, "span, center, gaptop 10");

                btnSeleccionarImagen.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                                JFileChooser chooser = new JFileChooser();
                                chooser.setFileFilter(new FileNameExtensionFilter("Imágenes (*.jpg, *.jpeg, *.png, *.gif)",
                                                "jpg", "jpeg", "png", "gif"));
                                int resp = chooser.showOpenDialog(VehicleCreateDialog.this);
                                if (resp == JFileChooser.APPROVE_OPTION) {
                                        File selected = chooser.getSelectedFile();
                                        try {
                                                String rutaRel = fileService.store(selected);
                                                imagenSeleccionada = rutaRel;
                                                ImageIcon ico = new ImageIcon(new ImageIcon(selected.getAbsolutePath()).getImage()
                                                                .getScaledInstance(120, 90, java.awt.Image.SCALE_SMOOTH));
                                                lblImagenPreview.setIcon(ico);
                                        } catch (IOException ex) {
                                                SwingUtils.showError(VehicleCreateDialog.this,
                                                                "No se pudo guardar la imagen: " + ex.getMessage());
                                        }
                                }
                        }
                });

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

        @Override
        public VehicleDTO getValue() {
                return getVehicle();
        }
}
