public class PatternRings extends Pattern{
    RColor a, b;

    public PatternRings(RColor a, RColor b){
        super();
        this.a = a;
        this.b = b;
    }

    public RColor patternAt(Shape object, RTuple worldPoint){
        RTuple objectPoint = object.InvTransformMat.mul(worldPoint);
        RTuple patternPoint = InvTransformMat.mul(objectPoint);
        if(Math.floor(Math.sqrt(Math.pow(patternPoint.x, 2) + Math.pow(patternPoint.z, 2))) % 2 == 0) return a;
        else return b;
    }
}
