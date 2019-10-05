package dev.chancho.ld45;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable{
	private static final long serialVersionUID = -7392545151087699625L;
	public final static int WIDTH = 960;
	public final static int HEIGHT= 576;
	private final int SCALE = 24;
	private final int DELAY= 60;
	public static int time = 0;
	
	public Thread timer;
	public Etch e;
	public KListener kAdapter = new KListener();
	public Player player;
	public int level, currentroomIndex;
	public ArrayList<Room> map;
	public ArrayList<Integer> currentRoom;
	public Board() {
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setBackground(Color.decode("#8067a0"));
		addKeyListener(kAdapter);
		addMouseListener(kAdapter);
		addMouseMotionListener(kAdapter);
		setFocusable(true);
		requestFocus();
		
		init();
	}
	private void init() {
		e=new Etch();
		player = new Player(WIDTH/2,HEIGHT/2);
		level=0;
		map = new ArrayList<Room>();
		for(int y = 0; y<4;y++) {
			for(int x = 0; x <8; x++) {
				map.add(new Room(x,y,map));
			}
		}
		Random rand= new Random();
		currentroomIndex = rand.nextInt(32);
		while(!map.get(currentroomIndex).setStart()) {
			currentroomIndex=rand.nextInt(32);
		}
		currentRoom = e.buildRoom(map.get(currentroomIndex));
		Cursor crosshair = Toolkit.getDefaultToolkit().createCustomCursor(e.getTile(7, 7).getScaledInstance(SCALE*2,SCALE*2 , Image.SCALE_DEFAULT),new Point(SCALE,SCALE), "crosshair");
		this.setCursor(crosshair);
	}
	
	private void tick() {
		time++;
		if(time%2==0)movePlayer();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(time>100)render(g);
		//ADD MENU RENDER HERE
		else splash(g);
	}
	private void render(Graphics g){
		//GUI
		g.setColor(Color.black);
		g.setFont(e.pixFont.deriveFont(24.0f));
		g.fillRect(0,0,WIDTH,SCALE*4);
		g.setColor(Color.white);
		g.drawString("LEVEL-"+level,50,30);
		//MAP
		for(int y = 0; y<4;y++) {
			for(int x = 0; x <8; x++) {
				for(int i = 0; i <4; i++) {
					if(map.get((8*y)+x).getDoors().get(i)) {
						switch(i){
						case 0:
							g.drawImage(e.mapDoorN,x*12+40,y*12+40,this);
							break;
						case 1:
							g.drawImage(e.mapDoorE,x*12+40,y*12+40,this);
							break;
						case 2:
							g.drawImage(e.mapDoorS,x*12+40,y*12+40,this);
							break;
						case 3:
							g.drawImage(e.mapDoorW,x*12+40,y*12+40,this);
							break;
						}
					}
				}
				if((8*y)+x == currentroomIndex)g.drawImage(e.map,x*12+40,y*12+40,this);
			}
		}
		//INVENTORY
		g.drawImage(e.ammo,WIDTH/2-SCALE,0+5,this);
		g.drawString("x"+player.getAmmo(),WIDTH/2+SCALE-10,SCALE +5);
		g.drawImage(e.bomb,WIDTH/2-SCALE,SCALE+10,this);
		g.drawString("x"+player.getBomb(),WIDTH/2+SCALE-10,SCALE*2+10);
		g.drawImage(e.coin,WIDTH/2-SCALE,SCALE*2+15,this);
		g.drawString("x"+player.getCoin(),WIDTH/2+SCALE-10,SCALE*3+15);
		
		//ROOM
		for(int i = 0; i<200; i++){
			int x = i;
			int y = i/20;
			while(x>=20)x-=20;
			Image currentTile = null;
			switch(currentRoom.get(i)) {
			case 0: 
				currentTile=e.grassTile;
				break;
			case 1:
				currentTile=e.wallTile;
				break;
			}
			g.drawImage(currentTile,x*SCALE*2,y*SCALE*2+SCALE*4,this);
		}
		
		
		g.drawImage(e.getTile(0, 1).getScaledInstance(SCALE*2, SCALE*2, Image.SCALE_DEFAULT), player.getX(), player.getY(),this);
		Toolkit.getDefaultToolkit().sync();
	}	
	private void splash(Graphics g) {
		g.drawImage(e.toBufferedImage(e.splashPNG).getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT), 0, 0, this);
		Toolkit.getDefaultToolkit().sync();
	}
	public void movePlayer() {
		player.setX(player.getX()+kAdapter.xdir);
		player.setY(player.getY()+kAdapter.ydir);
		player.setTargetX(kAdapter.mx);
		player.setTargetY(kAdapter.my);
	}
	
	
	
	@Override
	public void addNotify(){
		super.addNotify();
		timer = new Thread(this);
		timer.start();
	}
	@Override
	public void run() {
		long current,delta,sleep;
		current = System.currentTimeMillis();
		while(true) {
			tick();
			repaint();
			delta=System.currentTimeMillis()-current;
			sleep=DELAY-delta;
			if(sleep<0)sleep=2;
			try{
				Thread.sleep(sleep);
			}catch(InterruptedException e){
				String msg = String.format("Thread interrupted: %s", e.getMessage());
				JOptionPane.showMessageDialog(this, msg,"ERROR",JOptionPane.ERROR_MESSAGE);
			}
		}
	}	
}
