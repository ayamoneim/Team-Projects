package ObjectPool;

import java.io.IOException;

import factory.*;
import MainObjects.*;

//  Singleton Design Pattern  //
public class ShapePool extends ObjectPool<Shape> {

	private static ShapePool instance = null;

	private ShapePool() {
	}

	public static ShapePool getInstance() {
		if (instance == null) {
			instance = new ShapePool();

		}

		return instance;
	}

	@Override
	protected Shape create() throws IOException {
		return new ShapeFactory().createShape();
	}

}
