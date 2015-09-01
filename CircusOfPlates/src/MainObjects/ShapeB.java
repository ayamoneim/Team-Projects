package MainObjects;
import State.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;




public class ShapeB extends Shape {
	
	
	protected final String[]PICS =   {"bluerect.png","blackrect.png","redrect.png","yellowrect.png","greenrect.png","brownrect.png","pinkrect.png"};
	public ShapeB(int imageNumber, int upLeftCorner_x, int upLeftCorner_y) throws IOException {
		BufferedImage image = ImageIO.read(new File(PICS[imageNumber]));
		this.setImageNumber(imageNumber);
		this.setShapeImage(image);
		this.setUpLeftCorner_x(upLeftCorner_x);
		this.setUpLeftCorner_y(upLeftCorner_y);
		
		this.setState(new inPool(this));

	}


	
	

	
}
