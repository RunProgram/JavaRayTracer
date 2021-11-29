import java.util.*;
public class World {
    private PointLight lightSource;
    public ArrayList<Shape> allThings;

    RMatrix viewTransform;

    public World(){
        lightSource = new PointLight(new RTuple(-10, 10, -10, 1), new RColor());
        allThings = new ArrayList<Shape>();
    }

    public World(RColor lightColor){
        lightSource = new PointLight(new RTuple(-10, 10, -10, 1), lightColor);
        allThings = new ArrayList<Shape>();
    }

    public World(PointLight lightSource){
        this.lightSource = lightSource;
        allThings = new ArrayList<Shape>();
    }

    public World(PointLight lightSource, ArrayList<Shape> allThings){
        this.lightSource = lightSource;
        this.allThings = allThings;
    }

    public void addThing(Shape shape){
        allThings.add(shape);
    }

    public void clear(){
        allThings.clear();
    }

    public PointLight getLightSource(){
        return lightSource;
    }

    public void setView(RTuple from, RTuple to, RTuple up){
        RTuple forward = to.sub(from);
        forward.norm();
        up.norm();
        RTuple left = forward.cross(up);
        RTuple trueUp = left.cross(forward);
        double a[][] = {
                {left.x, left.y, left.z, 0},
                {trueUp.x, trueUp.y, trueUp.z, 0},
                {forward.neg().x, forward.neg().y, forward.neg().z, 0},
                {0, 0, 0, 1}
        };
        RMatrix orientation = new RMatrix(a);
        double b[][] = {
                {1, 0, 0, from.neg().x},
                {0, 1, 0, from.neg().y},
                {0, 0, 1, from.neg().z},
                {0, 0, 0, 1}
        };
        RMatrix translation = new RMatrix(b);
        viewTransform = orientation.mul(translation);
    }
}
