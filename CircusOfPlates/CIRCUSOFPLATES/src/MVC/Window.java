package MVC;

import java.awt.Graphics2D;

public abstract class Window {
	public abstract void initializeWindow();

	public abstract void drawWindow(Graphics2D g);

	public abstract void updateWindow();

	public abstract void keyPressed(int keyCode);

	public abstract void keyReleased(int keyCode);
}
