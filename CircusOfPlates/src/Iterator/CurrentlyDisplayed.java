package Iterator;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import MainObjects.*;
import ObjectPool.*;

public class CurrentlyDisplayed {
	ArrayList<Shape> list;

	public CurrentlyDisplayed() {
		list = new ArrayList<Shape>();
	}

	public void addShape(Shape s) {
		list.add(s);
	}

	public Shape removeShape(Shape s) {
		int index = list.indexOf(s);
		return list.remove(index);
	}

	public void removeAll() {
		list.clear();
	}

	public ArrayList<Shape> getList() {
		return list;
	}

	private Iterator<Shape> createIterator() {
		return list.iterator();
	}

	public void Display(Graphics g) {
		Iterator<Shape> iter = this.createIterator();
		while (iter.hasNext()) {
			Shape s = (Shape) iter.next();
			if (s.getState().getClass().getName().equals("State.onRailLeft1")) {
				System.out.println(s.getUpLeftCorner_x() + " "
						+ s.getShapeImage().getWidth());
			}
			s.draw(g);
		}
	}

	public int[] RailCheck() {
		int[] rails = new int[4];

		Iterator<Shape> iter = this.createIterator();
		while (iter.hasNext()) {
			Shape s = (Shape) iter.next();
			String state = s.getState().getClass().getName();
			// /////////////////////Wrong numbers
			if (state.equals("State.onRailLeft1")/* && s.getUpLeftCorner_x()<=300 */) {
				rails[0]++;
			} else if (state.equals("State.onRailLeft2") /*
														 * &&
														 * s.getUpLeftCorner_x
														 * ()<=140
														 */) {
				rails[1]++;
			} else if (state.equals("State.onRailRight1")/*
														 * &&
														 * s.getUpLeftCorner_x
														 * ()>=600
														 */) {
				rails[2]++;
			} else if (state.equals("State.onRailRight2")/*
														 * &&
														 * s.getUpLeftCorner_x
														 * ()>=780
														 */) {
				rails[3]++;
			}
		}
		if (rails[0] < 2) {
			rails[0] = 1;
		} else {
			rails[0] = 0;
		}
		if (rails[1] < 1) {
			rails[1] = 1;
		} else {
			rails[1] = 0;
		}
		if (rails[2] < 2) {
			rails[2] = 1;
		} else {
			rails[2] = 0;
		}
		if (rails[3] < 1) {
			rails[3] = 1;
		} else {
			rails[3] = 0;
		}
		// rails[0]=4-rails[0];
		// rails[1]=1-rails[1];
		// rails[2]=1-rails[2];
		// rails[3]=1-rails[3];

		return rails;
	}

	public void checkCaught(Player p1, Player p2) {

		int p1x1 = p1.getLeftStackPeak_x();
		int p1y1 = p1.getLeftStackPeak_y();
		int p1x2 = p1.getRightStackPeak_x();
		int p1y2 = p1.getRightStackPeak_y();

		int p2x1 = p2.getLeftStackPeak_x();
		int p2y1 = p2.getLeftStackPeak_y();
		int p2x2 = p2.getRightStackPeak_x();
		int p2y2 = p2.getRightStackPeak_y();
		Iterator<Shape> iter = this.createIterator();
		while (iter.hasNext()) {
			Shape s = (Shape) iter.next();
			int sx = s.getUpLeftCorner_x();
			int sy = s.getUpLeftCorner_y();
			int width = s.getShapeImage().getWidth();
			int pwin1 = -1;
			int pwin2 = -1;
			if (s.getState().getClass().getName().equals("State.caught")) {
				continue;
			}
			if (((p1y1 - (sy + s.getShapeImage().getHeight())) <= 5
					&& (p1y1 > (sy + s.getShapeImage().getHeight())) && (p1x1 >= sx && p1x1 <= sx
					+ width))) {
				pwin1 = 1;
			} else if ((p1y2 - (sy + s.getShapeImage().getHeight())) <= 5
					&& (p1y2 > (sy + s.getShapeImage().getHeight()))
					&& (p1x2 >= sx && p1x2 <= sx + width)) {
				pwin1 = 2;
			}
			if ((p2y1 - (sy + s.getShapeImage().getHeight())) <= 5
					&& (p2y1 > (sy + s.getShapeImage().getHeight()))
					&& (p2x1 >= sx && p2x1 <= sx + width)) {
				pwin2 = 1;
			} else if ((p2y2 - (sy + s.getShapeImage().getHeight())) <= 5
					&& (p2y2 > (sy + s.getShapeImage().getHeight()))
					&& (p2x2 >= sx && p2x2 <= sx + width)) {
				pwin2 = 2;
			}

			if (pwin1 != -1 || pwin2 != -1) {
				s.setState(s.getcaught());
			}

			if (pwin1 != -1 && pwin2 == -1) {
				if (pwin1 == 1) {
					s.setUpLeftCorner_y(p1y1 - s.getShapeImage().getHeight());
					p1.getLeftStack().add(s);
					p1.setLeftStackPeak_y(p1.getLeftStackPeak_y()
							- s.getShapeImage().getHeight());
				} else {
					s.setUpLeftCorner_y(p1y2 - s.getShapeImage().getHeight());
					p1.getRightStack().add(s);
					p1.setRightStackPeak_y(p1.getRightStackPeak_y()
							- s.getShapeImage().getHeight());

				}
			} else if (pwin1 == -1 && pwin2 != -1) {
				if (pwin2 == 1) {
					s.setUpLeftCorner_y(p2y1 - s.getShapeImage().getHeight());
					p2.getLeftStack().add(s);
					p2.setLeftStackPeak_y(p2.getLeftStackPeak_y()
							- s.getShapeImage().getHeight());

				} else {
					s.setUpLeftCorner_y(p2y2 - s.getShapeImage().getHeight());
					p2.getRightStack().add(s);
					p2.setRightStackPeak_y(p2.getRightStackPeak_y()
							- s.getShapeImage().getHeight());

				}
			} else if (pwin1 != -1 && pwin2 != -1) {
				Random winner = new Random();
				int w = winner.nextInt(2) + 1;
				if (w == 1) {
					if (pwin1 == 1) {
						s.setUpLeftCorner_y(p1y1
								- s.getShapeImage().getHeight());
						p1.getLeftStack().add(s);
						p1.setLeftStackPeak_y(p1.getLeftStackPeak_y()
								- s.getShapeImage().getHeight());

					} else {
						s.setUpLeftCorner_y(p1y2
								- s.getShapeImage().getHeight());
						p1.getRightStack().add(s);
						p1.setRightStackPeak_y(p1.getRightStackPeak_y()
								- s.getShapeImage().getHeight());

					}
				} else {
					if (pwin2 == 1) {
						s.setUpLeftCorner_y(p2y1
								- s.getShapeImage().getHeight());
						p2.getLeftStack().add(s);
						p2.setLeftStackPeak_y(p2.getLeftStackPeak_y()
								- s.getShapeImage().getHeight());

					} else {
						s.setUpLeftCorner_y(p2y2
								- s.getShapeImage().getHeight());
						p2.getRightStack().add(s);
						p2.setRightStackPeak_y(p2.getRightStackPeak_y()
								- s.getShapeImage().getHeight());

					}
				}
			}

		}
	}

	public void checkInPool(ShapePool pool) {
		Iterator<Shape> iter = this.createIterator();
		ArrayList<Shape> temp = new ArrayList<Shape>();
		while (iter.hasNext()) {
			Shape s = (Shape) iter.next();
			if (s.getUpLeftCorner_y() == 680 - s.getShapeImage().getHeight()) {
				s.setState(s.getinPool());
				temp.add(s);
			}
		}
		while (!temp.isEmpty()) {
			list.remove(temp.get(0));
			pool.checkIn(temp.remove(0));
		}
	}

	public void PerformAll() {
		Iterator<Shape> iter = this.createIterator();
		while (iter.hasNext()) {
			Shape s = (Shape) iter.next();
			s.perform();
		}
	}
}
