package com.tanner;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class Point extends Point2D implements Comparable<Point> { 
    public final int x;
    public final int y;

    public final Comparator<Point> BY_CCW = new ByCCWComparator();

    // create and initialize a point with given (x, y)
    public Point(int x, int y) {
       this.x = x;
       this.y = y;
    }

    // is a->b->c a counter-clockwise turn?
    // -1 if clockwise, +1 if counter-clockwise, 0 if collinear
    public static int ccw(Point a, Point b, Point c) {
       int area2 = (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
       if      (area2 < 0) return -1;
       else if (area2 > 0) return +1;
       else                return  0;
    }

    // convert to string
    public String toString() {
       return "(" + x + ", " + y + ")";
    }

    // natural order: compare by y-coordinate; break ties by x-coordinate
    public int compareTo(Point that) {
       if (this.y < that.y) return -1;
       if (this.y > that.y) return +1;
       if (this.x > that.x) return -1;
       if (this.x < that.x) return +1;
       return 0;
    }

    // are the x- y-coordinates of the two points the same?
    public boolean equals(Object y) {
       if (y == this) return true;
       if (y == null) return false;
       if (y.getClass() != this.getClass()) return false;
       Point that = (Point) y;
       return (this.x == that.x) && (this.y == that.y);
    }

    /**********************************************************************
     *  Comparator that compares points according to polar angle
     *  they make with this point. The polar angle is measured with
     *  respect to a ray emanating from this point and pointing eastward.
     *  Break ties according to distance to this point.
     *
     *  Tie-breaking rule is only for degeneracy, e.g., if three
     *  collinear points on the convex hull, then this enables us
     *  to grab only first and last points.
     *
     *  Precondition:  q1 and q2 are in upper quadrant, relative to p.
     *
     *  Technically, this breaks the contract for compare() if
     *  called with points with y coordinate less than p.
     *
     **********************************************************************/
    private class ByCCWComparator implements Comparator<Point> {
       public int compare(Point q1, Point q2) {
          int ccw = ccw(Point.this, q1, q2);
          if (ccw == -1) return -1;
          if (ccw == +1) return +1;

          int dx1 = q1.x - x;
          int dx2 = q2.x - x;
          int dy1 = q1.y - y;
          int dy2 = q2.y - y;

          // assert dy1 >= 0 && dy2 >= 0;  // or breaks compare() contract

          // break ties by distance to this point
          // should be able to replace distance calculation with
          // projection because three points are collinear
          if      (dx1*dx1 + dy1*dy1 < dx2*dx2 + dy2*dy2) return -1;
          else if (dx1*dx1 + dy1*dy1 > dx2*dx2 + dy2*dy2) return +1;
          else                                            return  0;
       }
    }

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setLocation(double x, double y) {}
}
