package componentes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PixeledImage extends BufferedImage {
	
	
	public PixeledImage(int width, int height) {
		
		super(width, height, BufferedImage.TYPE_INT_ARGB);
		
		clear();
	}

	public PixeledImage(BufferedImage img) {
		
		super(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
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
	
	public void clear() {
		
		Graphics2D g = (Graphics2D) getGraphics();
		g.setColor(new Color(0, 0, 0, 0));
		
		g.fillRect(0, 0, getWidth(), getHeight());
	}
		

	private void converter(BufferedImage img) {
		
		for (int x = 0; x < img.getWidth(); x++) {
			
			for (int y = 0; y < img.getHeight(); y++) {
				
				setRGB(x, y, img.getRGB(x, y));
			}
		}
	}
	
	
	public void setPixel(Pixel px) {
		
		setRGB(px.getX(), px.getY(), px.getRGB());
	}
	
	public Pixel getPixel(int x, int y) {
		
		Pixel p = new Pixel(x, y, this);
		
		return p;
	}

	public ArrayList<Pixel> getPixels() {
		
		ArrayList<Pixel> pixels = new ArrayList<>();
		
		for (int y = 0; y < getHeight(); y++) {
			
			for (int x = 0; x < getWidth(); x++) {
				
				if (getPixel(x, y).getRGB() != new Color(0, 0, 0, 0).getRGB())
					pixels.add(getPixel(x, y));
			}
		}
		
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