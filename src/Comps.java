public record Comps(Shape shape, double t, RTuple point, RTuple pointOver, RTuple pointUnder, RTuple eyev, RTuple normalv, boolean inside, RTuple reflectv, double n1, double n2) {
    public Comps(Shape shape, double t, RTuple point, RTuple pointOver, RTuple pointUnder, RTuple eyev, RTuple normalv, boolean inside, RTuple reflectv, double n1, double n2){
        this.shape = shape;
        this.t = t;
        this.point = point;
        this.pointOver = pointOver;
        this.pointUnder = pointUnder;
        this.eyev = eyev;
        this.normalv = normalv;
        this.inside = inside;
        this.reflectv = reflectv;
        this.n1 = n1;
        this.n2 = n2;
    }
}
