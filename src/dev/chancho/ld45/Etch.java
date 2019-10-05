package dev.chancho.ld45;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Etch{
	private ImageIcon splashII =new ImageIcon(Etch.class.getResource("res/splash.png"));
	private ImageIcon tilesetII = new ImageIcon(Etch.class.getResource("res/tileset.png"));
	public Image splashPNG = splashII.getImage();
	public Image tilesetPNG = tilesetII.getImage();
	
	public Image grassTile = getTile(0,0).getScaledInstance(48,48,Image.SCALE_DEFAULT);
	public Image wallTile = getTile(1,0).getScaledInstance(48,48,Image.SCALE_DEFAULT);
	
	//GUI
	public Image map = getTile(0,2).getScaledInstance(12, 16, Image.SCALE_DEFAULT);
	public Image mapDoorN = getTile(1,2).getScaledInstance(12, 16, Image.SCALE_DEFAULT);
	public Image mapDoorE = getTile(2,2).getScaledInstance(12, 16, Image.SCALE_DEFAULT);
	public Image mapDoorS = getTile(3,2).getScaledInstance(12, 16, Image.SCALE_DEFAULT);
	public Image mapDoorW = getTile(4,2).getScaledInstance(12, 16, Image.SCALE_DEFAULT);
	
	
	public Image ammo = getTile(2,1).getScaledInstance(24,24,Image.SCALE_DEFAULT);
	public Image bomb = getTile(3,1).getScaledInstance(24,24,Image.SCALE_DEFAULT);
	public Image coin = getTile(4,1).getScaledInstance(24,24,Image.SCALE_DEFAULT);

    String fName = "res/VT323.ttf";
    InputStream is = Etch.class.getResourceAsStream(fName);
	public Font pixFont; 
		
	public Etch() {
		try {
			pixFont= Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		ArrayList<Integer> roomLayout = new ArrayList<Integer>();
		//WALLS;
		for(int i = 0; i<200; i++) {
			if(i>=0 && i <20) roomLayout.add(1);
			else if(i%20 ==0 || (i+1)%20==0) roomLayout.add(1);
			else if(i>=180 && i<=200) roomLayout.add(1);
			else roomLayout.add(0);
		}
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
		return roomLayout;
	}
}
