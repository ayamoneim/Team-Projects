package MVC;



import java.io.IOException;

import Iterator.CurrentlyDisplayed;
import MainObjects.*;
import ObjectPool.ShapePool;

public class Model {

	private Player playerA;
	private Player playerB;
	private CurrentlyDisplayed currentlyDisplayed;

	public ShapePool pool ;
	
	public Model(){
		this.pool = ShapePool.getInstance();
		this.currentlyDisplayed = new CurrentlyDisplayed();
//		setCurrentlyDisplayed();
	}
	
	public void setCurrentlyDisplayed() {
		try {
			Shape s = getShapePool().checkOut();
			s.setUpLeftCorner_x(0);
			s.setUpLeftCorner_y(50-s.getShapeImage().getHeight());
			s.setState(s.getOnRailLeft1());

			currentlyDisplayed.addShape(s);
			Shape s2;
			s2 = getShapePool().checkOut();
			s2.setUpLeftCorner_x(2*s2.getShapeImage().getWidth()-20);
			s2.setUpLeftCorner_y(50-s2.getShapeImage().getHeight());
			s2.setState(s2.getOnRailLeft1());

			currentlyDisplayed.addShape(s2);

//			Shape s3;
//			s3 = getShapePool().checkOut();
//			s3.setUpLeftCorner_x(3*s.getShapeImage().getWidth()+3);
//			s3.setUpLeftCorner_y(50);
//			s3.setState(s3.getOnRailLeft1());
//			currentlyDisplayed.addShape(s3);

//			Shape s4;
//			s4 = getShapePool().checkOut();
//			s4.setUpLeftCorner_x(150);
//			s4.setUpLeftCorner_y(50);
//			s4.setState(s4.getOnRailLeft1());

//			currentlyDisplayed.addShape(s4);
			Shape s5 = getShapePool().checkOut();
			s5.setUpLeftCorner_x(740);
			s5.setUpLeftCorner_y(50-s5.getShapeImage().getHeight());
			s5.setState(s5.getOnRailRight1());

			currentlyDisplayed.addShape(s5);
			Shape s6;
			s6 = getShapePool().checkOut();
			s6.setUpLeftCorner_x(660);
			s6.setUpLeftCorner_y(50-s6.getShapeImage().getHeight());
			s6.setState(s6.getOnRailRight1());

			currentlyDisplayed.addShape(s6);
//
//			Shape s7;
//			s7 = getShapePool().checkOut();
//			s7.setUpLeftCorner_x(860);
//			s7.setUpLeftCorner_y(50);
//			s7.setState(s7.getOnRailRight1());
//			currentlyDisplayed.addShape(s7);
//
//			Shape s8;
//			s8 = getShapePool().checkOut();
//			s8.setUpLeftCorner_x(810);
//			s8.setUpLeftCorner_y(50);
//			s8.setState(s8.getOnRailRight1());
//
//			currentlyDisplayed.addShape(s8);
//			Shape s9;
//			s9 = getShapePool().checkOut();
//			s9.setUpLeftCorner_x(860);
//			s9.setUpLeftCorner_y(100);
//			s9.setState(s9.getOnRailRight2());
//
//			currentlyDisplayed.addShape(s9);

			Shape s10;
			s10 = getShapePool().checkOut();
			s10.setUpLeftCorner_x(860);
			s10.setUpLeftCorner_y(120-s10.getShapeImage().getHeight());
			s10.setState(s10.getOnRailRight2());
			currentlyDisplayed.addShape(s10);
//			Shape s11;
//			s11 = getShapePool().checkOut();
//			s11.setUpLeftCorner_x(0);
//			s11.setUpLeftCorner_y(100);
//			s11.setState(s11.getOnRailLeft2());
//
//			currentlyDisplayed.addShape(s11);

			Shape s12;
			s12 = getShapePool().checkOut();
			s12.setUpLeftCorner_x(0);
			s12.setUpLeftCorner_y(120-s12.getShapeImage().getHeight());
			s12.setState(s12.getOnRailLeft2());
			currentlyDisplayed.addShape(s12);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	

//	shapePool = shapePool.getInstance();
//	public void resetModel(BufferedImage imageA,BufferedImage imageB){
////		pool = ShapePool.getInstance();
//		
//		playerA = new Player(imageA,1,200,500);
//		
//		playerB = new Player(imageB,2,500,500);
////		playerB.setUpLeftCorner_x(600);
//		currentlyDisplayed = new CurrentlyDisplayed();
//		
//		
//	}
	public Player getPlayerA() {
		return playerA;
	}

	public void setPlayerA(Player playerA) {
		this.playerA = new Player(playerA.getPlayerImageNumber(),playerA.getPlayerNum(),playerA.getUpLeftCorner_x(),playerA.getUpLeftCorner_y());
	}

	public Player getPlayerB() {
		return playerB;
	}

	public void setPlayerB(Player playerB) {
		this.playerB = new Player(playerB.getPlayerImageNumber(),playerB.getPlayerNum(),playerB.getUpLeftCorner_x(),playerB.getUpLeftCorner_y());
	}

	public CurrentlyDisplayed getCurrentlyDisplayed() {
		return currentlyDisplayed;
	}

	public void setOnScreen(CurrentlyDisplayed onScreen) {
		this.currentlyDisplayed = onScreen;
	}
	public ShapePool getShapePool() {
		return pool;
	}


}
