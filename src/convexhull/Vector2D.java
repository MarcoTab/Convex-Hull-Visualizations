package convexhull;
/**
 * Implements a 2D vector object with cross product related methods 
 * @author marco
 * 
 */

import java.lang.Math;
import java.util.*;

public class Vector2D {
	
	private double i;
	private double j;
	
	
	public Vector2D() {
		this.i = 0;
		this.j = 0;
	}
	/**
	 * Class constructor
	 * @param i double representing i direction magnitude
	 * @param j double representing j direction magnitude
	 */
	public Vector2D(double i, double j) {
		this.i = i;
		this.j = j;
	}
	
	/**
	 * Class constructor using 2 points
	 * @param start Point2D
	 * @param end Point2D
	 */
	public Vector2D(Point2D start, Point2D end) {
		this.i = end.getX() - start.getX();
		this.j = end.getY() - start.getY();
	}
	
	/**
	 * Class constructor using a line
	 * @param line defining the Vector2D
	 */
	public Vector2D(Line2D line) {
		this.i = line.getEnd().getX() - line.getStart().getX();
		this.j = line.getEnd().getY() - line.getStart().getY();
	}
	
	/**
	 * Set function to rewrite a Vector2D
	 * @param i double representing i direction magnitude
	 * @param j double representing j direction magnitude
	 */
	public void set(double i, double j) {
		this.i = i;
		this.j = j;
	}
	
	/**
	 * Set function to rewrite a Vector2D
	 * @param start Point2D
	 * @param end Point2D
	 */
	public void set(Point2D start, Point2D end) {
		this.i = end.getX() - start.getX();
		this.j = end.getY() - start.getY();
	}
	
	/**
	 * Getters for testing / other purposes
	 * @return double representing i magnitude
	 */
	
	public double getI() {
		return this.i;
	}
	
	/**
	 * Getters for testing / other purposes
	 * @return double representing j magnitude
	 */
	public double getJ() {
		return this.j;
	}
	
	/**
	 * Getter that returns the magnitude of a Vector2D object
	 * @return double representing the magnitude of a Vector2D object
	 */
	public double getMag() {
		return Math.abs(Math.sqrt(Math.pow(this.i, 2) + Math.pow(this.j, 2)));
	}
	
	/**
	 * Computes the dot product of two 2D vectors
	 * @param u Vector2D
	 * @param v
	 * @return double representing the dot product
	 */
	public static double dot(Vector2D u, Vector2D v) {
		return u.i*v.i + u.j*v.j;
	}
	
	/**
	 * Computes the magnitude of the cross product of two 2D vectors
	 * @param u Vector2D
	 * @param v Vector2D
	 * @return The magnitude of the cross product of u and v
	 */
	public static double cross(Vector2D u, Vector2D v) {
		return u.i*v.j - u.j*v.i;
	}
	
	/**
	 * Takes 3 Point2D and determines if there is a left turn going from ab to bc.
	 * @param a First Point2D
	 * @param b Second Point2D
	 * @param c Third Point2D
	 * @return < 0 for right turn, > 0 for left turn, = 0 for straight
	 */
	public static double turn(Point2D a, Point2D b, Point2D c) {
		Vector2D u = new Vector2D(a, b);
		Vector2D v = new Vector2D(b, c);
		return cross(u, v);
	}
	
	/**
	 * Takes a point and a line and determines whether the point is above, below, or colinear.
	 * @param l Line segment to compare point to
	 * @param a Point to determine sidedness of
	 * @return > 0 for above, < 0 for below, = 0 for collinear
	 */
	public static double above(Line2D l, Point2D a) {
		Vector2D u = new Vector2D(l.getStart(), l.getEnd());
		Vector2D v = new Vector2D(l.getStart(), a);
		return cross(u, v);
	}
	
	/**
	 * Computes the farthest point (orthogonally) from a line.
	 * @param l Line2D
	 * @param pntarr Point2D array of which we will find the farthest from the line.
	 * @return
	 */
	public static Point2D maxOrthoDist(Line2D l, List<Point2D> pntarr) throws Exception {
		// Set up the vector representing the line
		Vector2D u = new Vector2D(l.getStart(), l.getEnd());
		
		Point2D max = new Point2D();  // Set up return value
		double maxval = -1;	// Set up comparison value
		
		int length = pntarr.size();
		for (int i = 0; i < length; i++) {	// Loop through every point in the array
			Vector2D v = new Vector2D(l.getStart(), pntarr.get(i)); // Set up comparison vector
			if (greater(Math.abs(cross(u, v)), maxval)) {  // If this value is the largest we've seen
				max = pntarr.get(i);			// Do appropriate redefinition
				maxval = Math.abs(cross(u, v));
			}
		}
		if (maxval == 0) {
			throw new Exception("All points were collinear.");
		}
		return max;		// Returns the maximum possible value
	}
	
	/**
	 * Sorting function that sorts points around a central point, radially. Merge sort is used.
	 * @param c Point2D representing the central point.
	 * @param pntarr List<Point2D> representing the set of points to sort.
	 * @param reverseColinear boolean representing whether collinear points should be ordered in reverse order or not.
	 * @return a sorted List<Point2D> 
	 */
	public static List<Point2D> sort(Point2D c, List<Point2D> pntarr, boolean reverseColinear) {
		
		double y = c.getY();
		double x = c.getX();
		List<Point2D> posY = new ArrayList<Point2D>();	// These two lists are to separate the points into two sets of points
		List<Point2D> negY = new ArrayList<Point2D>();	// based on their position with respect to the central point.
		
		int remove = pntarr.indexOf(c);
		boolean removed = remove >= 0;
		if (removed) {
			pntarr.remove(c);
//			System.out.println(c); 
		}
		
		int len = pntarr.size();
		
		for (int i = 0; i < len; i++) {
			double cur = pntarr.get(i).getY();
			if (greater(cur, y)) {
				posY.add(pntarr.get(i));
			} else if (less(cur, y)) {
				negY.add(pntarr.get(i));
			} else {
				cur = pntarr.get(i).getX();
				if (greater(cur, x) || equals(cur, x)) {
					posY.add(pntarr.get(i));
				} else {
					negY.add(pntarr.get(i));
				}
			}
		}
		// ^^^^ Sorts the points as mentioned
		
		posY = radialMergeSort(c, posY, reverseColinear);
		negY = radialMergeSort(c, negY, reverseColinear);
		
		negY.addAll(posY);
//		posY.addAll(negY);
		if (removed) {
			pntarr.add(remove, c);
		}
		return negY;
		
	}
	
	/**
	 * Merge sort algorithm that sorts points around a central point, radially
	 * @param c
	 * @param pntarr
	 * @return
	 */
	private static List<Point2D> radialMergeSort(Point2D c, List<Point2D> pntarr, boolean reverse) {
		int len = pntarr.size();
		if (len <= 1) {
			return pntarr;		// base case, An empty or 1 element array is already sorted
		}
		
		List<Point2D> left = new ArrayList<Point2D>();
		List<Point2D> right = new ArrayList<Point2D>();
		
		for (int i = 1; i <= len; i++) {	// this loop splits the given array in two equal arrays
			if (i <= len/2) {
				left.add(pntarr.get(i-1));
			} else if (i > len/2) {
				right.add(pntarr.get(i-1));
			}
		}
		
		left = radialMergeSort(c, left, reverse);	// Recursive call to sort the left side of the array
		right = radialMergeSort(c, right, reverse);	// Recursive call to sort the right side of the array
		List<Point2D> result = radialMerge(c, left, right, reverse);	// Merge the two arrays
		return result;	// done
	}
	
	/**
	 * Helper function to perform the mergeSort.
	 * @param c
	 * @param left
	 * @param right
	 * @return merged List<Point2D>
	 */
	private static List<Point2D> radialMerge(Point2D c, List<Point2D> left, List<Point2D> right, boolean reverse) {
		List<Point2D> result = new ArrayList<Point2D>();
		
		while (left.size() > 0 && right.size() > 0) { // While neither array is empty
			Vector2D u = new Vector2D(left.get(0).getX() - c.getX(), left.get(0).getY() - c.getY());	// Make 2 vectors with end points at the initial points of each array
			Vector2D v = new Vector2D(right.get(0).getX() - c.getX(), right.get(0).getY() - c.getY());	// The starting points are the central point. This serves as a comparator later on.
			
			if (greater(cross(u, v), 0)) { // If the cross product of u with v is larger than 0, this means the end point of u is smaller than the end point of v.
				result.add(left.get(0));	// Add the point to the return value
				left.remove(0);	// Get rid of the point we just sorted
			} else if (equals(cross(u, v), 0)) { // If the cross product is 0, then this means that the points are co-linear, we must do some extra checking to make sure they are correctly sorted
				if (greater(u.getMag(), v.getMag())) {
					if (reverse) {
						result.add(left.get(0));
						left.remove(0);
					} else {
						result.add(right.get(0));
						right.remove(0);
					}
				} else {
					if (reverse) {
						result.add(right.get(0));
						right.remove(0);
					} else {
						result.add(left.get(0));
						left.remove(0);
					}
				}
			} else {
				result.add(right.get(0));	// Add the point to the return value
				right.remove(0);	// Get rid of the point we just sorted
			}
		}
		//Once at least one of the arays is empty, only ONE of these loops executes, and all the points are put into the return value.
		while (left.size() > 0) {
			result.add(left.get(0));
			left.remove(0);
		}
		while (right.size() > 0) {
			result.add(right.get(0));
			right.remove(0);
		}
		return result;
	}
	
	private static final double THRESHOLD = 0.0001; 
	
	/**
	 * Checks equality between two doubles
	 * @param d1 double
	 * @param d2 double
	 * @return true if equal, false if not.
	 */
	public static boolean equals(double d1, double d2) {
		return compareTo(d1, d2) == 0; // If the two doubles are less than THRESHOLD apart, they are equal.
	}
	
	/**
	 * Checks if d1 is greater than d2
	 * @param d1 double 
	 * @param d2 double
	 * @return true if d1 > d2, false otherwise
	 */
	public static boolean greater(double d1, double d2) {
		return compareTo(d1, d2) == 1;
	}
	
	/**
	 * Checks if d1 is less than d2
	 * @param d1 double 
	 * @param d2 double
	 * @return true if d1 < d2, false otherwise
	 */
	public static boolean less(double d1, double d2) {
		return compareTo(d1, d2) == -1;
	}
	
	/**
	 * Finds the point on the line l that is on the line orthogonal to l that starts at p 
	 * Used for quickHull animation
	 * @param l Line2D line to find the orthogonal and intersection of
	 * @param p Point2D Point where the orthogonal to l originates
	 * @return a Point2D representing the intersection between l and the orthogonal line containing p
	 */
	public static Point2D findIntersection(Line2D l, Point2D p) {
		double ml = (l.getEnd().getY()-l.getStart().getY())/(l.getEnd().getX()-l.getStart().getX());
		double mp = -1/ml;
		
		double yintl = l.getStart().getY() - ml*l.getStart().getX();
		double yintp = p.getY() - mp*p.getX();
		
		return new Point2D(Math.round((yintp-yintl)/(ml-mp)), Math.round(ml*((yintp-yintl)/(ml-mp)) + yintl));
	}
	
	/**
	 * A function to compare doubles with precision
	 * @param left double representing LHS
	 * @param right double representing RHS
	 * @return 1 if left > right, 0 if left == right, -1 if left < right
	 */
	private static int compareTo(double left, double right) {
		if (Math.abs(left - right) < THRESHOLD) {
			return 0;
		} else {
			if ((left - right) > THRESHOLD) {
				return 1;
			} else {
				return -1;
			}
		}
	}
	
	/**
	 * A string representation of a Vector2D
	 */
	@Override
	public String toString() {
		if (j >= 0) {
			return i+" i + "+j+" j";
		} else {
			return i+" i "+j+" j";
		}
	}
}
