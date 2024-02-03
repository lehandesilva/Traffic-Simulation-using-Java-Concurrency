public class Clock {
    private long currentTime;
    private int tickDuration = 1000;
    public Clock() {
        this.currentTime = 0;
    }

    public void tick() {
        currentTime++;
        try {
            Thread.sleep(tickDuration);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
    public long getCurrentTime() {
        return currentTime;
    }
}
/*
* All objects should have access to a method to read the time
* 1 time period = 1 tick which means one increment of
a counter variable
*  1 tick = 10 real seconds
* Method to increment the tick by 1 second
* */