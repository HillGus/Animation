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
		
		BufferedImage[] imagens = Animation.getImages("input/standby.png", 35, 51, Color.decode("#33d402"));
		
		Animation anim = new Animation(0.5, 3, imagens, Animation.BACKWARDS);
		anim.setLocation(50, 50);
		
		add(anim);
		
		setVisible(true);
		
		anim.animate();
	}
}