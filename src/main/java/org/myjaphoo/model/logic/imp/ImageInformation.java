/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.logic.imp;

import java.awt.image.BufferedImage;
import java.io.File;
import org.myjaphoo.model.db.MovieEntry;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.common.IBufferedImageFactory;
/**
 * Gatherer for image information using sanselan.
 * @author mla
 */
public class ImageInformation {


    public void extractInfos(MovieEntry entry) {
        /*
        Sanselan.getICCProfile(file);
        Sanselan.getMetadata(file);
        Sanselan.getImageInfo(file);
        Sanselan.getImageSize(file);
         */
    }


	public static BufferedImage imageReadExample(File file)
			throws ImageReadException, IOException
	{
		Map params = new HashMap();

		// set optional parameters if you like
		params.put(SanselanConstants.BUFFERED_IMAGE_FACTORY,
				new ManagedImageBufferedImageFactory());

		//		params.put(SanselanConstants.PARAM_KEY_VERBOSE, Boolean.TRUE);

		// read image
		BufferedImage image = Sanselan.getBufferedImage(file, params);

		return image;
	}

	public static class ManagedImageBufferedImageFactory
			implements
				IBufferedImageFactory
	{

		public BufferedImage getColorBufferedImage(int width, int height,
				boolean hasAlpha)
		{
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			GraphicsDevice gd = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gd.getDefaultConfiguration();
			return gc.createCompatibleImage(width, height,
					Transparency.TRANSLUCENT);
		}

		public BufferedImage getGrayscaleBufferedImage(int width, int height,
				boolean hasAlpha)
		{
			return getColorBufferedImage(width, height, hasAlpha);
		}
	}

}
