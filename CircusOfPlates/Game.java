import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Audio.AudioPlayer;
import DynamicLoading.DynamicLoader;
import MVC.*;


public class Game {


	public static void main(String[]args) throws IOException{

		JFrame frame = new JFrame("Circus of Plates");
		Model model = new Model();
//		music.play();
		frame.setContentPane(new Controller(model));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		DynamicLoader loader = new DynamicLoader();
		loader.start();
	}

}
