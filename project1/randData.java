//Random point generator
import java.util.Random;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.math.*;

public class randData{
    public static void main(String[] args){
       
	int argCounter = 0;
	int min = -10000;
	int max = 10000;
	
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

	int n = Integer.parseInt(args[0]);
	for(int i = 0; i < n; i++){
	    double xCoord;
	    double yCoord;
	    Random r = new Random();
	    xCoord = round((min + (max - min) * r.nextDouble()), 7);
	    yCoord = round((min + (max - min) * r.nextDouble()), 7);
	    System.out.println(xCoord + " " + yCoord);
	}
    }

    //http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    public static double round(double value, int places) {
	if (places < 0) throw new IllegalArgumentException();

	BigDecimal bd = new BigDecimal(value);
	bd = bd.setScale(places, RoundingMode.HALF_UP);
	return bd.doubleValue();
    }
    
    
}
