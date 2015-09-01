package MainObjects;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import State.*;

public abstract class Shape {

	/*
	 * pictures of shapes to be chosen from randomly when created by the Factory
	 */

	private ShapeState onRailLeft1 = new onRailLeft1(this);
	private ShapeState onRailLeft2 = new onRailLeft2(this);
	private ShapeState onRailRight1 = new onRailRight1(this);
	private ShapeState onRailRight2 = new onRailRight2(this);

	private ShapeState falling = new falling(this);
	private ShapeState caught = new caught(this);
	private ShapeState inPool = new inPool(this);

	private ShapeState currentState;

	private int imageNumber;
	private BufferedImage shapeImage;
	private int upLeftCorner_x;
	private int upLeftCorner_y;

	public void draw(Graphics g) {
		g.drawImage(getShapeImage(), (int) getUpLeftCorner_x(),
				(int) getUpLeftCorner_y(), null);

	}

	public BufferedImage getShapeImage() {
		return shapeImage;
	}

	public void setShapeImage(BufferedImage shapeImage) {
		this.shapeImage = shapeImage;
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

	public int getImageNumber() {
		return imageNumber;
	}

	public void setImageNumber(int imageNumber) {
		this.imageNumber = imageNumber;
	}

	public void setState(ShapeState state) {
		this.currentState = state;
	}
	
	public void perform(){
		getState().perform();
	}
	
	public ShapeState getOnRailLeft1(){
		return onRailLeft1;
	}
	public ShapeState getOnRailLeft2(){
		return onRailLeft2;
	}
	public ShapeState getOnRailRight1(){
		return onRailRight1;
	}
	public ShapeState getOnRailRight2(){
		return onRailRight2;
	}
	public ShapeState getFalling(){
		return falling;
	}
	public ShapeState getcaught(){
		return caught;
	}
	public ShapeState getinPool(){
		return inPool;
	}
	public ShapeState getState(){
		return currentState;
	}
}
