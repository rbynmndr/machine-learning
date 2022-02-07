package application;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class ImageProcessing extends NepaliDigit {
	public static final int IMAGE_WIDTH = 28;
	public static final int IMAGE_HEIGHT = 28;
	public static final float GAUSS_FILTER = 2.0f;
	public static final float BRIGHT_FACTOR = 1.1f;
	public static final int OUTPUT_LENGTH = 10;
	
	/**
	 * Reshaped and filter canvas image.
	 * @param input Canvas input image.
	 * @return Buffered image with reshaped default value.
	 */
	public BufferedImage processedImage(BufferedImage input) {
		BufferedImage processImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Image resizedImage = input.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
		processImage.getGraphics().drawImage(resizedImage, 0, 0, null);
		processImage = new GaussianFilter(GAUSS_FILTER).filter(processImage, processImage);
		RescaleOp op = new RescaleOp(BRIGHT_FACTOR, 0, null);
		processImage = op.filter(processImage, processImage);
		return processImage;
	}
	
	public double[][] imageToArray(BufferedImage image){
		double[][] array = new double[1][IMAGE_WIDTH * IMAGE_HEIGHT];
		int index = 0;
		for(int i=0; i<IMAGE_HEIGHT; i++) {
			for(int j = 0; j<IMAGE_WIDTH; j++) {
				Color c = new Color(image.getRGB(j, i));
				int rgb = (int)Math.ceil((c.getBlue() + c.getGreen() + c.getRed()) / 3);
				if((255 - rgb) * 2 < 255 && (255 - rgb) * 2 > 150) rgb = 2 * rgb - 255;
				else if((255 - rgb) * 3 < 150) rgb = 255 - ((255 - rgb) * 3);
				else rgb = 0;
				array[0][index++] = ((255 - (double)rgb) / 255 * 0.99) + 0.01;
			}
		}
		return array;
	}
	
	public double[][] setOutputArray(String inputText){
		double[][] output = new double[1][OUTPUT_LENGTH];
		for(int i=0; i<OUTPUT_LENGTH; i++) {
			output[0][i] = 0.01;
		}
		output[0][getNumberFromNepali(inputText)] = 0.99;
		return output;
	}
	
	public int[] computeCenterOfMass(BufferedImage img) {
        long xSum = 0;
        long ySum = 0;
        long num = 0;

        for(int x = 0; x < img.getWidth(); x++) {
             for(int y = 0; y < img.getHeight(); y++) {
                 int weight = new Color(img.getRGB(x, y)).getRGB();
                 xSum += x * weight;
                 ySum += y * weight;
                 num += weight;
             }
        }
        return new int[] {(int)((double) xSum / num), (int)((double)ySum / num)};
    }
}
