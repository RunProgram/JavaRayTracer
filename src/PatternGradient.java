public class PatternGradient extends Pattern{
    RColor a, b, distance;

    public PatternGradient(RColor a, RColor b){
        super();
        this.a = a;
        this.b = b;
        distance = b.subNB(a);
    }

    public RColor patternAt(Shape object, RTuple worldPoint){
        RTuple objectPoint = object.InvTransformMat.mul(worldPoint);
        RTuple patternPoint = InvTransformMat.mul(objectPoint);
        return a.add(distance.mulNB(patternPoint.x - Math.floor(patternPoint.x)));
    }
}
