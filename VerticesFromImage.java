package com.tanner;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class VerticesFromImage {
	private LinkedList<Point> vertices;
	
	public VerticesFromImage(Image image){
		vertices = new LinkedList<Point>();
		
		for (int j = 0; j < image.getWidth(null) - 2; j++) {
			for (int i = 0; i < image.getHeight(null) - 2; i++) {
				if(containsVertex(image, j, i)){
					vertices.add(new Point(j + 1, i + 1));
				}
			}
		}
	}
	
	private boolean containsVertex(Image img, int x, int y){
		int w = 3, h = 3;
		int[] pixels = new int[w * h];
        PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("interrupted waiting for pixels!");
            return false;
        }
        if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
            System.err.println("image fetch aborted or errored");
            return false;
        }
        
        boolean allPixelsAreOpaque = true;
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
            	if (new Color(pixels[j * w + i], true).getAlpha() < 100){
            		allPixelsAreOpaque = false;
            	}
            }
        }
        
        if ((new Color(pixels[1 * w + 1], true).getAlpha() > 100) && (!allPixelsAreOpaque)){
        	if ((new Color(pixels[1 * w + 2], true).getAlpha() > 100) && (new Color(pixels[1 * w + 0], true).getAlpha() > 100) &&
        			(new Color(pixels[0 * w + 1], true).getAlpha() > 100) && (new Color(pixels[2 * w + 1], true).getAlpha() > 100)){
        		return true;
        	}else if ((new Color(pixels[1 * w + 2], true).getAlpha() > 100) && (new Color(pixels[1 * w + 0], true).getAlpha() > 100) ||
        			(new Color(pixels[0 * w + 1], true).getAlpha() > 100) && (new Color(pixels[2 * w + 1], true).getAlpha() > 100)){
        		return false;
        	}else{
        		return true;
        	}
        }
        
        return false;
	}

	/**
	 * @return the vertices
	 */
	public LinkedList<Point> getVertices() {
		return vertices;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("square.png"));
		} catch (IOException e) {
		}
		VerticesFromImage v = new VerticesFromImage(img);
		for (Point p : v.getVertices()){
			System.out.println(p.x + ", " + p.y);
		}
	}

}
