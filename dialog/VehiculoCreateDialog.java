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
import com.pinguela.rentexpres.service.VehiculoService;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.pinguela.rentexpres.desktop.util.FileService;
import com.pinguela.rentexpres.desktop.util.AppConfig;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CategoriaVehiculoDTO;
import com.pinguela.rentexpres.model.EstadoVehiculoDTO;
import com.pinguela.rentexpres.model.VehiculoDTO;

public class VehiculoCreateDialog extends JDialog implements ConfirmDialog<VehiculoDTO> {
	private static final long serialVersionUID = 1L;

        private JComboBox<String> cmbMarca;
        private JComboBox<String> cmbModelo;
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
	private String imagenSeleccionada = null;

        private final FileService fileService;
        private final VehiculoService vehiculoService;

        public VehiculoCreateDialog(Frame owner, List<CategoriaVehiculoDTO> categorias,
                        List<EstadoVehiculoDTO> estados, VehiculoService vehiculoService)
                        throws RentexpresException {
                super(owner, "Crear Vehículo", true);
                try {
                        fileService = new FileService(AppConfig.getImageDir("vehiculos"));
                        this.vehiculoService = vehiculoService;
                } catch (IOException e) {
                        throw new RentexpresException("No se pudo inicializar FileService: " + e.getMessage(), e);
                }
                initComponents(categorias, estados);
		pack();
		setLocationRelativeTo(owner);
	}

        private void initComponents(List<CategoriaVehiculoDTO> categorias, List<EstadoVehiculoDTO> estados) {
                JPanel panel = new JPanel(new net.miginfocom.swing.MigLayout(
                                "wrap 4", "[right]10[200:200:200]20[right]10[200:200:200]", ""));

                cmbMarca = new JComboBox<>();
                cmbModelo = new JComboBox<>();
                txtAnio = new JTextField(6);
                txtPrecioDia = new JTextField(10);
                txtPlaca = new JTextField(12);
                txtNumeroBastidor = new JTextField(20);
                txtKilometraje = new JTextField(10);
                cbEstado = new JComboBox<>(estados.toArray(new EstadoVehiculoDTO[0]));
                cbCategoria = new JComboBox<>(categorias.toArray(new CategoriaVehiculoDTO[0]));
                btnSeleccionarImagen = new JButton("Seleccionar Imagen");
                lblImagenPreview = new JLabel();
                lblImagenPreview.setPreferredSize(new java.awt.Dimension(120, 90));

                try {
                        cargarMarcas();
                        cargarModelosPorMarca(null);
                } catch (Exception ex) {
                        // ignore
                }

                cmbMarca.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                try {
                                        cargarModelosPorMarca((String) cmbMarca.getSelectedItem());
                                } catch (Exception ex) {
                                        // ignore
                                }
                        }
                });

                panel.add(new JLabel("Marca:"));
                panel.add(cmbMarca, "growx");
                panel.add(new JLabel("Modelo:"));
                panel.add(cmbModelo, "growx");

                panel.add(new JLabel("Año Fabricación:"));
                panel.add(txtAnio, "growx");
                panel.add(new JLabel("Precio/Día:"));
                panel.add(txtPrecioDia, "growx");

                panel.add(new JLabel("Placa:"));
                panel.add(txtPlaca, "growx");
                panel.add(new JLabel("Nº Bastidor:"));
                panel.add(txtNumeroBastidor, "growx");

                panel.add(new JLabel("Kilometraje Actual:"));
                panel.add(txtKilometraje, "growx");
                panel.add(new JLabel("Estado Vehículo:"));
                panel.add(cbEstado, "growx");

                panel.add(new JLabel("Categoría Vehículo:"));
                panel.add(cbCategoria, "growx");
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
                                int resp = chooser.showOpenDialog(VehiculoCreateDialog.this);
                                if (resp == JFileChooser.APPROVE_OPTION) {
                                        File seleccionado = chooser.getSelectedFile();
                                        try {
                                                String rutaRel = fileService.store(seleccionado);
                                                imagenSeleccionada = rutaRel;
                                                ImageIcon ico = new ImageIcon(new ImageIcon(seleccionado.getAbsolutePath()).getImage()
                                                                .getScaledInstance(120, 90, java.awt.Image.SCALE_SMOOTH));
                                                lblImagenPreview.setIcon(ico);
                                        } catch (IOException ex) {
                                                SwingUtils.showError(VehiculoCreateDialog.this,
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
                if (cmbMarca.getSelectedItem() == null || ((String)cmbMarca.getSelectedItem()).trim().isEmpty()) {
                        SwingUtils.showError(this, "La marca es obligatoria.");
                        return;
                }
                if (cmbModelo.getSelectedItem() == null || ((String)cmbModelo.getSelectedItem()).trim().isEmpty()) {
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
                dto.setMarca((String) cmbMarca.getSelectedItem());
                dto.setModelo((String) cmbModelo.getSelectedItem());
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

        private void cargarMarcas() throws RentexpresException {
                cmbMarca.removeAllItems();
                java.util.Set<String> marcas = new java.util.HashSet<>();
                for (VehiculoDTO v : CatalogCache.getVehiculos(vehiculoService)) {
                        marcas.add(v.getMarca());
                }
                java.util.List<String> lista = new java.util.ArrayList<>(marcas);
                java.util.Collections.sort(lista);
                for (String m : lista) {
                        cmbMarca.addItem(m);
                }
                cmbMarca.setSelectedIndex(-1);
        }

        private void cargarModelosPorMarca(String marca) throws RentexpresException {
                cmbModelo.removeAllItems();
                java.util.Set<String> modelos = new java.util.HashSet<>();
                for (VehiculoDTO v : CatalogCache.getVehiculos(vehiculoService)) {
                        if (marca == null || marca.equals(v.getMarca())) {
                                modelos.add(v.getModelo());
                        }
                }
                java.util.List<String> lista = new java.util.ArrayList<>(modelos);
                java.util.Collections.sort(lista);
                for (String m : lista) {
                        cmbModelo.addItem(m);
                }
                cmbModelo.setSelectedIndex(-1);
        }

        @Override
        public VehiculoDTO getValue() {
                return getVehiculo();
        }
}
