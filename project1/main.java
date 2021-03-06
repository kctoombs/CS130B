import java.io.*;
import java.lang.*;
import java.util.*;
import java.math.*;

public class main{

    public static void main(String[] args) throws Exception{
	int argCounter = 0;
	BufferedReader br = null;
	//Holds all of the points that are input
	ArrayList<point> points = new ArrayList<point>();

	
	//Iterate through command line arguments
	for(String thisArg : args){
	    argCounter++;
	}
	
	//Exit if theres too many arguments
	if(argCounter > 1){
	    System.out.println("Too many command line arguments. Exiting.");
	    System.exit(0);
	}
	
	//Exit if theres too few arguments
	if(argCounter == 0){
	    System.out.println("Too few command line arguments. Exiting.");
	    System.exit(0);
	}

	//Exit if the argument is not the correct word (brute, basic, or optimal)
	if(!args[0].equals("brute") && !args[0].equals("basic") && !args[0].equals("optimal")){
	    System.out.println("Argument must be \'brute\', \'basic\', or \'optimal\'. Exiting. ");
	    System.exit(0);
	}

	String arg = args[0];

	try{
	    int numPoints = 0; //Counts the number of points that are input
	    br = new BufferedReader(new InputStreamReader(System.in)); //Reads from stdin
	    String thisLine = null; //Contains the line of input
	    while((thisLine = br.readLine()) != null){
		String[] numbers = thisLine.split("\\s+");
		//Check if there are exactly 2 inputs
		if(numbers.length == 2){
		    //Check that the inputs are actually numbers
		    if(isNumber(numbers[0]) && isNumber(numbers[1])){
			//System.out.println("Valid Point");
			double xCoord = Double.parseDouble(numbers[0]);
			double yCoord = Double.parseDouble(numbers[1]);
			double x = round(xCoord, 7);
			double y = round(yCoord, 7);
			
			//Create a point out of the entered numbers
			point p = new point(x, y);
			if (!exists(p, points)){
			    //Add the point to the ArrayList
			    points.add(p);
			    numPoints++;
			}
		    }
		    /*else{
			System.out.println("Invalid Point");
			}*/
		}
		/*for(int i = 0; i < numbers.length; i++){
		    System.out.println(numbers[i] + "\n");
		    }*/
		//System.out.println(thisLine);
	    }
	    if(numPoints < 2){
		System.out.println("Must provide 2 or more unique points. Exiting.");
		System.exit(0);
	    }
	} catch(IOException e){
	    e.printStackTrace();
	}

	if(arg.equals("brute")){
	    couple c = new couple();
	    c = (brute(points));
	    printPairs(c.getList(), c.getDistance());
	}

	if(arg.equals("basic")){
	    couple c = new couple();
	    c = basic(points, points.size());
	    printPairs(c.getList(), c.getDistance());
	}

	if(arg.equals("optimal")){
	    //ArrayList<point> copy = new ArrayList<point>();
	    ArrayList<point> sortedX = new ArrayList<point>();
	    //ArrayList<point> sortedY = new ArrayList<point>();
	    //copy.addAll(points);
	    sortedX = sortX(points);
	    //sortedY = sortY(points);
	    couple c = new couple();
	    c = optimal(sortedX, sortedX.size());
	    printPairs(c.getList(), c.getDistance());
	    /*for(int i = 0; i < sortedX.size(); i++){
		sortedX.get(i).printPoint();
	    }
	    for(int i = 0; i <sortedY.size();i++){
		sortedY.get(i).printPoint();
		}*/
	}

	/*for(int i = 0; i < points.size(); i++){
	    points.get(i).printPoint();
	    }*/
	
	//System.out.println("Reached return statement");
	return;
	
    } //public static void main

    

    public static couple brute(ArrayList<point> points){
	ArrayList<point> minPoints = new ArrayList<point>();
	double minDistance = Double.MAX_VALUE;
	for(int i = 0; i < points.size(); i++){
	    for(int j = i + 1; j < points.size(); j++){
		double distance = distance(points.get(i), points.get(j));
		//System.out.println("Distance: " + distance);
		if(distance < minDistance){
		    minDistance = distance;
		    minPoints.clear();
		    minPoints.add(points.get(i));
		    minPoints.add(points.get(j));
		}

		else if(distance == minDistance){
		    minPoints.add(points.get(i));
		    minPoints.add(points.get(j));
		}
	    }
	}
	/*for(int i = 0; i < minPoints.size(); i++){
	    minPoints.get(i).printPoint();
	}*/
	//printPairs(minPoints, minDistance);
	sortResults(minPoints);
	return new couple(minPoints, minDistance);
    }


    public static couple basic(ArrayList<point> points, int numPoints){
	//Sort by X-coordinate
	sortX(points);
	ArrayList<point> rightHalf = new ArrayList<point>(); //Points on left half
	ArrayList<point> leftHalf = new ArrayList<point>();  //Points on right half
	ArrayList<point> strip = new ArrayList<point>();     //Points on middle strip
	ArrayList<point> minStrip = new ArrayList<point>();  //Min points on middle strip
	ArrayList<point> minPoints = new ArrayList<point>(); //Contains min dist points
	
	if(numPoints <= 3){
	    return brute(points);
	}

	int mid;

	if(numPoints % 2 == 0){ //Even number of points
	    mid = numPoints/2;
	}
	else{ //Odd number of points
	    mid = (numPoints/2) + 1;
	}
	//Create arraylist of points on the left side
	for(int i = 0; i < mid; i++){
	    leftHalf.add(points.get(i));
	}
	//Create arraylist of points on the right side
	for(int j = mid; j < points.size(); j++){
	    rightHalf.add(points.get(j));
	}

	point midPoint = points.get(mid); //Determine the midpoint

	/*double*/ couple dl = basic(leftHalf, leftHalf.size());
	/*double*/ couple dr = basic(rightHalf, rightHalf.size());

	//Min distance between left and right halves    
	double minDistancelr = Math.min(dl.getDistance(), dr.getDistance());

	//If a point is < minDistance away from the midpoint line in
	//the X-direction, then add it to the strip
	for(int i = 0; i < points.size(); i++){
	    if(Math.abs(points.get(i).getX() - midPoint.getX()) <= minDistancelr){
		strip.add(points.get(i));
	    }
	}

	//Sort by Y-value and calculate distances in the strip
	sortY(strip);
	double stripDistance = minDistancelr;
	for(int i = 0; i < strip.size(); i++){
	    for(int j = i + 1; j < strip.size() &&
		    (strip.get(j).getY() - strip.get(i).getY() < minDistancelr); j++){
		if(distance(strip.get(i), strip.get(j)) < stripDistance){
		    stripDistance = distance(strip.get(i), strip.get(j));
		    minStrip.clear();
		    minStrip.add(strip.get(i));
		    minStrip.add(strip.get(j));
		}
		if(distance(strip.get(i), strip.get(j)) == stripDistance){
		    minStrip.add(strip.get(i));
		    minStrip.add(strip.get(j));
		}
	    }
	}

	double finalMin = Math.min(minDistancelr, stripDistance);
	if(dl.getDistance() == finalMin){
	    minPoints.addAll(dl.getList());
	}
	if(dr.getDistance() == finalMin){
	    minPoints.addAll(dr.getList());
	}
	if(stripDistance == finalMin){
	    minPoints.addAll(minStrip);
	}

	/*Set<point> uniquePoints = new HashSet<point>();
	uniquePoints.addAll(minPoints);
	minPoints.clear();
	minPoints.addAll(uniquePoints);
	*/
	
	sortResults(minPoints);
	removeDups(minPoints);
	return new couple(minPoints, finalMin);
    }

    public static couple optimal(ArrayList<point> points, int numPoints){
	ArrayList<point> rightHalf = new ArrayList<point>(); //Points on left half
	ArrayList<point> leftHalf = new ArrayList<point>();  //Points on right half
	ArrayList<point> strip = new ArrayList<point>();     //Points on middle strip
	ArrayList<point> minStrip = new ArrayList<point>();  //Min points on middle strip
	ArrayList<point> minPoints = new ArrayList<point>(); //Contains min dist points
	
	if(numPoints <= 3){
	    return brute(points);
	}
	
	int mid;
	
	if(numPoints % 2 == 0){ //Even number of points
	    mid = numPoints/2;
	}
	else{ //Odd number of points
	    mid = (numPoints/2) + 1;
	}
	//Create arraylist of points on the left side
	for(int i = 0; i < mid; i++){
	    leftHalf.add(points.get(i));
	}
	//Create arraylist of points on the right side
	for(int j = mid; j < points.size(); j++){
	    rightHalf.add(points.get(j));
	}
	
	point midPoint = points.get(mid); //Determine the midpoint
	
	/*double*/ couple dl = optimal(leftHalf, leftHalf.size());
	/*double*/ couple dr = optimal(rightHalf, rightHalf.size());

	//Min distance between left and right halves
	double minDistancelr = Math.min(dl.getDistance(), dr.getDistance());

	//If a point is < minDistance away from the midpoint line in
	//the X-direction, then add it to the strip
	for(int i = 0; i < points.size(); i++){
	    if(Math.abs(points.get(i).getX() - midPoint.getX()) <= minDistancelr){
		strip.add(points.get(i));
	    }
	}

	//Sort by Y-value and calculate distances in the strip
	sortY(strip);
	double stripDistance = minDistancelr;
	for(int i = 0; i < strip.size(); i++){
	    for(int j = i + 1; j < strip.size() &&
		    (strip.get(j).getY() - strip.get(i).getY() < minDistancelr); j++){
		if(distance(strip.get(i), strip.get(j)) < stripDistance){
		    stripDistance = distance(strip.get(i), strip.get(j));
		    minStrip.clear();
		    minStrip.add(strip.get(i));
		    minStrip.add(strip.get(j));
		}
		if(distance(strip.get(i), strip.get(j)) == stripDistance){
		    minStrip.add(strip.get(i));
		    minStrip.add(strip.get(j));
		}
	    }
	}

	double finalMin = Math.min(minDistancelr, stripDistance);
	if(dl.getDistance() == finalMin){
	    minPoints.addAll(dl.getList());
	}
	if(dr.getDistance() == finalMin){
	    minPoints.addAll(dr.getList());
	}
	if(stripDistance == finalMin){
	    minPoints.addAll(minStrip);
	}

	/*Set<point> uniquePoints = new HashSet<point>();
	uniquePoints.addAll(minPoints);
	minPoints.clear();
	minPoints.addAll(uniquePoints);
	*/
	sortResults(minPoints);
	removeDups(minPoints);
	return new couple(minPoints, finalMin);
    }
	

    public static double distance(point p1, point p2){
	double x1 = p1.getX();
	double y1 = p1.getY();
	double x2 = p2.getX();
	double y2 = p2.getY();
	double distance = round(Math.sqrt(Math.pow((x1 - x2), 2) +  Math.pow((y1 - y2), 2)), 7);
	return distance;
    }

    public static void printPairs(ArrayList<point> points, double distance){
	System.out.printf("Closest pair distance: %.7f \n", distance);
	for(int i = 0; i < points.size(); i++){
	    if((i > 0) && ((i % 2) == 0)){
		System.out.print("\n");
	    }
	    points.get(i).printPoint();
	}
	System.out.print("\n");
    }


    public static boolean isNumber(String s){
	try{
	    double d = Double.parseDouble(s);
	}catch(NumberFormatException nfe){
	    return false;
	}
	return true;
    }

    //http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    public static double round(double value, int places) {
	if (places < 0) throw new IllegalArgumentException();

	BigDecimal bd = new BigDecimal(value);
	bd = bd.setScale(places, RoundingMode.HALF_UP);
	return bd.doubleValue();
    }

    public static boolean exists(point p, ArrayList<point> points){
	for(int i = 0; i < points.size(); i++){
	    if(p.getX() == points.get(i).getX() && p.getY() == points.get(i).getY()){
		return true;
	    }
	}
	return false;
    }

    public static ArrayList<point> sortX(ArrayList<point> points){
	Collections.sort(points, new pointComparatorX());
	return points;
    }

    public static ArrayList<point> sortY(ArrayList<point> points){
	Collections.sort(points, new pointComparatorY());
	return points;
    }

    public static ArrayList<point> removeDups(ArrayList<point> points){
	ArrayList<point> noDups = new ArrayList<point>();
	if(points.size() == 4){
	    if(points.get(0).getX() == points.get(2).getX() &&
	       points.get(0).getY() == points.get(2).getY() &&
	       points.get(1).getX() == points.get(3).getX() &&
	       points.get(1).getY() == points.get(3).getY()){
		points.subList(2,4).clear();
	    }
	    noDups.addAll(points);
	    return noDups;
	}
	return points;
    }
    
    /*public static ArrayList<point> removeDups(ArrayList<point> points){
	ArrayList<point> noDups = new ArrayList<point>();
	for(int i = 0; i < points.size() - 1; i += 2){
	    for(int j = i + 1; j < points.size(); j++){
		if((points.get(i).getX() == points.get(j).getX())
		   && (points.get(i).getY() == points.get(j).getY())){
		    
		}
		else{
		    noDups.add(i);
		}
	    }
	}
	}*/
    
    public static ArrayList<point> sortResults(ArrayList<point> points){
	for(int i = 0; i < points.size() - 1; i += 2){
	    if(points.get(i).getX() > points.get(i+1).getX()){
		point tempPoint = new point(points.get(i).getX(), points.get(i).getY());
		//Swap the points
		points.set(i, points.get(i+1));
		points.set(i+1, tempPoint);
	    }
	}
	return points;
    }
	

	/*ArrayList<point> newList = new ArrayList<point>();
	//find point with min x-value
	int indexOfMin = 0;
	while(points.size() > 0){
	    double min = Double.MAX_VALUE;
	    System.out.println("Size: " + points.size());
	    for(int i = 0; i < points.size(); i += 2){
		if(points.get(i).getX() < min){
		    min = points.get(i).getX();
		    indexOfMin = i;
		}
	    }
	    newList.add(points.get(indexOfMin));
	    newList.add(points.get(indexOfMin + 1));
	    points.remove(points.get(indexOfMin));
	    points.remove(points.get(indexOfMin + 1));
	}
	return newList;*/
    
} //public class main
