import java.io.Serializable;
public class RColor implements Serializable{
    public double r, g, b;

    public RColor(){
        r = 1;
        g = 1;
        b = 1;
    }
    public RColor(double r, double g, double b){
        this.r = r;
        this.g = g;
        this.b = b;
    }
    void show(){
        System.out.println("(" + r + ", " + g + ", " + b + ")");
    }
    public RColor add(RColor c){
        double red = r + c.r;
        double green = g + c.g;
        double blue = b + c.b;
        if(red < 0) red = 0;
        if(red > 1) red = 1;
        if(green < 0) green = 0;
        if(green > 1) green = 1;
        if(blue < 0) blue = 0;
        if(blue > 1) blue = 1;
        return new RColor(red, green, blue);
    }
    public RColor sub(RColor c){
        double red = r - c.r;
        double green = g - c.g;
        double blue = b - c.b;
        if(red < 0) red = 0;
        if(red > 1) red = 1;
        if(green < 0) green = 0;
        if(green > 1) green = 1;
        if(blue < 0) blue = 0;
        if(blue > 1) blue = 1;
        return new RColor(red, green, blue);
    }

    public RColor subNB(RColor c){
        return new RColor(r - c.r, g - c.g, b - c.b);
    }

    public RColor mul(double k){
        double red = r * k;
        double green = g * k;
        double blue = b * k;
        if(red < 0) red = 0;
        if(red > 1) red = 1;
        if(green < 0) green = 0;
        if(green > 1) green = 1;
        if(blue < 0) blue = 0;
        if(blue > 1) blue = 1;
        return new RColor(red, green, blue);
    }

    public RColor mulNB(double k){
        return new RColor(k*r, k*g, k*b);
    }

    public RColor mul(RColor c){
        return new RColor(r * c.r, g * c.g, b * c.b);
    }
}
