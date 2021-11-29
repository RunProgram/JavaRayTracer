public class Camera {
    int hsize;
    int vsize;
    RMatrix TransformMat;
    RMatrix InvTransformMat;
    double halfWidth;
    double halfHeight;
    double pixel;

    public Camera(int hsize, int vsize, double fieldOfView){
        this.hsize = hsize;
        this.vsize = vsize;
        TransformMat = new RMatrix(4);
        InvTransformMat = TransformMat.inv();
        double halfView = Math.tan(fieldOfView/2);
        double aspect = (double)hsize/(double)vsize;
        if(aspect > 1){
            halfWidth = halfView;
            halfHeight = halfView/aspect;
        }
        else{
            halfWidth = halfView * aspect;
            halfHeight = halfView;
        }
        pixel = (halfWidth*2)/hsize;
    }
    public void setTransform(RMatrix im){
        TransformMat = im;
        InvTransformMat = im.inv();
    }
}
