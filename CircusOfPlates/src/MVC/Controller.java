package MVC;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import MainObjects.Shape;

public class Controller extends JPanel implements Runnable, KeyListener {
	protected final String PIC[] = { "clown.png", "clown1.png", "clown7.png" };

	public BufferedImage image;
	public Graphics2D g;
	public boolean running = false;
	public Thread thread;
	private long DELTA = 1000 / 20;
	public Model model;
	private View view;

	private boolean paused = false;

	public Controller(Model model) {
		super();
		this.model = model;
		addKeyListener(this);
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}

		setPreferredSize(new Dimension(860, 680));
		setFocusable(true);
		requestFocus();
	}

	private int mask = 0;

	int getType(KeyEvent e) {
		int c = e.getKeyCode();
		if (c == KeyEvent.VK_LEFT)
			return 0;
		if (c == KeyEvent.VK_RIGHT)
			return 1;
		if (c == KeyEvent.VK_A)
			return 2;
		if (c == KeyEvent.VK_D)
			return 3;
		return -1;
	}

	public void pause(boolean p) {
		this.paused = p;
	}

	public boolean isPaused() {
		return paused;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (view.getCurrentWindow() == view.GAME_WINDOW) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
				view.keyPressed(e.getKeyCode());
			else {
				int c = getType(e);
				if (c == -1)
					return;
				mask |= (1 << c);
				for (int i = 0; i < 4; i++) {
					if ((mask & (1 << i)) == 0)
						continue;
					if (i == 0) {
						if (model.getPlayerB().getUpLeftCorner_x() - 5 >= 0) {

							model.getPlayerB().setUpLeftCorner_x(
									model.getPlayerB().getUpLeftCorner_x() - 5);
							model.getPlayerB()
									.setLeftStackPeak_x(
											model.getPlayerB()
													.getLeftStackPeak_x() - 5);

							model.getPlayerB()
									.setRightStackPeak_x(
											model.getPlayerB()
													.getRightStackPeak_x() - 5);

							for (int j = 0; j < model.getPlayerB()
									.getLeftStack().size(); j++)
								model.getPlayerB()
										.getLeftStack()
										.get(j)
										.setUpLeftCorner_x(
												model.getPlayerB()
														.getLeftStack().get(j)
														.getUpLeftCorner_x() - 5);
							for (int j = 0; j < model.getPlayerB()
									.getRightStack().size(); j++)
								model.getPlayerB()
										.getRightStack()
										.get(j)
										.setUpLeftCorner_x(
												model.getPlayerB()
														.getRightStack().get(j)
														.getUpLeftCorner_x() - 5);
						}
					} else if (i == 1) {
						BufferedImage p = null;
						try {
						p = ImageIO.read(new File(PIC[model.getPlayerB().getPlayerImageNumber()]));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
						if (model.getPlayerB().getUpLeftCorner_x()
								+ p
										.getWidth() + 5 <= 860) {

							model.getPlayerB().setUpLeftCorner_x(
									model.getPlayerB().getUpLeftCorner_x() + 5);
							model.getPlayerB()
									.setLeftStackPeak_x(
											model.getPlayerB()
													.getLeftStackPeak_x() + 5);
							model.getPlayerB()
									.setRightStackPeak_x(
											model.getPlayerB()
													.getRightStackPeak_x() + 5);

							for (int j = 0; j < model.getPlayerB()
									.getLeftStack().size(); j++)
								model.getPlayerB()
										.getLeftStack()
										.get(j)
										.setUpLeftCorner_x(
												model.getPlayerB()
														.getLeftStack().get(j)
														.getUpLeftCorner_x() + 5);
							for (int j = 0; j < model.getPlayerB()
									.getRightStack().size(); j++)
								model.getPlayerB()
										.getRightStack()
										.get(j)
										.setUpLeftCorner_x(
												model.getPlayerB()
														.getRightStack().get(j)
														.getUpLeftCorner_x() + 5);

						}

					} else if (i == 2) {//

						if (model.getPlayerA().getUpLeftCorner_x() - 5 >= 0) {
							model.getPlayerA().setUpLeftCorner_x(
									model.getPlayerA().getUpLeftCorner_x() - 5);
							model.getPlayerA()
									.setRightStackPeak_x(
											model.getPlayerA()
													.getRightStackPeak_x() - 5);
							model.getPlayerA()
									.setLeftStackPeak_x(
											model.getPlayerA()
													.getLeftStackPeak_x() - 5);

							for (int j = 0; j < model.getPlayerA()
									.getLeftStack().size(); j++)
								model.getPlayerA()
										.getLeftStack()
										.get(j)
										.setUpLeftCorner_x(
												model.getPlayerA()
														.getLeftStack().get(j)
														.getUpLeftCorner_x() - 5);
							for (int j = 0; j < model.getPlayerA()
									.getRightStack().size(); j++)
								model.getPlayerA()
										.getRightStack()
										.get(j)
										.setUpLeftCorner_x(
												model.getPlayerA()
														.getRightStack().get(j)
														.getUpLeftCorner_x() - 5);

						}

					} else if (i == 3) {// D
						BufferedImage p = null;
						try {
						p = ImageIO.read(new File(PIC[model.getPlayerA().getPlayerImageNumber()]));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						if (model.getPlayerA().getUpLeftCorner_x()
								+ 5
								+ p
										.getWidth() <= 860) {
							model.getPlayerA().setUpLeftCorner_x(
									model.getPlayerA().getUpLeftCorner_x() + 5);

							model.getPlayerA()
									.setLeftStackPeak_x(
											model.getPlayerA()
													.getLeftStackPeak_x() + 5);
							model.getPlayerA()
									.setRightStackPeak_x(
											model.getPlayerA()
													.getRightStackPeak_x() + 5);

							for (int j = 0; j < model.getPlayerA()
									.getLeftStack().size(); j++)
								model.getPlayerA()
										.getLeftStack()
										.get(j)
										.setUpLeftCorner_x(
												model.getPlayerA()
														.getLeftStack().get(j)
														.getUpLeftCorner_x() + 5);
							for (int j = 0; j < model.getPlayerA()
									.getRightStack().size(); j++)
								model.getPlayerA()
										.getRightStack()
										.get(j)
										.setUpLeftCorner_x(
												model.getPlayerA()
														.getRightStack().get(j)
														.getUpLeftCorner_x() + 5);
						}
					}
				}
			}

		} else {
			view.keyPressed(e.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (view.getCurrentWindow() == view.GAME_WINDOW) {
			int c = getType(e);
			if (c == -1)
				return;
//			mask ^= (1 << c);
			 mask = 0;
		} else {
			view.keyReleased(e.getKeyCode());
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		init();
		long start;
		long elapsed;
		long wait;

		long currTick = System.currentTimeMillis();
		long lastTick = System.currentTimeMillis();
		while (running) {

			start = System.nanoTime();

			currTick = System.currentTimeMillis();

			if (!paused) {
				if (view.getCurrentWindow() == view.GAME_WINDOW) {
					 update();

				}
			}
			
			getView();
			getOnScreen();
			elapsed = System.nanoTime() - start;

			wait = DELTA - elapsed / 1000000;
			if (wait < 0)
				wait = 5;

			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (currTick - lastTick >= 60) {
				lastTick = currTick;
				
				if (view.getCurrentWindow() == view.GAME_WINDOW && !paused) {

					tick();
				}
			}
		}

	}

	private void getOnScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, 860, 680, null);
		g2.dispose();

	}

	private void getView() {

		view.draw(g);

	}

	private void update() {
		model.getCurrentlyDisplayed().checkCaught(model.getPlayerA(),
				model.getPlayerB());
		model.getPlayerA().notifyObserver(this);
		model.getPlayerB().notifyObserver(this);
	}

	private void tick() {
		
		model.getCurrentlyDisplayed().PerformAll();
		
		rails();

		model.getCurrentlyDisplayed().checkInPool(model.getShapePool());

	}

	private void init() {
		image = new BufferedImage(860, 680, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		running = true;

		view = new View(this);
	}

	public void Observe(boolean ps1, boolean ps2, int playernum) {
		if (view.GAME_WINDOW == view.getCurrentWindow()) {
			if (ps1) {
				if (playernum == 1) {
					model.getPlayerA().setScore(
							model.getPlayerA().getScore() + 1);

					Shape s = model
							.getPlayerA()
							.getLeftStack()
							.remove(model.getPlayerA().getLeftStack().size() - 1);
					model.getPlayerA().setLeftStackPeak_y(
							model.getPlayerA().getLeftStackPeak_y()
									+ s.getShapeImage().getHeight());

					s.setState(s.getinPool());
					model.getShapePool().checkIn(s);
					model.getCurrentlyDisplayed().removeShape(s);
					Shape s1 = model
							.getPlayerA()
							.getLeftStack()
							.remove(model.getPlayerA().getLeftStack().size() - 1);
					model.getPlayerA().setLeftStackPeak_y(
							model.getPlayerA().getLeftStackPeak_y()
									+ s1.getShapeImage().getHeight());
					s1.setState(s1.getinPool());
					model.getCurrentlyDisplayed().removeShape(s1);

					model.getShapePool().checkIn(s1);
					Shape s2 = model
							.getPlayerA()
							.getLeftStack()
							.remove(model.getPlayerA().getLeftStack().size() - 1);
					model.getPlayerA().setLeftStackPeak_y(
							model.getPlayerA().getLeftStackPeak_y()
									+ s2.getShapeImage().getHeight());

					s2.setState(s2.getinPool());
					model.getCurrentlyDisplayed().removeShape(s2);

					model.getShapePool().checkIn(s2);

				} else {
					model.getPlayerB().setScore(
							model.getPlayerB().getScore() + 1);

					Shape s = model
							.getPlayerB()
							.getLeftStack()
							.remove(model.getPlayerB().getLeftStack().size() - 1);
					model.getPlayerB().setLeftStackPeak_y(
							model.getPlayerB().getLeftStackPeak_y()
									+ s.getShapeImage().getHeight());

					s.setState(s.getinPool());
					model.getShapePool().checkIn(s);
					model.getCurrentlyDisplayed().removeShape(s);

					Shape s1 = model
							.getPlayerB()
							.getLeftStack()
							.remove(model.getPlayerB().getLeftStack().size() - 1);
					model.getPlayerB().setLeftStackPeak_y(
							model.getPlayerB().getLeftStackPeak_y()
									+ s1.getShapeImage().getHeight());

					s1.setState(s1.getinPool());
					model.getShapePool().checkIn(s1);
					model.getCurrentlyDisplayed().removeShape(s1);

					Shape s2 = model
							.getPlayerB()
							.getLeftStack()
							.remove(model.getPlayerB().getLeftStack().size() - 1);
					model.getPlayerB().setLeftStackPeak_y(
							model.getPlayerB().getLeftStackPeak_y()
									+ s2.getShapeImage().getHeight());

					s2.setState(s.getinPool());
					model.getShapePool().checkIn(s2);
					model.getCurrentlyDisplayed().removeShape(s2);


				}
			}

			if (ps2) {
				if (playernum == 1) {
					model.getPlayerA().setScore(
							model.getPlayerA().getScore() + 1);
					Shape s = model
							.getPlayerA()
							.getRightStack()
							.remove(model.getPlayerA().getRightStack().size() - 1);
					model.getPlayerA().setRightStackPeak_y(
							model.getPlayerA().getRightStackPeak_y()
									+ s.getShapeImage().getHeight());

					s.setState(s.getinPool());
					model.getShapePool().checkIn(s);
					model.getCurrentlyDisplayed().removeShape(s);

					Shape s1 = model
							.getPlayerA()
							.getRightStack()
							.remove(model.getPlayerA().getRightStack().size() - 1);
					model.getPlayerA().setRightStackPeak_y(
							model.getPlayerA().getRightStackPeak_y()
									+ s1.getShapeImage().getHeight());

					s1.setState(s1.getinPool());
					model.getShapePool().checkIn(s1);
					model.getCurrentlyDisplayed().removeShape(s1);

					Shape s2 = model
							.getPlayerA()
							.getRightStack()
							.remove(model.getPlayerA().getRightStack().size() - 1);
					model.getPlayerA().setRightStackPeak_y(
							model.getPlayerA().getRightStackPeak_y()
									+ s2.getShapeImage().getHeight());

					s2.setState(s2.getinPool());
					model.getShapePool().checkIn(s2);
					model.getCurrentlyDisplayed().removeShape(s2);


				} else {
					model.getPlayerB().setScore(
							model.getPlayerB().getScore() + 1);

					Shape s = model
							.getPlayerB()
							.getRightStack()
							.remove(model.getPlayerB().getRightStack().size() - 1);
					model.getPlayerB().setRightStackPeak_y(
							model.getPlayerB().getRightStackPeak_y()
									+ s.getShapeImage().getHeight());

					s.setState(s.getinPool());
					model.getShapePool().checkIn(s);
					model.getCurrentlyDisplayed().removeShape(s);

					Shape s1 = model
							.getPlayerB()
							.getRightStack()
							.remove(model.getPlayerB().getRightStack().size() - 1);
					model.getPlayerB().setRightStackPeak_y(
							model.getPlayerB().getRightStackPeak_y()
									+ s1.getShapeImage().getHeight());

					s1.setState(s1.getinPool());
					model.getShapePool().checkIn(s1);
					model.getCurrentlyDisplayed().removeShape(s1);

					Shape s2 = model
							.getPlayerB()
							.getRightStack()
							.remove(model.getPlayerB().getRightStack().size() - 1);
					model.getPlayerB().setRightStackPeak_y(
							model.getPlayerB().getRightStackPeak_y()
									+ s2.getShapeImage().getHeight());

					s2.setState(s.getinPool());
					model.getShapePool().checkIn(s2);
					model.getCurrentlyDisplayed().removeShape(s2);

				}
			}
		}
	}

	public void rails() {

		int[] currentlyOnRails = model.getCurrentlyDisplayed().RailCheck();

		for (int i = 0; i < currentlyOnRails.length; i++) {
			try {

				if (i == 0 && currentlyOnRails[i] == 1) {
					Shape s = model.getShapePool().checkOut();
					s.setUpLeftCorner_x(s.getShapeImage().getWidth());
					s.setUpLeftCorner_y(50 - s.getShapeImage().getHeight());
					s.setState(s.getOnRailLeft1());
					model.getCurrentlyDisplayed().addShape(s);
					currentlyOnRails[i] = 0;
				} else if (i == 1 && currentlyOnRails[i] == 1) {
					Shape s = model.getShapePool().checkOut();
					s.setUpLeftCorner_x(s.getShapeImage().getWidth());
					s.setUpLeftCorner_y(120 - s.getShapeImage().getHeight());
					s.setState(s.getOnRailLeft2());
					model.getCurrentlyDisplayed().addShape(s);
					currentlyOnRails[i] = 0;
				} else if (i == 2 && currentlyOnRails[i] == 1) {
					Shape s = model.getShapePool().checkOut();
					s.setUpLeftCorner_x(860 + s.getShapeImage().getWidth());
					s.setUpLeftCorner_y(50 - s.getShapeImage().getHeight());
					s.setState(s.getOnRailRight1());
					model.getCurrentlyDisplayed().addShape(s);
					currentlyOnRails[i] = 0;
				} else if (i == 3 && currentlyOnRails[i] == 1) {
					Shape s = model.getShapePool().checkOut();
					s.setUpLeftCorner_x(860 + s.getShapeImage().getWidth());
					s.setUpLeftCorner_y(120 - s.getShapeImage().getHeight());
					s.setState(s.getOnRailRight2());
					model.getCurrentlyDisplayed().addShape(s);
					currentlyOnRails[i] = 0;
				}
				currentlyOnRails[i] = 0;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
