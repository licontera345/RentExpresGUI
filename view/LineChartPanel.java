package com.pinguela.rentexpres.desktop.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Simple line chart panel for visualizing integer data series.
 */
public class LineChartPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private List<Integer> values = new ArrayList<>();
    private List<String> labels = new ArrayList<>();

    public LineChartPanel() {
        setPreferredSize(new Dimension(300, 200));
    }

    public void setData(List<Integer> values, List<String> labels) {
        this.values = values != null ? new ArrayList<>(values) : new ArrayList<>();
        this.labels = labels != null ? new ArrayList<>(labels) : new ArrayList<>();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (values.isEmpty())
            return;
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth();
        int h = getHeight();
        int padding = 40;
        int labelPad = 20;
        int originX = padding + labelPad;
        int originY = h - padding - labelPad;

        int chartW = w - 2 * padding - labelPad;
        int chartH = h - 2 * padding - labelPad;

        int max = 0;
        for (int v : values) {
            if (v > max)
                max = v;
        }
        if (max == 0)
            max = 1;

        double xScale = values.size() > 1 ? (double) chartW / (values.size() - 1) : chartW;
        double yScale = (double) chartH / max;

        // axes
        g2.setColor(Color.BLACK);
        g2.drawLine(originX, originY, originX, padding);
        g2.drawLine(originX, originY, originX + chartW, originY);

        // y labels
        int divisions = 5;
        for (int i = 0; i <= divisions; i++) {
            int y = originY - (int) (i * chartH / divisions);
            g2.drawLine(originX - 5, y, originX, y);
            String lbl = String.valueOf(max * i / divisions);
            g2.drawString(lbl, 5, y + 5);
        }

        // x labels
        for (int i = 0; i < labels.size(); i++) {
            int x = originX + (int) (i * xScale);
            g2.drawLine(x, originY, x, originY + 5);
            String lbl = labels.get(i);
            if (lbl != null)
                g2.drawString(lbl, x - 10, originY + 20);
        }

        // line path
        g2.setColor(new Color(33, 150, 243));
        Path2D path = new Path2D.Double();
        for (int i = 0; i < values.size(); i++) {
            int x = originX + (int) (i * xScale);
            int y = originY - (int) (values.get(i) * yScale);
            if (i == 0)
                path.moveTo(x, y);
            else
                path.lineTo(x, y);
            g2.fillOval(x - 3, y - 3, 6, 6);
        }
        g2.draw(path);
        g2.dispose();
    }
}
