package componentes;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageHandler {

	
	/* Primeiro método criado
	 *
	 * Pros:
	 * 
	 * 		- Divide qualquer parte da imagem
	 * 		- Tira a cor que você define
	 *
	 * Cons:
	 * 
	 * 		- Muitos parâmetros, fazendo com que o desenvolvedor tenha que saber demais sobre a imagem
	 *  	- Não dá opção ao desenvolvedor de salvar ou não
	 */
	public static PixeledImage[] cutImages(String arquivo, int x, int y, int w, int h, int dw, int dh, Color corFundo) {

		PixeledImage imagemPrincipal = getImage(arquivo);

		imagemPrincipal.retirarCor(corFundo);

		return separarImagens(imagemPrincipal, x, y, w, h, dw, dh);
	}
	
	private static PixeledImage[] separarImagens(PixeledImage imagemPrincipal, int x, int y, int w, int h, int dw,
			int dh) {

		PixeledImage[] imagens = new PixeledImage[(w / dw) * (h / dh)];

		int i = 0;

		for (int l = 0; l < h / dh; l++) {

			for (int c = 0; c < w / dw; c++) {

				imagens[i] = separarImagem(imagemPrincipal, x, y, dw, dh, l, c);

				i++;
			}
		}

		return imagens;
	}

	private static PixeledImage separarImagem(PixeledImage imagemPrincipal, int x, int y, int dw, int dh, int l,
			int c) {

		PixeledImage imagem = new PixeledImage(dw, dh);

		for (int j = 0; j < dw; j++) {

			for (int k = 0; k < dh; k++) {

				int pixelOriginal = imagemPrincipal.getRGB(x + j + c * dw, y + k + l * dh);

				imagem.setRGB(j, k, pixelOriginal);
			}
		}

		return imagem;
	}

	
	/* Segundo método criado
	 * 
	 * Pros:
	 * 
	 * 		- Menos parâmetros
	 * 		- Tira a cor que você define
	 * 
	 * Cons:
	 * 
	 *  	- Ainda muitos parâmetros
	 *  	- Não dá opção ao desenvolvedor de salvar ou não
	 *  	- Divide a imagem toda e não apenas uma parte em específico
	 */
	public static PixeledImage[] cutImages(String arquivo, int dw, int dh, Color corFundo) {

		PixeledImage imagemPrincipal = getImage(arquivo);

		int w = imagemPrincipal.getWidth();
		int h = imagemPrincipal.getHeight();

		return cutImages(arquivo, 0, 0, w, h, dw, dh, corFundo);
	}

	
	/* Terceiro método criado
	 * 
	 * Pros:
	 * 
	 * 		- Apenas dois parâmetros
	 * 		- Tira a cor que você define
	 * 		- Identifica quais partes da imagem estão separadas
	 * 
	 * Cons:
	 * 
	 * 		- Caso os sprites estejam colados não divide direito
	 * 		- Ainda obriga o desenvolvedor a saber a cor de fundo da imagem
	 *  	- Não dá opção ao desenvolvedor de salvar ou não
	 *  	- Divide a imagem toda e não apenas uma parte em específico
	 */
	public static PixeledImage[] cutImages(String arquivo, Color corFundo) {

		PixeledImage originalImage = getImage(arquivo);

		originalImage.retirarCor(corFundo);

		ArrayList<PixeledImage> imagens = dividirImagens(originalImage);
		
		return imagens.toArray(new PixeledImage[imagens.size()]);
	}
	
	private static ArrayList<PixeledImage> dividirImagens(PixeledImage originalImage) {

		ArrayList<PixeledImage> imagens = new ArrayList<>();
		
		ArrayList<Pixel> originalPixels = originalImage.getPixels();

		for (int i = 0; i < originalPixels.size(); i++) {

			Pixel px = originalPixels.get(i);

			ArrayList<Pixel> pixels = getAllConnectedPixels(px);
			originalPixels.removeAll(pixels);
			
			System.out.println((i+1) + "ª Imagem sendo processada");
			
			imagens.add(montarImagem(pixels));
		}
		
		return imagens;
	}
	
	private static PixeledImage montarImagem(ArrayList<Pixel> pixels) {
		
		int[] dimen = getDivisionDimension(pixels);
		
		PixeledImage img = new PixeledImage(dimen[2], dimen[3]);
		
		System.out.println("Bounds: (" + dimen[0] + ", " + dimen[1] + ", " + dimen[2] + ", " + dimen[3] + ")");
		System.out.println("=================================");
		
		for (Pixel p : pixels) {

			p.setX(p.getX() - dimen[0]);
			p.setY(p.getY() - dimen[1]);

			img.setPixel(p);
		}
		
		return img;
	}

	private static ArrayList<Pixel> getAllConnectedPixels(Pixel px) {

		ArrayList<Pixel> pixels = new ArrayList<>();

		pixels.add(px);

		ArrayList<Pixel> seguintes = new ArrayList<>();

		for (Pixel p : px.getNearbyPixels()) {

			seguintes.add(p);
		}

		while (seguintes.size() > 0) {

			Pixel p = seguintes.get(0);

			seguintes.remove(p);

			if (pixels.contains(p))
				continue;

			pixels.add(p);

			for (Pixel p2 : p.getNearbyPixels()) {

				if (pixels.contains(p2))
					continue;

				seguintes.add(p2);
			}
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

		return new int[] { ix, iy, fx - ix + 1, fy - iy + 1 };
	}

	
	/* Quarto método criado
	 * 
	 * Pros:
	 * 		
	 * 		- Apenas dois parâmetros
	 * 		- Não precisa passar parâmetro de cor para retirar
	 * 		- Identifica quais partes da imagem estão separadas
	 * 		- Salva as imagens, fazendo com que o programa economize tempo nas proximas inicializações
	 * 
	 * Cons:
	 * 
	 * 		- Caso os sprites estejam colados não divide direito
	 * 		- Divide a imagem toda e não apenas uma parte em específico
	 * 		- Não dá a opção de passar a cor de fundo para retirar
	 */
	public static PixeledImage[] cutImages(String arquivo, String output) {
		
		PixeledImage imagemPrincipal = getImage(arquivo);
		Color cor = new Color(imagemPrincipal.getRGB(0, 0));
		
		File pasta = getOutputDir(output, arquivo);
		File[] arquivos = pasta.listFiles();
		
		if (arquivos.length > 0) {
			
			return getImagesAt(pasta);
		}
		
		PixeledImage[] imagens = cutImages(arquivo, cor);	
		salvarImagens(imagens, pasta.getPath());
		
		return imagens;
	}
	
	
	/*
	 * Métodos gerais
	 */
	private static PixeledImage[] getImagesAt(File pasta) {
		
		File[] arquivos = pasta.listFiles();
		
		ArrayList<PixeledImage> imagens = new ArrayList<PixeledImage>();
		
		for (File arq : arquivos) {
			
			if (!arq.isFile())
				continue;
			
			if (!arq.getName().split("\\.")[arq.getName().split("\\.").length - 1].equals("png"))
				continue;
			
			try {
				
				imagens.add(new PixeledImage(ImageIO.read(arq)));
			} catch (IOException e) {
				
				System.out.println("Não foi possível ler a imagem");
				e.printStackTrace();
			}
		}
		
		return imagens.toArray(new PixeledImage[imagens.size()]);
	}
	
	private static PixeledImage getImage(String arquivo) {
		
		PixeledImage imagem = null;
		
		try {
			
			imagem = new PixeledImage(ImageIO.read(new File(arquivo)));
		} catch (IOException e) {
			
			System.out.println("Não foi possível ler a imagem");
			e.printStackTrace();
		}
		
		return imagem;
	}

	private static File getOutputDir(String output, String fileName) {
		
		String nomeArquivo = fileName.split("/")[fileName.split("/").length - 1];
		String pastaSalvar = "";
		for (int i = 0; i < nomeArquivo.split("\\.").length - 1; i++) {
			
			pastaSalvar += i != 0? (i == nomeArquivo.split("\\.").length - 2 ? "" : ".") : "";
			pastaSalvar += nomeArquivo.split("\\.")[i];
		}
		
		File pasta = new File(output + "/" + pastaSalvar);
		
		if (!pasta.exists())
			pasta.mkdir();
		else if (!pasta.isDirectory())
			pasta.mkdir();
		
		return pasta;
	}
	
	private static void salvarImagens(PixeledImage[] imagens, String output) {
		
		for (int i = 0; i < imagens.length; i++) {
			
			PixeledImage imagem = imagens[i];
			
			try {
				
				ImageIO.write(imagem, "png", new File(output + "/" + i + ".png"));
			} catch (IOException e) {
				
				System.out.println("Não foi possível salvar a imagem");
				e.printStackTrace();
			}
		}
	}
}