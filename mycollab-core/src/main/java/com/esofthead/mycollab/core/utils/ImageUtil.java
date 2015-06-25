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

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class to process image
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ImageUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * @param buffImage
     * @param percenScale
     * @return
     */
    public static BufferedImage scaleImage(BufferedImage buffImage, float percenScale) {
        float width = buffImage.getWidth() * percenScale;
        float height = buffImage.getHeight() * percenScale;
        BufferedImage rescaledImage = Scalr.resize(buffImage, Method.BALANCED,
                Mode.AUTOMATIC, (int) width, (int) height);
        return rescaledImage;
    }

    /**
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

        return Scalr.resize(buffImage, Method.BALANCED, Mode.AUTOMATIC, (int) destWidth, (int) destHeight);
    }

    /**
     * @param pngData
     * @return
     */
    public static byte[] convertJpgToPngFormat(byte[] pngData) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(pngData));
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outStream);
            return outStream.toByteArray();
        } catch (Exception e) {
            LOG.error("Exception to convert jpg file format to png", e);
            return null;
        }
    }

    /**
     * @param image
     * @return
     */
    public static byte[] convertImageToByteArray(BufferedImage image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            LOG.error("Exception to convert Image to Byte Array: ", e);
            return null;
        }
    }

    public static BufferedImage generateImageThumbnail(InputStream imageStream)
            throws IOException {
        try {
            int idealWidth = 256;
            BufferedImage source = ImageIO.read(imageStream);
            if (source == null) {
                return null;
            }
            int imgHeight = source.getHeight();
            int imgWidth = source.getWidth();

            float scale = (float) imgWidth / idealWidth;
            int height = (int) (imgHeight / scale);

            BufferedImage rescaledImage = Scalr.resize(source, Method.QUALITY,
                    Mode.AUTOMATIC, idealWidth, height);
            if (height > 400) {
                rescaledImage = rescaledImage.getSubimage(0, 0,
                        Math.min(256, rescaledImage.getWidth()), 400);
            }
            return rescaledImage;
        } catch (Exception e) {
            LOG.error("Generate thumbnail for error", e);
            return null;
        }
    }
}
