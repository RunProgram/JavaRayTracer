public class Material {
    RColor color;
    double ambient;
    double diffuse;
    double specular;
    double shininess;
    double reflective;
    double transparency;
    double refractiveIndex;
    boolean isPattern;
    Pattern pattern;

    public Material(){
        color = new RColor(1, 1, 1);
        ambient = 0.1;
        diffuse = 0.9;
        specular = 0.9;
        shininess = 200;
        reflective = 0;
        transparency = 0;
        refractiveIndex = 1;
        isPattern = false;
        pattern = null;
    }
    public Material(RColor color){
        this.color = color;
        ambient = 0.1;
        diffuse = 0.9;
        specular = 0.9;
        shininess = 200;
        reflective = 0;
        transparency = 0;
        refractiveIndex = 1;
        isPattern = false;
        pattern = null;
    }
    public Material(RColor color, double ambient, double diffuse, double specular, double shininess, double reflective, double transparency, double refractiveIndex){
        this.color = color;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
        this.reflective = reflective;
        this.transparency = transparency;
        this.refractiveIndex = refractiveIndex;
        isPattern = false;
        pattern = null;
    }
    public Material(Pattern pattern){
        color = new RColor(1, 1, 1);
        ambient = 0.1;
        diffuse = 0.9;
        specular = 0.9;
        shininess = 200;
        reflective = 0;
        transparency = 0;
        refractiveIndex = 1;
        isPattern = true;
        this.pattern = pattern;
    }
    public Material(double ambient, double diffuse, double specular, double shininess, double reflective, double transparency, double refractiveIndex, Pattern pattern) {
        color = new RColor(1, 1, 1);
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
        this.reflective = reflective;
        this.transparency = transparency;
        this.refractiveIndex = refractiveIndex;
        isPattern = true;
        this.pattern = pattern;
    }
}
