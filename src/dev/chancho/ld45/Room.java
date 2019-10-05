package dev.chancho.ld45;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Room {
	private ArrayList<Boolean> doors;
	private Random rand;
	private HashSet<Integer> exclusions;
	private int roomlayout;
	private String binary;
	private Point coords;
	private boolean start;
	public Room(int mapX, int mapY, ArrayList<Room> map) {
		generateRoom(mapX,mapY,map);
		coords = new Point(mapX,mapY);
		start=false;
	}
	private void generateRoom(int mapX,int mapY,ArrayList<Room>map) {
		exclusions = new HashSet<Integer>();
		if(mapY == 0)for(int x = 15; x>=8; x-- )exclusions.add(x);
		if(mapY == 3)for(int x = 15; x>=2; x-- )if(x%4!=0||x-1%4!=0)exclusions.add(x);
		if(mapX == 7)for(int x = 15; x>=4; x-- )if(!(x<=11 && x>=8))exclusions.add(x);
		if(mapX == 0)for(int x = 15; x>=0; x-- )if(x%2!=0)exclusions.add(x);
		rand = new Random();
		roomlayout=rand.nextInt(16);
		while(exclusions.contains(roomlayout)){
			roomlayout=rand.nextInt(16);			
		}
		doors=new ArrayList<Boolean>();
		binary = Integer.toBinaryString(roomlayout);
		while(binary.length()<4) {
			binary = "0"+binary;
		}
		for(int d=0;d<4;d++) {
			doors.add(binary.substring(d,d+1).equals("1"));
		}
		if(mapX>0)changeDoor(3, map.get((mapY*8)+mapX-1).getDoors().get(1));
		if(mapY>0)changeDoor(0, map.get((mapY*8)+mapX-8).getDoors().get(2));
		exclusions.clear();
	}
	public int getLayout() {
		int result=0;
		if(getDoors().get(0))result+=8;
		if(getDoors().get(1))result+=4;
		if(getDoors().get(2))result+=2;
		if(getDoors().get(3))result+=1;
		return result;
	}
	public boolean setStart() {
		if(getLayout()!=0)start=true;
		return start;
	}
	public Point getCoords() {
		return coords;
	}
	public ArrayList<Boolean> getDoors() {
		return doors;
	}
	public void changeDoor(int index, boolean bool) {
		doors.set(index, bool);
	}
}
