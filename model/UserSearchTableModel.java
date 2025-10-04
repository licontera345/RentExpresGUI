package com.pinguela.rentexpres.desktop.model;

import java.util.List;

import com.pinguela.rentexpres.model.UserDTO;

/**
 * Table model for users.
 */
public class UserSearchTableModel extends AbstractSearchTableModel<UserDTO> {
    private static final long serialVersionUID = 1L;

    private static final String[] DATA_COLUMNS = { "ID", "Name", "Apellido 1", "Apellido 2",
            "Email", "User", "Tipo", "Acciones" };

    private static final Class<?>[] DATA_CLASSES = { Integer.class, String.class, String.class,
            String.class, String.class, String.class, String.class, Object.class };

    public UserSearchTableModel() {
        super(DATA_COLUMNS, DATA_CLASSES);
    }

    public UserSearchTableModel(List<UserDTO> data) {
        this();
        setUsers(data);
    }

    @Override
    protected Integer getIdOf(UserDTO u) {
        return u.getId();
    }

    @Override
    protected Object getFieldAt(UserDTO u, int col) {
        switch (col) {
        case 0:
            return u.getId();
        case 1:
            return u.getName();
        case 2:
            return u.getLastName();
        case 3:
            return u.getSecondLastName();
        case 4:
            return u.getEmail();
        case 5:
            return u.getUsername();
        case 6:
            return u.getUsernameType();
        case 7:
            return null;
        default:
            return null;
        }
    }

    public void setUsers(List<UserDTO> lista) {
        setData(lista);
    }

    public UserDTO getUserAt(int row) {
        return getItem(row);
    }
}
