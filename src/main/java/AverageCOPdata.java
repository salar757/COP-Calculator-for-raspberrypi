import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AverageCOPdata {

    //this class will cycle trhough all the stored data and
    //fish out the diffrent inteverals of data to help generate
    //the graph.

    ArrayList<String> dateList = new ArrayList<String>();
    ArrayList<String> fileContentsList = new ArrayList();
    ArrayList<String> averageList = new ArrayList();
    ArrayList<String> DailizerList = new ArrayList();


    ArrayList<hourlyStorage>  HourlyStorageArrayList = new ArrayList();
    ArrayList<dailyStorage> DailyStorageArrayList = new ArrayList();
    ArrayList<monthlyStorage> MonthlyStorageArrayList = new ArrayList();
    ArrayList<yearlyStorage> YearlyStorageArrayList = new ArrayList();




    String fileContents;
    String filePath;

    int ddMax = 31;
    int mmMax = 12;

    //store for the day of the month
    int startDD;
    int endDD;
    //store for the month of the year
    int startMM;
    int endMM;
    //store for the year
    int startYY;
    int endYY;
    String selection;
    int dd;
    int mm;
    int yy;

    int DD;
    int MM;
    int YY;



    public AverageCOPdata() throws FileNotFoundException {
    }

    // meathod below will convert
    public void hourlyFileReader(String startDate, String endDate, String selection) throws IOException {

        this.dateList.clear();
        this.fileContentsList.clear();

        this.HourlyStorageArrayList.clear();
        this.DailyStorageArrayList.clear();
        this.MonthlyStorageArrayList.clear();
        this.YearlyStorageArrayList.clear();


        String staDD = startDate.charAt(0) + Character.toString(startDate.charAt(1));
        String staMM = startDate.charAt(3) + Character.toString(startDate.charAt(4));
        String staYY = startDate.charAt(6) + Character.toString(startDate.charAt(7));

        String enDD = endDate.charAt(0) + Character.toString(endDate.charAt(1));
        String enMM = endDate.charAt(3) + Character.toString(endDate.charAt(4));
        String enYY = endDate.charAt(6) + Character.toString(endDate.charAt(7));

        this.startDD = Integer.parseInt(staDD);
        this.startMM = Integer.parseInt(staMM);
        this.startYY = Integer.parseInt(staYY);

        this.endDD = Integer.parseInt(enDD);
        this.endMM = Integer.parseInt(enMM);
        this.endYY = Integer.parseInt(enYY);

        dd = startDD;
        mm = startMM;
        yy = startYY;

        DD = endDD;
        MM = endMM;
        YY = endYY;

        //this is for the chosen interval for graphs
        this.selection = selection;

        //moves onto the date sorting alogrithm
        dateSortingAlgo();

    }


    // the dateSortingAlgo will generate dates between the two chosen
    // dates to be used to check the files.
    private void dateSortingAlgo() throws IOException {

        if( startYY == endYY){
            if(startMM == endMM){
                if(startDD == endDD){
                    System.out.println("you have chosen a specific day");
                }else if(startDD < endDD){
                    System.out.println("you have chosen two dates within a month");
                }else{
                    System.out.println("switchero time");
                    switchDates();// used a switch to make sure that
                    // the early dates are chosen first regardless of order
                }
            }else if(startMM < endMM){
                System.out.println("you have chosen two dates within a year");
            }else{
                System.out.println("switchero time");
                switchDates();
            }

        }else if(startYY < endYY){
            if(startMM == endMM){
                if(startDD == endDD){
                    System.out.println("you have chosen the same day and month in diffrent years");
                }else if(startDD < endDD){
                    System.out.println("you have chosen the same month in diffrent years");
                }else{
                    //might not need this for the algorithm
                    System.out.println("you have chosen a date in which less date and same month but bigger year");
                }
            }else if(startMM < endMM){
                System.out.println("you have chosen month and year less than end5020");
            }else{
                System.out.println("you have chosen a date in which less date/month but bigger year");
            }
        }else{
            System.out.println("you have chosen a year which is more than end year");
            System.out.println("switchero time");
            switchDates();// used to swithc dates such that the
            //early dates are chosen first.
        } storeContentsIntoList();


    }

    private void storeContentsIntoList() throws IOException {

        while (!((startDD == endDD) && (startMM == endMM) && (startYY == endYY))) {

            if(startDD > 31){
                startDD = 1;
                startMM ++;
            }else if(startMM > 12){
                startMM = 1;
                startYY ++;
            }else if(startYY > 100){
                startDD = 1;
                startMM = 1;
                startYY = 1;
            }
            //stores dates in an arraylist
            dateList.add("(Date_"  + String.format("%02d",startDD)
                    + "_" + String.format("%02d",startMM) + "_" + String.format("%02d",startYY) + ")" );

            startDD++;
        }dateList.add("(Date_"  + String.format("%02d",startDD)
                + "_" + String.format("%02d",startMM) + "_" + String.format("%02d",startYY) + ")");

        //calling the printer for testing purposes only
        fileCanOpener();
    }


    /*
    private void storeFileContentToList() throws IOException {


        File mainDataFile = new File("saves/MainDataFile.txt");
        BufferedReader lineCounter = new BufferedReader(new FileReader(mainDataFile));
        int numberOfLines = 0;
        while (lineCounter.readLine() != null) numberOfLines++;
        lineCounter.close();



        for(int i = 0; i < dateList.size(); i++){

            Scanner scanLine = new Scanner(mainDataFile);
            String lineStore = scanLine.nextLine();

            for(int r = 0; r < numberOfLines; r++){
                if(lineStore.contains(dateList.get(i))){
                    System.out.println(lineStore);
                }else{

                }if(scanLine.hasNext()){
                     lineStore = scanLine.nextLine();
                }
            }

        }
        System.out.println("number of lines is " + numberOfLines);

        printDateList();
    }*/

    private void fileCanOpener() throws IOException {

        // weird enchanced for loop bellow never used them before
        for (String value : dateList) {


            try {
                File mainDataFile = new File("saves/" + value + ".txt");
                BufferedReader lineCounter = new BufferedReader(new FileReader(mainDataFile));
                Scanner scanLine = new Scanner(mainDataFile);
                String lineStore = scanLine.nextLine();


                String lineStoreAverage = "null";
                int numberOfLines = 0;
                while (lineCounter.readLine() != null) numberOfLines++;
                lineCounter.close();

                if(selection == "Hourly" || selection == null){
                    for (int r = 0; r < numberOfLines; r++) {
                        fileContentsList.add(lineStore);
                        if (scanLine.hasNext()) {
                            lineStore = scanLine.nextLine();
                        }
                    }
                }else{
                    fileContentsList.add(averageIzer1(lineStore, lineStoreAverage, numberOfLines, scanLine));
                }



            } catch (FileNotFoundException s) {
                System.out.println("" + value + ".txt does not exist");
            }


        }

        //below orginal for loop before changed to enhanced (use for references only)
        /*
        for(int i = 0; i < dateList.size(); i++) {



            try{
                File mainDataFile = new File("saves/" + dateList.get(i) + ".txt");
                BufferedReader lineCounter = new BufferedReader(new FileReader(mainDataFile));
                Scanner scanLine = new Scanner(mainDataFile);
                String lineStore = scanLine.nextLine();
                int numberOfLines = 0;
                while (lineCounter.readLine() != null) numberOfLines++;
                lineCounter.close();

                for(int r = 0; r < numberOfLines; r++) {
                    System.out.println(lineStore);
                    if (scanLine.hasNext()) {
                        lineStore = scanLine.nextLine();
                    }
                }



            }catch(FileNotFoundException s){
                System.out.println("" + dateList.get(i) + ".txt does not exist");
            }



        }*/

        if(this.selection == "Hourly" || this.selection == null){
            setHourlyStorageArrayList();
            for(int i = 0; i < fileContentsList.size(); i++){
                System.out.println(fileContentsList.get(i));
            }
        }else if(this.selection == "Daily"){
            setDailyStorageArrayList();
            for(int i = 0; i < fileContentsList.size(); i++){
                System.out.println(fileContentsList.get(i));
            }
        }else if(this.selection == "Monthly"){
            averageIzer2();
            for(int i = 0; i < fileContentsList.size(); i++){
                System.out.println(fileContentsList.get(i));
            }
            setMonthlyStorageArrayList();
        }else if(this.selection == "Yearly"){
            averageIzer2();
            averageIzer3();
            setYearlyStorageArrayList();
        }



    }

    private String averageIzer1(String lineStore, String lineStoreAverage, int numberOfLines, Scanner scanLine){
        for (int r = 0; r < numberOfLines; r++) {
            averageList.add(lineStore);
            if (scanLine.hasNext()) {
                lineStore = scanLine.nextLine();
            }
        }

        String date = Character.toString(averageList.get(0).charAt(7)) +
                averageList.get(0).charAt(8) +  averageList.get(0).charAt(9) +
                averageList.get(0).charAt(10) + averageList.get(0).charAt(11) +
                averageList.get(0).charAt(12) + averageList.get(0).charAt(13) +
                averageList.get(0).charAt(14) + averageList.get(0).charAt(15);

        double sum = 0;
        for( int j = 0; j < averageList.size(); j++){


            String StoreDouble = Character.toString(averageList.get(j).charAt(33)) +
                    averageList.get(j).charAt(34) +  averageList.get(j).charAt(35) +
                    averageList.get(j).charAt(36) + "." + averageList.get(j).charAt(37);

            Double avCopValue = Double.parseDouble(StoreDouble);

            sum = sum + avCopValue;

        }

        int av = (int)Math.round((sum/averageList.size())*10);

        lineStoreAverage = "|Date: "+ date + "|time:00:05 |COP:" + String.format("%05d",av);
        System.out.println(lineStoreAverage);
        averageList.clear();

        String theReturner = lineStoreAverage;

        return theReturner;
    }

    private void averageIzer2(){

        String date;
        String prevDate = "null";
        Double sum = 0.0;
        Double firstVal = 0.0;
        String lineStoreAverage = "null";
        int sumCounter = 1;
        for(int i = 0; i < fileContentsList.size(); i++){

            date = Character.toString(fileContentsList.get(i).charAt(10)) +
                    fileContentsList.get(i).charAt(11) +  fileContentsList.get(i).charAt(12) +
                    fileContentsList.get(i).charAt(13) + fileContentsList.get(i).charAt(14) +
                    fileContentsList.get(i).charAt(15);
            if(date.equals(prevDate)){

                String StoreDouble = Character.toString(fileContentsList.get(i).charAt(33)) +
                        fileContentsList.get(i).charAt(34) +  fileContentsList.get(i).charAt(35) +
                        fileContentsList.get(i).charAt(36) + "." + fileContentsList.get(i).charAt(37);

                Double avCopValue = Double.parseDouble(StoreDouble);

                sum = sum + avCopValue;
                sumCounter ++;


                int av = (int)Math.round((sum/sumCounter)*10);

                lineStoreAverage = "|Date: xx/"+ prevDate + "|time:00:00 |COP:" + String.format("%05d",av);

            }else{

                String StoreDouble = Character.toString(fileContentsList.get(i).charAt(33)) +
                        fileContentsList.get(i).charAt(34) +  fileContentsList.get(i).charAt(35) +
                        fileContentsList.get(i).charAt(36) + "." + fileContentsList.get(i).charAt(37);

                Double avCopValue = Double.parseDouble(StoreDouble);



                if(i == 0){
                    prevDate = date;
                    sum = avCopValue;
                    if(fileContentsList.size() == 1){
                        int av = (int)Math.round((firstVal)*10);
                        lineStoreAverage = "|Date: xx/"+ prevDate + "|time:00:00 |COP:" + String.format("%05d",av);
                    }
                }else{
                    int av = (int)Math.round((sum/sumCounter)*10);

                    lineStoreAverage = "|Date: xx/"+ prevDate + "|time:00:00 |COP:" + String.format("%05d",av);
                    DailizerList.add(lineStoreAverage);
                    prevDate = date;

                    sum = avCopValue;
                }

                sumCounter = 1;
            }
        }DailizerList.add(lineStoreAverage);
        fileContentsList.clear();

        fileContentsList.addAll(DailizerList);


        DailizerList.clear();

    }

    private void averageIzer3(){

        String date;
        String prevDate = "null";
        Double sum = 0.0;
        Double firstVal = 0.0;
        String lineStoreAverage = "null";
        int sumCounter = 1;
        for(int i = 0; i < fileContentsList.size(); i++){
            System.out.println(fileContentsList.get(i));
        }

        for(int i = 0; i < fileContentsList.size(); i++){

            date = Character.toString(fileContentsList.get(i).charAt(13)) +
                    fileContentsList.get(i).charAt(14) +  fileContentsList.get(i).charAt(15);
            if(date.equals(prevDate)){

                String StoreDouble = Character.toString(fileContentsList.get(i).charAt(33)) +
                        fileContentsList.get(i).charAt(34) +  fileContentsList.get(i).charAt(35) +
                        fileContentsList.get(i).charAt(36) + "." + fileContentsList.get(i).charAt(37);

                Double avCopValue = Double.parseDouble(StoreDouble);

                sum = sum + avCopValue;
                sumCounter ++;


                int av = (int)Math.round((sum/sumCounter)*10);

                lineStoreAverage = "|Date: xx/xx/"+ prevDate + "|time:00:00 |COP:" + String.format("%05d",av);

            }else{

                String StoreDouble = Character.toString(fileContentsList.get(i).charAt(33)) +
                        fileContentsList.get(i).charAt(34) +  fileContentsList.get(i).charAt(35) +
                        fileContentsList.get(i).charAt(36) + "." + fileContentsList.get(i).charAt(37);

                Double avCopValue = Double.parseDouble(StoreDouble);


                if(i == 0){
                    prevDate = date;
                    sum = avCopValue;
                    if(fileContentsList.size() == 1){
                        int av = (int)Math.round((firstVal)*10);
                        lineStoreAverage = "|Date: xx/xx/"+ prevDate + "|time:00:00 |COP:" + String.format("%05d",av);
                    }
                }else{
                    int av = (int)Math.round((sum/sumCounter)*10);

                    lineStoreAverage = "|Date: xx/xx/"+ prevDate + "|time:00:00 |COP:" + String.format("%05d",av);
                    DailizerList.add(lineStoreAverage);
                    prevDate = date;
                    sum = avCopValue;
                }
                sumCounter = 1;
            }
        }DailizerList.add(lineStoreAverage);
        fileContentsList.clear();

        fileContentsList.addAll(DailizerList);

        for(int i = 0; i < fileContentsList.size(); i++){
            System.out.println(fileContentsList.get(i));
        }

        DailizerList.clear();

    }




    private void setHourlyStorageArrayList(){
        String date;
        String time;
        String StoreDouble;
        Double avCopValue;

        for(int i = 0; i < fileContentsList.size(); i++){
            date = Character.toString(fileContentsList.get(i).charAt(7)) +
                    fileContentsList.get(i).charAt(8) +  fileContentsList.get(i).charAt(9) +
                    fileContentsList.get(i).charAt(10) + fileContentsList.get(i).charAt(11) +
                    fileContentsList.get(i).charAt(12) + fileContentsList.get(i).charAt(13) +
                    fileContentsList.get(i).charAt(14) + fileContentsList.get(i).charAt(15);


            time = Character.toString(fileContentsList.get(i).charAt(22)) +
                    fileContentsList.get(i).charAt(23) +  fileContentsList.get(i).charAt(24) +
                    fileContentsList.get(i).charAt(25) + fileContentsList.get(i).charAt(26) +
                    fileContentsList.get(i).charAt(27);

            StoreDouble = Character.toString(fileContentsList.get(i).charAt(33)) +
                    fileContentsList.get(i).charAt(34) +  fileContentsList.get(i).charAt(35) +
                    fileContentsList.get(i).charAt(36) + "." + fileContentsList.get(i).charAt(37);

            avCopValue = Double.parseDouble(StoreDouble);

            System.out.println("date= " + date);
            System.out.println("time= " + time);
            System.out.println("doubleString= " + StoreDouble);
            System.out.println("double= " + avCopValue);
            System.out.println(fileContentsList.get(i));

            hourlyStorage store = new hourlyStorage(date,time,avCopValue);
            HourlyStorageArrayList.add(store);
            System.out.println(HourlyStorageArrayList.get(i).getDate());
        }


    }

    private void setDailyStorageArrayList(){
        String date;
        String StoreDouble;
        Double avCopValue;

        for(int i = 0; i < fileContentsList.size(); i++){
            date = Character.toString(fileContentsList.get(i).charAt(7)) +
                    fileContentsList.get(i).charAt(8) +  fileContentsList.get(i).charAt(9) +
                    fileContentsList.get(i).charAt(10) + fileContentsList.get(i).charAt(11) +
                    fileContentsList.get(i).charAt(12) + fileContentsList.get(i).charAt(13) +
                    fileContentsList.get(i).charAt(14) + fileContentsList.get(i).charAt(15);



            StoreDouble = Character.toString(fileContentsList.get(i).charAt(33)) +
                    fileContentsList.get(i).charAt(34) +  fileContentsList.get(i).charAt(35) +
                    fileContentsList.get(i).charAt(36) + "." + fileContentsList.get(i).charAt(37);

            avCopValue = Double.parseDouble(StoreDouble);

            System.out.println("date= " + date);
            System.out.println("doubleString= " + StoreDouble);
            System.out.println("double= " + avCopValue);
            System.out.println(fileContentsList.get(i));

            dailyStorage store = new dailyStorage(date,avCopValue);
            DailyStorageArrayList.add(store);
            System.out.println(DailyStorageArrayList.get(i).getDate());
        }

    }

    private void setMonthlyStorageArrayList(){

        String date;
        String StoreDouble;
        Double avCopValue;

        for(int i = 0; i < fileContentsList.size(); i++){
            date = Character.toString(fileContentsList.get(i).charAt(7)) +
                    fileContentsList.get(i).charAt(8) +  fileContentsList.get(i).charAt(9) +
                    fileContentsList.get(i).charAt(10) + fileContentsList.get(i).charAt(11) +
                    fileContentsList.get(i).charAt(12) + fileContentsList.get(i).charAt(13) +
                    fileContentsList.get(i).charAt(14) + fileContentsList.get(i).charAt(15);



            StoreDouble = Character.toString(fileContentsList.get(i).charAt(33)) +
                    fileContentsList.get(i).charAt(34) +  fileContentsList.get(i).charAt(35) +
                    fileContentsList.get(i).charAt(36) + "." + fileContentsList.get(i).charAt(37);

            avCopValue = Double.parseDouble(StoreDouble);

            System.out.println("date= " + date);
            System.out.println("doubleString= " + StoreDouble);
            System.out.println("double= " + avCopValue);
            System.out.println(fileContentsList.get(i));

            monthlyStorage store = new monthlyStorage(date,avCopValue);
            MonthlyStorageArrayList.add(store);
            System.out.println(MonthlyStorageArrayList.get(i).getDate());
        }

    }

    private void setYearlyStorageArrayList(){
        String date;
        String StoreDouble;
        Double avCopValue;

        for(int i = 0; i < fileContentsList.size(); i++){
            date = Character.toString(fileContentsList.get(i).charAt(7)) +
                    fileContentsList.get(i).charAt(8) +  fileContentsList.get(i).charAt(9) +
                    fileContentsList.get(i).charAt(10) + fileContentsList.get(i).charAt(11) +
                    fileContentsList.get(i).charAt(12) + fileContentsList.get(i).charAt(13) +
                    fileContentsList.get(i).charAt(14) + fileContentsList.get(i).charAt(15);



            StoreDouble = Character.toString(fileContentsList.get(i).charAt(33)) +
                    fileContentsList.get(i).charAt(34) +  fileContentsList.get(i).charAt(35) +
                    fileContentsList.get(i).charAt(36) + "." + fileContentsList.get(i).charAt(37);

            avCopValue = Double.parseDouble(StoreDouble);

            System.out.println("date= " + date);
            System.out.println("doubleString= " + StoreDouble);
            System.out.println("double= " + avCopValue);
            System.out.println(fileContentsList.get(i));

            yearlyStorage store = new yearlyStorage(date,avCopValue);
            YearlyStorageArrayList.add(store);
            System.out.println(YearlyStorageArrayList.get(i).getDate());
        }
    }

    public String getSelectionString(){
        return this.selection;
    }

    public String getHourlyStorageDate(int i){

        return HourlyStorageArrayList.get(i).getDate();
    }
    public String getHourlyStorageTime(int i){

        return HourlyStorageArrayList.get(i).getTime();
    }
    public Double getHourlyStorageCOP(int i){

        return HourlyStorageArrayList.get(i).getHourlyAverageCopVal();
    }

    public String getDailyStorageDate(int i){

        return DailyStorageArrayList.get(i).getDate();
    }
    public Double getDailyStorageCOP(int i){

        return DailyStorageArrayList.get(i).getDailyAverageCopCal();
    }

    public String getMonthlyStorageDate(int i){

        return MonthlyStorageArrayList.get(i).getDate();
    }
    public Double getMonthlyStorageCOP(int i){

        return MonthlyStorageArrayList.get(i).getMonthlyAverageCopCal();
    }

    public String getYearlyStorageDate(int i){

        return YearlyStorageArrayList.get(i).getDate();
    }

    public Double getYearlyStorageCOP(int i){

        return YearlyStorageArrayList.get(i).getYearlyAverageCopCal();
    }

    public String getChosenStartDate(){
        String date = "" + dd + "/" + mm + "/" + yy;
        return date;
    }


    public String getChosenEndDate(){
        String date = "" + DD + "/" + MM + "/" + YY;
        return date;
    }



    // used to switch dates around
    private void switchDates() throws IOException {

        int storDD = this.startDD;
        int storMM = this.startMM;
        int storYY = this.startYY;

        this.startDD = this.endDD;
        this.startMM = this.endMM;
        this.startYY = this.endYY;

        this.endDD = storDD;
        this.endMM = storMM;
        this.endYY = storYY;

        dateSortingAlgo();
    }
}
