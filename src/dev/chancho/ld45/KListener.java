package dev.chancho.ld45;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class KListener implements KeyListener, MouseListener, MouseMotionListener{
	private boolean N,S,E,W;
	public boolean clicked, space,reset;
	public int xdir=0;
	public int ydir=0;
	public int mx = -1;
	public int my = -1;
	
	public void updateKeyState() {
		if(N && !S)ydir = -1;
		else if(S && !N)ydir = 1;
		else ydir=0;
		
		if(E && !W)xdir = 1;
		else if(W && !E)xdir = -1;
		else xdir=0;
	}
	
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
		N=false;
		E=false;
		S=false;
		W=false;
		clicked=false;
		space = false;
	}

	@Override
	public void mousePressed(MouseEvent m) {
		clicked=true;
		mx=m.getX();
		my=m.getY();
	}

	@Override
	public void mouseReleased(MouseEvent m) {
		clicked=false;
		mx=-1;
		my=-1;
	}

	//MOUSE MOTION
	@Override
	public void mouseMoved(MouseEvent m) {
		
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
		if(key==KeyEvent.VK_W||key==KeyEvent.VK_UP)N=true;
		if(key==KeyEvent.VK_D||key==KeyEvent.VK_RIGHT)E=true;
		if(key==KeyEvent.VK_S||key==KeyEvent.VK_DOWN)S=true;
		if(key==KeyEvent.VK_A||key==KeyEvent.VK_LEFT)W=true;
		if(key == KeyEvent.VK_ENTER)reset=true;
		if(key==KeyEvent.VK_SPACE) space=true;
	}
	@Override
	public void keyReleased(KeyEvent k) {
		int key = k.getKeyCode();
		if(key==KeyEvent.VK_W||key==KeyEvent.VK_UP)N=false;
		if(key==KeyEvent.VK_D||key==KeyEvent.VK_RIGHT)E=false;
		if(key==KeyEvent.VK_S||key==KeyEvent.VK_DOWN)S=false;
		if(key==KeyEvent.VK_A||key==KeyEvent.VK_LEFT)W=false;
		if(key==KeyEvent.VK_SPACE) space=false;
	}

	@Override
	public void keyTyped(KeyEvent k) {
		
	}
}
