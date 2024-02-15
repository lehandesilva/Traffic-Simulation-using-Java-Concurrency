import java.util.Random;
public class EntryPoint implements Runnable{
    private final String[] destinations = {"University","Station","Shopping Centre","Industrial Park"};
    private Random random = new Random();
    private final int carsPerHour;
    private int carsGenerated;
    private final Clock clock;
    private final Road road;

    public EntryPoint(int carsPerHour, Clock clock, Road road){
        this.carsPerHour = carsPerHour;
        this.clock = clock;
        this.road = road;
    }
    public void run(){
        try {
            while(clock.getCurrentTime() <= 360) {
                while (carsGenerated <= carsPerHour) {
                    if (!road.isFull()) {
                        road.acquireMutex();
                        Vehicle carHolder = generateVehicle();
                        road.addVehicle(carHolder);
//                        System.out.println("car generated with dest:"+ carHolder.getDestination());
                        carsGenerated++;
                        road.releaseMutex();
                        Thread.sleep(100);
                    }
                }
            }
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    private String getRandomDestination() {
        double rand = random.nextDouble();
        if (rand < 0.1) return destinations[0];
        else if (rand < 0.3) return destinations[1];
        else if (rand < 0.6) return destinations[2];
        else return destinations[3];
    }

    private Vehicle generateVehicle() {
        String destination = getRandomDestination();
        long enterTime = clock.getCurrentTime();
        return new Vehicle(destination, enterTime);
    }
}

/*
* Check if the road that's connected to it is empty and if it
  is then add a vehicle*/