package componentes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageHandler {

	public static PixeledImage[] cutImages(String arquivo, int x, int y, int w, int h, int dw, int dh,
			Color corFundo) {

		PixeledImage imagemPrincipal = null;

		try {

			BufferedImage img = ImageIO.read(new File(arquivo));
			imagemPrincipal = new PixeledImage(img);
		} catch (IOException e) {

			System.out.println("Imposs�vel encontrar imagem principal");
		}

		imagemPrincipal.retirarCor(corFundo);
		
		return separarImagens(imagemPrincipal, x, y, w, h, dw, dh);
	}
	
	public static PixeledImage[] cutImages(String arquivo, int dw, int dh, Color corFundo) {
		
		PixeledImage imagemPrincipal = null;
		
		try {
			
			BufferedImage img = ImageIO.read(new File(arquivo));
			imagemPrincipal = new PixeledImage(img);
		} catch (Exception e) {
			
			System.out.println("n deu");
		}
		
		int w = imagemPrincipal.getWidth();
		int h = imagemPrincipal.getHeight();
		
		return cutImages(arquivo, 0, 0, w, h, dw, dh, corFundo);
	}
	
	public static PixeledImage[] cutImages(String arquivo, Color corFundo) {
		
		ArrayList<PixeledImage> imagens = new ArrayList<>();
		
		PixeledImage img = null;
		try {
			img = new PixeledImage(ImageIO.read(new File(arquivo)));
		} catch (IOException e) {
			
			System.out.println("Não foi possível ler a imagem");
			e.printStackTrace();
		}
		
		ArrayList<Pixel> originalPixels = img.getPixels();
		
		for (int i = 0; i < originalPixels.size(); i++) {
			
			Pixel px = originalPixels.get(i);
			
			if (px.getRGB() != corFundo.getRGB()) {
				
				ArrayList<Pixel> pixels = getAllConnectedPixels(px, corFundo);
				
				originalPixels.removeAll(pixels);
				i -= pixels.size();
				
				if (i < 0)
					i = 0;
				
				int[] dimen = getDivisionDimension(pixels);
				int width = dimen[2];
				int height = dimen[3];
				
				PixeledImage newImg = new PixeledImage(width, height, img.getType());
				
				for (Pixel pixel : pixels) {
					
					pixel.setX(pixel.getX() - dimen[0]);
					pixel.setY(pixel.getY() - dimen[1]);
					
					newImg.addPixel(pixel);
				}
				
				if (!imagens.contains(newImg)) {
					
					imagens.add(newImg);
					
					try {
						
						ImageIO.write(newImg, "png", new File("output/" + px.getX() + "" + px.getY() + ".png"));
					} catch (Exception e) {}
				}
			}
		}	
		
		return imagens.toArray(new PixeledImage[imagens.size()]);
	}
	
	
	private static ArrayList<Pixel> getAllConnectedPixels(Pixel px, Color corFundo) {
		
		ArrayList<Pixel> pixels = new ArrayList<>();
		
		pixels.add(px);
		
		ArrayList<Pixel> seguintes = new ArrayList<>();
		
		for (Pixel p : px.getNearbyPixels()) {
			
			seguintes.add(p);
		}
		
		for (int i = 0; i < seguintes.size(); i++) {
			
			Pixel p = seguintes.get(i);
			
			seguintes.remove(p);
			
			if ((pixels.contains(p)) || (p.getRGB() == corFundo.getRGB()))
				continue;
			
			pixels.add(p);
			
			for (Pixel p2 : p.getNearbyPixels()) {
				
				if (pixels.contains(p2))		
					continue;
				
				seguintes.add(p2);
			}
			
			i--;
		}
		
		return pixels;
	}
	
	private static int[] getDivisionDimension(ArrayList<Pixel> pixels) {
		
		int fx = 0;
		int fy = 0;
		int ix = Integer.MAX_VALUE;
		int iy = Integer.MAX_VALUE;
		
		for (Pixel p : pixels) {
			
			if (p.getX() > fx) {
				
				fx = p.getX();
			}
			
			if (p.getY() > fy) {
				
				fy = p.getY();
			}
			
			if (p.getX() < ix) {
				
				ix = p.getX();
			}
			
			if (p.getY() < iy) {
				
				iy = p.getY();
			}
		}
		
		return new int[] {ix, iy, fx - ix + 1, fy - iy + 1};
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

					System.out.println("N�o foi poss�vel salvar a imagem");
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