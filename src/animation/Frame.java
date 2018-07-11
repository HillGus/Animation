package animation;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import componentes.ImageHandler;
import componentes.Pixel;
import componentes.PixeledImage;

public class Frame extends JFrame {

	
	public static void main(String args[]) {
		
		
		new Frame();
	}
	
	
	public Frame() {
		
		setSize(600, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setLocationRelativeTo(null);
		
		PixeledImage[] imagens = ImageHandler.cutImages("input/standby.png", Color.decode("#33d402"));
		
		Animation anim = new Animation(0.7, 4, imagens, Animation.BOOMERANGUE);
		anim.setLocation(50, 50);
		
		add(anim);
		
		setVisible(true);
		
		anim.animate();
	}
}