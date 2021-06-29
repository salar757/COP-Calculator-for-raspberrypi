import com.diozero.devices.Button;
import com.diozero.api.GpioPullUpDown;

public class WattCounter {

    public static theSecondCounter counter = new theSecondCounter();


    public static class MyRunnable implements Runnable{


        @Override
        public void run(){

            try (Button button = new Button(17, GpioPullUpDown.PULL_UP)) {
                button.addListener(event -> counter.setCounter());// two pulses equal 1 watt

                // make the program run forever until stoped
                // with CTRL c
                while (true){
                    Thread.sleep(500);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void wattCounting() throws InterruptedException {
        Thread thread = new Thread( new MyRunnable());
        thread.start();

    }

    public void setTotalWatts() {
        counter.addNumber(counter.getCounter());
    }

    public int getTotalWatts() {
        return counter.getNumber();
    }

    public void resetTotalWatts(){
        counter.resetNumber();
    }


    public void resetCounter(){
        counter.resetCounter();
    }

    public int getWatt(){
        return counter.getCounter();
    }
}