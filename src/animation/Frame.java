package animation;

import javax.swing.JFrame;

import componentes.ImageHandler;
import componentes.PixeledImage;

public class Frame extends JFrame {

	
	public static void main(String args[]) {
		
		//new Frame();
	}
	
	
	public Frame() {
		
		setSize(700, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setLocationRelativeTo(null);
		
		PixeledImage[] imagens = ImageHandler.cutImages("input/standby.png", "output");
		
		Animation anim = new Animation(1, 10, imagens, Animation.BOOMERANGUE);
		anim.setLocation(50, 50);
		
		add(anim);
		
		setVisible(true);
		
		anim.animate();
	}
}