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
//import com.pinguela.rentexpres.desktop.model.UserSearchTableModel;
//import com.pinguela.rentexpres.model.UserCriteria;
//import com.pinguela.rentexpres.model.UserDTO;
//import com.pinguela.rentexpres.service.UserService;
//import com.pinguela.rentexpres.service.impl.UserServiceImpl;
//
//public class SearchUserAction extends AbstractAction {
//	private static final long serialVersionUID = 1L;
//	private final JTable table;
//	private final UserService userService;
//	private final JTextField txtName;
//	private final JTextField txtLastName;
//	private final JTextField txtSecondLastName;
//	private final JTextField txtEmail;
//	private final JTextField txtUserLogin;
//	private final JComboBox<?> cmbUserType;
//
//	public SearchUserAction(JTable table, JTextField txtName, JTextField txtLastName, JTextField txtSecondLastName,
//			JTextField txtEmail, JTextField txtUserLogin, JComboBox<?> cmbUserType) {
//		super("Buscar");
//		this.table = table;
//		this.userService = new UserServiceImpl();
//		this.txtName = txtName;
//		this.txtLastName = txtLastName;
//		this.txtSecondLastName = txtSecondLastName;
//		this.txtEmail = txtEmail;
//		this.txtUserLogin = txtUserLogin;
//		this.cmbUserType = cmbUserType;
//	}
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		try {
//			UserCriteria crit = new UserCriteria();
//			String name = txtName.getText().trim();
//			if (!name.isEmpty()) {
//				crit.setName(name);
//			}
//			String ap1 = txtLastName.getText().trim();
//			if (!ap1.isEmpty()) {
//				crit.setLastName(ap1);
//			}
//			String ap2 = txtSecondLastName.getText().trim();
//			if (!ap2.isEmpty()) {
//				crit.setSecondLastName(ap2);
//			}
//			String email = txtEmail.getText().trim();
//			if (!email.isEmpty()) {
//				crit.setEmail(email);
//			}
//			String login = txtUserLogin.getText().trim();
//			if (!login.isEmpty()) {
//				crit.setUsername(login);
//			}
//			if (cmbUserType.getSelectedItem() != null) {
//				try {
//					Object sel = cmbUserType.getSelectedItem();
//					
//				} catch (Exception ex) {
//
//				}
//			}
//
//			
//			List<UserDTO> lista;
//			try {
//
//			} catch (NoSuchMethodError | AbstractMethodError ex) {
//			
//				lista = userService.findAll();
//			}
//
//		
//			UserSearchTableModel model = (UserSearchTableModel) table.getModel();
//		} catch (Exception ex) {
//			JOptionPane.showMessageDialog(table, MessageFormat.format("Error al buscar users: {0}", ex.getMessage()),
//					"Error", JOptionPane.ERROR_MESSAGE);
//		}
//	}
//}
