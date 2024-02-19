public class Vehicle {
    private final String destination;
    private long entryTime;
    private long parkTime;

    public Vehicle(String destination, long entryTime) {
        this.destination = destination;
        this.entryTime = entryTime;
    }

    public String getDestination() {
        return destination;
    } // gets destination
    public long getEntryTime() {
        return entryTime;
    } // gets entry time
    public void setParkTime(long parkTime){
        this.parkTime = parkTime;
    } // sets park time
    public long getParkTime() {return parkTime;} // gets park time
}
