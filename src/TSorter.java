import java.util.Comparator;

public class TSorter implements Comparator<Intersection> {
    @Override
    public int compare(Intersection o1, Intersection o2){
        return Double.compare(o1.t(), o2.t());
    }
}
