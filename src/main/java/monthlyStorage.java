public class monthlyStorage {
    private String date;
    private Double monthlyAverageCopCal;
    public monthlyStorage(String date,  Double monthlyAverageCopCal){

        // this just removes the days from the months list
        this.date = Character.toString(date.charAt(3)) + date.charAt(4) +
                date.charAt(5) + date.charAt(6) +
                date.charAt(7) + date.charAt(8);
        this.monthlyAverageCopCal = monthlyAverageCopCal;

    }

    public String getDate(){
        return this.date;
    }

    public Double getMonthlyAverageCopCal(){
        return this.monthlyAverageCopCal;
    }
}
