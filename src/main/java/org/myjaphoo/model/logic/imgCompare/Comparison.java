package org.myjaphoo.model.logic.imgCompare;

import java.awt.*;
import java.awt.image.*;

public class Comparison {

	protected State s1;
	protected State s2;
	protected int[][] variance;
	protected int width; 
	protected int height; 
	protected boolean match;

	public Comparison(State s1, State s2, int[][] variance, boolean match) {
		this.s1 = s1;
		this.s2 = s2;
		this.variance = variance;
		this.match = match;
		height = variance.length;
		width = variance[0].length;
	}
 
	// return the image that indicates the regions where changes where detected.
	// 		there is something pitifully wrong with the images this method creates :(   dunno what it is to fix it. correction blocks get rendered in a different scale than the original image.
	public BufferedImage getChangeIndicator(BufferedImage cx, Comparer comparer) {
		// setup change display image
		Graphics2D gc = cx.createGraphics();
		gc.setColor(Color.RED);

		float bx = (cx.getWidth() / width);
		float by = (cx.getHeight() / height);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (variance[y][x] > comparer.leniency)
					gc.drawRect((int)(x * bx), (int)(y * by), (int)bx, (int)by);
			}
		}
		return cx;
	}
	
	public int[][] getVariance() {
		return variance;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public boolean isMatch() {
		return match;
	}
	
	public State getState1() {
		return s1;
	}
	
	public State getState2() {
		return s2;
	}
	
	
}
