package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JTable;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.ActionCallbackThread;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.pinguela.rentexpres.desktop.dialog.ClienteDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.ClienteEditDialog;
import com.pinguela.rentexpres.desktop.model.ClienteSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.service.ClienteService;
import java.util.List;

public class ClienteController {

	private static final String TXT_CARGA_ERR = "Error cargando clientes:\n";
	private static final String TXT_UPD_ERR = "Error actualizando cliente:\n";
	private static final String TXT_SELECCIONA = "Selecciona un cliente para continuar";

	private final Frame frame;
	private final ClienteService service;
	private final JTable table;
	private final JButton btnVer;
	private final JButton btnEditar;

	public ClienteController(Frame frame, ClienteService service, JTable table, JButton btnVer, JButton btnEditar)
			throws RentexpresException {

		if (frame == null || service == null || table == null || btnVer == null || btnEditar == null)
			throw new IllegalArgumentException("Los argumentos del controlador no pueden ser nulos");

		this.frame = frame;
		this.service = service;
		this.table = table;
		this.btnVer = btnVer;
		this.btnEditar = btnEditar;

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

	private ClienteDTO getSelectedCliente() {
		int row = table.getSelectedRow();
		return row < 0 ? null : ((ClienteSearchTableModel) table.getModel()).getClienteAt(row);
	}

	private void showDetail() {
		ClienteDTO dto = getSelectedCliente();
		if (dto == null) {
			SwingUtils.showWarning(frame, TXT_SELECCIONA);
			return;
		}
		new ClienteDetailDialog(frame, dto).setVisible(true);
	}

	private void showEdit() {
		ClienteDTO dto = getSelectedCliente();
		if (dto == null) {
			SwingUtils.showWarning(frame, TXT_SELECCIONA);
			return;
		}

                ClienteEditDialog dlg = new ClienteEditDialog(frame, dto);
		dlg.setVisible(true);

		if (dlg.isConfirmed())
			updateClienteAsync(dlg.getCliente());
	}

        private void loadDataAsync() {
                new ActionCallbackThread(new ActionCallback() {
                        @Override
                        public void execute() {
                                try {
                                        List<ClienteDTO> clientes = service.findAll();
                                        SwingUtils.invokeLater(new ActionCallback() {
                                                @Override
                                                public void execute() {
                                                        table.setModel(new ClienteSearchTableModel(clientes,
                                                                        ClienteSearchTableModel.buildLocalidadMap(clientes),
                                                                        ClienteSearchTableModel.buildProvinciaMap(clientes)));
                                                }
                                        });
                                } catch (RentexpresException ex) {
                                        SwingUtils.invokeLater(new ActionCallback() {
                                                @Override
                                                public void execute() {
                                                        SwingUtils.showError(frame, TXT_CARGA_ERR + ex.getMessage());
                                                }
                                        });
                                }
                        }
                }).start();
        }

        private void updateClienteAsync(ClienteDTO cli) {
                new ActionCallbackThread(new ActionCallback() {
                        @Override
                        public void execute() {
                                try {
                                        service.update(cli);
                                        SwingUtils.invokeLater(new ActionCallback() {
                                                @Override
                                                public void execute() {
                                                        loadDataAsync();
                                                }
                                        });
                                } catch (RentexpresException ex) {
                                        SwingUtils.invokeLater(new ActionCallback() {
                                                @Override
                                                public void execute() {
                                                        SwingUtils.showError(frame, TXT_UPD_ERR + ex.getMessage());
                                                }
                                        });
                                }
                        }
                }).start();
        }

	public void refreshData() {
		loadDataAsync();
	}
}
