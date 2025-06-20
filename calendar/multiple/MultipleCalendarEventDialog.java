package com.pinguela.rentexpres.desktop.calendar.multiple.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import com.pinguela.rentexpres.desktop.calendar.multiple.MultipleCalendar;
import com.pinguela.rentexpres.desktop.calendar.multiple.MultipleCalendarEvent;
import com.toedter.calendar.JDateChooser;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class MultipleCalendarEventDialog extends JDialog {
	private MultipleCalendar calendar;
	private MultipleCalendarEvent calendarEvent;
	private JTextField text;
	private JButton okButton;
	private JButton removeButton;
	private JButton cancelButton;
	private JComboBox<LocalTime> comboHoras;
	private JPanel panelEvento;
	private JDateChooser dateChooser;
	private JLabel lblDialog;
	private JLabel lblComboType;
	private JComboBox<Integer> comboType;

	public static void main(String[] args) {
		try {

			MultipleCalendarEvent newCalendarEvent = new MultipleCalendarEvent(LocalDate.of(2025, 3, 8), LocalTime.of(11, 30),
					LocalTime.of(11, 45), "Test ", 0);
			MultipleCalendarEventDialog dialog = new MultipleCalendarEventDialog(newCalendarEvent, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MultipleCalendarEventDialog(MultipleCalendarEvent e, MultipleCalendar calendar) {
		this.calendar=calendar;
		this.calendarEvent = e;
		setTitle("CalendarEvent");
		setBounds(100, 100, 342, 200);
		getContentPane().setLayout(new BorderLayout());
		{
			panelEvento = new JPanel();
			GridBagLayout gbl_panelEvento = new GridBagLayout();
			gbl_panelEvento.columnWeights = new double[]{0.0, 0.0, 1.0};

			panelEvento.setLayout(gbl_panelEvento);
			


			dateChooser = new JDateChooser();
			dateChooser.setDate(Date.from(e.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			GridBagConstraints gbc_dateChooser = new GridBagConstraints();
			gbc_dateChooser.weightx = 0.5;
			gbc_dateChooser.insets = new Insets(20, 20, 20, 5);
			gbc_dateChooser.fill = GridBagConstraints.HORIZONTAL;
			gbc_dateChooser.gridx = 0;
			gbc_dateChooser.gridy = 1;
			panelEvento.add(dateChooser, gbc_dateChooser);			
			
			comboHoras = new JComboBox<LocalTime>();
			loadComboHoras(comboHoras);
			comboHoras.setSelectedItem(e.getStart());

			GridBagConstraints gbc_comboHoras = new GridBagConstraints();
			gbc_comboHoras.insets = new Insets(20, 5, 20, 20);
			gbc_comboHoras.weightx = 0.1;
			gbc_comboHoras.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboHoras.gridx = 1;
			gbc_comboHoras.gridy = 1;
			panelEvento.add(comboHoras, gbc_comboHoras);
			
			
			lblDialog = new JLabel("New label");
			GridBagConstraints gbc_lblDialog = new GridBagConstraints();
			gbc_lblDialog.gridwidth = 2;
			gbc_lblDialog.insets = new Insets(0, 0, 5, 5);
			gbc_lblDialog.gridx = 0;
			gbc_lblDialog.gridy = 0;
			panelEvento.add(lblDialog, gbc_lblDialog);
			
			lblComboType = new JLabel("Type");
			GridBagConstraints gbc_lblType = new GridBagConstraints();
			gbc_lblType.insets = new Insets(0, 0, 5, 10);
			gbc_lblType.gridx = 2;
			gbc_lblType.gridy = 0;
			panelEvento.add(lblComboType, gbc_lblType);
			
			comboType = new JComboBox<Integer>();
			loadComboType(comboType);
			comboType.setSelectedItem(e.getIndex());
			GridBagConstraints gbc_comboType = new GridBagConstraints();
			gbc_comboType.insets = new Insets(0, 0, 0, 20);
			gbc_comboType.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboType.gridx = 2;
			gbc_comboType.gridy = 1;
			panelEvento.add(comboType, gbc_comboType);
		
			text = new JTextField();
			text.setText(e.getText());
			text.setColumns(e.getText().length());
			GridBagConstraints gbc_text = new GridBagConstraints();
			gbc_text.insets = new Insets(0, 10, 0, 10);
			gbc_text.gridwidth = 3;
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

	private void loadComboType(JComboBox<Integer> comboType) {
		for (int i=0; i<calendar.getMaxSimulEvents();i++ ) {
			comboType.addItem(i);
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

	public void setMultipleCalendarEvent(MultipleCalendarEvent e) {
		this.calendarEvent = e;
	}

	public MultipleCalendarEvent getMultipleCalendarEvent() {
		LocalTime timeStart = (LocalTime) comboHoras.getSelectedItem();
		LocalDate date = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		MultipleCalendarEvent e = new MultipleCalendarEvent(date, timeStart, timeStart.plusMinutes(15),
				text.getText(), (Integer)comboType.getSelectedItem());
		return e;
	}

	public void hideRemoveButton() {
		removeButton.setVisible(false);
	}
	public void setDialogLabel( String text) {
		lblDialog.setText(text);
	}
	public void setDialogErrorLabel (String text) {
		lblDialog.setForeground(Color.RED);
		lblDialog.setText(text);
	}
}
