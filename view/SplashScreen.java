package com.pinguela.rentexpres.desktop.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.pinguela.rentexpres.desktop.util.AppIcons;

public class SplashScreen extends JDialog {
	private static final long serialVersionUID = 1L;

	public SplashScreen(Frame parent) {
		super(parent, "Bienvenido a RentExpres", true);
		initComponents();
		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}

	private void initComponents() {
		JPanel container = new JPanel(new BorderLayout());
		container.setBackground(Color.WHITE);
		container.setBorder(new EmptyBorder(20, 20, 20, 20));
		getContentPane().add(container);

		if (AppIcons.INICIO != null) {
			ImageIcon iconoInicio = AppIcons.INICIO;
			Image img = iconoInicio.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
			JLabel lblIcon = new JLabel(new ImageIcon(img));
			lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
			container.add(lblIcon, BorderLayout.NORTH);
		}

		JLabel lblTitle = new JLabel("RentExpres", SwingConstants.CENTER);
		lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 28f));
		lblTitle.setForeground(new Color(33, 150, 243));
		lblTitle.setBorder(new EmptyBorder(15, 0, 30, 0));
		container.add(lblTitle, BorderLayout.CENTER);

		JButton btnContinue = new JButton("Continuar");
		btnContinue.setFont(btnContinue.getFont().deriveFont(Font.PLAIN, 16f));
		btnContinue.setFocusPainted(false);
		btnContinue.setBackground(new Color(33, 150, 243));
		btnContinue.setForeground(Color.WHITE);
		btnContinue.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnContinue.setPreferredSize(new Dimension(140, 40));
		btnContinue.addActionListener(e -> dispose());

		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		btnPanel.setBackground(Color.WHITE);
		btnPanel.add(btnContinue);
		container.add(btnPanel, BorderLayout.SOUTH);

		getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

			}
		});
	}

	public void showDialog() {
		setVisible(true);
	}
}
