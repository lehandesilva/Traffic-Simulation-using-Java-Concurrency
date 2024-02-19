import java.util.Random;
public class EntryPoint extends Thread{
    private final String[] destinations = {"University","Station","Shopping Centre","Industrial Park"};
    private Random random = new Random();
    private final int carsPerHour; // limit on cars that could be generated per hour
    private int carsGenerated; // cars thats generated
    private final Clock clock;
    private final Road road;
    public EntryPoint(int carsPerHour, Clock clock, Road road){
        this.carsPerHour = carsPerHour;
        this.clock = clock;
        this.road = road;
    }
    @Override
    public void run(){
        try {
            // Loops until clock.hasStopped is true
            while(!clock.hasStopped()) {
                if (carsGenerated <= carsPerHour){
                    // checks if road is full before adding car
                    if (!road.isFull()) {
                        road.addVehicle(generateVehicle());
                        carsGenerated++;
                        sleep(500);
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("EntryPoint" + road.getEntryPoint()+ "Interrupted");
        }
    }
    public int getCarsGenerated() {
        return carsGenerated;
    }

    // Returns a random destination based on probability
    private String getRandomDestination() {
        double rand = random.nextDouble();
        if (rand < 0.1) return destinations[0];
        else if (rand < 0.3) return destinations[1];
        else if (rand < 0.6) return destinations[2];
        else return destinations[3];
    }
    // Generate vehicle
    private Vehicle generateVehicle() {
        String destination = getRandomDestination();
        long enterTime = clock.getCurrentTime();
        return new Vehicle(destination, enterTime);
    }
}
/*
* Check if the road that's connected to it is empty and if it
  is then add a vehicle*/