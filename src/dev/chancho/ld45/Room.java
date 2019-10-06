package dev.chancho.ld45;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Room {
	private ArrayList<Boolean> doors;
	private Random rand;
	private HashSet<Integer> exclusions;
	private int roomlayout;
	public RoomLayout rlay;
	private String binary;
	private Point coords;
	boolean start,exit;
	public boolean visited;
	public Room(int mapX, int mapY, ArrayList<Room> map) {
		generateRoom(mapX,mapY,map);
		coords = new Point(mapX,mapY);
		start=false;
		visited=false;
		rlay=RoomLayout.exit;
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
	@SuppressWarnings("unchecked")
	public ArrayList<Room> finalizeMap(ArrayList<Room> map){
		ArrayList<HashSet<Integer>> paths = new ArrayList<HashSet<Integer>>();
		for(Room init: map) {
			HashSet<Integer> path = new HashSet<Integer>();
			path.add(map.indexOf(init));
			boolean haspath = true;
			while(haspath) {
				HashSet<Integer> current = (HashSet<Integer>) path.clone();
				int delta=current.size();
				for(Integer room : current) {
					ArrayList<Boolean> doors = map.get(room).getDoors();
					if(doors.get(0))path.add(room-8);
					if(doors.get(1))path.add(room+1);
					if(doors.get(2))path.add(room+8);
					if(doors.get(3))path.add(room-1);
				}
				if(path.size()-delta==0)haspath=false;
			}
			paths.add(path);
		}
		HashSet<Integer> maxPath = new HashSet<Integer>();
		for(HashSet<Integer> path : paths) {
			if(path.size()>maxPath.size())maxPath=(HashSet<Integer>) path.clone();
		}
		ArrayList<Room> newMap = new ArrayList<Room>();
		Room eRoom = new Room(0,0,null);
		eRoom.changeDoor(0, false);
		eRoom.changeDoor(1, false);
		eRoom.changeDoor(2, false);
		eRoom.changeDoor(3, false);
		for(int r = 0;r<32;r++){
			if(maxPath.contains(r))newMap.add(map.get(r));
			else newMap.add(eRoom);
		}
		return newMap;
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
		if(start)rlay=RoomLayout.start;
		return start;
	}

	public boolean setLanding() {
		if(getLayout()!=0)start=true;
		if(start)rlay=RoomLayout.landing;
		return start;
	}
	public boolean setExit() {
		if(getLayout()!=0&& !start)exit=true;
		if(exit)rlay=RoomLayout.exit;
		return exit;
	}
	public void setLayout() {
		if(rlay!=RoomLayout.start)rlay= rlay.setRandomLayout();
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
	public enum RoomLayout{
		start,landing,
		eneA,eneB,eneC,eneD,eneE,eneF,eneG,
		lootA,lootB,lootC,exit;
		public RoomLayout setRandomLayout() {
	        int max = values().length-2; 
	        int min = 1; 
	        int range = max - min + 1; 
	  
			return values()[(int)(Math.random()*range)+min];
		}
	}
	public ArrayList<Integer> getLayout(RoomLayout rlay){
		List<Integer> layout = null;
		
		switch(rlay) {
		case start:
			layout =Arrays.asList(
					1,1,1,1 ,1 ,1,1,1,1,1,1,1,1,1,1,1 ,1 ,1,1,1,
					1,0,0,0 ,0 ,0,0,0,0,0,0,0,0,0,0,0 ,0 ,0,0,1,
					1,0,0,48,49,0,0,0,0,50,51,0,0,0,0,52,53,0,0,1,
					1,0,0,56,57,0,0,0,0,58,59,0,0,0,0,60,61,0,0,1,
					1,0,0,0 ,0 ,0,0,0,0,0,0,0,0,0,0,0 ,0 ,0,0,1,
					1,0,0,0 ,0 ,0,0,0,0,0,0,0,0,0,0,0 ,0 ,0,0,1,
					1,0,0,2 ,0 ,0,0,0,0,0,0,0,0,0,0,0 ,2 ,0,0,1,
					1,0,0,2 ,2 ,0,0,0,0,0,0,0,0,0,0,2 ,2 ,0,0,1,
					1,0,0,0 ,0 ,0,0,0,0,0,0,0,0,0,0,0 ,0 ,0,0,1,
					1,1,1,1 ,1 ,1,1,1,1,1,1,1,1,1,1,1 ,1 ,1,1,1);
			break;
		case eneA:
			layout =Arrays.asList(
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,2,0,0,24,0,0,0,0,0,0,24,0,0,2,0,0,1,
					1,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,1,
					1,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,1,
					1,0,0,2,0,0,24,0,0,0,0,0,0,24,0,0,2,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
			break;
		case eneB:
			layout =Arrays.asList(
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,25,2,2,25,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,2,25,25,2,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,25,2,2,25,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
			break;
		case eneC:
			layout =Arrays.asList(
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,2,0,0,24,0,0,0,0,0,0,24,0,0,2,0,0,1,
					1,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,1,
					1,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,1,
					1,0,0,2,0,0,24,0,0,0,0,0,0,24,0,0,2,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
			break;
		
		case eneD:
			layout =Arrays.asList(
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
					1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,
					1,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,1,
					1,0,0,2,0,25,0,0,0,0,0,0,0,0,26,0,2,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,2,0,24,0,0,0,0,0,0,0,0,24,0,2,0,0,1,
					1,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,1,
					1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
			break;
		case eneE:
			layout =Arrays.asList(
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,26,0,0,0,0,0,0,0,0,0,0,0,0,0,0,26,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,2,2,2,0,2,2,2,0,0,0,0,0,1,
					1,0,0,0,0,0,0,2,26,0,0,26,2,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,2,2,2,0,2,2,2,0,0,0,0,0,1,
					1,0,26,0,0,0,0,0,0,0,0,0,0,0,0,0,0,26,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
			break;
		case eneF:
			layout =Arrays.asList(
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,25,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
			break;
		case eneG:
			layout =Arrays.asList(
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
					1,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,
					1,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,
					1,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,
					1,0,0,0,0,0,0,0,0,24,24,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,24,24,0,0,0,0,0,0,0,0,1,
					1,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,
					1,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,
					1,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
			break;		
		case lootA:
			layout =Arrays.asList(
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
					1,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,1,
					1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,10,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,
					1,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,1,
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
			break;
		case lootB:
			layout =Arrays.asList(
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,1,
					1,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,11,0,0,0,0,0,0,0,0,1,
					1,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,1,
					1,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
			break;
		case lootC:
			layout =Arrays.asList(
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,2,2,2,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,2,12,2,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,2,0,2,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
			break;
		case exit:
			layout =Arrays.asList(
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,22,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
			break;
				
		default:
			layout =Arrays.asList(
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,1,
					1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
					1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
			break;
		}
		return new ArrayList<Integer>(layout);		
	}
}
