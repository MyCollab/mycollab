package com.mycollab.core.utils;

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
        return Scalr.resize(buffImage, Method.BALANCED,
                Mode.AUTOMATIC, (int) width, (int) height);
    }

    /**
     * @param buffImage
     * @param scaleWidth
     * @param scaleHeight
     * @return
     */
    public static BufferedImage scaleImage(BufferedImage buffImage, int scaleWidth, int scaleHeight) {
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

    public static BufferedImage generateImageThumbnail(InputStream imageStream) throws IOException {
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
