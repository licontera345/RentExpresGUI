package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CalendarView extends JPanel {
    private static final long serialVersionUID = 1L;

    private YearMonth currentMonth;
    private final JLabel lblMonth = new JLabel();
    private final JPanel daysPanel = new JPanel(new GridLayout(0, 7));

    public CalendarView() {
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnPrev = new JButton("<");
        JButton btnNext = new JButton(">");

        btnPrev.addActionListener(e -> shiftMonth(-1));
        btnNext.addActionListener(e -> shiftMonth(1));

        header.add(btnPrev);
        header.add(lblMonth);
        header.add(btnNext);

        add(header, BorderLayout.NORTH);
        add(daysPanel, BorderLayout.CENTER);

        updateMonth(YearMonth.now());
    }

    private void shiftMonth(int delta) {
        updateMonth(currentMonth.plusMonths(delta));
    }

    private void updateMonth(YearMonth month) {
        currentMonth = month;
        lblMonth.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault())
                + " " + currentMonth.getYear());

        daysPanel.removeAll();

        for (DayOfWeek d : DayOfWeek.values()) {
            JLabel lbl = new JLabel(d.getDisplayName(TextStyle.SHORT, Locale.getDefault()), JLabel.CENTER);
            daysPanel.add(lbl);
        }

        int first = currentMonth.atDay(1).getDayOfWeek().getValue() % 7;
        for (int i = 0; i < first; i++) {
            daysPanel.add(new JLabel(""));
        }

        int length = currentMonth.lengthOfMonth();
        for (int day = 1; day <= length; day++) {
            final LocalDate date = currentMonth.atDay(day);
            JButton b = new JButton(String.valueOf(day));
            b.setMargin(new Insets(2, 2, 2, 2));
            b.addActionListener(e -> JOptionPane.showMessageDialog(this,
                    "Seleccionaste " + date));
            daysPanel.add(b);
        }

        int cells = first + length;
        int total = ((cells + 6) / 7) * 7;
        for (int i = cells; i < total; i++) {
            daysPanel.add(new JLabel(""));
        }

        revalidate();
        repaint();
    }
}
