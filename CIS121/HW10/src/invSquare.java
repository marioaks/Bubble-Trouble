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
public class invSquare extends GameObj {
	public invSquare(int v_x, int v_y, int pos_x, int pos_y, int width,
			int height, int court_width, int court_height) {
		super(v_x, v_y, pos_x, pos_y, width, height, court_width, court_height,
				0, Color.black, Color.black, 0, 1);
		// TODO Auto-generated constructor stub
	}

	public static int SIZE = 1;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;

	/**
	 * Note that, because we don't need to do anything special when constructing
	 * a Square, we simply use the superclass constructor called with the
	 * correct parameters
	 */
	public invSquare(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, 0, 0, SIZE, SIZE,
				courtWidth, courtHeight, 0, Color.black, Color.black, 0, 1);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(pos_x, pos_y, width, height);
	}

}
