//package com.pinguela.rentexpres.desktop.controller;
//
//import java.awt.event.ActionEvent;
//import java.text.MessageFormat;
//import java.util.List;
//
//import javax.swing.AbstractAction;
//import javax.swing.JComboBox;
//import javax.swing.JOptionPane;
//import javax.swing.JTable;
//import javax.swing.JTextField;
//
//import com.pinguela.rentexpres.desktop.model.UsuarioSearchTableModel;
//import com.pinguela.rentexpres.model.UsuarioCriteria;
//import com.pinguela.rentexpres.model.UsuarioDTO;
//import com.pinguela.rentexpres.service.UsuarioService;
//import com.pinguela.rentexpres.service.impl.UsuarioServiceImpl;
//
//public class SearchUsuarioAction extends AbstractAction {
//	private static final long serialVersionUID = 1L;
//	private final JTable table;
//	private final UsuarioService usuarioService;
//	private final JTextField txtNombre;
//	private final JTextField txtApellido1;
//	private final JTextField txtApellido2;
//	private final JTextField txtEmail;
//	private final JTextField txtUsuarioLogin;
//	private final JComboBox<?> cmbTipoUsuario;
//
//	public SearchUsuarioAction(JTable table, JTextField txtNombre, JTextField txtApellido1, JTextField txtApellido2,
//			JTextField txtEmail, JTextField txtUsuarioLogin, JComboBox<?> cmbTipoUsuario) {
//		super("Buscar");
//		this.table = table;
//		this.usuarioService = new UsuarioServiceImpl();
//		this.txtNombre = txtNombre;
//		this.txtApellido1 = txtApellido1;
//		this.txtApellido2 = txtApellido2;
//		this.txtEmail = txtEmail;
//		this.txtUsuarioLogin = txtUsuarioLogin;
//		this.cmbTipoUsuario = cmbTipoUsuario;
//	}
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		try {
//			UsuarioCriteria crit = new UsuarioCriteria();
//			String nombre = txtNombre.getText().trim();
//			if (!nombre.isEmpty()) {
//				crit.setNombre(nombre);
//			}
//			String ap1 = txtApellido1.getText().trim();
//			if (!ap1.isEmpty()) {
//				crit.setApellido1(ap1);
//			}
//			String ap2 = txtApellido2.getText().trim();
//			if (!ap2.isEmpty()) {
//				crit.setApellido2(ap2);
//			}
//			String email = txtEmail.getText().trim();
//			if (!email.isEmpty()) {
//				crit.setEmail(email);
//			}
//			String login = txtUsuarioLogin.getText().trim();
//			if (!login.isEmpty()) {
//				crit.setNombreUsuario(login);
//			}
//			if (cmbTipoUsuario.getSelectedItem() != null) {
//				try {
//					Object sel = cmbTipoUsuario.getSelectedItem();
//					
//				} catch (Exception ex) {
//
//				}
//			}
//
//			
//			List<UsuarioDTO> lista;
//			try {
//
//			} catch (NoSuchMethodError | AbstractMethodError ex) {
//			
//				lista = usuarioService.findAll();
//			}
//
//		
//			UsuarioSearchTableModel model = (UsuarioSearchTableModel) table.getModel();
//		} catch (Exception ex) {
//			JOptionPane.showMessageDialog(table, MessageFormat.format("Error al buscar usuarios: {0}", ex.getMessage()),
//					"Error", JOptionPane.ERROR_MESSAGE);
//		}
//	}
//}
