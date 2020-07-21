package edu.war.robotics.vector;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private double totalPathDistance = 0.0;
    private List<WayPoint> wayPoints = new ArrayList<>();

    private Path() {}
    /**
     * @param wayPoints Array of X,Y points.  Consecutive duplicate points are discarded
     *                  A path must have at least 2 non-identical points
     * @throws IllegalArgumentException for paths with fewer than 2 non-duplicate points.
     */
    private Path(List<WayPoint> wayPoints) {
        this.wayPoints = wayPoints;
        for (WayPoint wayPoint : this.wayPoints ) {
            this.totalPathDistance += wayPoint.getDistanceFromPrevious();
        }
    }

    public static Path createPath(Point[] pathPoints) throws IllegalArgumentException {
        if (pathPoints == null || pathPoints.length < 2) {
            throw new IllegalArgumentException("Path requires at least two points.");
        }
        List<WayPoint> wayPoints = new ArrayList<>();
        // adding the first point into our list
        wayPoints.add(new WayPoint(pathPoints[0],0,0,0,0));
        for (int i = 1; i < pathPoints.length; i++) {
            Point currentPoint = pathPoints[i];
            Point previousPoint = pathPoints[i-1];
            if (!currentPoint.equals(previousPoint)) {
                Point deltaPoint = currentPoint.getPointDelta(previousPoint);
                double distanceFromPrevious = currentPoint.getDistance(previousPoint);
                double distanceFromStart = currentPoint.getDistance(pathPoints[0]);
                WayPoint wayPoint = new WayPoint(currentPoint, deltaPoint.getX(), deltaPoint.getY(), distanceFromPrevious, distanceFromStart);
                wayPoints.add(wayPoint);
            }
        }
        // check at the end to make sure we created an array with at least two different points
        if (wayPoints.size() == 1) {
            throw new IllegalArgumentException("A Path must have at least two different points.");
        }
        return new Path(wayPoints);
    }

    public List<WayPoint> getWayPoints() {
        return wayPoints;
    }

    /**
     * @return total distance along the path
     */
    public double totalDistance() {
        return this.totalPathDistance;
    }

    /**
     * @return a point at the supplied distance along the path from the supplied robot position
     * Note that the point will usually be interpolated between the points that originally defined the Path
     */
    public WayPoint getTargetPoint(Point robotPosition, double lookAheadDistance) {
//        WayPoint nextWayPoint = null;
//        int nextWayPointIndex = -1;
//        double distanceToWayPoint = 0.0;
//        WayPoint[] wayPointArray = (WayPoint[]) wayPoints.toArray();
//        for (int i = 0; i < wayPointArray.length; i++) {
//            distanceToWayPoint = wayPointArray[i].componentAlongPath(robotPosition);
//            if (distanceToWayPoint > 0) {
//                nextWayPointIndex = i;
//                nextWayPoint = wayPointArray[i];
//                break;
//            }
//        }
        // check if WayPoint is not null
//        double distanceFromCurrent = distanceToWayPoint;
//        WayPoint lastWayPoint = null;
//        for (int i = nextWayPointIndex+1; i < wayPointArray.length; i++) {
//            distanceFromCurrent += wayPointArray[i].getDistanceFromPrevious();
//            if (distanceFromCurrent > lookAheadDistance) {
//                lastWayPoint = wayPointArray[i];
//            }
//        }

        Distance distanceToWayPoint = new Distance();
        WayPoint firstWayPoint = getClosestWayPoint(robotPosition, distanceToWayPoint);
        WayPoint lastWayPoint = getEndWayPoint(firstWayPoint, distanceToWayPoint.length, lookAheadDistance);

        Line segment = new Line(firstWayPoint.getPoint(), lastWayPoint.getPoint());
        Point[] points = segment.getSegments(2);
        Point targetPoint = points[1];
        Point deltaPoint = targetPoint.getPointDelta(points[0]);
        double distance = targetPoint.getDistance(points[0]);
        return new WayPoint(targetPoint, deltaPoint.getX(), deltaPoint.getY(), distance, distance);
    }

    private class Distance {
        public double length = 0.0;
    }

    private WayPoint getClosestWayPoint(Point robotPosition, Distance distanceToWayPoint) {
        WayPoint nextWayPoint = null;
        for ( WayPoint wayPoint : this.wayPoints ){
            distanceToWayPoint.length = wayPoint.componentAlongPath(robotPosition);
            if (distanceToWayPoint.length > 0) {
                nextWayPoint = wayPoint;
                break;
            }
        }
        return nextWayPoint;
    }

    private WayPoint getEndWayPoint(WayPoint firstWayPoint, double initialDistance, double lookAheadDistance) {
        double distanceFromCurrent = initialDistance;
        WayPoint endWayPoint = null;
        WayPoint[] wayPointArray = (WayPoint[]) wayPoints.toArray();
        for (int i = wayPoints.indexOf(firstWayPoint) + 1; i < wayPointArray.length; i++) {
            distanceFromCurrent += wayPointArray[i].getDistanceFromPrevious();
            if (distanceFromCurrent > lookAheadDistance) {
                endWayPoint = wayPointArray[i];
            }
        }
        return endWayPoint;
    }



//    List<TargetPoint> getPoints() {
//        return new ArrayList<>();
//    }

//    public static class TargetPoint {
//        public Point point;
//        public double distanceFromStart;
//        public double distanceToEnd;
//
//        private TargetPoint(Point point, double distanceFromStart, double distanceToEnd) {
//            this.point = point;
//            this.distanceFromStart = distanceFromStart;
//            this.distanceToEnd = distanceToEnd;
//        }
//
////        private TargetPoint(WayPoint wayPoint) {
////            this.point = wayPoint.getPoint();
////            this.distanceFromStart = wayPoint.getDistanceFromStart();
////            this.distanceToEnd = wayPoint.getDistanceToEnd();
////        }
//    }


}