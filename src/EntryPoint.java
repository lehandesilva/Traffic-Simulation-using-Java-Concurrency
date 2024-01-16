import java.util.Random;
public class EntryPoint implements Runnable{
    private String[] destinations = {"University","Station","Shopping Centre","Industrial Park"};
    private Random random = new Random();
    private int carsPerHour;
    private final Clock clock;
    public EntryPoint(int carsPerHour, Clock clock){
        this.carsPerHour = carsPerHour;
        this.clock = clock;

    }
    public void run(){
        
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