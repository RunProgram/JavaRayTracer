import java.util.ArrayList;
import java.util.Collections;

public class Cube extends Shape {

    public Cube() {
        super();
    }

    public Cube(Material material) {
        super(material);
    }

    // RTC p. 173
    public RTuple localNormalAt(RTuple objectPoint) {
        ArrayList<Double> maxcArr = new ArrayList<>();
        maxcArr.add(Math.abs(objectPoint.x));
        maxcArr.add(Math.abs(objectPoint.y));
        maxcArr.add(Math.abs(objectPoint.z));
        double maxc = Collections.max(maxcArr);
        if (Math.abs(maxc - Math.abs(objectPoint.x)) < RT.EPSILON) return new RTuple(objectPoint.x, 0, 0, 0);
        if (Math.abs(maxc - Math.abs(objectPoint.y)) < RT.EPSILON) return new RTuple(0, objectPoint.y, 0, 0);
        return new RTuple(0, 0, objectPoint.z, 0);
    }

    private double[] checkAxis(double origin, double direction, double pmin, double pmax) {
        double tmin, tmax;
        double tminNumerator = pmin - origin;
        double tmaxNumerator = pmax - origin;
        if (Math.abs(direction) >= RT.EPSILON) {
            tmin = tminNumerator / direction;
            tmax = tmaxNumerator / direction;
        } else {
            tmin = Math.signum(tminNumerator) * RT.INFINITY;
            tmax = Math.signum(tmaxNumerator) * RT.INFINITY;
        }

        if (tmin > tmax) {
            double temp = tmin;
            tmin = tmax;
            tmax = temp;
        }

        double arr[] = {tmin, tmax};
        return arr;
    }


    public ArrayList<Intersection> localIntersect(Ray rayTransform) {
        ArrayList<Intersection> shapeIntersections = new ArrayList<>();
        double x[] = checkAxis(rayTransform.origin.x, rayTransform.direction.x, -1, 1);
        double y[] = checkAxis(rayTransform.origin.y, rayTransform.direction.y, -1, 1);
        ;
        double z[] = checkAxis(rayTransform.origin.z, rayTransform.direction.z, -1, 1);
        double xtmin = x[0];
        double xtmax = x[1];
        double ytmin = y[0];
        double ytmax = y[1];
        double ztmin = z[0];
        double ztmax = z[1];
        ArrayList<Double> tminArr = new ArrayList<>();
        tminArr.add(0, xtmin);
        tminArr.add(1, ytmin);
        tminArr.add(2, ztmin);
        ArrayList<Double> tmaxArr = new ArrayList<>();
        tmaxArr.add(0, xtmax);
        tmaxArr.add(1, ytmax);
        tmaxArr.add(2, ztmax);
        double tmin = Collections.max(tminArr);
        double tmax = Collections.min(tmaxArr);

        if (tmin <= tmax) {
            shapeIntersections.add(new Intersection(this, tmin, 0, 0));
            shapeIntersections.add(new Intersection(this, tmax, 0, 0));
        }
        return shapeIntersections;
    }

    public ArrayList<Intersection> localIntersect(Ray rayTransform, Bounds bounds) {
        ArrayList<Intersection> shapeIntersections = new ArrayList<>();
        double x[] = checkAxis(rayTransform.origin.x, rayTransform.direction.x, bounds.pmin().x, bounds.pmax().x);
        double y[] = checkAxis(rayTransform.origin.y, rayTransform.direction.y, bounds.pmin().y, bounds.pmax().y);
        double z[] = checkAxis(rayTransform.origin.z, rayTransform.direction.z, bounds.pmin().z, bounds.pmax().z);
        double xtmin = x[0];
        double xtmax = x[1];
        double ytmin = y[0];
        double ytmax = y[1];
        double ztmin = z[0];
        double ztmax = z[1];
        ArrayList<Double> tminArr = new ArrayList<>();
        tminArr.add(0, xtmin);
        tminArr.add(1, ytmin);
        tminArr.add(2, ztmin);
        ArrayList<Double> tmaxArr = new ArrayList<>();
        tmaxArr.add(0, xtmax);
        tmaxArr.add(1, ytmax);
        tmaxArr.add(2, ztmax);
        double tmin = Collections.max(tminArr);
        double tmax = Collections.min(tmaxArr);

        if (tmin <= tmax) {
            shapeIntersections.add(new Intersection(this, tmin, 0, 0));
            shapeIntersections.add(new Intersection(this, tmax, 0, 0));
        }
        return shapeIntersections;
    }
}
