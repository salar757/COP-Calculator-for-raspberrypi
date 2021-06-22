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

        AvCalculator average = new AvCalculator();
        WattCounter wattCounter = new WattCounter();
        try {
            wattCounter.wattCounting();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
            // get a random integer between 0-10
            Integer random = ThreadLocalRandom.current().nextInt(50);

            Date now = new Date();

            int ran = 0;

            System.out.println("Minute for watts is " +
                    wattCounter.getWatt());

            wattCounter.resetCounter();

            average.setAverage(random);



            // makes name for file data with time for the miniute


            // Update the chart
            Platform.runLater(() -> {
                // get current time

                // put random number with current time
                series.getData().add(new XYChart.Data<>(simpleTimeFormat.format(now), random));

                if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
            });
        }, 0, 60, TimeUnit.SECONDS);// you can edit time of intervals here



        scheduledExecutorService.scheduleAtFixedRate(() -> {





            Date now = new Date();

            String fileNamer = "saves/" + "(Date_" + fileDateFormat.format(now)
                    + ").txt";

            double saverage = 0;
            for(int i = 0; i < 60; i++){
                saverage = saverage + average.getAverage()[i];
            }

            int maverage = (int)Math.round((saverage/60)*10);





            try {
                FileWriter writeToFile = new FileWriter(fileNamer, true);
                writeToFile.write("|Date: " + simpleDateFormat.format(now) + " |time:" + simpleTimeFormat.format(now) + " |COP:"
                        + String.format("%05d",maverage) + " |tmp1:" + String.format("%07d",maverage) + " |tmp2:" + String.format("%07d",maverage) +
                        " |tmp3:" + String.format("%07d",maverage) + " |tmp4:" + String.format("%07d",maverage) +
                        " |watt:"+ String.format("%011d",maverage) + "\n");
                writeToFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            average.resetAverage();



            }, 59, 60, TimeUnit.MINUTES);




        // put dummy data onto graph per second






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

