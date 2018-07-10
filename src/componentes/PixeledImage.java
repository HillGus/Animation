package componentes;

import java.awt.image.BufferedImage;

public class PixeledImage extends BufferedImage {

	public PixeledImage(int width, int height, int imageType) {
		
		super(width, height, imageType);
	}

	
	public Pixel getPixel(int x, int y) {
		
		Pixel p = new Pixel(x, y, this);
		
		return p;
	}
}
