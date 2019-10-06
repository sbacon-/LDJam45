package dev.chancho.ld45;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Enemy {
	public int x,y,type,health;
	public int speed,ticks;
	public Enemy(int x, int y, int type) {
		speed = 3;
		this.health=3;
		this.x=x;
		this.y=y;
		this.type = type;
	}
	public Rectangle getBounds() {
		return new Rectangle(x,y,48,48);
	}
	public void tick(int px, int py,ArrayList<Enemy> enemies){
		if(ticks%speed==0) {
			if(x>px)x--;
			else x++;
			if(y>py)y--;
			else y++;
		}
		ticks++;
	}
}
