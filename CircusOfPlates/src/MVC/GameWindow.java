package MVC;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;

public class GameWindow extends Window {

	private Controller controller;
	private View view;
	private BufferedImage bg;
	private AudioPlayer music;

	public GameWindow(View view, Controller controller) {
		this.controller = controller;
		this.view = view;
		initializeWindow();
	}

	@Override
	public void initializeWindow() {
		controller.model.setCurrentlyDisplayed();
		try {
			bg = ImageIO.read(new File("stage.jpg"));
		} catch (Exception e) {
			e.getStackTrace();
		}

		if (music == null) {
			music = new AudioPlayer("sound.mp3");
			music.play();
		}
	}

	@Override
	public void drawWindow(Graphics2D g) {

		if (controller.model.getPlayerA().getLeftStackPeak_y() <= 250
				|| controller.model.getPlayerA().getRightStackPeak_y() <= 250
				|| controller.model.getPlayerB().getLeftStackPeak_y() <= 250
				|| controller.model.getPlayerB().getRightStackPeak_y() <= 250) {
			music.stop();
			controller.model.getCurrentlyDisplayed().removeAll();
			controller.model.getShapePool().removeAll();
			view.setCurrentWindow(view.END_WINDOW);
		} else {
			g.drawImage(bg, (int) 0, (int) 0, null);

			controller.model.getPlayerA().draw(g);
			controller.model.getPlayerB().draw(g);
			g.setFont(new Font("Arial", Font.PLAIN, 20));

			g.setColor(Color.ORANGE);
			StringBuilder b = new StringBuilder();
			b.append("PLAYER 1 : ");
			b.append(controller.model.getPlayerA().getScore());
			g.drawString(b.toString(), 400, 30);

			StringBuilder b1 = new StringBuilder();
			b1.append("PLAYER 2 : ");
			b1.append(controller.model.getPlayerB().getScore());
			g.drawString(b1.toString(), 400, 60);
			controller.model.getCurrentlyDisplayed().Display(g);
		}
	}

	@Override
	public void updateWindow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(int keyCode) {
		if (keyCode == KeyEvent.VK_SPACE) {

			view.setCurrentWindow(view.SAVE_WINDOW);

			controller.pause(!controller.isPaused());

		}

	}

	@Override
	public void keyReleased(int keyCode) {
		// TODO Auto-generated method stub

	}

}
