package dev.chancho.ld45;

public class Player {
	private int x,y;
	private int targetx,targety;
	private int ammo,bombs,coins;
	public Player(int x, int y){
		this.x = x;
		this.y = y;
		this.ammo=0;
		this.bombs=0;
		this.coins=0;		
	}
	public int getAmmo() {
		return ammo;
	}
	public int getBomb() {
		return bombs;
	}
	public int getCoin() {
		return coins;
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
}
