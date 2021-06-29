import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable{

    @FXML LineChart realTimelineChart;
    @FXML LineChart setTimeLineChart;
    @FXML DatePicker startDatePicker;
    @FXML DatePicker endDatePicker;
    @FXML Button generateGraphButton;
    @FXML ChoiceBox timeIntervalChoiceBox;

    private static ScheduledExecutorService scheduledExecutorService;
    String timeInterval = "hourly";
    final int WINDOW_SIZE = 10;

    AverageCOPdata averageCOPdata = new AverageCOPdata();



    public Controller() throws IOException, InterruptedException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // this is used to display time in HH:mm:ss format
        final SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        final SimpleDateFormat fileDateFormat = new SimpleDateFormat("dd_MM_yy");
        final SimpleDateFormat weekDateFormat = new SimpleDateFormat("ww");

        AvCalculator wattAverage = new AvCalculator();
        AvCalculator COPAverage = new AvCalculator();
        WattCounter wattCounter = new WattCounter();
        try {
            wattCounter.wattCounting();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TempSensor tmpYellow = new  TempSensor("28-01205fb194d5");
        TempSensor tmpRed = new     TempSensor("28-01205fc82ac9");
        TempSensor tmpGray = new    TempSensor("28-01205fd15c16");
        TempSensor tmpRedGray = new TempSensor("28-01205fd5eb03");

        final int waterHeatCapcityPerKG = 4184;
        final int airHeatCapcityPerKG = 700;
        final int waterCapcity = 200;
        final double[] minuteCop = {0};
        final int[] prevTempRed = {tmpRed.getTemp()};

        realTimelineChart.setTitle("Realtime Graph demo");
        realTimelineChart.setAnimated(false); // disable animations

        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis(); // we are gonna plot against time
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time/s");
        xAxis.setAnimated(false); // axis animations are removed
        yAxis.setLabel("Value");
        yAxis.setAnimated(false); // axis animations are removed

        //defining a series to display data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        XYChart.Series<String, Number> setSeries = new XYChart.Series<>();
        series.setName("Data Series");
        setSeries.setName("Data Series");

        // add series to chart
        realTimelineChart.getData().add(series);
        setTimeLineChart.getData().add(setSeries);



        // setup a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println("fixed scheduled Executer just started");

            Date now = new Date();


            // multiplied by 10 and divided by 2 to get watts in 60 seconds
            // the number of watts is given multiplied by 10 to remain as an int
            // when divided by 2
            wattAverage.setAverage((wattCounter.getWatt()*10)/2);


            tmpYellow.setTemp();
            tmpYellow.setAverageTemp();


            tmpRed.setTemp();
            tmpRed.setAverageTemp();


            tmpGray.setTemp();
            tmpGray.setAverageTemp();


            tmpRedGray.setTemp();
            tmpRedGray.setAverageTemp();


            int wattJoules = ((wattCounter.getWatt()/2)* 60);
            System.out.println("watt Joules = " + wattJoules);
            double tempDiffrenceOne = (tmpRed.getTemp() -
                    prevTempRed[0]);
            double tempDiffrence = tempDiffrenceOne/10000;

            System.out.println("Temp diffrence = " + tempDiffrence);

            double tempJoules = tempDiffrence * waterHeatCapcityPerKG * waterCapcity;

            System.out.println("tempJoules = " + tempJoules);

            System.out.println(wattJoules);// 0
            System.out.println(tempJoules);// 120000

            //todo: change this bellow!

            prevTempRed[0] = tmpRed.getTemp();

            if(wattJoules == 0){
                minuteCop[0] = 0;
            }else{
                minuteCop[0] = tempJoules/wattJoules;
            }



            COPAverage.setAverage(minuteCop[0]);

            System.out.println("Minute for watts is " +
                    wattCounter.getWatt()/2);

            System.out.println("Minute for tmp yellow is " +
                    tmpYellow.getTemp());

            System.out.println("Minute for tmp red is " +
                    tmpRed.getTemp());

            System.out.println("Minute for tmp gray is " +
                    tmpGray.getTemp());

            System.out.println("Minute for tmp red gray is " +
                    tmpRedGray.getTemp());


            System.out.println("Minute for COP is " +
                    minuteCop[0]);

            wattCounter.setTotalWatts();

            System.out.println("total watts for the hour at the moment is " +
                    wattCounter.getTotalWatts());


            wattCounter.resetCounter();


            // Update the chart
            Platform.runLater(() -> {
                // get current time

                // put random number with current time
                series.getData().add(new XYChart.Data<>(simpleTimeFormat.format(now), minuteCop[0]));

                if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
            });
        }, 0, 60, TimeUnit.SECONDS);// you can edit time of intervals here



        scheduledExecutorService.scheduleAtFixedRate(() -> {


            System.out.println(1);


            Date now = new Date();

            String fileNamer = "saves/" + "(Date_" + fileDateFormat.format(now)
                    + ").txt";

            System.out.println(2);

            double saverage = 0;
            for(int i = 0; i < 60; i++){
                saverage = saverage + wattAverage.getAverage()[i];
            }

            System.out.println(3);

            double CopSaverage = 0;
            for(int i = 0; i < 60; i++){
                CopSaverage = CopSaverage + COPAverage.getAverage()[i];
            }

            System.out.println(4);

            double yellowSaverage = 0;
            for(int i = 0; i < 60; i++){
                yellowSaverage = yellowSaverage + tmpYellow.getAverageTemp()[i];
            }
            System.out.println(5);

            double redSaverage = 0;
            for(int i = 0; i < 60; i++){
                redSaverage = redSaverage + tmpRed.getAverageTemp()[i];
            }
            System.out.println(6);

            double graySaverage = 0;
            for(int i = 0; i < 60; i++){
                graySaverage = graySaverage + tmpGray.getAverageTemp()[i];
            }
            System.out.println(7);

            double redGraySaverage = 0;
            for(int i = 0; i < 60; i++){
                redGraySaverage = redGraySaverage + tmpRedGray.getAverageTemp()[i];
            }
            System.out.println(8);


            int maverage = (int)Math.round((saverage/60)*10);
            int COPMaverage = (int)Math.round((CopSaverage/60)*10);
            int yellowMaverage = (int)Math.round((yellowSaverage/60));
            int redMaverage = (int)Math.round((redSaverage/60));
            int grayMaverage = (int)Math.round((graySaverage/60));
            int redGrayMaverage = (int)Math.round((redGraySaverage/60));

            System.out.println(9);

            if ((wattCounter.getTotalWatts() / 2) >= 50){

                try {
                    FileWriter writeToFile = new FileWriter(fileNamer, true);
                    writeToFile.write("|Date: " + simpleDateFormat.format(now) + " |time:" + simpleTimeFormat.format(now) + " |COP:"
                            + String.format("%05d",COPMaverage) + " |tmp1:" + String.format("%07d",yellowMaverage) + " |tmp2:" + String.format("%07d",redMaverage) +
                            " |tmp3:" + String.format("%07d",grayMaverage) + " |tmp4:" + String.format("%07d",redGrayMaverage) +
                            " |watt:" + String.format("%011d", (wattCounter.getTotalWatts() / 2) ) + "\n");
                    writeToFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{

                System.out.println("less than 50 watts used so heat pump is off!");
            }

            System.out.println(10);

            wattAverage.resetAverage();
            COPAverage.resetAverage();
            tmpYellow.resetAverageTempCounter();
            tmpRed.resetAverageTempCounter();
            tmpGray.resetAverageTempCounter();
            tmpRedGray.resetAverageTempCounter();
            wattCounter.resetTotalWatts();

            System.out.println(11);


            }, 59, 60, TimeUnit.MINUTES);



        Date date1 = new Date();
        Date date2 = new Date();

        timeIntervalChoiceBox.getItems().add("Hourly");
        timeIntervalChoiceBox.getItems().add("Daily");
        timeIntervalChoiceBox.getItems().add("Monthly");
        timeIntervalChoiceBox.getItems().add("Yearly");


        generateGraphButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Integer random = ThreadLocalRandom.current().nextInt(10);

                Date now = new Date();

                String selection = (String) timeIntervalChoiceBox.getValue();


                if((startDatePicker.getValue() == null) && (endDatePicker.getValue() == null)) {

                    try {
                        averageCOPdata.hourlyFileReader(simpleDateFormat.format(now), simpleDateFormat.format(now), selection);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else if(startDatePicker.getValue() == null){

                    try {
                        averageCOPdata.hourlyFileReader(simpleDateFormat.format(now),
                                endDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd_MM_yy")), selection);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else if(endDatePicker.getValue() == null){

                    try {
                        averageCOPdata.hourlyFileReader(startDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd_MM_yy")),
                                simpleDateFormat.format(now), selection);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{

                    try {
                        averageCOPdata.hourlyFileReader(startDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd_MM_yy")),
                                endDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd_MM_yy")), selection);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



                Platform.runLater(() -> {
                    // get current time

                    // put random number with current time

                    setSeries.getData().clear();
                    if(averageCOPdata.getSelectionString() == "Hourly" || averageCOPdata.getSelectionString() == null){
                        for(int i = 0; i < averageCOPdata.HourlyStorageArrayList.size(); i++){
                            setSeries.getData().add(new XYChart.Data<>(averageCOPdata.getHourlyStorageTime(i),
                                    averageCOPdata.getHourlyStorageCOP(i)));
                        }
                    } else if(averageCOPdata.getSelectionString() == "Daily"){
                        for(int i = 0; i < averageCOPdata.DailyStorageArrayList.size(); i++){
                            setSeries.getData().add(new XYChart.Data<>(averageCOPdata.getDailyStorageDate(i),
                                    averageCOPdata.getDailyStorageCOP(i)));
                        }
                    } else if(averageCOPdata.getSelectionString() == "Monthly"){
                        for(int i = 0; i < averageCOPdata.MonthlyStorageArrayList.size(); i++){
                            setSeries.getData().add(new XYChart.Data<>(averageCOPdata.getMonthlyStorageDate(i),
                                    averageCOPdata.getMonthlyStorageCOP(i)));
                        }
                    } else if(averageCOPdata.getSelectionString() == "Yearly"){
                        for(int i = 0; i < averageCOPdata.YearlyStorageArrayList.size(); i++){
                            setSeries.getData().add(new XYChart.Data<>(averageCOPdata.getYearlyStorageDate(i),
                                    averageCOPdata.getYearlyStorageCOP(i)));
                        }
                    }


                });

            }
        });

        setTimeLineChart.setTitle("Average " + timeInterval +
                " COP between " + averageCOPdata.getChosenStartDate() + " & " + averageCOPdata.getChosenEndDate());
        setTimeLineChart.setAnimated(false); // disable animations

    }

    public static void shutdownNow(){

        scheduledExecutorService.shutdownNow();


    }



}

