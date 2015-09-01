
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;


public class Square extends Shape{

	private int firstx,firsty;

	public void draw(Graphics g){
		cornerX = x;
		cornerY = y;
		g.drawRect(x, y, width, height);
		g.setColor(c);
		if(c!=Color.WHITE)
			g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
	}

	public boolean InsideShape(Point last){
		return new Rectangle2D.Float(x,y,width,height).contains(last);
	}
	
	public void pressing(Point last){
		x=last.x;
		y=last.y;
		firstx=last.x;
		firsty=last.y;
		width=height=0;
		c=Color.WHITE;
	}
	
	public void drag(Point e, Point last, Boolean dragging,Integer point){
		int dx = e.x - last.x;
        int dy = e.y - last.y;
        if(dragging){
        	x+=dx;
        	y+=dy;
        }
        else{
        	if(point==-1){
        	int diagonal = (e.x-firstx)*(e.x-firstx) + (e.y-firsty)*(e.y-firsty);
        	diagonal/=2;
        	width=height= (int) Math.sqrt(diagonal);
        	x=Math.min(firstx , e.x);
        	y=Math.min(firsty,  e.y);
        	}else {
        		resize(point,e);
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
