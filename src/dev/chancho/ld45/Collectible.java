package dev.chancho.ld45;

import java.awt.Rectangle;

public class Collectible {
	public int x,y,type,lifespan, floating;
	public Collectible(int x, int y, int type) {
		this.x=x;
		this.y=y;
		this.type = type;
		this.floating=1;
	}
	public int pickup() {
		return type;
	}
	public Rectangle getBounds() {
		return new Rectangle(x,y,48,48);
	}
	public void tick() {
		lifespan++;
		if(lifespan > 60 && floating==1) {
			floating = 6;			
		}
		if(lifespan > 120 && floating==6) {
			floating = 1;
			lifespan=0;			
		}
	}
}
