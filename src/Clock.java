public class Clock{
    private long currentTime;
    private int tickDuration = 1000; // Since 1000 milliseconds = 1 second
    private boolean hasStopped; // Flag for threads to check if the clock has stopped, initialized as false
    public Clock() {
        this.currentTime = 0;
        this.hasStopped = false;
    }
    public void tick() {
        // increments at each iteration
        currentTime++;
        try {
            // To simulate 1 second pass by
            Thread.sleep(tickDuration);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
    public long getCurrentTime() {
        return currentTime;
    }
    public boolean hasStopped() {
        return hasStopped;
    }
    public void setHasStopped(boolean hasStopped) {
        this.hasStopped = hasStopped;
    }
}
/*
* All objects should have access to a method to read the time
* 1 time period = 1 tick which means one increment of
a counter variable
*  1 tick = 10 real seconds
* Method to increment the tick by 1 second
* */