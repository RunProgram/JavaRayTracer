import java.util.ArrayList;

public class Cylinder extends Shape{
    private double min, max;
    boolean closed;

    public Cylinder(){
        super();
        min = -RT.INFINITY;
        max = RT.INFINITY;
        closed = false;
    }

    public Cylinder(double min, double max, boolean closed){
        super();
        this.min = min;
        this.max = max;
        this.closed = closed;
    }

    public Cylinder(Material material){
        super(material);
        min = -RT.INFINITY;
        max = RT.INFINITY;
        closed = false;
    }

    public Cylinder(Material material, double min, double max, boolean closed){
        super(material);
        this.min = min;
        this.max = max;
        this.closed = closed;
    }

    public double getMin(){
        return min;
    }

    public double getMax(){
        return max;
    }

    public RTuple localNormalAt(RTuple objectPoint){
        double dist = Math.pow(objectPoint.x, 2) + Math.pow(objectPoint.z, 2);
        if(dist < 1 && objectPoint.y >= (max - RT.EPSILON)){
            return new RTuple(0, 1, 0, 0);
        }
        else if(dist < 1 && objectPoint.y <= min + RT.EPSILON){
            return new RTuple(0, -1, 0, 0);
        }
        else return new RTuple(objectPoint.x, 0, objectPoint.z, 0);
    }

    public ArrayList<Intersection> localIntersect(Ray rayTransform){
        ArrayList<Intersection> shapeIntersections = new ArrayList<>();
        double a = Math.pow(rayTransform.direction.x, 2) + Math.pow(rayTransform.direction.z, 2);
        if(Math.abs(a) > RT.EPSILON){
            double b = 2 * rayTransform.origin.x * rayTransform.direction.x + 2 * rayTransform.origin.z * rayTransform.direction.z;
            double c = Math.pow(rayTransform.origin.x, 2) + Math.pow(rayTransform.origin.z, 2) - 1;
            double D = b * b - 4 * a * c;
            if(D >= 0){
                double t1 = (-b - Math.sqrt(D)) / (2*a);
                double t2 = (-b + Math.sqrt(D)) / (2*a);
                double y1 = rayTransform.origin.y + t1 * rayTransform.direction.y;
                if(min < y1 && y1 < max) shapeIntersections.add(new Intersection(this, t1, 0, 0));
                double y2 = rayTransform.origin.y + t2 * rayTransform.direction.y;
                if(min < y2 && y2 < max) shapeIntersections.add(new Intersection(this, t2, 0, 0));
            }
        }
        double t;
        if(closed && Math.abs(rayTransform.direction.y) > RT.EPSILON){
            t = (min - rayTransform.origin.y)/rayTransform.direction.y;
            if(checkCap(rayTransform, t)) shapeIntersections.add(new Intersection(this, t, 0, 0));
            t = (max - rayTransform.origin.y)/rayTransform.direction.y;
            if(checkCap(rayTransform, t)) shapeIntersections.add(new Intersection(this, t, 0, 0));
        }
        return shapeIntersections;
    }
    private boolean checkCap(Ray ray, double t){
        double x = ray.origin.x + t * ray.direction.x;
        double z = ray.origin.z + t * ray.direction.z;
        return Math.pow(x, 2) + Math.pow(z, 2) <= 1;
    }
}
