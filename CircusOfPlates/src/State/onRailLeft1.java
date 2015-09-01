package State;
import MainObjects.*;

import java.util.Random;

import MainObjects.*;


public class onRailLeft1 implements ShapeState {

	final int railEnd = 250 ;
	Random randomGenerator = new Random();
	int lastx = randomGenerator.nextInt(400)+250;
	
	Shape shape ;
	public onRailLeft1(Shape s){
		shape=s;
	}
	
	public void perform() {
		if(shape.getUpLeftCorner_x()>=railEnd){
			shape.setUpLeftCorner_x(shape.getUpLeftCorner_x()+5);
			shape.setUpLeftCorner_y(shape.getUpLeftCorner_y()+5);
			if(shape.getUpLeftCorner_x()>=lastx){
				shape.setState(shape.getFalling());
			}

		}
		else
			shape.setUpLeftCorner_x(shape.getUpLeftCorner_x()+5);;
		
	}

}
