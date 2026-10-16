package com.mycompany.visitormanagementsystem.servlet;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

final class PhotoUtil {
    private static final int MAX_DIMENSION = 5000;
    private static final int MAX_EDGE = 640;

    private PhotoUtil() {
    }

    static byte[] normalize(byte[] uploadedBytes) {
        try (ImageInputStream input = ImageIO.createImageInputStream(new ByteArrayInputStream(uploadedBytes))) {
            if (input == null) {
                throw new IllegalArgumentException("The selected file is not a supported image.");
            }
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) {
                throw new IllegalArgumentException("The selected file is not a supported image.");
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(input, true, true);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                if (width < 1 || height < 1 || width > MAX_DIMENSION || height > MAX_DIMENSION) {
                    throw new IllegalArgumentException("Choose an image no larger than 5000 by 5000 pixels.");
                }

                BufferedImage source = reader.read(0);
                double scale = Math.min(1.0, (double) MAX_EDGE / Math.max(width, height));
                int scaledWidth = Math.max(1, (int) Math.round(width * scale));
                int scaledHeight = Math.max(1, (int) Math.round(height * scale));
                BufferedImage normalized = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = normalized.createGraphics();
                try {
                    graphics.setComposite(AlphaComposite.Src);
                    graphics.setColor(java.awt.Color.WHITE);
                    graphics.fillRect(0, 0, scaledWidth, scaledHeight);
                    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    graphics.drawImage(source, 0, 0, scaledWidth, scaledHeight, null);
                } finally {
                    graphics.dispose();
                }

                ByteArrayOutputStream output = new ByteArrayOutputStream();
                if (!ImageIO.write(normalized, "jpg", output)) {
                    throw new IllegalStateException("JPEG image support is not available.");
                }
                return output.toByteArray();
            } finally {
                reader.dispose();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("The selected image could not be read. Please choose another image.", e);
        }
    }
}
