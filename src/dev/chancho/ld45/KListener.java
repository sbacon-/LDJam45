package dev.chancho.ld45;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class KListener implements KeyListener, MouseListener, MouseMotionListener{
	public int xdir=0;
	public int ydir=0;
	public int mx = -1;
	public int my = -1;
	
	//MOUSE
	@Override
	public void mouseClicked(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent m) {
	}

	@Override
	public void mouseExited(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent m) {
		mx=m.getX();
		my=m.getY();
	}

	@Override
	public void mouseReleased(MouseEvent m) {
		mx=-1;
		my=-1;
	}

	//MOUSE MOTION
	@Override
	public void mouseMoved(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent m) {
		mx=m.getX();
		my=m.getY();
	}
	
	
	//KEYBOARD
	@Override
	public void keyPressed(KeyEvent k) {
		int key = k.getKeyCode();
		if(key==KeyEvent.VK_W)ydir=-1;
		if(key==KeyEvent.VK_S)ydir=1;
		if(key==KeyEvent.VK_D)xdir=1;
		if(key==KeyEvent.VK_A)xdir=-1;
	}
	@Override
	public void keyReleased(KeyEvent k) {
		int key = k.getKeyCode();
		if(key==KeyEvent.VK_W)ydir=0;
		if(key==KeyEvent.VK_S)ydir=0;
		if(key==KeyEvent.VK_D)xdir=0;
		if(key==KeyEvent.VK_A)xdir=0;
	}

	@Override
	public void keyTyped(KeyEvent k) {
		// TODO Auto-generated method stub
		
	}
}
