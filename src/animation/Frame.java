package animation;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Frame extends JFrame {

	
	public static void main(String args[]) {
		
		new Frame();
	}
	
	
	public Frame() {
		
		setSize(300, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setLocationRelativeTo(null);
		setBackground(Color.WHITE);
		
		BufferedImage[] imagens = Animation.getImages("input/standby.png", 0, 0, 140, 51, 35, 51, Color.decode("#33d402"));
		
		Animation anim = new Animation(0.7, 4, imagens);
		anim.setLocation(50, 50);
		
		getContentPane().add(anim);
		
		setVisible(true);
		
		anim.animate();
	}
}