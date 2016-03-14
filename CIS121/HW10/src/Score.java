/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A basic game object displayed as a black square, starting in the upper left
 * corner of the game court.
 * 
 */
public class Score extends GameObj {
	public static int SIZE = 20;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;

	/**
	 * Note that, because we don't need to do anything special when constructing
	 * a Square, we simply use the superclass constructor called with the
	 * correct parameters
	 */
	public Score(int courtWidth, int courtHeight, String score) {
		super(INIT_VEL_X, INIT_VEL_Y, courtWidth - 120, 10, score.length(), 30,
				courtWidth, courtHeight, 0, Color.black, Color.white, 0, 1);
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D gd = (Graphics2D) g;
		g.setColor(color_border);
		Font f = new Font("Dialog", Font.PLAIN, 20);
		Rectangle2D rect = f.getStringBounds(Integer.toString(GameCourt.score),
				gd.getFontRenderContext());
		gd.fillRect(pos_x, pos_y, (int) rect.getWidth() + 10, 30);
		g.setColor(color);
		g.drawRect(pos_x, pos_y, (int) rect.getWidth() + 10, 30);

		g.setFont(f);
		g.drawString(Integer.toString(GameCourt.score), pos_x + 3, pos_y + 22);
	}

}
