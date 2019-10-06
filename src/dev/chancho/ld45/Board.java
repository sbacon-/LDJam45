package dev.chancho.ld45;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
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
	public int level, currentroomIndex,exitroomIndex;
	public ArrayList<Room> map;
	public ArrayList<Integer> currentRoom;
	public ArrayList<Collectible> items;
	public ArrayList<Bomb> bombslist;
	public ArrayList<Enemy> enemies;
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
		level=-1;
		e=new Etch();
		e.sound("lvlup");
		e=new Etch();
		e.sound("lvlup");
		player = new Player(WIDTH/2,HEIGHT/2,0,0,1);
		map = new ArrayList<Room>();
		items = new ArrayList<Collectible>();
		enemies = new ArrayList<Enemy>();
		bombslist= new ArrayList<Bomb>();
		for(int y = 0; y<4;y++) {
			for(int x = 0; x <8; x++) {
				map.add(new Room(x,y,map));
			}
		}
		Room room = new Room(-1,-1,null);
		map = room.finalizeMap(map);
		Random rand= new Random();
		if(level==0) {
			currentroomIndex = rand.nextInt(32);
			while(!map.get(currentroomIndex).setStart()) {
				currentroomIndex=rand.nextInt(32);
			}
		}else {
			currentroomIndex = rand.nextInt(32);
			while(!map.get(currentroomIndex).setLanding()) {
				currentroomIndex=rand.nextInt(32);
			}
		}
		for(Room r: map) {
			if(!r.start)r.setLayout();
		}
		rand= new Random();
		exitroomIndex = rand.nextInt(32);
		while(!map.get(exitroomIndex).setExit()) {
			exitroomIndex=rand.nextInt(32);
		}
		currentRoom = e.buildRoom(map.get(currentroomIndex));
		Cursor crosshair = Toolkit.getDefaultToolkit().createCustomCursor(e.getTile(7, 7).getScaledInstance(SCALE*2,SCALE*2 , Image.SCALE_DEFAULT),new Point(SCALE,SCALE), "crosshair");
		this.setCursor(crosshair);
	}
	private void init(int level,int ammo,int bombs, int health) {
		e=new Etch();
		e.sound("lvlup");
		player = new Player(WIDTH/2,HEIGHT/2,ammo,bombs,health);
		this.level=level;
		map = new ArrayList<Room>();
		items = new ArrayList<Collectible>();
		enemies = new ArrayList<Enemy>();
		bombslist= new ArrayList<Bomb>();
		for(int y = 0; y<4;y++) {
			for(int x = 0; x <8; x++) {
				map.add(new Room(x,y,map));
			}
		}
		Room room = new Room(-1,-1,null);
		map = room.finalizeMap(map);
		Random rand= new Random();
		if(level==0) {
			currentroomIndex = rand.nextInt(32);
			while(!map.get(currentroomIndex).setStart()) {
				currentroomIndex=rand.nextInt(32);
			}
		}else {
			currentroomIndex = rand.nextInt(32);
			while(!map.get(currentroomIndex).setLanding()) {
				currentroomIndex=rand.nextInt(32);
			}
		}
		for(Room r: map) {
			if(!r.start)r.setLayout();
		}
		rand= new Random();
		exitroomIndex = rand.nextInt(32);
		while(!map.get(exitroomIndex).setExit()) {
			exitroomIndex=rand.nextInt(32);
		}
		currentRoom = e.buildRoom(map.get(currentroomIndex));
		Cursor crosshair = Toolkit.getDefaultToolkit().createCustomCursor(e.getTile(7, 7).getScaledInstance(SCALE*2,SCALE*2 , Image.SCALE_DEFAULT),new Point(SCALE,SCALE), "crosshair");
		this.setCursor(crosshair);
	}
	
	private void tick() {
		kAdapter.updateKeyState();
		time++;		
		if(time%2==0)movePlayer();
		if(player.gameOver) {
			e.sound("go");
			init(0,0,0,3);
		}
		if(kAdapter.reset) {
			kAdapter.reset=false;
			init(0,0,0,3);
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(time>1000) {
			if(level>=0)render(g);
			else mainMenu(g);
		}else {
			splash(g);
		}
	}
	private void render(Graphics g){
		//ROOM
		for(int i = 0; i<200; i++){
			int x = i;
			int y = i/20;
			while(x>=20)x-=20;
			Image currentTile = e.tileSet.get(currentRoom.get(i));
			if(!e.spawnable.contains(currentRoom.get(i))) {
				g.drawImage(currentTile,x*SCALE*2,y*SCALE*2+SCALE*4,this);
			}else if(e.loadin){
				if(currentRoom.get(i)>23) {
					enemies.add(new Enemy(x*SCALE*2,y*SCALE*2+SCALE*4,currentRoom.get(i)));
				}else {
					items.add(new Collectible(x*SCALE*2,y*SCALE*2+SCALE*4,currentRoom.get(i)));
					g.drawImage(e.tileSet.get(0),x*SCALE*2,y*SCALE*2+SCALE*4,this);
					//System.out.println("new item loaded");
				}
			}else {
				g.drawImage(e.tileSet.get(0),x*SCALE*2,y*SCALE*2+SCALE*4,this);
			}
		}
		e.loadin=false;
		for(Collectible c : items) {
			g.drawImage(e.tileSet.get(13), c.x+1,c.y,this);
			g.drawImage(e.tileSet.get(c.type),c.x,c.y-c.floating,this);	
		}		
		for(Enemy en : enemies) {
			g.drawImage(e.tileSet.get(en.type),en.x,en.y,this);
		}
		for(Bomb b : bombslist) {
			g.drawImage(e.tileSet.get(11),b.x,b.y,this);
			if(b.fuse<0) {
				for(int i = 1; i<=3; i++) {
					g.drawImage(e.tileSet.get(28+i),b.getBounds().x+(48*(i-1)),b.getBounds().y,this);
					g.drawImage(e.tileSet.get(36+i),b.getBounds().x+(48*(i-1)),b.getBounds().y+48,this);
					g.drawImage(e.tileSet.get(44+i),b.getBounds().x+(48*(i-1)),b.getBounds().y+48*2,this);
				}
			}
		}
		//Player		
		int proj = player.getAmmo()>0?1:6;
		if(player.attack.range>0)g.drawImage(e.getOPFilter(e.getTile(proj, 1).getScaledInstance(SCALE, SCALE, Image.SCALE_DEFAULT),player.attack.dir),(int) player.attack.px+12,(int) player.attack.py+12, this);
		g.drawImage((player.grace!=0&&player.grace%10==0)?e.tileSet.get(0):e.tileSet.get(8), player.getX(), player.getY(),this);
		
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
					if(map.get((8*y)+x).getDoors().get(i)&&map.get((8*y)+x).visited) {
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
		g.drawImage(e.tileSet.get(10),WIDTH/2-SCALE*7,SCALE,this);
		g.drawString("x"+player.getAmmo(),WIDTH/2-SCALE*5,SCALE*2);
		g.drawImage(e.tileSet.get(11),WIDTH/2-SCALE*3,SCALE,this);
		g.drawString("x"+player.getBomb(),WIDTH/2-SCALE,SCALE*2);
		
		g.drawString("HEALTH", WIDTH-SCALE*10, SCALE);
		for(int i = player.health-1;i>=0;i--) {
			g.drawImage(e.heart, WIDTH-SCALE*10+(20*i),SCALE+10,this);
		}
		
		
		/*
		//COLLISION TEST
		g.setColor(Color.red);
		g.drawRect(player.attack.getBounds().x, player.attack.getBounds().y, player.attack.getBounds().width, player.attack.getBounds().height);
		g.setColor(Color.orange);//E		
		g.drawRect(player.getX()+SCALE+SCALE/2,player.getY()+SCALE/2,SCALE/2,SCALE);
		g.setColor(Color.yellow);//S
		g.drawRect(player.getX()+SCALE/2,player.getY()+SCALE+SCALE/2,SCALE,SCALE/2);
		g.setColor(Color.green);//W
		g.drawRect(player.getX(),player.getY()+SCALE/2,SCALE/2,SCALE);
		g.setColor(Color.cyan);
		g.drawRect(19*48,((4+2)*48),48,48);
		
		g.setColor(Color.pink);
		if(!enemies.isEmpty())g.drawRect(enemies.get(0).getBounds().x, enemies.get(0).getBounds().y, enemies.get(0).getBounds().width, enemies.get(0).getBounds().height);
		*/
		Toolkit.getDefaultToolkit().sync();
	}	
	private void splash(Graphics g) {
		g.drawImage(e.toBufferedImage(e.splashPNG).getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT), 0, 0, this);
		Toolkit.getDefaultToolkit().sync();
	}
	private void mainMenu(Graphics g) {
		g.drawImage(e.toBufferedImage(e.menuPNG).getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT), 0, 0, this);
		Toolkit.getDefaultToolkit().sync();
	}
	public void movePlayer() {
		checkCollision();
		int xdir = kAdapter.xdir;
		int ydir = kAdapter.ydir;
		player.setX(player.getX()+xdir);
		player.setY(player.getY()+ydir);
		player.setTargetX(kAdapter.mx);
		player.setTargetY(kAdapter.my);
		if(kAdapter.space&& player.bomb>0 && player.bombcooldown==0) {
			player.bombcooldown=200;
			player.bomb--;
			bombslist.add(new Bomb(player.getX()+SCALE,player.getY()+SCALE));
		}
		player.tick();
	}
	private void checkCollision() {
		ArrayList<Collectible> remDex = new ArrayList<Collectible>();
		for(Collectible c : items) {
			if(c.getBounds().intersects(player.getBounds())) {
				player.pickup(c.pickup());
				remDex.add(c);
			}
			c.tick();
		}
		for(Collectible r : remDex) {
			if(r.type==12)e.sound("heal");
			else e.sound("pickup");
			items.remove(items.indexOf(r));
		}
		remDex.clear();
		
		int x = player.getX()+SCALE;
		int y = player.getY()+SCALE;
		while(x>48)x/=48;
		while(y>48)y/=48;
		if(y>12)y=2;
		if(x>20)x=0;
		y-=2;
		Rectangle playerN = new Rectangle(player.getX()+SCALE/2,player.getY(),SCALE,SCALE/2);
		Rectangle playerE = new Rectangle(player.getX()+SCALE+SCALE/2,player.getY()+SCALE/2,SCALE/2,SCALE);
		Rectangle playerS = new Rectangle(player.getX()+SCALE/2,player.getY()+SCALE+SCALE/2,SCALE,SCALE/2);
		Rectangle playerW = new Rectangle(player.getX(),player.getY()+SCALE/2,SCALE/2,SCALE);
		ArrayList<Enemy> eremDex = new ArrayList<Enemy>();
		ArrayList<Bomb> bremDex = new ArrayList<Bomb>();
		for(Bomb b: bombslist) {
			b.tick();
			if(b.fuse==0)e.sound("boom");
			if(b.fuse<-100)bremDex.add(b);
		}
		for(Bomb r: bremDex) {
			bombslist.remove(bombslist.indexOf(r));
		}
		for(Enemy en : enemies) {
			en.tick(player.getX(),player.getY(),enemies);
			
			if(en.getBounds().intersects(playerN)){
				if(player.grace==0)e.sound("hurt");
				player.damage();
				//player.setY(player.getY()+10);
			}else if(en.getBounds().intersects(playerE)){
				if(player.grace==0)e.sound("hurt");
				player.damage();
				//player.setX(player.getX()-10);
			}else if(en.getBounds().intersects(playerS)){
				if(player.grace==0)e.sound("hurt");
				player.damage();
				//player.setY(player.getY()-10);
			}else if(en.getBounds().intersects(playerW)){
				if(player.grace==0)e.sound("hurt");
				player.damage();
				//player.setX(player.getX()+10);
			}
			if(player.attack.getBounds().intersects(en.getBounds())){
				if(player.ammo>0)en.health-=3;
				else en.health--;
				if(player.ammo>0)player.ammo--;
				e.sound("hit");
				player.attack= new Projectile(0,0,0,0,0);
			}
			for(Bomb b: bombslist) {
				if(b.getBounds().intersects(en.getBounds()))en.health-=3;
			}
			if(en.health<=0)eremDex.add(en);
		}
		for(Enemy r : eremDex) {
			items.add(new Collectible(r.x,r.y,(int)(Math.random()*(12-10+1))+10));
			e.sound("kill");
			enemies.remove(enemies.indexOf(r));
		}
		eremDex.clear();
		if(currentRoom.get((20*y)+x)==22) {
			e.sound("lvlup");
			init(level+1, player.ammo, player.bomb, player.health);
		}
		if(y>0 && kAdapter.ydir<0&& e.solids.contains(currentRoom.get((20*(y-1))+x))&& playerN.intersects(new Rectangle(x*48,((y+1)*48),48,48)))kAdapter.ydir=0;
		if(y<9 && kAdapter.ydir>0 && e.solids.contains(currentRoom.get((20*(y+1))+x)) && playerS.intersects(new Rectangle(x*48,((y+3)*48),48,48)))kAdapter.ydir=0;
		if(x>0 && kAdapter.xdir<0&& e.solids.contains(currentRoom.get((20*y)+x-1)) && playerW.intersects(new Rectangle((x-1)*48,(y+2)*48,48,48)))kAdapter.xdir=0;
		if(x<19 && kAdapter.xdir>0&& e.solids.contains(currentRoom.get((20*y)+x+1)) && playerE.intersects(new Rectangle((x+1)*48,(y+2)*48,48,48)))kAdapter.xdir=0;
		if(y==0) {
			//System.out.println(currentroomIndex);
			if(enemies.isEmpty())map.get(currentroomIndex).visited=true;
			currentroomIndex-=8;
			currentRoom = e.buildRoom(map.get(currentroomIndex));
			enemies.clear();
			items.clear();
			player.setY(480);
		}
		if(y==9) {
			//System.out.println(currentroomIndex);
			if(enemies.isEmpty())map.get(currentroomIndex).visited=true;
			currentroomIndex+=8;
			currentRoom = e.buildRoom(map.get(currentroomIndex));
			enemies.clear();
			items.clear();
			player.setY(145);
		}
		if(x==0) {
			//System.out.println(currentroomIndex);
			if(enemies.isEmpty())map.get(currentroomIndex).visited=true;
			currentroomIndex-=1;
			currentRoom = e.buildRoom(map.get(currentroomIndex));
			enemies.clear();
			items.clear();
			player.setX(858);
		}
		if(x==19) {
			//System.out.println(currentroomIndex);
			if(enemies.isEmpty())map.get(currentroomIndex).visited=true;
			currentroomIndex+=1;
			currentRoom = e.buildRoom(map.get(currentroomIndex));
			enemies.clear();
			items.clear();
			player.setX(48);
		}
		
		//System.out.println(player.getX()+" "+player.getY());
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
