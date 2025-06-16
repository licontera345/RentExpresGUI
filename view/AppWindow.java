package com.pinguela.rentexpres.desktop.view;

import com.pinguela.rentexpres.desktop.util.SwingUtils;

import javax.swing.*;
import java.awt.*;

public abstract class AppWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	public AppWindow(String title) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(1000, 600));
		SwingUtils.center(this, null);
	}
}
