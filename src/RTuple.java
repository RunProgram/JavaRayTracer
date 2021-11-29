public class RTuple {
    public double x, y, z, w;

    public RTuple(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void show() {
        System.out.println("(" + x + ", " + y + ", " + z + ", " + w + ")");
    }

    RTuple add(RTuple t) {
        return new RTuple(x+t.x, y+t.y, z+t.z, w+t.w);
    }

    RTuple sub(RTuple t) {
        return new RTuple(x-t.x, y-t.y, z-t.z, w-t.w);
    }

    RTuple neg() {
        return new RTuple(-x, -y, -z, w);
    }

    RTuple mul(double k){
        return new RTuple(x*k, y*k, z*k, w);
    }

    double dot(RTuple it) {
        return (x*it.x) + (y*it.y) + (z*it.z) + (w*it.w);
    }

    RTuple cross(RTuple it){
        return new RTuple(y*it.z - z*it.y, z*it.x - x*it.z, x*it.y - y*it.x, 0);
    }

    double mag(){
        return Math.sqrt((x*x) + (y*y) + (z*z));
    }

    void norm(){
        double vmag = mag();
        x /= vmag;
        y /= vmag;
        z /= vmag;
    }

    RTuple trans(RMatrix im)
    {
        RTuple tv = new RTuple(x,y,z,w);
        return im.mul(tv);
    }

    RTuple reflect(RTuple normal){
        return sub(normal.mul(2.0).mul(dot(normal)));
    }
}
