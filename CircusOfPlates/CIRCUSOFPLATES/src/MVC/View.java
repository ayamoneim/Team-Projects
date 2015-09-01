package MVC;

import java.awt.Graphics2D;

public class View {

	protected final int START_WINDOW = 0;
	protected final int CHOOSE_PLAYER_WINDOW = 1;
	protected final int GAME_WINDOW = 2;
	protected final int SAVE_WINDOW = 3;
	protected final int END_WINDOW = 4;

	private int currentWindow;
	private Window[] windows;

	private Controller controller;

	public View(Controller controller) {
		this.windows = new Window[5];
		this.controller = controller;
		currentWindow = this.START_WINDOW;
		createWindow(currentWindow);
	}

	public int getCurrentWindow() {
		return currentWindow;
	}

	public void createWindow(int windowNumber) {
		if (windowNumber == this.START_WINDOW) {
			windows[windowNumber] = new StartWindow(this, controller);
		} else if (windowNumber == this.END_WINDOW) {
			windows[windowNumber] = new EndWindow(this, controller);
		} else if (windowNumber == this.GAME_WINDOW) {
			windows[windowNumber] = new GameWindow(this, controller);
		} else if (windowNumber == this.SAVE_WINDOW) {
			windows[windowNumber] = new SaveWindow(this, controller);
		} else if (windowNumber == this.CHOOSE_PLAYER_WINDOW) {
			windows[windowNumber] = new ChoosePlayerWindow(this, controller);
		}
	}

	public void disposeWindow(int windowNumber) {

		windows[windowNumber] = null;
	}

	public void setCurrentWindow(int windowNumber) {
		disposeWindow(currentWindow);
		currentWindow = windowNumber;
		createWindow(currentWindow);
	}

	public void draw(Graphics2D g) {
		if (windows[currentWindow] != null) {
			windows[currentWindow].drawWindow(g);
		}

	}

	public void update() {

	}

	public void keyPressed(int keyCode) {
		if (windows[currentWindow] != null) {
			windows[currentWindow].keyPressed(keyCode);
			;
		}
	}

	public void keyReleased(int keyCode) {
		if (windows[currentWindow] != null) {
			windows[currentWindow].keyReleased(keyCode);
			;
		}
	}

}
