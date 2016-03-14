/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;

/**
 * A basic game object displayed as a black square, starting in the upper left
 * corner of the game court.
 * 
 */
public class TimeKeeper extends GameObj {

	public static int position = 55;
	/**
	 * Note that, because we don't need to do anything special when constructing
	 * a Square, we simply use the superclass constructor called with the
	 * correct parameters
	 */
	public TimeKeeper(int courtWidth, int courtHeight, double width, 
			Color color) {
		super(0, 0, 30, position, (int) width, 30, courtWidth,
				courtHeight, 0, color, Color.black, 0, 1);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color_border);
		g.fillRect(pos_x, pos_y, GameCourt.COURT_WIDTH - 57, 30);
		g.setColor(color);
		g.fillRect(pos_x, pos_y, width, 30);
	}

}
