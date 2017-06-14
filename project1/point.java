public class point{
    private double x;
    private double y;

    point(double xCoord, double yCoord){
	this.x = xCoord;
	this.y = yCoord;
    }

    public double getX(){
	return this.x;
    }

    public double getY(){
	return this.y;
    }

    public void printPoint(){
	System.out.print("(" + this.x + ", " + this.y + ")" + " ");
    }
}
