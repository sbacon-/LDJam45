package dev.chancho.ld45;

import java.awt.Rectangle;

public class Bomb {
	public int x,y,fuse;
	public Bomb(int x,int y) {
		this.x=x;
		this.y=y;
		fuse=300;
	}
	public void tick() {
		fuse--;
	}
	public Rectangle getBounds(){
		if(fuse<=0)return new Rectangle(x-48,y-48,48*3,48*3);
		else return new Rectangle(-1,-1,0,0);
	}
}
