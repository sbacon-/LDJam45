package dev.chancho.ld45;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable{
	private static final long serialVersionUID = -7392545151087699625L;
	private final int WIDTH = 800;
	private final int HEIGHT= 600;
	private final int DELAY= 60;
	
	public Thread timer;
	
	public Board() {
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setBackground(Color.decode("#0000ff"));
		addKeyListener(new KListener());
		setFocusable(true);
		requestFocus();
		
		init();
	}
	private void init() {
	
	}
	
	private void tick() {
		
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		render(g);
	}
	private void render(Graphics g){
		
		
		Toolkit.getDefaultToolkit().sync();
	}	
	@Override
	public void addNotify(){
		super.addNotify();
		timer = new Thread(this);
	}
	@Override
	public void run() {
		long current,delta,sleep;
		current = System.currentTimeMillis();
		while(true) {
			tick();
			repaint();
			delta=System.currentTimeMillis()-current;
			sleep=DELAY-delta;
			if(sleep<0)sleep=2;
			try{
				Thread.sleep(sleep);
			}catch(InterruptedException e){
				String msg = String.format("Thread interrupted: %s", e.getMessage());
				JOptionPane.showMessageDialog(this, msg,"ERROR",JOptionPane.ERROR_MESSAGE);
			}
		}		
	}	
}
