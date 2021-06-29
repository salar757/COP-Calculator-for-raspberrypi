import java.io.*;
import java.util.Scanner;

public class TempSensor {

    private int Temp = 0;
    private String id;
    private int i = 0;
    private double[] average = new double[65];

    public TempSensor(String id){

        this.id = id;

    }

    public void setTemp(){

        try {
            File mainTempFile = new File("/sys/bus/w1/devices/" + id + "/w1_slave");
            Scanner scanLine = new Scanner(mainTempFile);
            String lineStore = scanLine.nextLine();
            lineStore = scanLine.nextLine();

            String newLineStore = lineStore.substring(29);


            this.Temp = Integer.parseInt(newLineStore)* 10;
            System.out.println( this.Temp);


        } catch (FileNotFoundException s) {
            System.out.println("" + "w1_slave" + " does not exist");
        }

    }

    public int getTemp(){
        return this.Temp;
    }

    public void setAverageTemp(){

        this.average[i] = this.Temp;
        this.i++;

    }

    public double[] getAverageTemp(){

        return this.average;
    }

    public void resetAverageTempCounter(){

        i = 0;
    }

    public boolean getFalse(){
        return false;
    }


}
