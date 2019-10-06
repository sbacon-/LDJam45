package dev.chancho.ld45;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

public class Etch{
	private ImageIcon splashII =new ImageIcon(Etch.class.getResource("res/splash.png"));
	private ImageIcon menuII =new ImageIcon(Etch.class.getResource("res/menu.png"));
	private ImageIcon tilesetII = new ImageIcon(Etch.class.getResource("res/tileset.png"));
	public Image splashPNG = splashII.getImage();
	public Image tilesetPNG = tilesetII.getImage();
	public Image menuPNG = menuII.getImage();
		
	//GUI
	public Image map = getTile(0,2).getScaledInstance(12, 16, Image.SCALE_DEFAULT);
	public Image mapDoorN = getTile(1,2).getScaledInstance(12, 16, Image.SCALE_DEFAULT);
	public Image mapDoorE = getTile(2,2).getScaledInstance(12, 16, Image.SCALE_DEFAULT);
	public Image mapDoorS = getTile(3,2).getScaledInstance(12, 16, Image.SCALE_DEFAULT);
	public Image mapDoorW = getTile(4,2).getScaledInstance(12, 16, Image.SCALE_DEFAULT);
	
	
	public Image ammo = getTile(2,1).getScaledInstance(24,24,Image.SCALE_DEFAULT);
	public Image bomb = getTile(3,1).getScaledInstance(24,24,Image.SCALE_DEFAULT);
	public Image heart = getTile(4,1).getScaledInstance(24,24,Image.SCALE_DEFAULT);
	public HashMap<Integer,Image> tileSet = new HashMap<Integer,Image>();
	public ArrayList<Integer> spawnable = new ArrayList<Integer>();
	public HashSet<Integer> solids = new HashSet<Integer>();
	
	public boolean loadin = false;
	private Clip pickup,hit,hurt,kill,heal,boom,lvlup,go;
	
	String fName = "res/VT323.ttf";
    InputStream is = Etch.class.getResourceAsStream(fName);
	public Font pixFont; 
		
	public Etch() {
		try {
			pixFont= Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		for(int i = 0;i<64;i++) {
			tileSet.put(i, getTile(i%8,i/8).getScaledInstance(48, 48, Image.SCALE_DEFAULT));
		}
		solids.add(1);
		solids.add(2);
		
		
		spawnable.add(10);//Ammo
		spawnable.add(11);//Bomb
		spawnable.add(12);//Heart
		for(int e = 24; e<27; e++) {
			spawnable.add(e);//Enemies
		}
		//Audio
		try {
			pickup = AudioSystem.getClip();
			pickup.open(AudioSystem.getAudioInputStream(Etch.class.getResource("res/pickup.wav")));
			hit = AudioSystem.getClip();
			hurt= AudioSystem.getClip();
			hurt.open(AudioSystem.getAudioInputStream(Etch.class.getResource("res/hurt.wav")));
			kill = AudioSystem.getClip();
			kill.open(AudioSystem.getAudioInputStream(Etch.class.getResource("res/kill.wav")));	
			heal = AudioSystem.getClip();
			heal.open(AudioSystem.getAudioInputStream(Etch.class.getResource("res/heal.wav")));	
			boom = AudioSystem.getClip();
			boom.open(AudioSystem.getAudioInputStream(Etch.class.getResource("res/boom.wav")));	
			lvlup = AudioSystem.getClip();
			lvlup.open(AudioSystem.getAudioInputStream(Etch.class.getResource("res/nextlvl.wav")));	
			go = AudioSystem.getClip();
			go.open(AudioSystem.getAudioInputStream(Etch.class.getResource("res/go.wav")));	
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}

	}
	
	//From Java Game Engine	
	public BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();
	    return bimage;
	}	
	public BufferedImage getTile(int x,int y) {
		BufferedImage ts = toBufferedImage(tilesetPNG);
		return ts.getSubimage(x*8, y*8, 8, 8);
	}
	public ArrayList<Integer> buildRoom(Room r) {
		ArrayList<Integer> roomLayout = r.getLayout(r.rlay);

		//DOORS
		String binlayout = Integer.toBinaryString(r.getLayout());
		while(binlayout.length()<4) {
			binlayout="0"+binlayout;
		}
		if(binlayout.substring(0,1).equals("1")){
			roomLayout.set(9, 0);
			roomLayout.set(10, 0);
		}if(binlayout.substring(1,2).equals("1")){
			roomLayout.set(99, 0);
			roomLayout.set(119, 0);
		}if(binlayout.substring(2,3).equals("1")){
			roomLayout.set(189, 0);
			roomLayout.set(190, 0);
		}if(binlayout.substring(3,4).equals("1")){
			roomLayout.set(80, 0);
			roomLayout.set(100, 0);
		}
		//System.out.println(r.rlay);
		loadin=!r.visited;
		return roomLayout;
	}

	public Image getOPFilter(Image proj, int dir) {
		BufferedImage image = toBufferedImage(proj);
		double rotationRequired = Math.toRadians (45*dir);
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		
		return op.filter(image, null);
		
	}
	
	public void sound(String sfx){
		switch(sfx) {
		case "pickup":
			pickup.stop();
			pickup.setMicrosecondPosition(0);
			pickup.start();
			break;
		case "hit":
			hit.stop();
			hit.setMicrosecondPosition(0);
			hit.start();
			break;
		case "hurt":
			hurt.stop();
			hurt.setMicrosecondPosition(0);
			hurt.start();
			break;
		case "kill":
			kill.stop();
			kill.setMicrosecondPosition(0);
			kill.start();
			break;
		case "heal":
			heal.stop();
			heal.setMicrosecondPosition(0);
			heal.start();
			break;
		case "boom":
			boom.stop();
			boom.setMicrosecondPosition(0);
			boom.start();
			break;
		case "lvlup":
			lvlup.stop();
			lvlup.setMicrosecondPosition(0);
			lvlup.start();
			break;
		case "go":
			go.stop();
			go.setMicrosecondPosition(0);
			go.start();
			break;
		}
	}
}
