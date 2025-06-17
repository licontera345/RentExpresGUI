package com.pinguela.rentexpres.desktop.model;

import java.util.List;

import com.pinguela.rentexpres.model.UsuarioDTO;

/**
 * Table model for usuarios.
 */
public class UsuarioSearchTableModel extends AbstractSearchTableModel<UsuarioDTO> {
    private static final long serialVersionUID = 1L;

    private static final String[] DATA_COLUMNS = { "ID", "Nombre", "Apellido 1", "Apellido 2",
            "Email", "Usuario", "Tipo", "Acciones" };

    private static final Class<?>[] DATA_CLASSES = { Integer.class, String.class, String.class,
            String.class, String.class, String.class, String.class, Object.class };

    public UsuarioSearchTableModel() {
        super(DATA_COLUMNS, DATA_CLASSES);
    }

    public UsuarioSearchTableModel(List<UsuarioDTO> data) {
        this();
        setUsuarios(data);
    }

    @Override
    protected Integer getIdOf(UsuarioDTO u) {
        return u.getId();
    }

    @Override
    protected Object getFieldAt(UsuarioDTO u, int col) {
        switch (col) {
        case 0:
            return u.getId();
        case 1:
            return u.getNombre();
        case 2:
            return u.getApellido1();
        case 3:
            return u.getApellido2();
        case 4:
            return u.getEmail();
        case 5:
            return u.getNombreUsuario();
        case 6:
            return u.getNombreTipoUsuario();
        case 7:
            return null;
        default:
            return null;
        }
    }

    public void setUsuarios(List<UsuarioDTO> lista) {
        setData(lista);
    }

    public UsuarioDTO getUsuarioAt(int row) {
        return getItem(row);
    }
}
