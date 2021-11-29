public class PatternStripes extends Pattern{
    RColor a, b;

    public PatternStripes(RColor a, RColor b){
        super();
        this.a = a;
        this.b = b;
    }

    public RColor patternAt(Shape object, RTuple worldPoint){
        RTuple objectPoint = object.InvTransformMat.mul(worldPoint);
        RTuple patternPoint = InvTransformMat.mul(objectPoint);
        if(Math.floor(patternPoint.x) % 2 == 0) return a;
        else return b;
    }
}
