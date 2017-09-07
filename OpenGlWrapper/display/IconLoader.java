package display;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import utils.MyFile;

public class IconLoader {

	public static ByteBuffer[] load(MyFile file16, MyFile file32, MyFile file128) {
		ByteBuffer[] buffers = null;
		try {
			InputStream stream16 = file16.getInputStream();
			InputStream stream32 = file32.getInputStream();
			InputStream stream128 = file128.getInputStream();
			BufferedImage image16 = ImageIO.read(stream16);
			BufferedImage image32 = ImageIO.read(stream32);
			BufferedImage image128 = ImageIO.read(stream128);
			String OS = System.getProperty("os.name").toUpperCase();
			if (OS.contains("WIN")) {
				buffers = new ByteBuffer[2];
				buffers[0] = loadInstance(image16, 16);
				buffers[1] = loadInstance(image32, 32);
			} else if (OS.contains("MAC")) {
				buffers = new ByteBuffer[1];
				buffers[0] = loadInstance(image128, 128);
			} else {
				buffers = new ByteBuffer[1];
				buffers[0] = loadInstance(image32, 32);
			}
			stream16.close();
			stream32.close();
			stream128.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffers;
	}

	private static ByteBuffer loadInstance(BufferedImage image, int dimension) {
		BufferedImage scaledIcon = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g = scaledIcon.createGraphics();
		double ratio = getIconRatio(image, scaledIcon);
		double width = image.getWidth() * ratio;
		double height = image.getHeight() * ratio;
		g.drawImage(image, (int) ((scaledIcon.getWidth() - width) / 2), (int) ((scaledIcon.getHeight() - height) / 2),
				(int) (width), (int) (height), null);
		g.dispose();

		return convertToByteBuffer(scaledIcon);
	}

	private static double getIconRatio(BufferedImage src, BufferedImage icon) {
		double ratio = 1;
		if (src.getWidth() > icon.getWidth())
			ratio = (double) (icon.getWidth()) / src.getWidth();
		else
			ratio = (int) (icon.getWidth() / src.getWidth());
		if (src.getHeight() > icon.getHeight()) {
			double r2 = (double) (icon.getHeight()) / src.getHeight();
			if (r2 < ratio)
				ratio = r2;
		} else {
			double r2 = (int) (icon.getHeight() / src.getHeight());
			if (r2 < ratio)
				ratio = r2;
		}
		return ratio;
	}

	public static ByteBuffer convertToByteBuffer(BufferedImage image) {
		byte[] buffer = new byte[image.getWidth() * image.getHeight() * 4];
		int counter = 0;
		for (int i = 0; i < image.getHeight(); i++)
			for (int j = 0; j < image.getWidth(); j++) {
				int colorSpace = image.getRGB(j, i);
				buffer[counter + 0] = (byte) ((colorSpace << 8) >> 24);
				buffer[counter + 1] = (byte) ((colorSpace << 16) >> 24);
				buffer[counter + 2] = (byte) ((colorSpace << 24) >> 24);
				buffer[counter + 3] = (byte) (colorSpace >> 24);
				counter += 4;
			}
		return ByteBuffer.wrap(buffer);
	}
}
