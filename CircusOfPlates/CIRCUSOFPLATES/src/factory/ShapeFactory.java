package factory;
import java.io.IOException;
import java.util.Random;

import MainObjects.Plate;
import MainObjects.Shape;
import MainObjects.ShapeA;
import MainObjects.ShapeB;

public class ShapeFactory {


	public Shape createShape() throws IOException {
//		DynamicLoader loader = new DynamicLoader();
		Shape shape = null;
		Random randomGenerator = new Random();
		int randomShapeNumber = randomGenerator.nextInt(3);
		int randomImageNumber = randomGenerator.nextInt(7);
		if (randomShapeNumber == 0) {
//			shape = loader.createShape(0, randomImageNumber, -3, 300);
			shape = new Plate(randomImageNumber, -3, 300);
		} else if (randomShapeNumber == 1) {
//			shape = loader.createShape(1, randomImageNumber, -3, 300);
			shape = new ShapeA(randomImageNumber, -3, 300);
		} else {
//			shape = loader.createShape(2, randomImageNumber, -3, 300);
			shape = new ShapeB(randomImageNumber, -3, 300);
		}

		return shape;
	}



}
