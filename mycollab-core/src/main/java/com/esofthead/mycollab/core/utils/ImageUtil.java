/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to process image
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ImageUtil {
	private static Logger log = LoggerFactory.getLogger(ImageUtil.class);

	/**
	 * 
	 * @param buffImage
	 * @param percenScale
	 * @return
	 */
	public static BufferedImage scaleImage(BufferedImage buffImage,
			float percenScale) {
		float width = buffImage.getWidth() * percenScale;
		float height = buffImage.getHeight() * percenScale;
		BufferedImage rescaledImage = Scalr.resize(buffImage, Method.BALANCED,
				Mode.AUTOMATIC, (int) width, (int) height);
		return rescaledImage;
	}

	/**
	 * 
	 * @param buffImage
	 * @param scaleWidth
	 * @param scaleHeight
	 * @return
	 */
	public static BufferedImage scaleImage(BufferedImage buffImage,
			int scaleWidth, int scaleHeight) {
		int imgHeight = buffImage.getHeight();
		int imgWidth = buffImage.getWidth();

		float destHeight = scaleHeight;
		float destWidth = scaleWidth;

		if ((imgWidth >= imgHeight) && (imgWidth > scaleWidth)) {
			destHeight = imgHeight * ((float) scaleWidth / imgWidth);
		} else if ((imgWidth < imgHeight) && (imgHeight > scaleHeight)) {
			destWidth = imgWidth * ((float) scaleHeight / imgHeight);
		} else {
			return buffImage;
		}

		BufferedImage rescaledImage = Scalr.resize(buffImage, Method.BALANCED,
				Mode.AUTOMATIC, (int) destWidth, (int) destHeight);
		return rescaledImage;
	}

	/**
	 * 
	 * @param pngData
	 * @return
	 */
	public static byte[] convertJpgToPngFormat(byte[] pngData) {
		try {
			BufferedImage image = ImageIO
					.read(new ByteArrayInputStream(pngData));
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			ImageIO.write(image, "png", outStream);
			return outStream.toByteArray();
		} catch (Exception e) {
			log.error("Exception to convert jpg file format to png", e);
			return null;
		}
	}

	/**
	 * 
	 * @param image
	 * @return
	 */
	public static byte[] convertImageToByteArray(BufferedImage image) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch (IOException e) {
			log.error("Exception to convert Image to Byte Array: ", e);
			return null;
		}
	}

	public static BufferedImage generateImageThumbnail(InputStream imageStream)
			throws IOException {
		int idealWidth = 512;
		BufferedImage source = ImageIO.read(imageStream);
		int imgHeight = source.getHeight();
		int imgWidth = source.getWidth();

		float scale = imgWidth / idealWidth;
		int height = (int) (imgHeight / scale);

		BufferedImage rescaledImage = Scalr.resize(source, Method.QUALITY,
				Mode.FIT_TO_WIDTH, idealWidth, (int) height);
		return rescaledImage;
	}

	public static void main(String[] args) throws IOException {
		BufferedImage thumbnail = generateImageThumbnail(new FileInputStream(
				new File(
						"/Users/haiphucnguyen/Downloads/FLO002_HomePage_V1.jpg")));
		ImageIO.write(thumbnail, "png", new File(
				"/Users/haiphucnguyen/Desktop/test.png"));
		System.out.println("Thumb: " + thumbnail);
	}
}
