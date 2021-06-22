public class yearlyStorage {
    private String date;
    private Double yearlyAverageCopCal;
    public yearlyStorage(String date,  Double yearlyAverageCopCal){

        // only shows the last two digits for the year date
        this.date = Character.toString(date.charAt(6)) + date.charAt(7) +
                date.charAt(8);
        this.yearlyAverageCopCal = yearlyAverageCopCal;

    }

    public String getDate(){
        return this.date;
    }

    public Double getYearlyAverageCopCal(){
        return this.yearlyAverageCopCal;
    }
}