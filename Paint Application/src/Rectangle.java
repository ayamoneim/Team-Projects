

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

public class Rectangle extends Shape {

	private int firstx, firsty;

	public void draw(Graphics g) {
		cornerX = x;
		cornerY = y;
		g.drawRect(x, y, width, height);
		g.setColor(c);
		if (c != Color.WHITE)
			g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
	}

	public boolean InsideShape(Point last) {
		return new Rectangle2D.Float(x, y, width, height).contains(last);
	}

	public void pressing(Point last) {
		x = last.x;
		y = last.y;
		firstx = x;
		firsty = y;
		width = height = 0;
		c = Color.WHITE;
	}

	@Override
	public void drag(Point e, Point last, Boolean dragging, Integer point) {
		int dx = e.x - last.x;
		int dy = e.y - last.y;
		if (dragging) {
			x += dx;
			y += dy;
		} else {
			if (point == -1) {
				width = Math.abs(e.x - firstx);
				height = Math.abs(e.y - firsty);
				x = Math.min(e.x, firstx);
				y = Math.min(e.y, firsty);
			} else {
				resize(point, e);
			}
		}
	}

	@Override
	public Integer getWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	@Override
	public Integer getHeight() {
		// TODO Auto-generated method stub
		return height;
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return selected;
	}

	@Override
	public void resize(Integer i, Point p) {
		if (i == 0 || i == 2 || i == 4 || i == 6) {

			int w = 0, h = 0;
			if (i == 0) {
				w = (int) (width - (p.getX() - x));
				h = (int) (height - (p.getY() - y));
				x = (int) p.getX();
				y = (int) p.getY();
			} else if (i == 2) {
				w = (int) (width + (p.getX() - (x + width)));
				h = (int) (height - (p.getY() - y));
				x = (int) (p.getX() - w);
				y = (int) p.getY();

			} else if (i == 4) {
				w = (int) (width + (p.getX() - (x + width)));
				h = (int) (height + (p.getY() - (y + height)));
				x = (int) (p.getX() - w);
				y = (int) (p.getY() - h);
			} else if (i == 6) {
				w = (int) (width - (p.getX() - (x)));
				h = (int) (height + (p.getY() - (y + height)));
				x = (int) (p.getX());
				y = (int) (p.getY() - h);
			}
			height = h;
			width = w;

		} else if (i == 1 || i == 5) {
			if (i == 1) {
				height -= (int) (p.getY() - y);
			} else {
				height += (int) (p.getY() - (y + height));
			}
			if (i == 1) {
				y = (int) p.getY();
			}

		} else {
			if (i == 7) {
				width -= (int) (p.getX() - x);
			} else {
				width += (int) (p.getX() - (x + width));
			}
			if (i == 7) {
				x = (int) p.getX();
			}

		}

	}


}



/*import java.awt.BasicStroke;
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

	// When the program First Starts

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
		Graphics2D g2= (Graphics2D)g;
		g2.setStroke(new BasicStroke(1));
		for (int i = 0; i < s.size(); i++) {
			if((s.get(i).getClass().toString().equals("class Line") || s.get(i).getClass().toString().equals("class Triangle"))&& s.get(i).isSelected()){
				g.setColor(Color.LIGHT_GRAY);
			}
			s.get(i).draw(g);
			g.setColor(Color.BLACK);
			if(s.get(i).isSelected()){
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
	private boolean dragging = false;
	private Shape CS;

	public LinkedList<Shape> s = new LinkedList<Shape>();
	
	
	public int current = -1;
	public int shape = 0;
	public boolean inside, drawpad, dragged;

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
			if (!dragging ) {
				for (int i = 0; i < s.size(); i++) {
					if (s.get(i).isSelected()) {
						for (int j = 0; j < 8; j++) {
							if (s.get(i).points[j] != null
									&& s.get(i).points[j]
											.contains(e.getPoint())) {
								point = j;

								return;
							}
						}
					}
				}
			}
			if (!dragging && shape != 0 && shape<=9 && point==-1) {
				if (shape == 1)
					CS = new Circle();

				else if (shape == 2)
					CS = new RoundRectangle();

				else if (shape == 3)
					CS = new Triangle();

				else if (shape == 4)
					CS = new Ellipse();

				else if (shape == 5)
					CS = new Square();
								
				else if(shape == 9)
					CS = new Line();
				
				else if(shape==6){
					//new shape
				}
				CS.pressing(last);
				s.add(CS);
				current = s.size() - 1;
			}
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			
			last = null;
			dragging = false;

			if (inside && !dragged) {

				s.get(current).selected = false;
			}

			point = -1;
			inside = false;
			dragged = false;

			
			repaint();
		}

		public void mouseDragged(MouseEvent e) {
			
			if (!(shape == 0 && !dragging)) {
				dragged = true;
				if (!dragging && point==-1) {
					CS.drag(e.getPoint(), last, dragging, point);
					s.set(current, CS);
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
			repaint();

			last = e.getPoint();

		}

	}

}*/