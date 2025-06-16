package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.pinguela.rentexpres.desktop.dialog.ReservaDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.ReservaEditDialog;
import com.pinguela.rentexpres.desktop.model.ReservaSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.service.ReservaService;
import java.util.List;

public class ReservaController {

    private static final String ERROR_CARGANDO = "Error cargando reservas:\n";
    private static final String ERROR_ACTUALIZANDO = "Error actualizando reserva:\n";
    private static final String SELECCIONE_RESERVA = "Seleccione una reserva para continuar";
    
    private final Frame frame;
    private final ReservaService service;
    private final JTable table;
    private final JButton btnVer, btnEditar;

    public ReservaController(Frame frame, ReservaService service, JTable table, 
                           JButton btnVer, JButton btnEditar) throws RentexpresException {
        validateConstructorArgs(frame, service, table, btnVer, btnEditar);
        
        this.frame = frame;
        this.service = service;
        this.table = table;
        this.btnVer = btnVer;
        this.btnEditar = btnEditar;
        
        setupController();
    }
    
    private void validateConstructorArgs(Object... args) {
        for (Object arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException("Los argumentos del controlador no pueden ser nulos");
            }
        }
    }
    
    private void setupController() throws RentexpresException {
        bindActions();
        loadDataAsync();
    }
    
    private void bindActions() {
        btnVer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDetail();
            }
        });
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEdit();
            }
        });
    }
    
    private ReservaDTO getSelectedReserva() {
        int row = table.getSelectedRow();
        return row < 0 ? null : ((ReservaSearchTableModel) table.getModel()).getReservaAt(row);
    }
    
    private void showDetail() {
        ReservaDTO selected = getSelectedReserva();
        if (selected != null) {
            new ReservaDetailDialog(frame, selected).setVisible(true);
        } else {
            SwingUtils.showWarning(frame, SELECCIONE_RESERVA);
        }
    }
    
    private void showEdit() {
        ReservaDTO selected = getSelectedReserva();
        if (selected == null) {
            SwingUtils.showWarning(frame, SELECCIONE_RESERVA);
            return;
        }
        
        ReservaEditDialog dialog = new ReservaEditDialog(frame, selected);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            updateReservaAsync(dialog.getReserva());
        }
    }
    
    private void loadDataAsync() {
        new Thread() {
            @Override
            public void run() {
                try {
                    final List<ReservaDTO> reservas = service.findAll();

                    com.pinguela.rentexpres.desktop.util.SwingUtils.invokeLater(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                        @Override
                        public void execute() {
                            table.setModel(new ReservaSearchTableModel(reservas, null));
                        }
                    });
                } catch (RentexpresException ex) {
                    com.pinguela.rentexpres.desktop.util.SwingUtils.invokeLater(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                        @Override
                        public void execute() {
                            SwingUtils.showError(frame, ERROR_CARGANDO + ex.getMessage());
                        }
                    });
                }
            }
        }.start();
    }
    
    private void updateReservaAsync(ReservaDTO reserva) {
        new Thread() {
            @Override
            public void run() {
                try {
                    service.update(reserva);

                    com.pinguela.rentexpres.desktop.util.SwingUtils.invokeLater(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                        @Override
                        public void execute() {
                            loadDataAsync();
                        }
                    });
                } catch (RentexpresException ex) {
                    com.pinguela.rentexpres.desktop.util.SwingUtils.invokeLater(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                        @Override
                        public void execute() {
                            SwingUtils.showError(frame, ERROR_ACTUALIZANDO + ex.getMessage());
                        }
                    });
                }
            }
        }.start();
    }
    
    public void refreshData() {
        loadDataAsync();
    }
}