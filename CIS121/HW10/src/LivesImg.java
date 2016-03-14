/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A game object displayed using an image.
 * 
 * Note that the image is read from the file when the object is constructed, and
 * that all objects created by this constructor share the same image data (i.e.
 * img is static). This important for efficiency: your program will go very
 * slowing if you try to create a new BufferedImage every time the draw method
 * is invoked.
 */
public class LivesImg extends GameObj {
	public static final String img_file_front = "guy_front.png";
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	public static final int SIZE = 50;

	public static BufferedImage img_front;

	public LivesImg(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, 0, 0, SIZE, SIZE, courtWidth,
				courtHeight, 0, Color.black, Color.black, 0, 1);
		try {
			if (img_front == null) {
				img_front = ImageIO.read(new File(img_file_front));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}

	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(img_front, pos_x, pos_y, width, height, null);
	}

}