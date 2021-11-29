// RTC Chapters 4, 9

import java.util.ArrayList;

abstract public class Shape {
    public RMatrix TransformMat; // all applied transformations
    public RMatrix InvTransformMat; // the inverse transformation for rays
    public ArrayList<String> AppliedTransforms = new ArrayList<>();

    public Material material;

    private String name;
    private Shape partOf; // the larger shape it is a part of

    public Shape() {
        RMatrix im = new RMatrix(4);
        TransformMat = im;
        InvTransformMat = im;
        AppliedTransforms.add("default");
        material = new Material();
        name = "defaultName";
        partOf = null;
    }

    public Shape(Material material) {
        RMatrix im = new RMatrix(4);
        TransformMat = im;
        InvTransformMat = im;
        AppliedTransforms.add("default");
        this.material = material;
        name = "defaultName";
        partOf = null;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setPartOf(Shape shape) {
        partOf = shape;
    }

    public Shape getPartOf() {
        return partOf;
    }

    // A matrix for translation and scaling
    public void transform(String operation, double x, double y, double z) {
        switch (operation) {
            case "translate" -> {
                double[][] ta = {
                        {1, 0, 0, x},
                        {0, 1, 0, y},
                        {0, 0, 1, z},
                        {0, 0, 0, 1}
                };
                RMatrix tm = new RMatrix(ta);
                TransformMat = tm.mul(TransformMat);
                AppliedTransforms.add("translate");
            }
            case "scale" -> {
                double[][] sa = {
                        {x, 0, 0, 0},
                        {0, y, 0, 0},
                        {0, 0, z, 0},
                        {0, 0, 0, 1}
                };
                RMatrix sm = new RMatrix(sa);
                TransformMat = sm.mul(TransformMat);
                AppliedTransforms.add("scale");
            }
            default -> AppliedTransforms.add("nothing"); // not expected
        }
        InvTransformMat = TransformMat.inv();
    }

    // A matrix for rotation around a specified axis
    public void transform(String axis, double angle) {
        switch (axis) {
            case "x" -> {
                double[][] xa = {
                        {1, 0, 0, 0},
                        {0, Math.cos(angle), -Math.sin(angle), 0},
                        {0, Math.sin(angle), Math.cos(angle), 0},
                        {0, 0, 0, 1}
                };
                RMatrix xm = new RMatrix(xa);
                TransformMat = xm.mul(TransformMat);
                AppliedTransforms.add("x-rotate");
            }
            case "y" -> {
                double[][] ya = {
                        {Math.cos(angle), 0, Math.sin(angle), 0},
                        {0, 1, 0, 0},
                        {-Math.sin(angle), 0, Math.cos(angle), 0},
                        {0, 0, 0, 1}
                };
                RMatrix ym = new RMatrix(ya);
                TransformMat = ym.mul(TransformMat);
                AppliedTransforms.add("y-rotate");
            }
            case "z" -> {
                double[][] za = {
                        {Math.cos(angle), -Math.sin(angle), 0, 0},
                        {Math.sin(angle), Math.cos(angle), 0, 0},
                        {0, 0, 1, 0},
                        {0, 0, 0, 1}
                };
                RMatrix zm = new RMatrix(za);
                TransformMat = zm.mul(TransformMat);
                AppliedTransforms.add("z-rotate");
            }
            default -> AppliedTransforms.add("nothing"); // not expected
        }
        InvTransformMat = TransformMat.inv();
    }

    // A matrix for shearing
    public void transform(double xy, double xz, double yx, double yz, double zx, double zy) {
        double[][] sa = {
                {1, xy, xz, 0},
                {yx, 1, yz, 0},
                {zx, zy, 1, 0},
                {0, 0, 0, 1}
        };
        RMatrix sm = new RMatrix(sa);
        TransformMat = sm.mul(TransformMat);
        AppliedTransforms.add("shear");
        InvTransformMat = TransformMat.inv();
    }

    void showTransforms() {
        for (String x : AppliedTransforms) System.out.print(x + " ");
    }

    RTuple normalAt(RTuple worldPoint) {
        RTuple localPoint = RT.worldToObject(this, worldPoint);
        RTuple localNormal = localNormalAt(localPoint);
        return RT.normalToWorld(this, localNormal);
    }

    ArrayList<Intersection> intersect(Ray ray) {
        RTuple originTransform = InvTransformMat.mul(ray.origin);
        RTuple directionTransform = InvTransformMat.mul(ray.direction);
        Ray rayTransform = new Ray(originTransform, directionTransform);
        return localIntersect(rayTransform);
    }

    abstract public RTuple localNormalAt(RTuple worldPoint);

    abstract public ArrayList<Intersection> localIntersect(Ray rayTransform);
}