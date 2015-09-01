
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Ellipse2D;


public class Circle extends Shape {
	
	
	
	@Override
	public boolean InsideShape(Point p){
		return new Ellipse2D.Float(this.x-width,this.y-width,2*this.width,2*this.width).contains(p);
	}
	@Override
	public void draw(Graphics g){
		
		
		cornerX = this.x-this.width;
		cornerY = this.y-this.height;
		g.drawOval(this.x-this.width, this.y-this.height, 2*this.width,2*this.height);
		g.setColor(c);
		if(c!=Color.WHITE)
			g.fillOval(this.x-width, this.y-height, 2*this.width,2*this.height);
		g.setColor(Color.BLACK);
	}
	@Override
	public void pressing(Point last){
		 x = last.x;
         y = last.y;
         width=height=0;	
         c=Color.WHITE;
	}
	@Override
	public void drag(Point e, Point last, Boolean dragging,Integer point){
		int dx = e.x - last.x;
        int dy = e.y - last.y;
        if(dragging){
        	x+=dx;
        	y+=dy;
        }
        else{
        	if(point==-1){
        		width=height=(int) e.distance(new Point(x,y));
        	}else{
        		resize(point,e);
        	}
        }

	}
	
	@Override
	public Integer getWidth() {
		return 2*width;
	}

	@Override
	public Integer getHeight() {
		return 2*height;
	}
	@Override
	public void resize(Integer i,Point p){
		int xC = x-width, yC = y-height;
		if(i==0 || i==2 || i==4 || i==6){

			
			int w = getWidth(), h = getHeight();
			if (i == 0) {
				w = (int) (2*width - (p.getX() - xC));
				h = (int) (2*height - (p.getY() - yC));

			} else if (i == 2) {
				w = (int) (2*width + (p.getX() - (xC + (2*width))));
				h = (int) (2*height - (p.getY() - yC));


			} else if (i == 4) {
				w = (int) (width + (p.getX() - (xC + width)));
				h = (int) (height + (p.getY() - (yC + height)));

			} else if (i == 6) {
				w = (int) ((2*width) - (p.getX() - (xC)));
				h = (int) (2*height + (p.getY() - (yC + (2*height))));

			}
			height = h/2;
			width = w/2;
			
			
		}else if(i==1 || i==5){	
			int h = height*2;
			
			if (i == 1) {
				h -= (int) (p.getY() - yC);
			} else {
				h += (int) (p.getY() - (yC + h));
			}
			if (i == 1) {
				yC = (int) p.getY();
			}
			width = height = h/2;
			
		}else{
			int w = width*2;
			if (i == 7) {
				w -= (int) (p.getX() - xC);
			} else {
				w += (int) (p.getX() - (xC + w));
			}
			if (i == 7) {
				xC = (int) p.getX();
			}
			
			height = width = w/2;
		}
		
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return selected;
	}

	
}
