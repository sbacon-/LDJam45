package dev.chancho.ld45;

import java.awt.Rectangle;

public class Projectile {
	public int x,y;
	public double px,py;
	public int range;
	public double slope;
	public int quadrant;
	public boolean status;
	public int dir;
	public int speed;
	public Projectile(int ammo,double px, double py, double tx,double ty) {
		if(tx==-1)status =true;
		else status = false;
		range=50;
		this.px=px;
		this.py=py;
		double y = ((py+24)-ty);
		double x = ((px+24)-tx);
		if(x!=0) {
			slope=(y/x);
			//System.out.println(slope);
		}
		if(x<0&&y>0)quadrant=1;
		if(x>0&&y>0)quadrant=2;
		if(x>0&&y<0)quadrant=3;
		if(x<0&&y<0)quadrant=4;
		if(slope>-1.5&&slope<-0.5) {
			switch(quadrant){
			case 1:
				dir=1;
				break;
			case 3:
				dir=5;
				break;
			}
		}else if(slope>=-0.5&&slope<=0.5) {
			switch(quadrant){
			case 1:
			case 4:
				dir=2;
				break;
			case 2:
			case 3:				
				dir=6;
				break;
			}
		}else if(slope<1.5&&slope>0.5) {
			switch(quadrant){
			case 4:
				dir=3;
				break;
			case 2:
				dir=7;
				break;
			}
		}else {
			switch(quadrant){
			case 1:
			case 2:
				dir=8;
				break;
			case 3:
			case 4:				
				dir=4;
				break;
			}
			//System.out.println(dir);
		}
		speed=(ammo>0)?6:3;
	}
	public void tick() {
		if(range>0){
			switch(dir) {
			case 1:
				px+=speed;
				py-=speed;
				break;
			case 2:
				px+=speed;
				break;
			case 3:
				px+=speed;
				py+=speed;
				break;
			case 4:
				py+=speed;
				break;
			case 5:
				px-=speed;
				py+=speed;
				break;
			case 6:
				px-=speed;
				break;
			case 7:
				px-=speed;
				py-=speed;
				break;
			case 8:
				py-=speed;
				break;
			}
		}else if(range<-100) {
			status=true;
		}
		range--;
	}
	public Rectangle getBounds() {
		if(!status)return new Rectangle((int)px+12,(int)py+12,24,24);
		else return new Rectangle(-1,-1,0,0);
	}
}
