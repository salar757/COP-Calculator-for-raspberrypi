import java.util.Random;

public class TempSamples {
    Random heatPumpTemp = new Random();
    Random outsideTemp = new Random();



    public int getHeatPumpTemp(){
       return heatPumpTemp.nextInt(140) - 20;
    }

    public int getOutsideTemp(){
        return heatPumpTemp.nextInt(140) - 40;
    }
}

