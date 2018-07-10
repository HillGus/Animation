package componentes;

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

	public int getY() {
		
		return y;
	}

	public int getRGB() {
		
		return rgb;
	}

	public PixeledImage getImagem() {
		
		return this.imagem;
	}


	public String toString() {
		
		return x + ";" + y + ";" + rgb + ";";
	}
}