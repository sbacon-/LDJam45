package dev.chancho.ld45;

import java.awt.Rectangle;

public class Player {
	private int x,y;
	public Projectile attack;
	private int targetx,targety;
	public int ammo,bomb;
	public int health, grace, bombcooldown;
	public boolean gameOver;
	public Player(int x, int y,int ammo,int bombs, int health){
		this.health=health;
		this.x = x;
		this.y = y;
		this.ammo=ammo;
		this.bomb=bombs;
		this.attack = new Projectile(ammo,x,y,targetx,targety);
	}
	public void tick() {
		if(grace>0)grace--;
		if(attack.status){
			attack = new Projectile(ammo,x,y,targetx,targety);
		}
		if(!attack.status)attack.tick();		
		if(health==0)gameOver=true;
		if(bombcooldown>0)bombcooldown--;
	}
	public void pickup(int pickup) {
		switch(pickup) {
		case 10:
			ammo++;
			break;
		case 11:
			bomb++;
			break;
		case 12:
			if(health<5)health++;
			break;
		}		
	}
	
	public int getAmmo() {
		return ammo;
	}
	public int getBomb() {
		return bomb;
	}	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setX(int x) {
		this.x=x;
	}
	public void setY(int y) {
		this.y=y;
	}
	public int getTargetX() {
		return targetx;
	}
	public int getTargetY() {
		return targety;
	}
	public void setTargetX(int x) {
		this.targetx=x;
	}
	public void setTargetY(int y) {
		this.targety=y;
	}
	public Rectangle getBounds() {
		return new Rectangle(x,y,48,48);
	}
	public void damage() {
		if(grace==0) {
			health--;
			grace=100;
		}
	}
}
