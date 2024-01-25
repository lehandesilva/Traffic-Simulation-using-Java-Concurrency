import java.util.Random;
public class EntryPoint implements Runnable{
    private String[] destinations = {"University","Station","Shopping Centre","Industrial Park"};
    private Random random = new Random();
    private int carsPerHour;
    private int carsGenerated;
    private final Clock clock;
    private Road road;

    public EntryPoint(int carsPerHour, Clock clock, Road road){
        this.carsPerHour = carsPerHour;
        this.clock = clock;
        this.road = road;
    }
    public void run(){
        try {
            while (carsGenerated < carsPerHour) {
                if (!road.isFull()) {
                    Vehicle carHolder = generateVehicle();
                    road.addVehicle(carHolder);
                    carsGenerated++;
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