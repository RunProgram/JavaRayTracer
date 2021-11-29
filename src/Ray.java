import java.util.ArrayList;

public class Ray {
    public RTuple origin;
    public RTuple direction;

    public Ray(RTuple or, RTuple dir){
        origin = or;
        direction = dir;
    }

    public RTuple findEnd(double t){
        return origin.add(direction.mul(t));
    }

    public ArrayList<Intersection> intersect(World world) {
        ArrayList<Intersection> allIntersections = new ArrayList<>();
        Shape shape;
        int n = world.allThings.size();
        ArrayList<Intersection> localIntersections;
        int m;

        if (n > 0) {
            for (int i = 0; i < n; i++) {
                shape = world.allThings.get(i);
                localIntersections = shape.intersect(this);
                m = localIntersections.size();
                if (m > 0) {
                    for (int j = 0; j < m; j++) {
                        allIntersections.add(localIntersections.get(j));
                    }
                }
            }
        }
        if (allIntersections.size() > 0) allIntersections.sort(new TSorter());
        return allIntersections;
    }


    public Ray translate(double x, double y, double z) {
        RMatrix im = new RMatrix("translate", x, y, z);
        return new Ray(im.mul(origin), im.mul(direction));
    }

    public Ray scale(double x, double y, double z) {
        RMatrix im = new RMatrix("scale", x, y, z);
        return new Ray(im.mul(origin), im.mul(direction));
    }

    public Ray rotate(String axis, double angle) {
        RMatrix im = new RMatrix(axis, angle);
        return new Ray(im.mul(origin), im.mul(direction));
    }

    public Ray shear(double xy, double xz, double yx, double yz, double zx, double zy) {
        RMatrix im = new RMatrix(xy, xz, yx, yz, zx, zy);
        return new Ray(im.mul(origin), im.mul(direction));
    }

}
