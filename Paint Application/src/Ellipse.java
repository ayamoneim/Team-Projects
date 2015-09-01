
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;


public class Ellipse extends Shape {
	
//	private Stroke s ;
	public boolean InsideShape(Point p){
		return new Ellipse2D.Float(this.x-width,this.y-height,2*this.width,2*this.height).contains(p);
	}
	
	public void draw(Graphics g){
		cornerX = this.x-width;
		cornerY = this.y-height;
//		s = new BasicStroke(2);
		g.drawOval(this.x-width, this.y-height, 2*this.width, 2*this.height);
		g.setColor(c);
		if(c!=Color.WHITE)
			g.fillOval(this.x-width, this.y-height, 2*this.width, 2*this.height);
		g.setColor(Color.BLACK);
	}
	
	public void pressing(Point last){
		 x = last.x;
         y = last.y;
         width=height=0;
         c=Color.WHITE;

	}
	
	public void drag(Point e, Point last, Boolean dragging,Integer  point){
		int dx = e.x - last.x;
        int dy = e.y - last.y;
        if(dragging){
        	x+=dx;
        	y+=dy;
        }
        else{
        	if(point==-1){
        		width=Math.abs(e.x-x);
        		height=Math.abs(e.y-y);
        	}else{
        		resize(point,e);
        	}
        }

	}
	
	@Override
	public Integer getWidth() {
		// TODO Auto-generated method stub
		return 2*width;
	}

	@Override
	public Integer getHeight() {
		// TODO Auto-generated method stub
		return 2*height;
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return selected;
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
			height = h/2;
			
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
			
			width = w/2;
		}
			
		
		
	}


}
