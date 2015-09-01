import java.io.IOException;

import javax.swing.JFrame;

import MVC.*;


public class Game {


	public static void main(String[]args) throws IOException{

		JFrame frame = new JFrame("Circus of Plates");
		Model model = new Model();
		frame.setContentPane(new Controller(model));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
