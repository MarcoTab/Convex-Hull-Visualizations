package convexhull;

//import java.util.List;

public class Line2D {
	private Point2D start;
	private Point2D end;
	
	/**
	 * Class constructor
	 */
	public Line2D() {
		this.start = new Point2D();
		this.end = new Point2D(1, 0);
	}
	
	/**
	 * Class constructor
	 * @param start Point2D representing the start of a line
	 * @param end Point2D representing the end of a line
	 */
	public Line2D(Point2D start, Point2D end) {
		this.start = start;
		this.end = end;
	}
	
	public Line2D(double x1, double y1, double x2, double y2) {
		this.start = new Point2D(x1, y1);
		this.end = new Point2D(x2, y2);
	}
	
	/**
	 * Set function to rewrite a Line2D
	 * @param start Point2D
	 * @param end Point2D
	 */
	public void set(Point2D start, Point2D end) {
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Returns the start of the line
	 * @return a Point2D representing the start of the line
	 */
	public Point2D getStart() {
		return this.start;
	}
	
	/**
	 * Returns the end of the line
	 * @return a Point2D representing the end of the line
	 */
	public Point2D getEnd() {
		return this.end;
	}
	
	/**
	 * A string representation of a Line2D
	 */
	@Override
	public String toString() {
		return start.toString() + " -> " + end.toString();
	}
}
