package MainObjects;
import State.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



public class ShapeA extends Shape {

	protected final String[]PICS =  {"bluestar.png","blackstar.png","redstar.png","yellowstar.png","greenstar.png","brownstar.png","pinkstar.png"};
	public ShapeA(int imageNumber, int upLeftCorner_x, int upLeftCorner_y) throws IOException {
		BufferedImage image = ImageIO.read(new File(PICS[imageNumber]));
		this.setImageNumber(imageNumber);
		this.setShapeImage(image);
		this.setUpLeftCorner_x(upLeftCorner_x);
		this.setUpLeftCorner_y(upLeftCorner_y);
		
		this.setState(new inPool(this));

	}
}
