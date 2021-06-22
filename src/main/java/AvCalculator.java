public class AvCalculator {
    private double[] average = new double[65];
    private int theNumber = 0;
    int i = 0;

    public double[] getAverage(){

        return this.average;
    }

    public void setAverage(double newNum){

        average[i] = newNum;
        i++;

    }

    public void setNumber(){
        theNumber++;
    }

    public int getTheNumber(){

        return theNumber;
    }

    public void resetAverage(){

        i = 0 ;

    }

}
