public class theSecondCounter {
    private int counter = 0;
    private int number = 0;

    public void setCounter(){
        counter ++;
    }

    public void resetCounter(){
        counter = 0;
    }

    public int getCounter(){
        return this.counter;
    }

    public void addNumber(int number) {
        this.number = this.number + number;
    }

    public int getNumber(){
        return this.number;
    }

    public void resetNumber(){
        this.number = 0;
    }

}
