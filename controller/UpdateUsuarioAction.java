package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.util.List;
import java.text.MessageFormat;

import com.pinguela.rentexpres.desktop.model.UsuarioSearchTableModel;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.UsuarioService;
import com.pinguela.rentexpres.service.impl.UsuarioServiceImpl;

public class UpdateUsuarioAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private final JTable table;
    private final UsuarioService usuarioService = new UsuarioServiceImpl();

    public UpdateUsuarioAction(JTable table) {
        super("Actualizar");
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            List<UsuarioDTO> lista = usuarioService.findAll();
            UsuarioSearchTableModel model = (UsuarioSearchTableModel) table.getModel();
            model.setUsuarios(lista);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(table,
                MessageFormat.format("Error al cargar usuarios: {0}", ex.getMessage()),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
