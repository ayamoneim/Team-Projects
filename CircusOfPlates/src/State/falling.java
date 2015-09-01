package State;
import MainObjects.*;


public class falling implements ShapeState{

	int endScreen = 680;
	
	Shape shape ;
	public falling(Shape s){
		shape=s;
	}
	
	public void perform() {
		shape.setUpLeftCorner_y(shape.getUpLeftCorner_y()+5);
		if(shape.getUpLeftCorner_y() + shape.getShapeImage().getHeight()>=endScreen){
			shape.setUpLeftCorner_y(endScreen - shape.getShapeImage().getHeight());
		}
	}

}
