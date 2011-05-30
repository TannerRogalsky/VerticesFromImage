package com.tanner;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.util.Vector;

public class VerticesFromImage {
	private Vector<Point> vertices;
	
	public VerticesFromImage(Image image){
		vertices = new Vector<Point>();
		int w = image.getWidth(null), h = image.getHeight(null);
		int[] pixels = new int[w * h];
		PixelGrabber pg = new PixelGrabber(image, 0, 0, w, h, pixels, 0, w);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			System.err.println("interrupted waiting for pixels!");
		}
		if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
			System.err.println("image fetch aborted or errored");
		}
		
		for (int j = 0; j < w - 2; j++) {
			for (int i = 0; i < h - 2; i++) {
				int[][] temp = new int[3][3];
				for (int x = 0; x < 3; x++){
					for (int y = 0; y < 3; y++){
						temp[x][y] = pixels[(j + x) * w + (i + y)];
					}
				}
				if(containsVertex(temp)){
					vertices.add(new Point(j + 1, i + 1));
				}
			}
		}
		
		for (int j = 0; j < w ; j++) {
			for (int i = 0; i < h; i++) {
				System.out.println(j + ", " + i);
				if(containsVertex(image, j, i)){
					vertices.add(new Point(j + 1, i + 1));
				}
			}
		}
	}
	
	public String pixelToString(int pixel){
		return ((pixel >> 24) & 0xff) + ", " + ((pixel >> 16) & 0xff) + ", " 
		+ ((pixel >> 8) & 0xff) + ", " + ((pixel) & 0xff);
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
		int[][] alpha = new int[w][h];
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				alpha[j][i] = (pixels[j * w + i] >> 24) & 0xff;
				if (alpha[j][i] < 100){
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
	
	private boolean containsVertex(int[][] pixels){
		int w = 3, h = 3;
		int[][] alpha = new int[w][h];
		boolean allPixelsAreOpaque = true;
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				alpha[j][i] = (pixels[j][i] >> 24) & 0xff;
				if (alpha[j][i] < 100){
					allPixelsAreOpaque = false;
				}
			}
		}
		
		if((alpha[w/2][h/2] > 100) && (!allPixelsAreOpaque)){
			return true;
		}
		
		return false;
	}

	/**
	 * @return the vertices
	 */
	public Point[] getVertices() {
		return vertices.toArray(new Point[vertices.size()]);
	}

}
