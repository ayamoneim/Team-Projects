


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

public abstract class Shape implements java.io.Serializable , ShapeInterface{

	
	public Color c;

	protected int cornerX;

	protected int cornerY;

	protected Rectangle2D[] points = new Rectangle2D[8];

	public int x, y, width, height;

	protected boolean selected = false;

	public abstract void draw(Graphics g);

	public abstract boolean InsideShape(Point last);

	public abstract void pressing(Point last);

	public abstract void drag(Point e, Point last, Boolean dragging, Integer point);

	public abstract boolean isSelected();

	public abstract Integer getWidth();

	public abstract Integer getHeight();

	public void setFrame(Graphics g) {
		g.setColor(new Color(123,115,232));
		for (int j = 0; j < 2; j++) {
			g.drawRect(cornerX + j, cornerY + j, getWidth() - (j * 2),
					getHeight() - (j * 2));

		}
		g.setColor(Color.black);

	}

	public void setResizeBorders(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		
		g2.setColor(new Color(97,27,22));

		if (this.getClass().toString().equals("class Geometry.Line")) {
			Line l = (Line) this;
			g2.fill(points[0] = new Rectangle2D.Double(l.x1 - 4, l.y1 - 5, 8, 8));
			g2.fill(points[1] = new Rectangle2D.Double(l.x2 - 4, l.y2 - 5, 8, 8));
		} else if(this.getClass().toString().equals("class Geometry.Triangle")){
			Triangle t = (Triangle)this;
			g2.fill(points[0] = new Rectangle2D.Double(t.xs[0]-4,t.ys[0]-4
					, 8, 8));
			
			g2.fill(points[1] = new Rectangle2D.Double(t.xs[1]-4,t.ys[1]-4
					, 8, 8));
			
			g2.fill(points[2] = new Rectangle2D.Double(t.xs[2]-4,t.ys[2]-4
					, 8, 8));
			
			
		}else {
		
			g2.fill(points[0] = new Rectangle2D.Double(cornerX - 6,
					cornerY - 6, 8, 8));
			g2.fill(points[1] = new Rectangle2D.Double(cornerX
					+ (getWidth() / 2), cornerY - 6, 8, 8));
			g2.fill(points[2] = new Rectangle2D.Double(
					cornerX + getWidth() , cornerY-6, 8, 8));
			g2.fill(points[3] = new Rectangle2D.Double(cornerX + (getWidth())
					, cornerY + getHeight() / 2, 8, 8));
			g2.fill(points[4] = new Rectangle2D.Double(cornerX + (getWidth())
					,  cornerY + getHeight(), 8, 8));
			g2.fill(points[5] = new Rectangle2D.Double(cornerX + (getWidth())
					/ 2,  cornerY + getHeight(), 8, 8));
			g2.fill(points[6] = new Rectangle2D.Double(cornerX-6, cornerY
					+ getHeight() , 8, 8));
			g2.fill(points[7] = new Rectangle2D.Double(cornerX - 6, cornerY
					+ getHeight() / 2, 8, 8));
		}
		g2.setColor(Color.black);
	}

	public abstract void resize(Integer from, Point to);

}
