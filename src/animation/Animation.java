package animation;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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

				BufferedImage buffImg = ImageIO.read(new File(pasta + "/" + imagem + "." + extensao));
				PixeledImage img = new PixeledImage(buffImg);

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
			
			if (imagem.getHeight() > maxHeight) {

				maxHeight = imagem.getHeight();
			}
		}
	}
	
	
	public void animate() {

		this.parent = SwingUtilities.getWindowAncestor(this);

		new Thread(new Runnable() {
			
			public void run() {
				
				g = (Graphics2D) parent.getGraphics();
				
				while (true) {

					desenharImagens();
				}
			}
		}).start();
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
}