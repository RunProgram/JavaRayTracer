import javax.lang.model.type.ArrayType;
import java.util.ArrayList;
public class Sphere extends Shape{
    final private RTuple center = new RTuple(0, 0, 0, 1);
    final private double radius = 1;

    public Sphere(){
        super();
    }

    public Sphere(Material material){
        super(material);
    }

    public RTuple localNormalAt(RTuple objectPoint){
        RTuple originPoint = new RTuple(0, 0, 0, 1);
        return objectPoint.sub(originPoint);
    }

    public ArrayList<Intersection> localIntersect(Ray rayTransform){
        ArrayList<Intersection> shapeIntersections = new ArrayList<>();
        RTuple sphereToRay = rayTransform.origin.sub(new RTuple(0, 0, 0, 1));
        double a = rayTransform.direction.dot(rayTransform.direction);
        double b = 2 * rayTransform.direction.dot(sphereToRay);
        double c = sphereToRay.dot(sphereToRay) - 1;
        double D = b*b-4*a*c;
        if(D >= 0){
            double t1 = (-b - Math.sqrt(D))/(2*a);
            double t2 = (-b + Math.sqrt(D))/(2*a);
            shapeIntersections.add(new Intersection(this, t1, 0, 0));
            shapeIntersections.add(new Intersection(this, t2, 0, 0));
        }
        return shapeIntersections;
    }
}
