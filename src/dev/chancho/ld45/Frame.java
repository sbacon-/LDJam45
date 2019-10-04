package dev.chancho.ld45;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Frame extends JFrame{
	private static final long serialVersionUID = -6915346476300185739L;
	public Frame() {
		initUI();
	}
	private void initUI() {
		add(new Board());
		setResizable(false);
		pack();
		
		setTitle("TITLE - made for LD45");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(()->{
			Frame f = new Frame();
			f.setVisible(true);
		});
	}

}
