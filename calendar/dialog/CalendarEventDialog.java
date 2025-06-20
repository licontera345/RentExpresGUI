package com.pinguela.rentexpres.desktop.calendar.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;

import com.pinguela.rentexpres.desktop.calendar.Calendar;
import com.pinguela.rentexpres.desktop.calendar.CalendarEvent;
import com.toedter.calendar.JDateChooser;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class CalendarEventDialog extends JDialog {
	private CalendarEvent calendarEvent;
	private JTextField text;
	private JButton okButton;
	private JButton removeButton;
	private JButton cancelButton;
	private JComboBox<LocalTime> comboHoras;
	private JPanel panelEvento;
	private JDateChooser dateChooser;
	private JLabel lblDialogType;

	public static void main(String[] args) {
		try {

			CalendarEvent newCalendarEvent = new CalendarEvent(LocalDate.of(2025, 3, 8), LocalTime.of(11, 30),
					LocalTime.of(11, 45), "Test ");
			CalendarEventDialog dialog = new CalendarEventDialog(newCalendarEvent);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CalendarEventDialog(CalendarEvent e) {
		this.calendarEvent = e;
		setTitle("CalendarEvent");
		setBounds(100, 100, 342, 200);
		getContentPane().setLayout(new BorderLayout());
		{
			panelEvento = new JPanel();
			GridBagLayout gbl_panelEvento = new GridBagLayout();

			panelEvento.setLayout(gbl_panelEvento);
			
			lblDialogType = new JLabel("New label");
			GridBagConstraints gbc_lblDialogType = new GridBagConstraints();
			gbc_lblDialogType.gridwidth = 2;
			gbc_lblDialogType.insets = new Insets(0, 0, 5, 5);
			gbc_lblDialogType.gridx = 0;
			gbc_lblDialogType.gridy = 0;
			panelEvento.add(lblDialogType, gbc_lblDialogType);
			

			dateChooser = new JDateChooser();
			dateChooser.setDate(Date.from(e.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			GridBagConstraints gbc_dateChooser = new GridBagConstraints();
			gbc_dateChooser.weightx = 0.5;
			gbc_dateChooser.insets = new Insets(20, 20, 20, 20);
			gbc_dateChooser.fill = GridBagConstraints.HORIZONTAL;
			gbc_dateChooser.gridx = 0;
			gbc_dateChooser.gridy = 1;
			panelEvento.add(dateChooser, gbc_dateChooser);			
			
			comboHoras = new JComboBox<LocalTime>();
			loadComboHoras(comboHoras);
			comboHoras.setSelectedItem(e.getStart());

			GridBagConstraints gbc_comboHoras = new GridBagConstraints();
			gbc_comboHoras.insets = new Insets(20, 20, 20, 20);
			gbc_comboHoras.weightx = 0.1;
			gbc_comboHoras.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboHoras.gridx = 1;
			gbc_comboHoras.gridy = 1;
			panelEvento.add(comboHoras, gbc_comboHoras);
		
			text = new JTextField();
			text.setText(e.getText());
			text.setColumns(e.getText().length());
			GridBagConstraints gbc_text = new GridBagConstraints();
			gbc_text.insets = new Insets(0, 10, 0, 10);
			gbc_text.gridwidth = 2;
			gbc_text.fill = GridBagConstraints.HORIZONTAL;
			gbc_text.gridx = 0;
			gbc_text.gridy = 2;
			panelEvento.add(text, gbc_text);

			getContentPane().add(panelEvento, BorderLayout.CENTER);

			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				removeButton = new JButton("Remove");
				buttonPane.add(removeButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	private void loadComboHoras(JComboBox<LocalTime> comboHoras) {

		LocalTime hora = LocalTime.of(9, 0);
		while (hora.isBefore(LocalTime.of(17, 0))) {
			comboHoras.addItem(hora);
			hora = hora.plusMinutes(15);
		}

	}

	public void setOkActionListener(ActionListener listener) {
		okButton.addActionListener(listener);
	}

	public void setRemoveActionListener(ActionListener listener) {
		removeButton.addActionListener(listener);
	}

	public LocalTime getTime() {
		return (LocalTime) comboHoras.getSelectedItem();

	}

	public void setCalendarEvent(CalendarEvent e) {
		this.calendarEvent = e;
	}

	public CalendarEvent getCalendarEvent() {
		LocalTime timeStart = (LocalTime) comboHoras.getSelectedItem();
		LocalDate date = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		CalendarEvent e = new CalendarEvent(date, timeStart, timeStart.plusMinutes(15),
				text.getText());
		return e;
	}

	public void hideRemoveButton() {
		removeButton.setVisible(false);
	}
	public void setDialogTypeLabel( String text) {
		lblDialogType.setText(text);
	}
}
