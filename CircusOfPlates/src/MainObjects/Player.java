package MainObjects;

import MVC.Controller;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Player {

	protected final String PIC[] = { "clown.png", "clown1.png", "clown7.png" };// ,

//	protected final BufferedImage STICK_IMAGE = null;// initialize

	private int playerNum;

	private int score;
	private int upLeftCorner_x;
	private int upLeftCorner_y;
	private int rightStackPeak_y;
	private int leftStackPeak_x;
	private int rightStackPeak_x;
	private int leftStackPeak_y;
	private int playerImageNumber;
	private ArrayList<Shape> leftStack = new ArrayList<Shape>();
	private ArrayList<Shape> rightStack = new ArrayList<Shape>();

	public Player(int playerImageNumber, int num, int upLeftCorner_x,
			int upLeftCorner_y) {

		this.setUpLeftCorner_y(upLeftCorner_y);
		this.setUpLeftCorner_x(upLeftCorner_x);
		this.setPlayerImageNumber(playerImageNumber);
		this.setPlayerImageNumber(playerImageNumber);
		if (getPlayerImageNumber() == 0) {
			this.setLeftStackPeak_x(getUpLeftCorner_x() + 13);
			this.setLeftStackPeak_y(getUpLeftCorner_y() + 40);
			this.setRightStackPeak_x(getUpLeftCorner_x() + 125);
			this.setRightStackPeak_y(getUpLeftCorner_y() + 25);
		} else if (getPlayerImageNumber() == 1) {
			this.setLeftStackPeak_x(getUpLeftCorner_x() + 9);
			this.setLeftStackPeak_y(getUpLeftCorner_y() + 112);
			this.setRightStackPeak_x(getUpLeftCorner_x() + 135);
			this.setRightStackPeak_y(getUpLeftCorner_y() + 112);
		} else if (getPlayerImageNumber() == 2) {
			this.setLeftStackPeak_x(getUpLeftCorner_x() + 4);
			this.setLeftStackPeak_y(getUpLeftCorner_y() + 75);
			this.setRightStackPeak_x(getUpLeftCorner_x() + 208);
			this.setRightStackPeak_y(getUpLeftCorner_y() + 75);
		}
		this.playerNum = num;
	}

	public void draw(Graphics g) {

		BufferedImage playerImage = null;
		try {
			playerImage = ImageIO.read(new File(PIC[playerImageNumber]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (playerImage != null) {
			g.drawImage(playerImage, (int) getUpLeftCorner_x(),
					(int) getUpLeftCorner_y(), null);
		}

	}

	public int getUpLeftCorner_x() {
		return upLeftCorner_x;
	}

	public void setUpLeftCorner_x(int upLeftCorner_x) {
		this.upLeftCorner_x = upLeftCorner_x;
	}

	public int getUpLeftCorner_y() {
		return upLeftCorner_y;
	}

	public void setUpLeftCorner_y(int upLeftCorner_y) {
		this.upLeftCorner_y = upLeftCorner_y;
	}

	public ArrayList<Shape> getRightStack() {
		return rightStack;
	}

	public int getRightStackPeak_y() {
		return rightStackPeak_y;
	}

	public void setRightStackPeak_y(int rightStackPeak_y) {
		this.rightStackPeak_y = rightStackPeak_y;
	}

	public ArrayList<Shape> getLeftStack() {
		return leftStack;
	}

	public void setLeftStack(ArrayList<Shape> leftStack) {
		this.leftStack = leftStack;
	}

	public int getLeftStackPeak_y() {
		return leftStackPeak_y;
	}

	public void setLeftStackPeak_y(int leftStackPeak_y) {
		this.leftStackPeak_y = leftStackPeak_y;
	}

	public int getRightStackPeak_x() {
		return rightStackPeak_x;
	}

	public void setRightStackPeak_x(int rightStackPeak_x) {
		this.rightStackPeak_x = rightStackPeak_x;
	}

	public int getLeftStackPeak_x() {
		return leftStackPeak_x;
	}

	public void setLeftStackPeak_x(int leftStackPeak_x) {
		this.leftStackPeak_x = leftStackPeak_x;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void notifyObserver(Controller c) {
		boolean p1s1 = false, p1s2 = false;
		int size = leftStack.size() - 1;
		if (leftStack.size() >= 3) {
			if ((leftStack.get(size).getImageNumber() == leftStack
					.get(size - 1).getImageNumber())
					&& (leftStack.get(size).getImageNumber() == leftStack.get(
							size - 2).getImageNumber())) {
				p1s1 = true;
			}
		}
		size = rightStack.size() - 1;

		if (rightStack.size() >= 3) {
			if ((rightStack.get(size).getImageNumber() == rightStack.get(
					size - 1).getImageNumber())
					&& (rightStack.get(size).getImageNumber() == rightStack
							.get(size - 2).getImageNumber())) {
				p1s2 = true;
			}
		}
		c.Observe(p1s1, p1s2, getPlayerNum());
	}

	public int getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}

	public int getPlayerImageNumber() {
		return playerImageNumber;
	}

	public void setPlayerImageNumber(int playerImageNumber) {
		this.playerImageNumber = playerImageNumber;
	}

}
