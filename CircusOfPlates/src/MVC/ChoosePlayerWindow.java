package MVC;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import MainObjects.Player;

public class ChoosePlayerWindow extends Window {

	BufferedImage bg;

	protected final String PIC[] = { "clown.png", "clown1.png", "clown7.png"};//,
//			"clown3.png",  "clown5.png" ,"clown6.png"};

	BufferedImage arrow;

	private int currentChoice = 0;
	private int numberOfChoices = 0;
	private View view;

	Controller controller;
	private AudioPlayer music;

	public ChoosePlayerWindow(View view,Controller controller) {
		this.view = view;
		this.controller = controller;
		initializeWindow();

	}

	@Override
	public void initializeWindow() {
		try {
			bg = ImageIO.read(new File("stage.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		music = new AudioPlayer("tick.mp3");

	}

	@Override
	public void drawWindow(Graphics2D g) {
		
		g.drawImage(bg, (int) 0, (int) 0, null);
		g.setFont(new Font("Arial", Font.PLAIN, 50));
		g.setColor(Color.WHITE);
		if(numberOfChoices == 0){
			g.drawString("Choose Player A", 300, 200);
		}else{
			g.drawString("Choose Player B", 300, 200);
		}
		for(int i = 0;i<PIC.length;i++){
			
			BufferedImage img = null;
			try {
				img = ImageIO.read(new File(PIC[i]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.drawImage(img, (int)100+i*200, 680-img.getHeight(), null);
			if(i == currentChoice){
				try {
					arrow = ImageIO.read(new File("arrow.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				g.drawImage(arrow,(int)( 50 + i *200 +0.5*img.getWidth() ),680 - 300, null);
			}
			
		}
		
		

	}

	@Override
	public void updateWindow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(int keyCode) {
		if (keyCode == KeyEvent.VK_ENTER) {
			music.play();
			numberOfChoices++;
		
			if(numberOfChoices==1){
				controller.model.setPlayerA(new Player(currentChoice,1,200,500));
			}else{
				
				controller.model.setPlayerB(new Player(currentChoice,2,600,500));
				view.setCurrentWindow(view.GAME_WINDOW);

			}
		} else if (keyCode == KeyEvent.VK_LEFT) {
			music.play();
			currentChoice = (currentChoice - 1) % this.PIC.length;
			if (currentChoice == -1)
				currentChoice = this.PIC.length - 1;

		} else if (keyCode == KeyEvent.VK_RIGHT) {
			music.play();
			currentChoice = (currentChoice + 1) % this.PIC.length;
			
		}

	}

	@Override
	public void keyReleased(int keyCode) {
		music.stop();
		// TODO Auto-generated method stub

	}

}
