
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;


public class Line extends Shape{

	public int  x1 ,y1,x2,y2;
	
	public void draw(Graphics g) {
		cornerX = x1;
		cornerY = x2;
		if (c != Color.WHITE)
			g.setColor(c);
		
		g.drawLine(x1, y1, x2, y2);
		g.setColor(Color.BLACK);
		
		
	}

	@Override
	public boolean InsideShape(Point last) {
		boolean vertical = false;
		double m=0;
		boolean horizontal = false;
		if(x2==x1 && y2!=y1)
			vertical =true;
		else if(y2==y1 && x2!=x1){
			horizontal = true;
		}else
			m= (double)(y2-y1)/(x2-x1);
		if(horizontal && last.y==y2 && last.x>Math.min(x1,x2) && last.x<Math.max(x1, x2)){
			return true;
		}else
		if(vertical && last.x==x2 && last.y>Math.min(y1,y2) && last.y<Math.max(y1, y2)){
			return true;
		}
		else if(!vertical){
			double ans=m*last.x - m*x1 - last.y + y1;
			
			if(Math.abs(ans)<=10 && last.x>Math.min(x1,x2) && last.x<Math.max(x1,x2) && last.y>Math.min(y1,y2) && last.y<Math.max(y1, y2))
				return true;
		}
		return false;
	}

	@Override
	public void pressing(Point last) {
		x1 = last.x;
		y1 = last.y;
		if(x2==0 && y2 == 0){
			x2 = x1;
			y2 = y1;
		}
		
	}

	@Override
	public void drag(Point e, Point last, Boolean dragging, Integer point) {
		int dx = e.x - last.x;
		int dy = e.y - last.y;
		if (dragging) {
			x1 += dx;
			y1 += dy;
			x2+=dx;
			y2+=dy;
		} else {
			if (point == -1) {
				x2 = e.x;
				y2 = e.y;
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
		return Math.abs(x1-x2);
	}

	@Override
	public Integer getHeight() {
		// TODO Auto-generated method stub
		return Math.abs(y1-y2);
	}

	@Override
	public void resize(Integer i, Point to) {
		if(i==0){
			x1 = to.x;
			y1 = to.y;
		}else{
			x2 = to.x;
			y2 = to.y;
		}
		
	}
	
	@Override
	public void setFrame(Graphics g){
		g.setColor(Color.LIGHT_GRAY);
	}



}
