public class CarPark implements Runnable{

    private Clock clock;
    private int capacity;
    private Vehicle[] carPark; //Array that holds the vehicles once parked
    private int count;
    private Road connectedRoad;
    private Vehicle car;

    public CarPark(Clock clock, int capacity, Road connectedRoad) {
        this.clock = clock;
        this.capacity = capacity;
        this.carPark = new Vehicle[capacity];
        this.count = 0;
        this.connectedRoad = connectedRoad;
    }

    public void run() {
        try {
            while (count <= capacity) {
                    car = connectedRoad.removeVehicle();
                    long time = clock.getCurrentTime();
                    car.setParkTime(time);
                    carPark[count] = car;
                    count++;
                    Thread.sleep(10);

            }
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
