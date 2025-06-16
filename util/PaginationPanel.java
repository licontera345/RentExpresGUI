package com.pinguela.rentexpres.desktop.util;

import java.awt.FlowLayout;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel de paginación – 100 % Java 8 clásico. Define su propia interfaz
 * {@code OnPagerListener} en lugar de usar Runnable.
 */
public class PaginationPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/* Botones */
	private final JButton btnFirst = new JButton("◀︎◀︎");
	private final JButton btnPrev = new JButton("◀︎");
	private final JButton btnNext = new JButton("▶︎");
	private final JButton btnLast = new JButton("▶︎▶︎");

	private final JLabel lblInfo = new JLabel(" pág 1/1 ");

	private int currentPage = 1;
	private int totalPages = 1;

	public PaginationPanel() {
		super(new FlowLayout(FlowLayout.CENTER, 4, 2));
		add(btnFirst);
		add(btnPrev);
		add(lblInfo);
		add(btnNext);
		add(btnLast);
		setButtons();
	}

	/* ────────── interfaz clásica ───────── */
	public interface OnPagerListener {
		void onAction();
	}

	public void onFirst(OnPagerListener l) {
		final OnPagerListener listener = Objects.requireNonNull(l);
		btnFirst.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				listener.onAction();
			}
		});
	}

	public void onPrev(OnPagerListener l) {
		final OnPagerListener listener = Objects.requireNonNull(l);
		btnPrev.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				listener.onAction();
			}
		});
	}

	public void onNext(OnPagerListener l) {
		final OnPagerListener listener = Objects.requireNonNull(l);
		btnNext.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				listener.onAction();
			}
		});
	}

	public void onLast(OnPagerListener l) {
		final OnPagerListener listener = Objects.requireNonNull(l);
		btnLast.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				listener.onAction();
			}
		});
	}

	/* ────────── info página / habilitar botones ───────── */
	public void setInfo(int page, int total) {
		currentPage = Math.max(1, page);
		totalPages = Math.max(1, total);
		lblInfo.setText(" pág " + currentPage + "/" + totalPages + " ");
		setButtons();
	}

	private void setButtons() {
		btnFirst.setEnabled(currentPage > 1);
		btnPrev.setEnabled(currentPage > 1);
		btnNext.setEnabled(currentPage < totalPages);
		btnLast.setEnabled(currentPage < totalPages);
	}
}
