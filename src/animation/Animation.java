package animation;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Animation extends JPanel {

	
	ArrayList<BufferedImage> imagens = new ArrayList<>();
	
	Container parent;
	
	int espera;
	int escala;
	int maxHeight = 0;
	
	
	public Animation(double segundos, int escala, String pasta, String extensao, String... imagens) {
		
		espera = (int) (segundos * 1000 / imagens.length);
		this.escala = escala;
		
		for (String imagem : imagens) {
			
			try {
				
				BufferedImage img = ImageIO.read(new File(pasta + "/" + imagem + "." + extensao));
				
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
	
	public Animation(double segundos, int escala, BufferedImage[] imagens) {
		
		espera = (int) (segundos * 1000 / imagens.length);
		this.escala = escala;
		
		for (BufferedImage imagem : imagens) {
			
			this.imagens.add(imagem);
			
			if (imagem.getHeight() > maxHeight) {
				
				maxHeight = imagem.getHeight();
			}
		}
	}
	
	
	public void animate() {
		
		this.parent = SwingUtilities.getWindowAncestor(this);
		
		Graphics2D g = (Graphics2D) parent.getGraphics();
		
		while (true) {
			
			for (BufferedImage imagem : imagens) {
				
				int x = getX();
				int y = getY() + maxHeight * escala - imagem.getHeight() * escala;
				int width = imagem.getWidth() * escala;
				int height = imagem.getHeight() * escala;
				
				g.drawImage(imagem, x, y, width, height, null);
				
				try {
					
					Thread.sleep(espera);
				} catch (Exception e) {
					
					System.out.println("to com pressa");
				}
				
				g.setColor(parent.getBackground());
				g.fillRect(x, y, width, height);
			}
		}
	}

	
	public static BufferedImage[] getImages(String arquivo, int x, int y, int w, int h, int dw, int dh, Color corFundo) {
		
		BufferedImage[] imagens = new BufferedImage[(w / dw) * (h / dh)];
		
		BufferedImage imagemPrincipal = null;
		
		try {
			
			imagemPrincipal = ImageIO.read(new File(arquivo));
		} catch (IOException e) {
			
			System.out.println("Impossível encontrar imagem principal");
		}
		
		int i = 0;
		
		for (int l = 0; l < h / dh; l++) {
			
			for (int c = 0; c < w / dw; c++) {
				
				BufferedImage imagem = imagens[i];
				
				imagem = new BufferedImage(dw, dh, BufferedImage.TYPE_INT_ARGB);
				
				for (int j = 0; j < dw; j++) {
					
					for (int k = 0; k < dh; k++) {
						
						int pixelOriginal = imagemPrincipal.getRGB(j + c * dw, k + l * dh);
						
						if (pixelOriginal != corFundo.getRGB()) {
							
							imagem.setRGB(j, k, pixelOriginal);
						}
					}
				}
				
				try {
					
					ImageIO.write(imagem, "png", new File("output/" + i + ".png"));
				} catch (IOException e) {
					
					System.out.println("Não foi possível salvar a imagem");
				}
				
				imagens[i] = imagem;
				
				i++;
			}
		}
		
		return imagens;
	}
}