package MainObjects;
import State.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Plate extends Shape {
	protected final String[]PICS =  {"blueplate.png","blackplate.png","redplate.png","yellowrect.png","greenplate.png","brownplate.png","pinkplate.png"};
	public Plate(int imageNumber, int upLeftCorner_x, int upLeftCorner_y) throws IOException {
		BufferedImage image = ImageIO.read(new File(PICS[imageNumber]));
		this.setImageNumber(imageNumber);
		this.setShapeImage(image);
		this.setUpLeftCorner_x(upLeftCorner_x);
		this.setUpLeftCorner_y(upLeftCorner_y);
//		this.setShapeID(shapeID);
		
		this.setState(new inPool(this));
		
	}
}
