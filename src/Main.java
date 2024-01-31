import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "./Scenario1.txt";
        String SHOPPINGCENTRE = "Shopping Centre";
        String INDUSTRIALPARK = "Industrial Park";
        String STATION = "Station";
        String UNIVERSITY = "University";
        long time;

        Map<String, Integer> entryPoints = readConfigFile( filePath,"ENTRYPOINTS");
        Map<String, Integer> junctions = readConfigFile(filePath, "JUNCTIONS");

        System.out.println(filePath);

        Clock clock = new Clock();
        //The naming conventions for each road is its entry point followed by its destination
        Road southA = new Road(60,"south", "A", new String[]{SHOPPINGCENTRE,INDUSTRIALPARK,STATION,UNIVERSITY});
        Road eastB = new Road(30, "East", "B", new String[]{SHOPPINGCENTRE,INDUSTRIALPARK,STATION,UNIVERSITY});
        Road northC = new Road(50, "North", "C", new String[]{SHOPPINGCENTRE,INDUSTRIALPARK,STATION,UNIVERSITY});
        Road AB = new Road(7,"A", "B", new String[]{SHOPPINGCENTRE,STATION,UNIVERSITY});
        Road AIndustrialPark = new Road(15,"A",INDUSTRIALPARK,new String[]{INDUSTRIALPARK});
        Road BA = new Road(7,"B","A",new String[]{INDUSTRIALPARK});
        Road BC = new Road(10,"B","C",new String[]{SHOPPINGCENTRE,STATION,UNIVERSITY});
        Road CB = new Road(10,"C","B", new String[]{INDUSTRIALPARK});
        Road CShoppingCentre = new Road(7,"C",SHOPPINGCENTRE,new String[]{SHOPPINGCENTRE});
        Road CD = new Road(10,"C", "D",new String[]{STATION, UNIVERSITY});
        Road DUniversity = new Road(15,"D",UNIVERSITY, new String[]{UNIVERSITY});
        Road DStation = new Road(15,"D", STATION, new String[]{STATION});

        EntryPoint south = new EntryPoint(entryPoints.get("south"),clock,southA);
        EntryPoint east = new EntryPoint(entryPoints.get("East"),clock,eastB);
        EntryPoint north = new EntryPoint(entryPoints.get("North"),clock,northC);

        CarPark industrialPark = new CarPark(clock,1000,AIndustrialPark);
        CarPark shoppingCentre = new CarPark(clock,400,CShoppingCentre);
        CarPark station = new CarPark(clock, 150,DStation);
        CarPark university = new CarPark(clock,100,DUniversity);

        Junction Ajunc = new Junction(junctions.get("A"), new Road[]{southA, BA},new Road[]{AB,AIndustrialPark},clock);
        Junction Bjunc = new Junction(junctions.get("B"), new Road[]{AB,CB,eastB},new Road[]{BA,BC},clock);
        Junction Cjunc = new Junction(junctions.get("C"), new Road[]{BC,northC},new Road[]{CShoppingCentre,CD,CB},clock);
        Junction Djunc = new Junction(junctions.get("D"), new Road[]{CD},new Road[]{DStation,DUniversity},clock);

        Thread southThread = new Thread(south);
        Thread eastThread = new Thread(east);
        Thread northThread = new Thread(north);
        Thread industrialParkThread = new Thread(industrialPark);
        Thread shoppingCentreThread = new Thread(shoppingCentre);
        Thread stationThread = new Thread(station);
        Thread universityThread = new Thread(university);
        Thread juncAThread = new Thread(Ajunc);
        Thread juncBThread = new Thread(Bjunc);
        Thread juncCThread = new Thread(Cjunc);
        Thread juncDThread = new Thread(Djunc);

        southThread.start();
        eastThread.start();
        northThread.start();
        industrialParkThread.start();
        shoppingCentreThread.start();
        stationThread.start();
        universityThread.start();
        juncAThread.start();
        juncBThread.start();
        juncCThread.start();
        juncDThread.start();

        for (int i = 0; i <= 360; i++) {
            clock.tick();
            time = clock.getCurrentTime();
            if (time % 60 == 0){
                System.out.println("Time: " + time/6 + "m :    University : " + university.getCount() + "\nStation: " + station.getCount() +"\nShopping Center: " + shoppingCentre.getCount() + "\nIndustrial Park: " + industrialPark.getCount());
            }
        }
        try {
            industrialParkThread.join();
            shoppingCentreThread.join();
            stationThread.join();
            universityThread.join();
            juncAThread.join();
            juncBThread.join();
            juncCThread.join();
            juncDThread.join();
            southThread.join();
            eastThread.join();
            northThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static Map<String, Integer> readConfigFile( String filePath, String section) throws FileNotFoundException {
        Map<String, Integer> values = new HashMap<>();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            // Skip lines until the specified section is found
            while (scanner.hasNextLine() && !scanner.nextLine().trim().equalsIgnoreCase(section));

            // Parse lines inside the section
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Stop reading the section when an empty line is encountered
                if (line.isEmpty()) {
                    break;
                }

                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    String key = parts[0];
                    int value = Integer.parseInt(parts[1]);
                    values.put(key, value);
                }
            }
        }

        return values;
    }

}