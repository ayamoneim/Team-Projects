
import java.awt.Graphics;
import java.awt.Point;

public interface ShapeInterface {

	/*
	 * Sets the frame for any selected shape
	 */
	public void setFrame(Graphics g);
	
	
	/*
	 * set the borders to resize the frame of a selected shape
	 */
	public void setResizeBorders(Graphics g);
	
	/*
	 * Resize any selected shape
	 */
	public abstract void resize(Integer from, Point to);
	
	/*
	 * Draws the shape
	 */
	public abstract void draw(Graphics g);
	
	/*
	 * Returns true if the user presses the mouse inside the shape else return false
	 */
	public abstract boolean InsideShape(Point last);
	
	/*
	 * Handles the action of the mouse within the shape
	 */
	public abstract void pressing(Point last);
	
	/*
	 * Handles shape movements
	 */
	public abstract void drag(Point e, Point last, Boolean dragging, Integer point);
	
	/*
	 * Returns boolean to indicate if current shape is selected
	 */
	public abstract boolean isSelected();
	
	/*
	 * Returns the width of the shape
	 */
	public abstract Integer getWidth();
	
	/*
	 * Returns the height of the shape
	 */
	public abstract Integer getHeight();
}
