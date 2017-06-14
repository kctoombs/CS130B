import java.util.*;

public class couple{
    private ArrayList<point> points;
    private double distance;

    couple(){}

    couple(ArrayList<point> p, double d){
	this.points = new ArrayList<point>(p);
	this.distance = d;
    }

    public ArrayList<point> getList(){
	return this.points;
    }

    public double getDistance(){
	return this.distance;
    }
}
