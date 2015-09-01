package MVC;

import Audio.AudioPlayer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class EndWindow extends 	Window {

	private BufferedImage bg;
	private BufferedImage bgblack;
	private static  int currentMenuChoice = 0;

	private AudioPlayer music,music1;
	
	private View view ;
	Controller controller;
	public EndWindow(View view,Controller controller) {
		this.view = view;
		this.controller = controller;
		initializeWindow();
	}

	@Override
	public void initializeWindow() {
		try {
			bg = ImageIO.read(new File("endbg.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			bgblack = ImageIO.read(new File("blackbg.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
			music = new AudioPlayer("applause.mp3");
			music.play();
			music1 = new AudioPlayer("tick.mp3");

		
		
	}

	@Override
	public void drawWindow(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setBackground(Color.BLACK);
		g.drawImage(bgblack, (int) 300, (int) 0, null);
		g.drawImage(bg, (int) 0, (int) 0, null);

		g.setColor(Color.YELLOW);
		g.setFont(new Font("Arial", Font.PLAIN, 30));

		

		StringBuilder b = new StringBuilder();
		b.append("Player 1 Score : ");
		b.append(controller.model.getPlayerA().getScore());
		StringBuilder b1 = new StringBuilder();
		b1.append("Player 2 Score : ");
		b1.append(controller.model.getPlayerB().getScore());
		g.drawString(b.toString(), 100, 300);
		g.drawString(b1.toString(), 100, 340);
		if (currentMenuChoice == 0) {
			g.setColor(Color.RED);
			g.drawString("Start Again !", 100, 400);
			g.setColor(Color.WHITE);
			g.drawString("Quit..", 100, 400 + 40);
		} else {
			g.setColor(Color.white);
			g.drawString("Start Again !", 100, 400);
			g.setColor(Color.RED);
			g.drawString("Quit..", 100, 400 + 40);
		}
		
	}

	@Override
	public void updateWindow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(int keyCode) {
		if(keyCode == KeyEvent.VK_DOWN){
			music1.play();
			currentMenuChoice = (currentMenuChoice + 1)%2;
		}else if(keyCode == KeyEvent.VK_UP){
			music1.play();

			currentMenuChoice = (currentMenuChoice  - 1 )%2 ;
			if(currentMenuChoice == -1)
				currentMenuChoice = 1;
		}else if(keyCode == KeyEvent.VK_ENTER){
			music1.play();

			if(currentMenuChoice == 0){
				view.setCurrentWindow(view.START_WINDOW);
			}else if(currentMenuChoice == 1){
				System.exit(0);
			}
		}
		
	}

	@Override
	public void keyReleased(int keyCode) {
		music1.stop();

		// TODO Auto-generated method stub
		
	}

}
