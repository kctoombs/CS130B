import java.util.Comparator;

public class pointComparatorX implements Comparator{
            @Override
	    public int compare(Object o1, Object o2){
		point p1 = (point)o1;
		point p2 = (point)o2;
		int result = Double.compare((double) p1.getX(), (double) p2.getX());
		if (result == 0 ) {
		    result = Double.compare((double) p1.getY(), (double) p2.getY());
		}
		return result;
	    }
}
