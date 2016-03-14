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
public class Guy extends GameObj {
	public static final String img_file_middle = "guy_middle.png";
	public static final String img_file_left = "guy_left.png";
	public static final String img_file_right = "guy_right.png";
	public static final String img_file_middle_flip = "guy_middle_flip.png";
	public static final String img_file_left_flip = "guy_left_flip.png";
	public static final String img_file_right_flip = "guy_right_flip.png";
	public static final int SIZE = 50;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 30;

	public static BufferedImage img_middle;
	public static BufferedImage img_left;
	public static BufferedImage img_right;
	
	public static BufferedImage img_middle_flip;
	public static BufferedImage img_left_flip;
	public static BufferedImage img_right_flip;

	public Guy(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, courtWidth / 2 - SIZE / 2, courtHeight
				- SIZE, SIZE, SIZE, courtWidth, courtHeight, 0, Color.black,
				Color.black, 0, 1);
		try {
			if (img_middle == null) {
				img_middle = ImageIO.read(new File(img_file_middle));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}

		try {
			if (img_left == null) {
				img_left = ImageIO.read(new File(img_file_left));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}

		try {
			if (img_right == null) {
				img_right = ImageIO.read(new File(img_file_right));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		
		try {
			if (img_middle_flip == null) {
				img_middle_flip = ImageIO.read(new File(img_file_middle_flip));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}

		try {
			if (img_left_flip == null) {
				img_left_flip = ImageIO.read(new File(img_file_left_flip));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}

		try {
			if (img_right_flip == null) {
				img_right_flip = ImageIO.read(new File(img_file_right_flip));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		
	}

	@Override
	public void draw(Graphics g) {
		if (pos_y < GameCourt.COURT_HEIGHT / 2) {
			if (v_x == 0) {
				g.drawImage(img_middle_flip, pos_x, pos_y, width, height, null);
			} else if (v_x > 0) {
				g.drawImage(img_right_flip, pos_x, pos_y, width, height, null);
			} else {
				g.drawImage(img_left_flip, pos_x, pos_y, width, height, null);
			}
		} else {
			if (v_x == 0) {
				g.drawImage(img_middle, pos_x, pos_y, width, height, null);
			} else if (v_x > 0) {
				g.drawImage(img_right, pos_x, pos_y, width, height, null);
			} else {
				g.drawImage(img_left, pos_x, pos_y, width, height, null);
			}
		}
	}

}