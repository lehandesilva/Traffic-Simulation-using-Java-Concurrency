import java.util.concurrent.Semaphore;

public class CarPark implements Runnable{

    private final Clock clock;
    private final Vehicle[] carPark; //Array that holds the vehicles once parked
    private int count;
    private final Road connectedRoad;
    private final Semaphore parkMutex;
    public CarPark(Clock clock, int capacity, Road connectedRoad) {
        this.clock = clock;
        this.carPark = new Vehicle[capacity];
        this.count = 0;
        this.connectedRoad = connectedRoad;
        this.parkMutex = new Semaphore(1);
    }

    public void run() {
        try {
            while (clock.getCurrentTime() < 360){
                while (!connectedRoad.isEmpty()) {
                    Vehicle car = connectedRoad.removeVehicle();
                    System.out.println("car removed at " + connectedRoad.getDestination());
                    long time = clock.getCurrentTime();
                    car.setParkTime(time);
                    parkMutex.acquire();
                    carPark[count] = car;
                    count++;
                    System.out.println("car added at " + connectedRoad.getDestination());
                    parkMutex.release();
                    Thread.sleep(1200);
                }
            }
            System.out.println("Count: " + count);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
