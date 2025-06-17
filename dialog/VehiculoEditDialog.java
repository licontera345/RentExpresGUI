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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.pinguela.rentexpres.desktop.util.FileService;
import com.pinguela.rentexpres.desktop.util.AppConfig;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CategoriaVehiculoDTO;
import com.pinguela.rentexpres.model.EstadoVehiculoDTO;
import com.pinguela.rentexpres.model.VehiculoDTO;

public class VehiculoEditDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTextField txtMarca;
	private JTextField txtModelo;
	private JTextField txtAnio;
	private JTextField txtPrecioDia;
	private JTextField txtPlaca;
	private JTextField txtNumeroBastidor;
	private JTextField txtKilometraje;
	private JComboBox<EstadoVehiculoDTO> cbEstado;
	private JComboBox<CategoriaVehiculoDTO> cbCategoria;
	private JLabel lblImagenPreview;
	private JButton btnSeleccionarImagen;

	private boolean confirmed = false;
	private String imagenSeleccionada;

	private final FileService fileService;
	private final VehiculoDTO originalDto;

	public VehiculoEditDialog(Frame owner, VehiculoDTO dto, List<CategoriaVehiculoDTO> categorias,
			List<EstadoVehiculoDTO> estados) throws RentexpresException {
		super(owner, "Editar Vehículo #" + dto.getId(), true);
		this.originalDto = dto;

                try {
                        fileService = new FileService(AppConfig.getImageDir("vehiculos"));
                } catch (IOException e) {
                        throw new RentexpresException("No se pudo inicializar FileService: " + e.getMessage(), e);
                }

		initComponents(categorias, estados);
		rellenarCamposConDto(dto);
		pack();
		setLocationRelativeTo(owner);
	}

	private void initComponents(List<CategoriaVehiculoDTO> categorias, List<EstadoVehiculoDTO> estados) {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(new JLabel("Marca:"), gbc);
		txtMarca = new JTextField(20);
		gbc.gridx = 1;
		panel.add(txtMarca, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Modelo:"), gbc);
		txtModelo = new JTextField(20);
		gbc.gridx = 1;
		panel.add(txtModelo, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Año Fabricación:"), gbc);
		txtAnio = new JTextField(6);
		gbc.gridx = 1;
		panel.add(txtAnio, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Precio/Día:"), gbc);
		txtPrecioDia = new JTextField(10);
		gbc.gridx = 1;
		panel.add(txtPrecioDia, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Placa:"), gbc);
		txtPlaca = new JTextField(12);
		gbc.gridx = 1;
		panel.add(txtPlaca, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Nº Bastidor:"), gbc);
		txtNumeroBastidor = new JTextField(20);
		gbc.gridx = 1;
		panel.add(txtNumeroBastidor, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Kilometraje Actual:"), gbc);
		txtKilometraje = new JTextField(10);
		gbc.gridx = 1;
		panel.add(txtKilometraje, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Estado Vehículo:"), gbc);
		cbEstado = new JComboBox<EstadoVehiculoDTO>(estados.toArray(new EstadoVehiculoDTO[0]));
		gbc.gridx = 1;
		panel.add(cbEstado, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Categoría Vehículo:"), gbc);
		cbCategoria = new JComboBox<CategoriaVehiculoDTO>(categorias.toArray(new CategoriaVehiculoDTO[0]));
		gbc.gridx = 1;
		panel.add(cbCategoria, gbc);

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
				int resp = chooser.showOpenDialog(VehiculoEditDialog.this);
				if (resp == JFileChooser.APPROVE_OPTION) {
					File seleccionado = chooser.getSelectedFile();
					try {
						String rutaRel = fileService.store(seleccionado);
						imagenSeleccionada = rutaRel;
						ImageIcon ico = new ImageIcon(new ImageIcon(seleccionado.getAbsolutePath()).getImage()
								.getScaledInstance(120, 90, java.awt.Image.SCALE_SMOOTH));
						lblImagenPreview.setIcon(ico);
					} catch (IOException ex) {
						SwingUtils.showError(VehiculoEditDialog.this,
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

	private void rellenarCamposConDto(VehiculoDTO dto) {
		txtMarca.setText(dto.getMarca());
		txtModelo.setText(dto.getModelo());
		txtAnio.setText(dto.getAnioFabricacion() != null ? dto.getAnioFabricacion().toString() : "");
		txtPrecioDia.setText(dto.getPrecioDia() != null ? dto.getPrecioDia().toString() : "");
		txtPlaca.setText(dto.getPlaca());
		txtNumeroBastidor.setText(dto.getNumeroBastidor());
		txtKilometraje.setText(dto.getKilometrajeActual() != null ? dto.getKilometrajeActual().toString() : "");

		for (int i = 0; i < cbEstado.getItemCount(); i++) {
			EstadoVehiculoDTO e = cbEstado.getItemAt(i);
			if (e.getId().equals(dto.getIdEstadoVehiculo())) {
				cbEstado.setSelectedIndex(i);
				break;
			}
		}
		for (int i = 0; i < cbCategoria.getItemCount(); i++) {
			CategoriaVehiculoDTO c = cbCategoria.getItemAt(i);
			if (c.getId().equals(dto.getIdCategoria())) {
				cbCategoria.setSelectedIndex(i);
				break;
			}
		}
                if (dto.getImagenPath() != null) {
                        Path imgFile = AppConfig.getImageDir("vehiculos").resolve(dto.getImagenPath());
			if (Files.exists(imgFile)) {
				ImageIcon ico = new ImageIcon(new ImageIcon(imgFile.toString()).getImage().getScaledInstance(120, 90,
						java.awt.Image.SCALE_SMOOTH));
				lblImagenPreview.setIcon(ico);
				imagenSeleccionada = dto.getImagenPath();
			}
		}
	}

	private void onOk() {
		if (txtMarca.getText().trim().isEmpty()) {
			SwingUtils.showError(this, "La marca es obligatoria.");
			return;
		}
		if (txtModelo.getText().trim().isEmpty()) {
			SwingUtils.showError(this, "El modelo es obligatorio.");
			return;
		}
		try {
			Integer.parseInt(txtAnio.getText().trim());
		} catch (NumberFormatException e) {
			SwingUtils.showError(this, "El año debe ser un número entero.");
			return;
		}
		try {
			Double.parseDouble(txtPrecioDia.getText().trim());
		} catch (NumberFormatException e) {
			SwingUtils.showError(this, "El precio/día debe ser un número.");
			return;
		}
		if (txtPlaca.getText().trim().isEmpty()) {
			SwingUtils.showError(this, "La placa es obligatoria.");
			return;
		}
		if (txtNumeroBastidor.getText().trim().isEmpty()) {
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
		if (cbCategoria.getSelectedItem() == null) {
			SwingUtils.showError(this, "Debe seleccionar una categoría.");
			return;
		}

		confirmed = true;
		setVisible(false);
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public VehiculoDTO getVehiculo() {
		if (!confirmed)
			return null;

		VehiculoDTO dto = new VehiculoDTO();
		dto.setId(originalDto.getId());
		dto.setMarca(txtMarca.getText().trim());
		dto.setModelo(txtModelo.getText().trim());
		dto.setAnioFabricacion(Integer.parseInt(txtAnio.getText().trim()));
		dto.setPrecioDia(Double.parseDouble(txtPrecioDia.getText().trim()));
		dto.setPlaca(txtPlaca.getText().trim());
		dto.setNumeroBastidor(txtNumeroBastidor.getText().trim());
		dto.setKilometrajeActual(Integer.parseInt(txtKilometraje.getText().trim()));
		EstadoVehiculoDTO estado = (EstadoVehiculoDTO) cbEstado.getSelectedItem();
		dto.setIdEstadoVehiculo(estado.getId());
		CategoriaVehiculoDTO categoria = (CategoriaVehiculoDTO) cbCategoria.getSelectedItem();
		dto.setIdCategoria(categoria.getId());
		dto.setImagenPath(imagenSeleccionada);

		return dto;
	}
}
