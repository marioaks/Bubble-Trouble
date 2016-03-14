/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 * An object in the game.
 * 
 * Game objects exist in the game court. They have a position, velocity, size
 * and bounds. Their velocity controls how they move; their position should
 * always be within their bounds.
 */
public class GameObj {

	/**
	 * Current position of the object (in terms of graphics coordinates)
	 * 
	 * Coordinates are given by the upper-left hand corner of the object. This
	 * position should always be within bounds. 0 <= pos_x <= max_x 0 <= pos_y
	 * <= max_y
	 */
	public int pos_x;
	public int pos_y;

	/** Size of object, in pixels */
	public int width;
	public int height;

	/** Velocity: number of pixels to move every time move() is called */
	public int v_x;
	public double v_y;

	/**
	 * Upper bounds of the area in which the object can be positioned. Maximum
	 * permissible x, y positions for the upper-left hand corner of the object
	 */
	public int max_x;
	public int max_y;

	// gravity
	public static double gravity;
	
	public double dropVel;
	
	public Color color;
	public Color color_border;
	
	public int time;
	public float opacity;
	
	/**
	 * Constructor
	 */
	public GameObj(int v_x, int v_y, int pos_x, int pos_y, int width,
			int height, int court_width, int court_height, double dropVel, 
			Color color, Color color_border, int time, float opacity) {
		this.v_x = v_x;
		this.v_y = v_y;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.width = width;
		this.height = height;
		gravity = .5;
		this.dropVel = dropVel;
		this.color = color;
		this.color_border = color_border;
		this.time = time; 
		this.opacity = opacity;

		// take the width and height into account when setting the
		// bounds for the upper left corner of the object.
		this.max_x = court_width - width;
		this.max_y = court_height - height;

	}

	/**
	 * Moves the object by its velocity. Ensures that the object does not go
	 * outside its bounds by clipping.
	 */
	public void move() {
		pos_x += v_x;
		pos_y += v_y;
		clip();
	}

	public void drop() {
		pos_x += v_x;
		pos_y += v_y;
	    v_y += gravity;
	    if (pos_y + v_y > max_y) { 
	    	v_y = dropVel;
	    	
	    } else if (pos_y + v_y <= 0) { 
	    	v_y = dropVel;
	    	
	    }
	}

	/**
	 * Prevents the object from going outside of the bounds of the area
	 * designated for the object. (i.e. Object cannot go outside of the active
	 * area the user defines for it).
	 */
	public void clip() {
		if (pos_x < 0)
			pos_x = 0;
		else if (pos_x > max_x)
			pos_x = max_x;

		if (pos_y < -1000000)
			pos_y = 0;
		else if (pos_y > max_y)
			pos_y = max_y;
	}

	/**
	 * Determine whether this game object is currently intersecting another
	 * object.
	 * 
	 * Intersection is determined by comparing bounding boxes. If the bounding
	 * boxes overlap, then an intersection is considered to occur.
	 * 
	 * @param obj
	 *            : other object
	 * @return whether this object intersects the other object.
	 */
	public boolean intersects(GameObj obj) {
		Rectangle rect = new Rectangle(pos_x, pos_y, width, height);
		Ellipse2D e = new Ellipse2D.Float(obj.pos_x, obj.pos_y, obj.width,
				obj.height);
		Area a = new Area(e);
		return a.intersects(rect);
	}

	public boolean intersectsArrow(GameObj obj) {
		if (Arrow.startPoint > 0) {
		Rectangle rect = new Rectangle(pos_x - 3, height, 6, pos_y);
		Ellipse2D e = new Ellipse2D.Float(obj.pos_x, obj.pos_y, obj.width,
				obj.height);
		Area a = new Area(e);
		return a.intersects(rect);
		} else {
			Rectangle rect = new Rectangle(pos_x - 3, 0, 6, height);
			Ellipse2D e = new Ellipse2D.Float(obj.pos_x, obj.pos_y, obj.width,
					obj.height);
			Area a = new Area(e);	
			return a.intersects(rect);
		}
		
	}

	/**
	 * Determine whether this game object will intersect another in the next
	 * time step, assuming that both objects continue with their current
	 * velocity.
	 * 
	 * Intersection is determined by comparing bounding boxes. If the bounding
	 * boxes (for the next time step) overlap, then an intersection is
	 * considered to occur.
	 * 
	 * @param obj
	 *            : other object
	 * @return whether an intersection will occur.
	 */
	public boolean willIntersect(GameObj obj) {
		int next_x = pos_x + v_x;
		double next_y = pos_y + v_y;
		int next_obj_x = obj.pos_x + obj.v_x;
		double next_obj_y = obj.pos_y + obj.v_y;
		return (next_x + width >= next_obj_x && next_y + height >= next_obj_y
				&& next_obj_x + obj.width >= next_x && next_obj_y + obj.height >= next_y);
	}

	/**
	 * Update the velocity of the object in response to hitting an obstacle in
	 * the given direction. If the direction is null, this method has no effect
	 * on the object.
	 */
	public void bounce(Direction d) {
		if (d == null)
			return;
		switch (d) {
		case UP:
			break;
		case DOWN:
			v_y = -v_y;
			break;
		case LEFT:
			v_x = Math.abs(v_x);
			break;
		case RIGHT:
			v_x = -Math.abs(v_x);
			break;
		}
	}

	/**
	 * Determine whether the game object will hit a wall in the next time step.
	 * If so, return the direction of the wall in relation to this game object.
	 * 
	 * @return direction of impending wall, null if all clear.
	 */
	public Direction hitWall() {
		if (pos_x + v_x < 0)
			return Direction.LEFT;
		else if (pos_x + v_x > max_x)
			return Direction.RIGHT;
		if (pos_y + v_y < -height * 2)
			return Direction.UP;
		else if (pos_y + v_y > max_y)
			return Direction.DOWN;
		else
			return null;
	}

	/**
	 * Determine whether the game object will hit another object in the next
	 * time step. If so, return the direction of the other object in relation to
	 * this game object.
	 * 
	 * @return direction of impending object, null if all clear.
	 */
	public Direction hitObj(GameObj other) {

		if (this.willIntersect(other)) {
			double dx = other.pos_x + other.width / 2 - (pos_x + width / 2);
			double dy = other.pos_y + other.height / 2 - (pos_y + height / 2);

			double theta = Math.acos(dx / (Math.sqrt(dx * dx + dy * dy)));
			double diagTheta = Math.atan2(height / 2, width / 2);

			if (theta <= diagTheta) {
				return Direction.RIGHT;
			} else if (theta > diagTheta && theta <= Math.PI - diagTheta) {
				if (dy > 0) {
					// Coordinate system for GUIs is switched
					return Direction.DOWN;
				} else {
					return Direction.UP;
				}
			} else {
				return Direction.LEFT;
			}
		} else {
			return null;
		}

	}

	/**
	 * Default draw method that provides how the object should be drawn in the
	 * GUI. This method does not draw anything. Subclass should override this
	 * method based on how their object should appear.
	 * 
	 * @param g
	 *            The <code>Graphics</code> context used for drawing the object.
	 *            Remember graphics contexts that we used in OCaml, it gives the
	 *            context in which the object should be drawn (a canvas, a
	 *            frame, etc.)
	 */
	public void draw(Graphics g) {
	}

}
