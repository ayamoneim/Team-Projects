package ObjectPool;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class ObjectPool<Shape> {

	private List<Shape> available;

	public ObjectPool() {

		available = new LinkedList<Shape>();

	}

	protected abstract Shape create() throws IOException;

	public synchronized Shape checkOut() throws IOException {
		Shape shape = null;
		if (available.size() > 0) {
			Random randomGenerator = new Random();

			int randomIndex = randomGenerator.nextInt(available.size());

			shape = available.remove(randomIndex);
			return shape;
		} else {
			shape = create();
		}

		return shape;
	}

	public void removeAll() {
		available.clear();
	}

	public synchronized void checkIn(Shape returnedShape) {
		available.add(returnedShape);
	}
}