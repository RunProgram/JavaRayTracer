import java.util.ArrayList;

public class TriangleS extends Triangle{
    public RTuple n1, n2, n3;

    public TriangleS(RTuple p1, RTuple p2, RTuple p3, RTuple n1, RTuple n2, RTuple n3){
        super(p1, p2, p3);
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
    }

    public TriangleS(Material material, RTuple p1, RTuple p2, RTuple p3, RTuple n1, RTuple n2, RTuple n3){
        super(material, p1, p2, p3);
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
    }

    @Override
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
        shapeIntersections.add(new Intersection(this, t, u, v));

        normal = n2.mul(u).add(n3.mul(v)).add(n1.mul(1 - u - v));

        return shapeIntersections;
    }
}
