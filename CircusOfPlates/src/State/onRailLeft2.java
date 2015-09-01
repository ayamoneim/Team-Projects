package State;
import java.util.Random;

import MainObjects.*;

public class onRailLeft2 implements ShapeState {

	final int railEnd = 100 ;
	Random randomGenerator = new Random();
	int lastx = randomGenerator.nextInt(400)+100;
	
	Shape shape ;
	public onRailLeft2(Shape s){
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
