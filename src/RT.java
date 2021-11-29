import java.util.ArrayList;
import java.util.Collections;

public class RT {
    public final static double EPSILON = 1E-10;
    public final static double INFINITY = 1E+10;
    static Intersection hit(ArrayList<Intersection> intersectionList)
    {
        int i = 0;
        if(intersectionList.size() == 0) return null;
        int maxIndex = intersectionList.size() - 1;
        while(i < maxIndex && intersectionList.get(i).t() < EPSILON){
            i++;
        }
        Intersection hitIntersection = intersectionList.get(i);
        if(i == maxIndex && hitIntersection.t() < EPSILON){
            return null;
        }
        return hitIntersection;
    }
    static boolean isShadowed(World world, RTuple point){
        RTuple v = world.getLightSource().position.sub(point);
        double distance = v.mag();
        RTuple direction = v;
        direction.norm();
        Ray r = new Ray(point, direction);
        ArrayList<Intersection> intersections = r.intersect(world);
        Intersection h = hit(intersections);
        if(h != null && h.t() < distance) return true;
        return false;
    }
    static RColor lighting(Shape object, PointLight pointLight, RTuple point, RTuple eyeVector, RTuple normalVector, boolean inShadow){

        Material material = object.material;
        RColor color;
        if(material.isPattern) color = material.pattern.patternAt(object, point);
        else color = material.color;

        RColor effectiveColor = color.mul(pointLight.intensity);
        RTuple lightVector = pointLight.position.sub(point);
        lightVector.norm();
        RColor ambient = effectiveColor.mul(material.ambient);

        double lightDotNormal = lightVector.dot(normalVector);
        RColor diffuse, specular;

        if(inShadow || lightDotNormal < 0){
            diffuse = new RColor(0, 0, 0);
            specular = new RColor(0, 0, 0);
        }
        else{
            diffuse = effectiveColor.mul(material.diffuse).mul(lightDotNormal);
            RTuple reflectVector = lightVector.neg().reflect(normalVector);
            double reflectDotEye = reflectVector.dot(eyeVector);
            if(reflectDotEye <= 0){
                specular = new RColor(0, 0, 0);
            }
            else{
                double factor = Math.pow(reflectDotEye, material.shininess);
                specular = pointLight.intensity.mul(material.specular).mul(factor);
            }
        }
        return ambient.add(diffuse).add(specular);
    }
    static Comps prepareComps(Intersection i, Ray ray, ArrayList<Intersection> xs){
        boolean inside;
        RTuple pointAtHit = ray.findEnd(i.t());
        RTuple eyeDirection = ray.direction.neg();
        RTuple normalAtHit =  i.shape().normalAt(pointAtHit);
        if(normalAtHit.dot(eyeDirection) < 0){
            inside = true;
            normalAtHit = normalAtHit.neg();
        }
        else{
            inside = false;
        }
        RTuple pointAtHitOver = pointAtHit.add(normalAtHit.mul(EPSILON));
        RTuple pointAtHitUnder = pointAtHit.sub(normalAtHit.mul(EPSILON));

        double n1 = 1, n2 = 1;
        boolean alreadyIntersected;
        int j;
        ArrayList<Intersection> containers = new ArrayList<>();
        for(Intersection currentInt : xs){
            if(containers.size() == 0) n1 = 1;
            else n1 = containers.get(containers.size() - 1).shape().material.refractiveIndex;

            alreadyIntersected = false;
            for(j = 0; j < containers.size(); j++){
                if(containers.get(j).shape().equals(currentInt.shape())){
                    alreadyIntersected = true;
                    break;
                }
            }
            if(alreadyIntersected) containers.remove(j);
            else containers.add(currentInt);
            if(containers.size() == 0) n2 = 1;
            else n2 = containers.get(containers.size() - 1).shape().material.refractiveIndex;
            if(currentInt.equals(i)) break;
        }

        return new Comps(i.shape(), i.t(), pointAtHit, pointAtHitOver, pointAtHitUnder, eyeDirection, normalAtHit, inside, ray.direction.reflect(normalAtHit), n1, n2);
    }
    static RColor shadeHit(World world, Comps comps, int remaining){
        boolean shadowed = isShadowed(world, comps.pointOver());
        RColor surface = lighting(comps.shape(), world.getLightSource(), comps.pointOver(), comps.eyev(), comps.normalv(), shadowed);
        RColor reflected = RT.reflectedColor(world, comps, remaining);
        RColor refracted = RT.refractedColor(world, comps, remaining);
        Material material = comps.shape().material;
        if(material.reflective > 0 && material.transparency > 0){
            double reflectance = schlick(comps);
            return surface.add(reflected.mul(reflectance)).add(refracted.mul(1 - reflectance));
        }
        return surface.add(reflected).add(refracted);
    }
    static RColor colorAt(World world, Ray ray, int remaining){
        ArrayList<Intersection> ai = ray.intersect(world);
        if(ai.size() == 0) return new RColor(0, 0, 0);
        Intersection i = hit(ai);
        if(i == null) return new RColor(0, 0, 0);
        Comps comps = prepareComps(hit(ai), ray, ai);
        return shadeHit(world, comps, remaining);
    }

    static Ray rayForPixel(Camera cam, int px, int py){
        double xoffset = (px + 0.5)*cam.pixel;
        double yoffset = (py+0.5)*cam.pixel;
        double worldx = cam.halfWidth - xoffset;
        double worldy = cam.halfHeight - yoffset;
        RTuple pix = cam.InvTransformMat.mul(new RTuple(worldx, worldy, -1, 1));
        RTuple origin = cam.InvTransformMat.mul(new RTuple(0, 0, 0, 1));
        RTuple direction = pix.sub(origin);
        direction.norm();
        return new Ray(origin, direction);
    }

    static RColor reflectedColor(World world, Comps comps, int remaining){
        if(remaining == 0) return new RColor(0, 0, 0);
        if(comps.shape().material.reflective == 0) return new RColor(0, 0, 0);
        Ray reflectRay = new Ray(comps.pointOver(), comps.reflectv());
        RColor color = RT.colorAt(world, reflectRay, remaining - 1);
        return color.mul(comps.shape().material.reflective);
    }

    static RColor refractedColor(World world, Comps comps, int remaining){
        if(comps.shape().material.transparency == 0 || remaining == 0) return new RColor(0, 0, 0);
        double nRatio = comps.n1()/comps.n2();
        double cosi = comps.eyev().dot(comps.normalv());
        double sin2t = Math.pow(nRatio, 2) * (1 - Math.pow(cosi, 2));
        if(sin2t > 1) return new RColor(0, 0, 0);
        double cost = Math.sqrt(1 - sin2t);
        RTuple direction = comps.normalv().mul(nRatio * cosi - cost).sub(comps.eyev().mul(nRatio));
        Ray refractRay = new Ray(comps.pointUnder(), direction);
        return colorAt(world, refractRay, remaining - 1).mul(comps.shape().material.transparency);
    }
    private static double schlick(Comps comps){
        double cosen = comps.eyev().dot(comps.normalv());
        if(comps.n1() > comps.n2()){
            double n = comps.n1()/comps.n2();
            double sin2t = Math.pow(n, 2) * (1 - Math.pow(cosen, 2));
            if(sin2t > 1) return 1;
            cosen = Math.sqrt(1 - sin2t);
        }
        double r0 = Math.pow((comps.n1() - comps.n2())/(comps.n1() + comps.n2()), 2);
        return r0 + (1 - r0) * Math.pow(1 - cosen, 5);
    }

    static public RTuple worldToObject(Shape shape, RTuple point){
        RTuple currentPoint = point;
        if(shape.getPartOf() != null) currentPoint = worldToObject(shape.getPartOf(), currentPoint);
        return shape.InvTransformMat.mul(currentPoint);
    }

    static public RTuple normalToWorld(Shape shape, RTuple normal){
        RTuple currentNormal = shape.InvTransformMat.transpose().mul(normal);
        currentNormal.w = 0;
        currentNormal.norm();
        if(shape.getPartOf() != null) currentNormal = normalToWorld(shape.getPartOf(), currentNormal);
        return currentNormal;
    }

    static public Bounds bounds(Shape shape) {
        double min, max;
        RTuple pmin, pmax;
        if (shape instanceof Sphere) return new Bounds(new RTuple(-1, -1, -1, 1), new RTuple(1, 1, 1, 1));
        if (shape instanceof Plane) return new Bounds(new RTuple(-INFINITY, 0, -INFINITY, 1), new RTuple(INFINITY, 0, INFINITY, 1));
        if (shape instanceof Cube) return new Bounds(new RTuple(-1, -1, -1, 1), new RTuple(1, 1, 1, 1));

        if (shape instanceof Cylinder cylinder) {
            min = cylinder.getMin();
            max = cylinder.getMax();
            if (min > -INFINITY) pmin = new RTuple(-1, min, -1, 1);
            else pmin = new RTuple(-1, -INFINITY, -1, 1);
            if (max < INFINITY) pmax = new RTuple(1, max, 1, 1);
            else pmax = new RTuple(1, INFINITY, 1, 1);
            return new Bounds(pmin, pmax);
        }
        if (shape instanceof Cone cone) {
            min = cone.getMin();
            max = cone.getMax();
            if (min > -INFINITY) pmin = new RTuple(-1, min, -1, 1);
            else pmin = new RTuple(-1, -INFINITY, -1, 1);
            if (max < INFINITY) pmax = new RTuple(1, max, 1, 1);
            else pmax = new RTuple(1, INFINITY, 1, 1);
            return new Bounds(pmin, pmax);
        }
        if (shape instanceof Triangle triangle) {
            ArrayList<Double> allX = new ArrayList<>();
            ArrayList<Double> allY = new ArrayList<>();
            ArrayList<Double> allZ = new ArrayList<>();
            allX.add(triangle.p1.x);
            allX.add(triangle.p2.x);
            allX.add(triangle.p3.x);
            allY.add(triangle.p1.y);
            allY.add(triangle.p2.y);
            allY.add(triangle.p3.y);
            allZ.add(triangle.p1.z);
            allZ.add(triangle.p2.z);
            allZ.add(triangle.p3.z);
            double minX = Collections.min(allX);
            double minY = Collections.min(allY);
            double minZ = Collections.min(allZ);
            double maxX = Collections.max(allX);
            double maxY = Collections.max(allY);
            double maxZ = Collections.max(allZ);
            return new Bounds(new RTuple(minX, minY, minZ, 1), new RTuple(maxX, maxY, maxZ, 1));
        }
        if (shape instanceof Group group) {
            return group.groupBounds;
        }
        return null;
    }

}