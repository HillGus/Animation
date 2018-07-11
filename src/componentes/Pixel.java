package componentes;

import java.util.ArrayList;

public class Pixel {

	
	private PixeledImage imagem;
	
	private int x, y;
	private int rgb;
	
	
	public Pixel(int x, int y, PixeledImage imagem) {
		
		this.x = x;
		this.y = y;
		this.rgb = imagem.getRGB(x, y);
		
		this.imagem = imagem;
	}

	
	public int getX() {
		
		return x;
	}

	public void setX(int x) {
		
		this.x = x;
	}
		
	public int getY() {
		
		return y;
	}
	
	public void setY(int y) {
		
		this.y = y;
	}

	public int getRGB() {
		
		return rgb;
	}

	public PixeledImage getImagem() {
		
		return this.imagem;
	}

	public ArrayList<Pixel> getNearbyPixels() {
		
		ArrayList<Pixel> pixels = new ArrayList<>();
		
		for (int x = getX() - 1; x <= getX() + 1; x++) {
			
			for (int y = getY() - 1; y <= getY() + 1; y++) {
				
				try {
					pixels.add(new Pixel(x, y, imagem));
				} catch (Exception e) {}
			}
		}
		
		return pixels;
	}
	

	public String toString() {
		
		return x + ";" + y + ";" + rgb + ";";
	}

	@Override
	public boolean equals(Object obj) {
		
		if ((obj == null) || !(obj instanceof Pixel))
			return false;
		
		Pixel p = (Pixel) obj;
		
		if (p.getX() != getX())
			return false;
		
		if (p.getY() != getY())
			return false;
		
		if (p.getRGB() != getRGB())
			return false;
		
		return true;
	}
}