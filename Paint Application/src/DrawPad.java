
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

class DrawPad extends JComponent {
	Image image;
	Graphics2D graphics2D;

	MouseHandler m = new MouseHandler();

	public DrawPad() {
		setDoubleBuffered(false);
		addMouseListener(m);
		addMouseMotionListener(m);

	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image == null) {
			image = createImage(getSize().width, getSize().height);
			graphics2D = (Graphics2D) image.getGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			clear();
		}

		g.drawImage(image, 0, 0, null);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		for (int i = 0; i < s.size(); i++) {
			if ((s.get(i).getClass().toString().equals("class Geometry.Line") || s
					.get(i).getClass().toString()
					.equals("class Geometry.Triangle"))
					&& s.get(i).isSelected()) {
				g.setColor(new Color(123, 115, 232));
			}
			s.get(i).draw(g);
			g.setColor(Color.BLACK);
			if (s.get(i).isSelected()) {
				s.get(i).setFrame(g);
				s.get(i).setResizeBorders(g);
			}
		}

		repaint();
	}

	public void clear() {
		graphics2D.setPaint(Color.white);
		graphics2D.fillRect(0, 0, getSize().width, getSize().height);
		s.clear();
		graphics2D.setPaint(Color.black);
		repaint();
	}

	private Point last;
	private LinkedList<Point> lastPoint = new LinkedList<Point>();
	private boolean dragging = false;
	private Shape CS;

	public LinkedList<Shape> s = new LinkedList<Shape>();
	public Stack<LinkedList> undo = new Stack<LinkedList>();
	public Stack<LinkedList> redo = new Stack<LinkedList>();
	public deepCopy cpy = new deepCopy();

	public int current = -1;
	public int shape = 0;
	public boolean inside, drawpad, dragged;

	public int index = -1;

	public int point = -1;

	public class MouseHandler extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			last = e.getPoint();

			point = -1;

			for (int i = s.size() - 1; i >= 0; i--) {
				if (s.get(i).InsideShape(last)) {
					dragging = true;
					current = s.size() - 1;
					CS = s.remove(i);
					s.add(CS);

					if (s.getLast().isSelected()) {
						inside = true;
					} else {
						s.getLast().selected = true;
					}
					break;
				}
			}
			if (!dragging) {
				for (int i = 0; i < s.size(); i++) {
					if (s.get(i).isSelected()) {
						for (int j = 0; j < 8; j++) {
							if (s.get(i).points[j] != null
									&& s.get(i).points[j]
											.contains(e.getPoint())) {
								point = j;
								current = s.size() - 1;
								CS = s.remove(i);
								s.add(CS);
								lastPoint = new LinkedList<Point>();
								for (int k = 0; k < s.size(); k++) {
									lastPoint.add(null);
								}

								return;
							}
						}
					}
				}
			}
			if (!dragging && shape != 0 && shape <= 9 && point == -1) {

				if (shape == 1)
					CS = new Circle();

				else if (shape == 2)
					CS = new Rectangle();

				else if (shape == 3)
					CS = new Triangle();

				else if (shape == 4)
					CS = new Ellipse();

				else if (shape == 5)
					CS = new Square();

				else if (shape == 9)
					CS = new Line();

				else if (shape == 6) {

					if (LoaderFileChooser.filen != "cancel") {
						ClassLoader parentClassLoader = ClassLoading.class
								.getClassLoader();
						ClassLoading classLoader = new ClassLoading(
								parentClassLoader);
						try {
							Class rcl = classLoader
									.loadClass(LoaderFileChooser.pacclassname);

							Object rl = rcl.newInstance();
							Class[] param1 = new Class[1];
							param1[0] = Point.class;

							Class[] param2 = new Class[1];
							param2[0] = Graphics.class;

							Class[] param3 = new Class[1];
							param3[0] = Point.class;

							Class[] param4 = new Class[4];
							param4[0] = param4[1] = Point.class;
							param4[2] = Boolean.class;
							param4[3] = Integer.class;

							Class[] param5 = new Class[2];
							param5[0] = Integer.class;
							param5[1] = Point.class;

							Method m1 = rcl.getMethod("InsideShape", param1);
							Method m2 = rcl.getMethod("draw", param2);
							Method m3 = rcl.getMethod("pressing", param3);
							Method m4 = rcl.getMethod("drag", param4);
							Method m5 = rcl.getMethod("getWidth");
							Method m6 = rcl.getMethod("getHeight");
							Method m7 = rcl.getMethod("isSelected");
							Method m8 = rcl.getMethod("resize", param5);

							CS = (Shape) rl;

						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				}
				if ((shape == 6 && LoaderFileChooser.filen != "cancel")
						|| shape != 6) {
					CS.pressing(last);
					s.add(CS);
					current = s.size() - 1;
				}
			}

			repaint();
		}

		public void mouseReleased(MouseEvent e) {

			last = null;
			dragging = false;

			lastPoint.clear();
			if (inside && !dragged) {

				s.get(current).selected = false;
			}

			if (shape != 0 && shape <= 9) {

				undo.add((LinkedList) cpy.copy(s));
				redo.clear();
			}
			point = -1;
			inside = false;
			dragged = false;

			repaint();
		}

		private void resizeGroup(Point e) {
			int dx = (e.x - last.x);
			int dy = (e.y - last.y);
			s.get(current).drag(e, last, dragging, point);

			for (int i = 0; i < s.size(); i++) {
				Shape sh = s.get(i);

				if (s.get(i).isSelected() && i != current) {

					if (lastPoint.get(i) == null) {// first time to add point

						if (s.get(i).getClass().toString()
								.equals("class Geometry.Triangle")
								|| s.get(i).getClass().toString()
										.equals("class Geometry.Line")) {
							if (s.get(i).getClass().toString()
									.equals("class Geometry.Triangle")
									&& !s.get(current).getClass().toString()
											.equals("class Geometry.Triangle")) {
								lastPoint.set(i, new Point(
										(int) (s.get(i).points[0].getX()),
										(int) (s.get(i).points[0].getY())));

							} else if (s.get(i).getClass().toString()
									.equals("class Geometry.Triangle")
									&& s.get(current).getClass().toString()
											.equals("class Geometry.Triangle")) {
								lastPoint.set(
										i,
										new Point((int) (s.get(i).points[point]
												.getX() + 8),
												(int) (s.get(i).points[point]
														.getY() + 8)));
							}

							if (s.get(i).getClass().toString()
									.equals("class Geometry.Line")) {
								Line l = (Line) s.get(i);
								lastPoint.set(i, new Point(l.x1, l.y1));
							}
						}

						else {

							lastPoint.set(i, new Point(
									(int) (s.get(i).points[point].getX() + 8),
									(int) (s.get(i).points[point].getY() + 8)));
						}

					} else {

						Point n = lastPoint.get(i);
						if (!s.get(i).getClass().toString()
								.equals("class Geometry.Triangle")
								&& !s.get(i).getClass().toString()
										.equals("class Geometry.Line")) {

							if (s.get(current).getClass().toString()
									.equals("class Geometry.Triangle")
									&& ((point == 1 || point == 2))) {
								if (point != 1 && point != 2) {
									n.y -= 2 * dy;
								} else {
									if (point == 2) {
										n.y += 2 * dx;
									} else {
										n.y -= 2 * dx;
									}
								}

							} else {

								n.x += dx;
								n.y += dy;
							}
							sh.drag(lastPoint.get(i), lastPoint.get(i),
									dragging, point);
							s.set(i, sh);
						} else if (s.get(i).getClass().toString()
								.equals("class Geometry.Triangle")) {
							if (s.get(current).getClass().toString()
									.equals("class Geometry.Triangle")) {
								n.x += dx;
								n.y += dy;
								sh.drag(lastPoint.get(i), lastPoint.get(i),
										dragging, point);
								lastPoint.set(i, n);
							} else {
								int dz = 0;
								if (Math.abs(dx) > Math.abs(dy)) {
									dz = dx;
								} else {
									dz = dy;
								}
								if (point != 6 && point != 2) {
									if (point != 7 && point != 3) {
										if (point == 5 || point == 6
												|| point == 4) {
											n.y -= dz;

										} else {
											n.y += dz;

										}
									} else {
										if (point == 7) {
											n.y += dx;
										} else {
											n.y -= dx;
										}
									}

									sh.drag(lastPoint.get(i), lastPoint.get(i),
											dragging, 0);
									lastPoint.set(i, n);

								} else {
									if (isMaximizing(dx, dy)) {
										sh.drag(new Point(
												(int) s.get(i).points[0].getX(),
												(int) s.get(i).points[0].getY() - 1),
												lastPoint.get(i), dragging, 0);
									} else {
										sh.drag(new Point(
												(int) s.get(i).points[0].getX(),
												(int) s.get(i).points[0].getY() + 8),
												lastPoint.get(i), dragging, 0);
									}

								}

							}
							s.set(i, sh);

						} else {

							Line l = (Line) s.get(i);
							double lengthBefore = new Point(l.x1, l.y1)
									.distance(new Point(l.x2, l.y2));
							if (l.x1 == l.x2) {// vertical
								n.y += dy;
								lastPoint.set(i, n);
								sh.drag(lastPoint.get(i), lastPoint.get(i),
										dragging, 0);
								Line l2 = (Line) sh;
								if (isMaximizing(dx, dy)) {
									if (new Point(l2.x1, l2.y1).distance(l2.x2,
											l2.y2) - (lengthBefore) < 0.000000001) {
										n.y -= 2 * dy;
										lastPoint.set(i, n);
										sh.drag(lastPoint.get(i),
												lastPoint.get(i), dragging, 0);

									}
								} else {
									if (new Point(l2.x1, l2.y1).distance(l2.x2,
											l2.y2) - (lengthBefore) > 0.000000001) {
										n.y -= 2 * dy;
										lastPoint.set(i, n);
										sh.drag(lastPoint.get(i),
												lastPoint.get(i), dragging, 0);

									}
								}
								s.set(i, sh);
							} else if (l.y1 == l.y2) {// horizontal
								n.x += dx;
								lastPoint.set(i, n);
								sh.drag(lastPoint.get(i), lastPoint.get(i),
										dragging, 0);
								Line l2 = (Line) sh;
								if (isMaximizing(dx, dy)) {
									if (new Point(l2.x1, l2.y1).distance(l2.x2,
											l2.y2) - (lengthBefore) < 0.000000001) {
										n.x -= 2 * dx;
										lastPoint.set(i, n);
										sh.drag(lastPoint.get(i),
												lastPoint.get(i), dragging, 0);

									}
								} else {
									if (new Point(l2.x1, l2.y1).distance(l2.x2,
											l2.y2) - (lengthBefore) > 0.000000001) {
										n.x -= 2 * dx;
										lastPoint.set(i, n);
										sh.drag(lastPoint.get(i),
												lastPoint.get(i), dragging, 0);

									}
								}
								s.set(i, sh);
							} else {

								if ((double) (l.y2 - l.y1) / (l.x2 - l.x1) < 0.0) {
									n.x += dx;
									n.y += dy;
									lastPoint.set(i, n);
									sh.drag(lastPoint.get(i), lastPoint.get(i),
											dragging, 0);
									Line l2 = (Line) sh;
									if (isMaximizing(dx, dy)) {
										if (new Point(l2.x1, l2.y1).distance(
												l2.x2, l2.y2) - (lengthBefore) < 0.000000001) {
											n.x -= 2 * dx;
											n.y -= 2 * dy;
											lastPoint.set(i, n);
											sh.drag(lastPoint.get(i),
													lastPoint.get(i), dragging,
													0);

										}
									} else {
										if (new Point(l2.x1, l2.y1).distance(
												l2.x2, l2.y2) - (lengthBefore) > 0.000000001) {
											n.x -= 2 * dx;
											n.y -= 2 * dy;
											lastPoint.set(i, n);
											sh.drag(lastPoint.get(i),
													lastPoint.get(i), dragging,
													0);

										}
									}
									s.set(i, sh);
								} else {
									n.x += dx;
									n.y += dy;
									lastPoint.set(i, n);
									sh.drag(lastPoint.get(i), lastPoint.get(i),
											dragging, 0);
									Line l2 = (Line) sh;
									if (isMaximizing(dx, dy)) {
										if (new Point(l2.x1, l2.y1).distance(
												l2.x2, l2.y2) - (lengthBefore) < 0.000000001) {
											n.x += 2 * dx;
											n.y += 2 * dy;
											lastPoint.set(i, n);
											sh.drag(lastPoint.get(i),
													lastPoint.get(i), dragging,
													0);

										}
									} else {
										if (new Point(l2.x1, l2.y1).distance(
												l2.x2, l2.y2) - (lengthBefore) > 0.000000001) {
											n.x += 2 * dx;
											n.y += 2 * dy;
											lastPoint.set(i, n);
											sh.drag(lastPoint.get(i),
													lastPoint.get(i), dragging,
													0);

										}
									}
									s.set(i, sh);
								}
							}
						}
					}

				}

			}

		}

		private boolean isMaximizing(int dx, int dy) {
			if ((point == 0 && (dx < 0 || dy < 0))
					|| (point == 1 && dy < 0)
					|| (point == 2 && ((dx > 0 && dy == 0)
							|| (dx > 0 && dy < 0) || (dy < 0 && dx == 0)))
					|| (point == 3 && dx >= 0)
					|| (point == 4 && (dy >= 0 || dx >= 0))
					|| (point == 5 && dy >= 0)
					|| (point == 6 && ((dx < 0 && dy == 0)
							|| (dx < 0 && dy > 0) || (dy > 0 && dx == 0)))
					|| (point == 7 && dx < 0)) {
				return true;
			}
			return false;
		}

		public void mouseDragged(MouseEvent e) {
/*
 * if buttons redo/undo ,color,delete are pressed nothing to be done here
 */
			if (shape == 14 && point == -1 && !dragging)
				return;
			if (shape == 10 && point == -1 && !dragging)
				return;
			if (shape == 11 && point == -1 && !dragging)
				return;
			if (shape == 0 && point == -1 && !dragging)
				return;
			if (!((!dragging && shape != 0 && shape <= 9) && (inside))) {
				dragged = true;
				if (!dragging && point == -1) {
					if ((shape == 6 && LoaderFileChooser.filen != "cancel")
							|| shape != 6) {
						CS.drag(e.getPoint(), last, dragging, point);
						s.set(current, CS);
					}
				} else {
					if (point != -1) {
						resizeGroup(e.getPoint());
					} else {
						for (int i = 0; i < s.size(); i++) {
							if (s.get(i).isSelected()) {
								Shape sh = s.get(i);
								sh.drag(e.getPoint(), last, dragging, point);
								s.set(i, sh);

							}
						}
					}
				}
			}
			repaint();

			last = e.getPoint();

		}

	}

}