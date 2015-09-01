
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

public class Triangle extends Shape {
	int[] xs, ys;
	int side;

	public void draw(Graphics g) {
		boolean flag = true;
		int cnt = 0;
		for (int i = 0; i < 3; i++) {
			if (xs[i] == 0 || ys[i] == 0)
				cnt++;
		}
		if (cnt >= 2)
			flag = false;
		if (flag) {
			g.drawPolygon(new Polygon(xs, ys, 3));
			g.setColor(c);
			if (c != Color.WHITE)
				g.fillPolygon(xs, ys, 3);
			g.setColor(Color.BLACK);
		}
	}

	public boolean InsideShape(Point last) {
		Polygon p = new Polygon();
		p.addPoint(xs[0], ys[0]);
		p.addPoint(xs[1], ys[1]);
		p.addPoint(xs[2], ys[2]);
		return p.contains(last.x, last.y);
	}

	public void pressing(Point last) {
		side = 0;
		xs = new int[3];
		ys = new int[3];
		xs[0] = last.x;
		ys[0] = last.y;
		c = Color.WHITE;
	}

	public void drag(Point e, Point last, Boolean dragging, Integer point) {
		int dx = e.x - last.x;
		int dy = e.y - last.y;
		if (dragging && point==-1) {
			xs[0] += dx;
			ys[0] += dy;

			xs[1] = xs[0] + side / 2;
			ys[1] = (int) (ys[0] + side / 2 * Math.sqrt(3));
			xs[2] = xs[1] - side;
			ys[2] = ys[1];
		} else {
			if (point == -1) {
				side = (int) e.distance(new Point(xs[0], ys[0]));

				xs[1] = xs[0] + side / 2;
				ys[1] = (int) (ys[0] + side / 2 * Math.sqrt(3));
				xs[2] = xs[1] - side;
				ys[2] = ys[1];
			} else {
				resize(point, e);
			}
		}
		

	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return selected;
	}

	@Override
	public Integer getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void resize(Integer i, Point to) {

		
		if (i == 0) {
			side = (int) ((ys[1] - to.y) / (Math.sqrt(3) / 2));
			ys[0] = to.y;
			xs[1] = xs[0] + side / 2;
			xs[2] = xs[1] - side;

		} else if (i == 1) {
			side+=to.x-xs[1];
			xs[1] = xs[0] + side / 2;
			ys[1] = (int) (ys[0] + side / 2 * Math.sqrt(3));
			xs[2] = xs[1] - side;
			ys[2] = ys[1];
			
		} else {
			side+=xs[2]-to.x;
			xs[1] = xs[0] + side / 2;
			ys[1] = (int) (ys[0] + side / 2 * Math.sqrt(3));
			xs[2] = xs[1] - side;
			ys[2] = ys[1];
		}

	}

}
