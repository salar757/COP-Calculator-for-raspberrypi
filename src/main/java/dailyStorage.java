public class dailyStorage{
    private String date;
    private Double dailyAverageCopCal;
    public dailyStorage(String date,  Double dailyAverageCOPval){

        this.date = date;
        this.dailyAverageCopCal = dailyAverageCOPval;

    }

    public String getDate(){
        return this.date;
    }


    public Double getDailyAverageCopCal(){
        return this.dailyAverageCopCal;
    }
}
