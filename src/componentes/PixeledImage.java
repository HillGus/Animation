package componentes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PixeledImage extends BufferedImage {
	
	
	private ArrayList<Pixel> pixels = new ArrayList<>();
	
	
	public PixeledImage(int width, int height, int imageType) {
		
		super(width, height, imageType);
	}

	public PixeledImage(BufferedImage img) {
		
		super(img.getWidth(), img.getHeight(), img.getType());
		
		converter(img);
	}
	
	
	public void retirarCor(Color cor) {
		
		for (int x = 0; x < getWidth(); x++) {
			
			for (int y = 0; y < getHeight(); y++) {
				
				if (getRGB(x, y) == cor.getRGB()) {
					
					setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
				}
			}
		}
	}
		

	private void converter(BufferedImage img) {
		
		for (int x = 0; x < img.getWidth(); x++) {
			
			for (int y = 0; y < img.getHeight(); y++) {
				
				setRGB(x, y, img.getRGB(x, y));
				pixels.add(new Pixel(x, y, this));
			}
		}
	}
	
	
	public void addPixel(Pixel px) {
		
		if (!pixels.contains(px)) {
		
			pixels.add(px);
			
			try {
				setRGB(px.getX(), px.getY(), px.getRGB());
			} catch (Exception e) {}
		}
	}
	
	public Pixel getPixel(int x, int y) {
		
		Pixel p = new Pixel(x, y, this);
		
		return p;
	}

	public ArrayList<Pixel> getPixels() {
		
		return pixels;
	}

	
	public String toString() {
		
		String retorno = "";
		
		for (int x = 0; x < getWidth(); x++) {
			
			for (int y = 0; y < getHeight(); y++) {
				
				retorno += getPixel(x, y).toString(); 
			}
		}
		
		return retorno;
	}
}