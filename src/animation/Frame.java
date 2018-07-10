package animation;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import componentes.PixeledImage;

public class Frame extends JFrame {

	
	public static void main(String args[]) {
		
		new Frame();
	}
	
	
	public Frame() {
		
		setSize(300, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setLocationRelativeTo(null);
		
		PixeledImage[] imagens = Animation.getImages("input/standby.png", 35, 51, Color.decode("#33d402"));
		
		Animation anim = new Animation(1, 5, imagens, Animation.BOOMERANGUE);
		anim.setLocation(50, 50);
		
		add(anim);
		
		setVisible(true);
		
		anim.animate();
	}
}