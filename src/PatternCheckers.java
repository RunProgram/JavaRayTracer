public class PatternCheckers extends Pattern{
    RColor a, b;

    public PatternCheckers(RColor a, RColor b){
        super();
        this.a = a;
        this.b = b;
    }

    public RColor patternAt(Shape object, RTuple worldPoint){
        RTuple objectPoint = object.InvTransformMat.mul(worldPoint);
        RTuple patternPoint = InvTransformMat.mul(objectPoint);
        if(Math.floor(Math.floor(patternPoint.x) + Math.floor(patternPoint.y) + Math.floor(patternPoint.z)) % 2 == 0) return a;
        else return b;
    }
}
