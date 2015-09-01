package MVC;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;

public class StartWindow extends Window {

	BufferedImage bg;
	String[]menu = {"Play","Load","Quit"};
	View view ;
	 int currentChoice = 0;
	private AudioPlayer music;
	Controller controller ;
	
	public StartWindow(View v,Controller controller){
		this.view = v;
		this.controller = controller;
		initializeWindow();
	}
	
	@Override
	public void initializeWindow() {
		currentChoice = 0;
		try{
			bg = ImageIO.read(new File("startbg.jpg"));
		}catch(Exception e){
			e.getStackTrace();
		}
		music = new AudioPlayer("tick.mp3");
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
		
		
	}

	@Override
	public void keyPressed(int keyCode) {
		if(keyCode == KeyEvent.VK_DOWN){
			music.play();
			currentChoice = (currentChoice + 1)%3;
		}else if(keyCode == KeyEvent.VK_UP){
			music.play();
			currentChoice = (currentChoice  - 1 )%3 ;
			if(currentChoice == -1)
				currentChoice = 2;
		}else if(keyCode == KeyEvent.VK_ENTER){
			music.play();
			if(currentChoice == 0){
				view.setCurrentWindow(view.CHOOSE_PLAYER_WINDOW);
			}else if(currentChoice == 1){
				////////////load game////////////
//				CareTaker caretaker = new CareTaker();
//				Model mnew = caretaker.getMemento().getSaved();
//				System.out.println("*********************************");
//				if(mnew!=null){
//					controller.model = mnew;
//					view.setCurrentWindow(view.GAME_WINDOW);
//				}
//				else{
//					view.setCurrentWindow(view.CHOOSE_PLAYER_WINDOW);
//				}

			}else{
				System.exit(0);
			}
		}
		
	}

	@Override
	public void keyReleased(int keyCode) {
		music.stop();;
		// TODO Auto-generated method stub
		
	}

}
