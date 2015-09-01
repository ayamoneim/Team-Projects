package MVC;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Memento.*;

public class SaveWindow extends Window {

	String[]menu = {"Save","Continue","Quit"};
	View view ;
	 int currentChoice = 0;
	Controller controller;
	BufferedImage bg;
	AudioPlayer music1;
	
	public SaveWindow(View view,Controller controller) {
		this.view = view;
		this.controller = controller;
		initializeWindow();
	}

	@Override
	public void initializeWindow() {
		currentChoice = 0;

		try{
			bg = ImageIO.read(new File("stage.jpg"));
		}catch(Exception e){
			e.getStackTrace();
		}
		music1 = new AudioPlayer("tick.mp3");

	}

	@Override
	public void drawWindow(Graphics2D g) {
		g.drawImage(bg,(int)0,(int)0,null);

		g.setFont(new Font("Arial", Font.PLAIN, 30));
		for(int i = 0;i<3;i++){
			if(i == currentChoice){
				g.setColor(Color.RED);
				
				g.drawString(menu[i], 400, 400+i*40);
				g.setColor(Color.BLACK);
			}else{
				g.drawString(menu[i], 400, 400+i*40);
			}
		}
	}

	@Override
	public void updateWindow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(int keyCode) {
		if(keyCode == KeyEvent.VK_SPACE){
			controller.pause(false);
			view.setCurrentWindow(view.GAME_WINDOW);

		}
		if(keyCode == KeyEvent.VK_DOWN){
			music1.play();
			currentChoice = (currentChoice + 1)%3;
		}else if(keyCode == KeyEvent.VK_UP){
			music1.play();
			currentChoice = (currentChoice  - 1 )%3 ;
			if(currentChoice == -1)
				currentChoice = 2;
		}else if(keyCode == KeyEvent.VK_ENTER){
			music1.play();
			if(currentChoice == 0){
				//save//
				Originator orig = new Originator();
				CareTaker caretaker = new CareTaker();
				orig.Set(controller.model);
				caretaker.addMemento(orig.StoreInMemento());
				//end save//
//				controller.pause(false);
//				view.setCurrentWindow(view.GAME_WINDOW);

			}else if(currentChoice == 1){
				controller.pause(false);
				music1.stop();
				view.setCurrentWindow(view.GAME_WINDOW);
				
			}else{
				System.exit(0);
			}
		}
		
		
	}

	@Override
	public void keyReleased(int keyCode) {
//		music.stop();
		// TODO Auto-generated method stub
		
	}

}
