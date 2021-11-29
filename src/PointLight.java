public class PointLight {
    public RTuple position;
    public RColor intensity;

    public PointLight(){
        position = new RTuple(0, 0, 0, 1);
        intensity = new RColor(1, 1, 1);
    }

    public PointLight(RTuple position, RColor intensity){
        this.position = position;
        this.intensity = intensity;
    }
}
