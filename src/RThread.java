public class RThread implements Runnable{
    World w;
    Ray r;
    RColor color;

    public RThread(Ray r, World w){
        this.w = w;
        this.r = r;
    }

    public RColor getC(){
        return color;
    }

    public void run(){
        int remaining = 4;
        color = RT.colorAt(w, r, remaining);
    }
}
