import java.util.ArrayList;
import java.util.Collections;

public class Group extends Shape{
    public ArrayList<Shape> shapeList;
    Bounds groupBounds;
    boolean isBounded;

    public Group(){
        super();
        shapeList = new ArrayList<>();
        groupBounds = null;
        isBounded = false;
    }

    void addPart(Shape shape) {
        shapeList.add(shape);
        shape.setPartOf(this);
        // Adjust the group bounding box
        Bounds shapeBounds = RT.bounds(shape);
        RTuple stMin = shapeBounds.pmin();
        RTuple stMax = shapeBounds.pmax();

        ArrayList<RTuple> corners = new ArrayList<>();
        corners.add(new RTuple(stMin.x, stMin.y, stMin.z, 1));
        corners.add(new RTuple(stMax.x, stMin.y, stMin.z, 1));
        corners.add(new RTuple(stMin.x, stMax.y, stMin.z, 1));
        corners.add(new RTuple(stMax.x, stMax.y, stMin.z, 1));
        corners.add(new RTuple(stMin.x, stMin.y, stMax.z, 1));
        corners.add(new RTuple(stMax.x, stMin.y, stMax.z, 1));
        corners.add(new RTuple(stMin.x, stMax.y, stMax.z, 1));
        corners.add(new RTuple(stMax.x, stMax.y, stMax.z, 1));

        RTuple cornersTransform;
        ArrayList<Double> xTransform = new ArrayList<>();
        ArrayList<Double> yTransform = new ArrayList<>();
        ArrayList<Double> zTransform = new ArrayList<>();
        for (RTuple c : corners) {
            cornersTransform = shape.TransformMat.mul(c);
            xTransform.add(cornersTransform.x);
            yTransform.add(cornersTransform.y);
            zTransform.add(cornersTransform.z);
        }
        double trMinX = Collections.min(xTransform);
        double trMinY = Collections.min(yTransform);
        double trMinZ = Collections.min(zTransform);
        double trMaxX = Collections.max(xTransform);
        double trMaxY = Collections.max(yTransform);
        double trMaxZ = Collections.max(zTransform);

        if (shapeList.size() == 1) {
            groupBounds = new Bounds(new RTuple(trMinX, trMinY, trMinZ, 1), new RTuple(trMaxX, trMaxY, trMaxZ, 1));
            isBounded = Math.abs(groupBounds.pmin().x) != RT.INFINITY && Math.abs(groupBounds.pmin().y) != RT.INFINITY && Math.abs(groupBounds.pmin().z) != RT.INFINITY &&
                    Math.abs(groupBounds.pmax().x) != RT.INFINITY && Math.abs(groupBounds.pmax().y) != RT.INFINITY && Math.abs(groupBounds.pmax().z) != RT.INFINITY;
        } else {
            double groupMinX = groupBounds.pmin().x;
            double groupMinY = groupBounds.pmin().y;
            double groupMinZ = groupBounds.pmin().z;
            double groupMaxX = groupBounds.pmax().x;
            double groupMaxY = groupBounds.pmax().y;
            double groupMaxZ = groupBounds.pmax().z;

            if (trMinX < groupMinX) groupMinX = trMinX;
            if (trMinY < groupMinY) groupMinY = trMinY;
            if (trMinZ < groupMinZ) groupMinZ = trMinZ;
            if (trMaxX > groupMaxX) groupMaxX = trMaxX;
            if (trMaxY > groupMaxY) groupMaxY = trMaxY;
            if (trMaxZ > groupMaxZ) groupMaxZ = trMaxZ;

            groupBounds = new Bounds(new RTuple(groupMinX, groupMinY, groupMinZ, 1), new RTuple(groupMaxX, groupMaxY, groupMaxZ, 1));
            isBounded = Math.abs(groupBounds.pmin().x) != RT.INFINITY && Math.abs(groupBounds.pmin().y) != RT.INFINITY && Math.abs(groupBounds.pmin().z) != RT.INFINITY &&
                    Math.abs(groupBounds.pmax().x) != RT.INFINITY && Math.abs(groupBounds.pmax().y) != RT.INFINITY && Math.abs(groupBounds.pmax().z) != RT.INFINITY;
        }
    }

    void clearAll(){
        shapeList.clear();
        groupBounds = null;
        isBounded = false;
    }

    public RTuple localNormalAt(RTuple objectPoint){
        System.out.println("Illegal call of Group.localNormalAt()!");
        return null;
    }

    public ArrayList<Intersection> localIntersect(Ray rayTransform) {
        ArrayList<Intersection> shapeIntersections = new ArrayList<>();
        ArrayList<Intersection> oneShapeIntersection;
        if (shapeList.size() > 0) {
            if (isBounded) {
                Cube borderCube = new Cube();
                ArrayList<Intersection> boundingBoxIntersections = borderCube.localIntersect(rayTransform, groupBounds);
                if (boundingBoxIntersections.size() > 0) {
                    for (Shape shape : shapeList) {
                        oneShapeIntersection = shape.intersect(rayTransform);
                        if (oneShapeIntersection.size() > 0) shapeIntersections.addAll(oneShapeIntersection);
                    }
                }
            } else {
                for (Shape shape : shapeList) {
                    oneShapeIntersection = shape.intersect(rayTransform);
                    if (oneShapeIntersection.size() > 0) shapeIntersections.addAll(oneShapeIntersection);
                }
            }
            if (shapeIntersections.size() > 0) shapeIntersections.sort(new TSorter());
        }
        return shapeIntersections;
    }
}
