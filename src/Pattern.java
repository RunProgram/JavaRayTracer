import java.util.ArrayList;

public abstract class Pattern {
    private RMatrix TransformMat; // all applied transformations
    public RMatrix InvTransformMat; // the inverse transformation for rays
    public ArrayList<String> AppliedTransforms = new ArrayList<>();

    public Pattern() {
        RMatrix im = new RMatrix(4);
        TransformMat = im;
        InvTransformMat = im;
        AppliedTransforms.add("default");
    }

    // A matrix for translation and scaling
    public void transform(String operation, double x, double y, double z) {
        switch (operation) {
            case "translate":
                double ta[][] = {
                        {1, 0, 0, x},
                        {0, 1, 0, y},
                        {0, 0, 1, z},
                        {0, 0, 0, 1}
                };
                RMatrix tm = new RMatrix(ta);
                TransformMat = tm.mul(TransformMat);
                AppliedTransforms.add("translate");
                break;
            case "scale":
                double sa[][] = {
                        {x, 0, 0, 0},
                        {0, y, 0, 0},
                        {0, 0, z, 0},
                        {0, 0, 0, 1}
                };
                RMatrix sm = new RMatrix(sa);
                TransformMat = sm.mul(TransformMat);
                AppliedTransforms.add("scale");
                break;
            default:
                AppliedTransforms.add("nothing"); // not expected
        }
        InvTransformMat = TransformMat.inv();
    }

    // A matrix for rotation around a specified axis
    public void transform(String axis, double angle) {
        switch (axis) {
            case "x":
                double xa[][] = {
                        {1, 0, 0, 0},
                        {0, Math.cos(angle), -Math.sin(angle), 0},
                        {0, Math.sin(angle), Math.cos(angle), 0},
                        {0, 0, 0, 1}
                };
                RMatrix xm = new RMatrix(xa);
                TransformMat = xm.mul(TransformMat);
                AppliedTransforms.add("x-rotate");
                break;
            case "y":
                double ya[][] = {
                        {Math.cos(angle), 0, Math.sin(angle), 0},
                        {0, 1, 0, 0},
                        {-Math.sin(angle), 0, Math.cos(angle), 0},
                        {0, 0, 0, 1}
                };
                RMatrix ym = new RMatrix(ya);
                TransformMat = ym.mul(TransformMat);
                AppliedTransforms.add("y-rotate");
                break;
            case "z":
                double za[][] = {
                        {Math.cos(angle), -Math.sin(angle), 0, 0},
                        {Math.sin(angle), Math.cos(angle), 0, 0},
                        {0, 0, 1, 0},
                        {0, 0, 0, 1}
                };
                RMatrix zm = new RMatrix(za);
                TransformMat = zm.mul(TransformMat);
                AppliedTransforms.add("z-rotate");
                break;
            default:
                AppliedTransforms.add("nothing"); // not expected
        }
        InvTransformMat = TransformMat.inv();
    }

    // A matrix for shearing
    public void transform(double xy, double xz, double yx, double yz, double zx, double zy) {
        double sa[][] = {
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

    abstract public RColor patternAt(Shape object, RTuple worldPoint);
}
