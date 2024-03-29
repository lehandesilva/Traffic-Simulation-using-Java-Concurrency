public class CarPark extends Thread{
    private final Clock clock;
    private final Vehicle[] carPark; //Array that holds the vehicles once parked
    private int count;
    private final Road connectedRoad;
    private long totalTime; // Holds total time taken by all cars collectively
    private final int capacity; // capacity of the car park
    public CarPark(Clock clock, int capacity, Road connectedRoad) {
        this.clock = clock;
        this.carPark = new Vehicle[capacity];
        this.capacity = capacity;
        this.count = 0;
        this.connectedRoad = connectedRoad;
    }
    @Override
    public void run() {
        try {
            // Loops till clock.hasStopped is true
            while (!clock.hasStopped()){
                // Checks if connected road has a vehicle and if the car park has reached its capacity
                // Removes the car with mutual exclusion and adds it to its own array
                if (connectedRoad.hasVehicle() && count < capacity) {
                    Vehicle car = connectedRoad.removeVehicle();
                    long time = clock.getCurrentTime();
                    car.setParkTime(time); // sets park time of car
                    long travelTime = time - car.getEntryTime();
                    totalTime = totalTime + travelTime;
                    carPark[count] = car;
                    count++;
                    sleep(1200); // to simulate car being parked
                }
            }
            System.out.println(connectedRoad.getDestination() + ": " + count + " cars parked, average journey time " + totalTime/count + "m" );
        } catch (InterruptedException e) {
            System.out.println("CarPark" + connectedRoad.getEntryPoint()+ "Interrupted");
        }
    }
    public int getCount() {
        return count;
    }
}
/*
* Should run as a thread
* Destination for cars (consumer) and it calls the remove vehicle method
* Should give a timestamp
* Should take 12 simulation seconds once consumed*/
