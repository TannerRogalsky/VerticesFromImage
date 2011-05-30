package com.tanner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test extends JFrame {
	BufferedImage img;
	Polygon poly;

	public Test() throws HeadlessException {
		super();
		
		img = null;
		poly = new Polygon();
		
		try {
			img = ImageIO.read(new File("clamps.png"));
		} catch (IOException e) {
		}
		long t = System.nanoTime();
		VerticesFromImage v = new VerticesFromImage(img);
		GrahamScan gs = new GrahamScan(v.getVertices());
		for (Point p : gs.hull()){
			poly.addPoint(p.x, p.y);
		}
		System.out.println("Seconds elapsed: " + (System.nanoTime() - t)/1000000000);
		
		JPanel panel = new MyPanel();
        this.getContentPane().add(panel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	class MyPanel extends JPanel {

	    public MyPanel() {
	        setBorder(BorderFactory.createLineBorder(Color.black));
	    }

	    public Dimension getPreferredSize() {
	        return new Dimension(img.getWidth(), img.getHeight());
	    }

	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);       

	        g.drawImage(img, 0, 0, null);
	        g.setColor(Color.black);
	        g.drawPolygon(poly);
	    }  
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Test();
	}

}
