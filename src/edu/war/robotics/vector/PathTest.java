package edu.war.robotics.vector;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PathTest {

    @Test
    void createPath() {

        Point[] happyPoints = new Point[] {new Point(0,0), new Point(3,4)};
        Path path = Path.createPath(happyPoints);
        // happy path
        assertEquals(2, path.getWayPoints().size());
    }
    @Test
    public void testDuplicatesRemoved() {
        // Make some points
        Point[] points = new Point[] {new Point(0,0), new Point(3,4), new Point(3,4), new Point(5,5)};
        Path path = Path.createPath(points);
        assertEquals(3, path.getWayPoints().size());
        // Check to make sure that you got rid of the duplicates
//        List<Path.TargetPoint> results = path.getPoints();
        // Your code here!
    }

    @Test
    public void testRejectZeroLengthPath() {
        // Make an empty point array and try to create a path
        Point[] points = new Point[0];
        try {
            Path path = Path.createPath(points);  // should throw IllegalArgumentException
            fail("Expected Path() to throw IllegalArgumentException for empty point array");
        } catch (IllegalArgumentException e) {
            // What we expected; do nothing and let the test pass.
        }
    }

    @Test
    public void testTotalDistance() {
        Point[] points = new Point[] {new Point(0,0), new Point(3,4)};
        Path path = Path.createPath(points);
        assertEquals(5, path.totalDistance(), 0.00001);
    }


}