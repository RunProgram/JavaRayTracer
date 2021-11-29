import java.util.ArrayList;
public class Plane extends Shape{
    public Plane(){
        super();
    }

    public Plane(Material material){
        super(material);
    }

    public RTuple localNormalAt(RTuple objectPoint){
        return new RTuple(0, 1, 0, 0);
    }

    public ArrayList<Intersection> localIntersect(Ray rayTransform){
        ArrayList<Intersection> shapeIntersections = new ArrayList<>();
        double EPSILON = 1E-6;
        if(Math.abs(rayTransform.direction.y) < EPSILON) return shapeIntersections;
        double t = -(rayTransform.origin.y / rayTransform.direction.y);
        shapeIntersections.add(new Intersection(this, t, 0, 0));
        return shapeIntersections;
    }
}
