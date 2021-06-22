public class hourlyStorage {
    private String date;
    private String time;
    private Double averageHourCopVal;

    // hourly storage class used for hourley intervals
    public hourlyStorage(String date, String time, Double averageHourCOPval){

        this.date = date;
        this.time = time;
        this.averageHourCopVal = averageHourCOPval;
    }

    public String getDate(){
        return this.date;
    }

    public String getTime(){
        return this.time;
    }

    public Double getHourlyAverageCopVal(){
        return this.averageHourCopVal;
    }
}
