import java.util.ArrayList;

public class Triangle extends Shape{
    public RTuple p1, p2, p3, e1, e2, normal;

    public Triangle(RTuple p1, RTuple p2, RTuple p3){
        super();
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        e1 = p2.sub(p1);
        e2 = p3.sub(p1);
        normal = e2.cross(e1);
        normal.norm();
    }

    public Triangle(Material material, RTuple p1, RTuple p2, RTuple p3){
        super(material);
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        e1 = p2.sub(p1);
        e2 = p3.sub(p1);
        normal = e2.cross(e1);
        normal.norm();
    }

    public RTuple localNormalAt(RTuple objectPoint){
        return normal;
    }

    public ArrayList<Intersection> localIntersect(Ray rayTransform){
        ArrayList<Intersection> shapeIntersections = new ArrayList<>();
        RTuple dirCrosse2 = rayTransform.direction.cross(e2);
        double det = e1.dot(dirCrosse2);
        if(Math.abs(det) < RT.EPSILON) return shapeIntersections;

        double f = 1/det;
        RTuple p1ToOrigin = rayTransform.origin.sub(p1);
        double u = f * p1ToOrigin.dot(dirCrosse2);
        if(u < 0 || u > 1) return shapeIntersections;

        RTuple originCrosse1 = p1ToOrigin.cross(e1);
        double v = f * rayTransform.direction.dot(originCrosse1);
        if(v < 0 || (u + v) > 1) return shapeIntersections;

        double t = f * e2.dot(originCrosse1);
        shapeIntersections.add(new Intersection(this, t, 0, 0));
        return shapeIntersections;
    }
}
