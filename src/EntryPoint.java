import java.util.Random;
public class EntryPoint extends Thread{
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
    @Override
    public void run(){
        try {
            while(!clock.hasStopped()) {
                if (carsGenerated <= carsPerHour){
                    if (!road.isFull()) {
                        road.addVehicle(generateVehicle());
                        carsGenerated++;
//                        System.out.println("Car generated at" + road.getEntryPoint());
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