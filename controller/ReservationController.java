package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.pinguela.rentexpres.desktop.dialog.ReservationDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.ReservationEditDialog;
import com.pinguela.rentexpres.desktop.model.ReservationSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.service.ReservationService;
import java.util.List;

public class ReservationController {

    private static final String ERROR_CARGANDO = "Error cargando reservations:\n";
    private static final String ERROR_ACTUALIZANDO = "Error actualizando reservation:\n";
    private static final String SELECCIONE_RESERVA = "Seleccione una reservation para continuar";
    
    private final Frame frame;
    private final ReservationService service;
    private final JTable table;
    private final JButton btnVer, btnEditar;

    public ReservationController(Frame frame, ReservationService service, JTable table, 
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
    
    private ReservationDTO getSelectedReservation() {
        int row = table.getSelectedRow();
        return row < 0 ? null : ((ReservationSearchTableModel) table.getModel()).getReservationAt(row);
    }
    
    private void showDetail() {
        ReservationDTO selected = getSelectedReservation();
        if (selected != null) {
            new ReservationDetailDialog(frame, selected).setVisible(true);
        } else {
            SwingUtils.showWarning(frame, SELECCIONE_RESERVA);
        }
    }
    
    private void showEdit() {
        ReservationDTO selected = getSelectedReservation();
        if (selected == null) {
            SwingUtils.showWarning(frame, SELECCIONE_RESERVA);
            return;
        }
        
        ReservationEditDialog dialog = new ReservationEditDialog(frame, selected);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            updateReservationAsync(dialog.getReservation());
        }
    }
    
    private void loadDataAsync() {
        new Thread() {
            @Override
            public void run() {
                try {
                    final List<ReservationDTO> reservations = service.findAll();

                    com.pinguela.rentexpres.desktop.util.SwingUtils.invokeLater(new ActionCallback() {
                        @Override
                        public void execute() {
                            table.setModel(new ReservationSearchTableModel(reservations, null));
                        }
                    });
                } catch (RentexpresException ex) {
                    com.pinguela.rentexpres.desktop.util.SwingUtils.invokeLater(new ActionCallback() {
                        @Override
                        public void execute() {
                            SwingUtils.showError(frame, ERROR_CARGANDO + ex.getMessage());
                        }
                    });
                }
            }
        }.start();
    }
    
    private void updateReservationAsync(ReservationDTO reservation) {
        new Thread() {
            @Override
            public void run() {
                try {
                    service.update(reservation);

                    com.pinguela.rentexpres.desktop.util.SwingUtils.invokeLater(new ActionCallback() {
                        @Override
                        public void execute() {
                            loadDataAsync();
                        }
                    });
                } catch (RentexpresException ex) {
                    com.pinguela.rentexpres.desktop.util.SwingUtils.invokeLater(new ActionCallback() {
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