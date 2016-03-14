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
public class Arrow extends GameObj {
	public static final int SIZE = 20;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	public static int startPoint = GameCourt.COURT_HEIGHT;
	Polygon p2 = new Polygon();

	/**
	 * Note that, because we don't need to do anything special when constructing
	 * a Square, we simply use the superclass constructor called with the
	 * correct parameters
	 */
	public Arrow(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, courtWidth, courtHeight, SIZE, SIZE,
				courtWidth, courtHeight, 0, Color.BLACK, Color.black, 0, 1);

	}

	@Override
	public void draw(Graphics g) {
		/*
		 * g.setColor(Color.BLACK); if (height < GameCourt.COURT_HEIGHT -
		 * Guy.SIZE) { for (int y = 0; y < 175; y++) { p2.addPoint(pos_x - (int)
		 * (2 * Math.cos(((y / 10.0)) * 2 Math.PI)), height); } }
		 * 
		 * g.drawPolyline(p2.xpoints, p2.ypoints, p2.npoints);
		 */

		g.setColor(color);
		g.drawLine(pos_x, startPoint, pos_x, height);
		if (startPoint > 0) {
			g.drawLine(pos_x, height, pos_x - 3, height + 3);
			g.drawLine(pos_x, height, pos_x + 3, height + 3);
		} else {
			g.drawLine(pos_x, height, pos_x - 3, height - 3);
			g.drawLine(pos_x, height, pos_x + 3, height - 3);
		}

	}

}
