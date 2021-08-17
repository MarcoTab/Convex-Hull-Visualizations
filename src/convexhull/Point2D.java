package convexhull;

import java.util.Comparator;

/**
 * Implements a 2D point object
 * @author marco
 *
 */
public class Point2D {
	
	private double x;
	private double y;
	
	
	/**
	 * Class constructor
	 */
	public Point2D() {
		this.x = 0;
		this.y = 0;
	}
	
	
	/**
	 * Class constructor
	 * @param x double representing x position
	 * @param y double representing y position
	 */
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * Set function to rewrite a Point2D
	 * @param x double representing x position
	 * @param y double representing y position
	 */
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * Returns the x coordinate of the point
	 * @return a double representing the x coordinate of the point
	 */
	public double getX() {
		return this.x;
	}
	
	
	/**
	 * Returns the y coordinate of the point
	 * @return a double representing the y coordinate of the point
	 */
	public double getY() {
		return this.y;
	}
	
	
	/**
	 * Class for use in graham hull to sort by x coordinate.
	 * @author marco
	 */
	class SortByX implements Comparator<Point2D> {
		
		/**
		 * This comparator imposes orderings that are inconsistent with equals.
		 * Used to sort points by x coordinate.
		 */
		public int compare(Point2D a, Point2D b) {
			if (Vector2D.less(a.getX(), b.getX())) return -1;
			
			else if (Vector2D.equals(a.getX(), b.getX())) {
				if (Vector2D.greater(a.getY(), b.getY())) return -1;
				else if (Vector2D.less(a.getY(), b.getY())) return 1;
				return 0;
			}
			
			// If a is greater than b.
			return 1;
		}
	}
	
	public boolean equals(Point2D p) {
		return (Vector2D.equals(this.getX(), p.getX()) && Vector2D.equals(this.getY(), p.getY()));
	}
	
	public boolean xLessThan(Point2D p) {
		return (Vector2D.less(this.getX(), p.getX()));
	}


	public boolean xGreaterThan(Point2D p) {
		return (Vector2D.greater(this.getX(), p.getX()));
	}


	public int compare(Point2D b) {
		if (Vector2D.less(this.getX(), b.getX())) return -1;

		else if (Vector2D.equals(this.getX(), b.getX())) {
			if (Vector2D.greater(this.getY(), b.getY())) return -1;
			else if (Vector2D.less(this.getY(), b.getY())) return 1;
			return 0;
		}

		// If a is greater than b.
		return 1;
	}
	
	/**
	 * A string representation of a Point2D
	 */
	@Override
	public String toString() {
		return "("+String.format("%.4f", x)+", "+String.format("%.4f", y)+")";
	}
}
