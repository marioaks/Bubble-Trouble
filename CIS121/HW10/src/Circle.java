/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;

/**
 * A basic game object displayed as a yellow circle, starting in the upper left
 * corner of the game court.
 * 
 */
public class Circle extends GameObj {

	public static int SIZE = 150;
	public static final int INIT_POS_X = 100;
	public static final int INIT_POS_Y = 300;
	public static final int INIT_VEL_X = 4;
	public static final int INIT_VEL_Y = 0;

	public Circle(int courtWidth, int courtHeight, double dropVel, Color color, Color color_border) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE,
				courtWidth, courtHeight, dropVel, color, color_border, 0, 1);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(pos_x, pos_y, width, height);
		g.setColor(color_border);
		g.drawOval(pos_x, pos_y, width, height);
		
	}

}