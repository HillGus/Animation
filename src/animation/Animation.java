package animation;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import componentes.PixeledImage;

public class Animation extends JPanel {

	private static final long serialVersionUID = 1L;


	public static final int DEFAULT = 1, BOOMERANGUE = 2, BACKWARDS = 3;
	
	
	private ArrayList<PixeledImage> imagens = new ArrayList<>();
	private Container parent;
	private Graphics2D g;
	
	private int espera;
	private int escala;
	private int maxHeight = 0;
	private int modo;

	public Animation(double segundos, int escala, String pasta, String extensao, String... imagens) {

		espera = (int) (segundos * 1000 / imagens.length);
		this.escala = escala;

		for (String imagem : imagens) {

			try {

				PixeledImage img = ImageIO.read(new File(pasta + "/" + imagem + "." + extensao));

				this.imagens.add(img);

				if (img.getHeight() > maxHeight) {

					maxHeight = img.getHeight();
				}

			} catch (IOException e) {

				System.out.println("erro");
				e.printStackTrace();
			}
		}
	}

	public Animation(double segundos, int escala, PixeledImage[] imagens) {

		this(segundos, escala, imagens, DEFAULT);
	}

	public Animation(double segundos, int escala, PixeledImage[] imagens, int modo) {
		
		this.modo = modo;
		
		if (modo == 2) {
			
			segundos = segundos * imagens.length / (imagens.length * 2 - 1);
		}
		
		espera = (int) (segundos * 1000 / imagens.length);
		this.escala = escala;
		
		for (PixeledImage imagem : imagens) {

			this.imagens.add(imagem);

			System.out.println();
			
			if (imagem.getHeight() > maxHeight) {

				maxHeight = imagem.getHeight();
			}
		}
	}
	
	
	public void animate() {

		this.parent = SwingUtilities.getWindowAncestor(this);

		g = (Graphics2D) parent.getGraphics();

		while (true) {

			desenharImagens();
		}
	}

	private void desenharImagens() {

		switch (modo) {
		
			case 1: {
				
				for (PixeledImage imagem : imagens) {
					
					desenharImagem(imagem);
				}
				break;
			}
			
			case 2: {
				
				for (int i = 0; i < imagens.size(); i++) {
					
					desenharImagem(imagens.get(i));
				}
				
				for (int i = imagens.size() - 1; i > 0; i--) {
					
					desenharImagem(imagens.get(i));
				}
				
				break;
			}
			
			case 3: {
				
				for (int i = imagens.size() - 1; i > -1; i--) {
					
					desenharImagem(imagens.get(i));
				}
				break;
			}
		}
	}
	
	private void desenharImagem(PixeledImage imagem) {
		
		int x = getX();
		int y = getY() + maxHeight * escala - imagem.getHeight() * escala;
		int width = imagem.getWidth() * escala;
		int height = imagem.getHeight() * escala;

		g.drawImage(imagem, x, y, width, height, null);
		
		waitAndClean(x, y, width, height);
	}
	
	private void waitAndClean(int x, int y, int w, int h) {
		
		try {

			Thread.sleep(espera);
		} catch (Exception e) {

			System.out.println("to com pressa");
		}

		g.setColor(parent.getBackground());
		g.fillRect(x, y, w, h);
	}
	
	
	public static PixeledImage[] getImages(String arquivo, int x, int y, int w, int h, int dw, int dh,
			Color corFundo) {

		PixeledImage imagemPrincipal = null;

		try {

			imagemPrincipal = ImageIO.read(new File(arquivo));
		} catch (IOException e) {

			System.out.println("Impossível encontrar imagem principal");
		}

		imagemPrincipal = tirarCor(imagemPrincipal, corFundo);
		
		return separarImagens(imagemPrincipal, x, y, w, h, dw, dh);
	}
	
	public static PixeledImage[] getImages(String arquivo, int dw, int dh, Color corFundo) {
		
		PixeledImage imagemPrincipal = null;
		
		try {
			
			imagemPrincipal = ImageIO.read(new File(arquivo));
		} catch (Exception e) {
			
			System.out.println("n deu");
		}
		
		int w = imagemPrincipal.getWidth();
		int h = imagemPrincipal.getHeight();
		
		return getImages(arquivo, 0, 0, w, h, dw, dh, corFundo);
	}


	private static PixeledImage tirarCor(PixeledImage imagem, Color cor) {
		
		PixeledImage copia = new PixeledImage(imagem.getWidth(), imagem.getHeight(), imagem.getType());
		
		for (int x = 0; x < imagem.getWidth(); x++) {
			
			for (int y = 0; y < imagem.getHeight(); y++) {
				
				if (imagem.getRGB(x, y) != cor.getRGB()) {
					
					copia.setRGB(x, y, imagem.getRGB(x, y));
				}
			}
		}
		
		return copia;
	}
	
	private static PixeledImage[] separarImagens(PixeledImage imagemPrincipal, int x, int y, int w, int h, int dw, int dh) {

		PixeledImage[] imagens = new PixeledImage[(w / dw) * (h / dh)];
		
		int i = 0;

		for (int l = 0; l < h / dh; l++) {

			for (int c = 0; c < w / dw; c++) {

				imagens[i] = separarImagem(imagemPrincipal, x, y, dw, dh, l, c);

				try {

					ImageIO.write(imagens[i], "png", new File("output/" + i + ".png"));
				} catch (IOException e) {

					System.out.println("Não foi possível salvar a imagem");
				}

				i++;
			}
		}
		
		return imagens;
	}

	private static PixeledImage separarImagem(PixeledImage imagemPrincipal, int x, int y, int dw, int dh, int l,
			int c) {

		PixeledImage imagem = new PixeledImage(dw, dh, PixeledImage.TYPE_INT_ARGB);

		for (int j = 0; j < dw; j++) {

			for (int k = 0; k < dh; k++) {

				int pixelOriginal = imagemPrincipal.getRGB(x + j + c * dw, y + k + l * dh);

				imagem.setRGB(j, k, pixelOriginal);
			}
		}

		return imagem;
	}
}