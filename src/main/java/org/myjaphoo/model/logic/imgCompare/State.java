package org.myjaphoo.model.logic.imgCompare;

import java.awt.image.*;



public class State {

	public byte[][] map;
	public int width;
	public int height;
	public int average;
	
	public State(BufferedImage img, int resX, int resY) {
		// setup brightness map
		width = (int)(img.getWidth() / resX);
                if (width <= 0) {
                    width = 1;
                }
		height = (int)(img.getHeight() / resY);
		if (height <=0) {
                    height = 1;
                }

                map = new byte[height][width];
		
		// build map and stats
		average = 0;
		int ta = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				ta = (int)(100*Util.getBrightnessAtPoint(img, x * resX, y * resY));
				//int ta1 = (int)(100*Util.getBrightnessAtPoint(img, x * resX, y * resY));
				//int ta2 = (int)(100*Util.getBrightnessAtPoint(img, x * resX +1, y * resY +1));
				//int ta3 = (int)(100*Util.getBrightnessAtPoint(img, x * resX +2, y * resY +2));
				//ta = (int)((ta1 + ta2 + ta3) / 3);
				map[y][x] = (byte)ta;
				average += ta;
			}
		}
		average = (int)(average / (width * height));
	}

	public State(BufferedImage img) {
		// setup brightness map
		width = img.getWidth() ;
		height = img.getHeight();

                map = new byte[height][width];
		
		// build map and stats
		average = 0;
		int ta = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				ta = (int)(100*Util.getBrightnessAtPoint(img, x , y ));
				//int ta1 = (int)(100*Util.getBrightnessAtPoint(img, x * resX, y * resY));
				//int ta2 = (int)(100*Util.getBrightnessAtPoint(img, x * resX +1, y * resY +1));
				//int ta3 = (int)(100*Util.getBrightnessAtPoint(img, x * resX +2, y * resY +2));
				//ta = (int)((ta1 + ta2 + ta3) / 3);
				map[y][x] = (byte)ta;
				average += ta;
			}
		}
		average = (int)(average / (width * height));
	}
	// future constructors here might interpret different kinds of data maps....


        public void normalizeBrightnessMap() {
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int val = map[y][x];
                    if (val < min) {
                        min = val;
                    }
                    if (val > max) {
                        max = val;
                    }
                }
            }
            int min_norm = -16000;
            int max_norm = +16000;


            int max_min_diff = max - min;
            if (max_min_diff == 0) {
                max_min_diff = 1;
            }
            // jetzt normalisieren:
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                        int val = map[y][x];

                        //map[y][x] = val / max * Integer.MAX_VALUE /4;
                        map[y][x] = (byte) ((val - min) * (max_norm - min_norm) / max_min_diff + min_norm);
                }
            }

        }
}
