import com.sun.source.tree.InstanceOfTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "./Scenario2.txt";
        String SHOPPINGCENTRE = "Shopping Centre";
        String INDUSTRIALPARK = "Industrial Park";
        String STATION = "Station";
        String UNIVERSITY = "University";
        long time;

        Map<String, Integer> entryPoints = readConfigFile( filePath,"ENTRYPOINTS");
        Map<String, Integer> junctions = readConfigFile(filePath, "JUNCTIONS");

        System.out.println(filePath);
        //The naming conventions for each road is its entry point followed by its destination
        Road southA = new Road(60,"south", "A", new String[]{});
        Road eastB = new Road(30, "East", "B", new String[]{});
        Road northC = new Road(50, "North", "C", new String[]{});
        Road AB = new Road(7,"A", "B", new String[]{SHOPPINGCENTRE,STATION,UNIVERSITY});
        Road AIndustrialPark = new Road(15,"A",INDUSTRIALPARK,new String[]{INDUSTRIALPARK});
        Road BA = new Road(7,"B","A",new String[]{INDUSTRIALPARK});
        Road BC = new Road(10,"B","C",new String[]{SHOPPINGCENTRE,STATION,UNIVERSITY});
        Road CB = new Road(10,"C","B", new String[]{INDUSTRIALPARK});
        Road CShoppingCentre = new Road(7,"C",SHOPPINGCENTRE,new String[]{SHOPPINGCENTRE});
        Road CD = new Road(10,"C", "D",new String[]{STATION, UNIVERSITY});
        Road DUniversity = new Road(15,"D",UNIVERSITY, new String[]{UNIVERSITY});
        Road DStation = new Road(15,"D", STATION, new String[]{STATION});

        Clock clock = new Clock();

        CarPark industrialPark = new CarPark(clock,1000,AIndustrialPark);
        industrialPark.setName("Industry");
        CarPark shoppingCentre = new CarPark(clock,400,CShoppingCentre);
        shoppingCentre.setName("shopping");
        CarPark station = new CarPark(clock, 150,DStation);
        station.setName("station");
        CarPark university = new CarPark(clock,100,DUniversity);
        university.setName("uni");

        EntryPoint south = new EntryPoint(entryPoints.get("south"),clock,southA);
        EntryPoint east = new EntryPoint(entryPoints.get("East"),clock,eastB);
        EntryPoint north = new EntryPoint(entryPoints.get("North"),clock,northC);

        Junction Ajunc = new Junction(junctions.get("A"), new Road[]{southA, BA},new Road[]{AB,AIndustrialPark},clock);
        Ajunc.setName("Ajunc");
        Junction Bjunc = new Junction(junctions.get("B"), new Road[]{eastB,AB,CB},new Road[]{BA,BC},clock);
        Bjunc.setName("Bjunc");
        Junction Cjunc = new Junction(junctions.get("C"), new Road[]{northC,BC},new Road[]{CShoppingCentre,CD,CB},clock);
        Cjunc.setName("Cjunc");
        Junction Djunc = new Junction(junctions.get("D"), new Road[]{CD},new Road[]{DStation,DUniversity},clock);
        Djunc.setName("Djunc");

        south.start();
        east.start();
        north.start();
        Ajunc.start();
        Bjunc.start();
        Cjunc.start();
        Djunc.start();
        industrialPark.start();
        shoppingCentre.start();
        station.start();
        university.start();

        for (int i = 0; i <= 360; i++) {
            clock.tick();
            time = clock.getCurrentTime();
            if (time % 60 == 0){
                System.out.println("Time: " + time/6 + "m :    University : " + university.getCount() + "\nStation: " + station.getCount() +"\nShopping Center: " + shoppingCentre.getCount() + "\nIndustrial Park: " + industrialPark.getCount());
            }
        }
        clock.setHasStopped(true);
        finalReport( south,  north,  east,  southA, eastB, northC, AB, AIndustrialPark, BA, BC, CB, CShoppingCentre, CD, DUniversity, DStation,university,  industrialPark, shoppingCentre, station);

    }
    private static void finalReport(EntryPoint south, EntryPoint north, EntryPoint east, Road southA,Road eastB,Road northC,Road AB,Road AIndustrialPark,Road BA,Road BC,Road CB,Road CShoppingCentre,Road CD,Road DUniversity,Road DStation, CarPark uni, CarPark industrial, CarPark shop, CarPark station) {
        int queued = southA.getCount() + eastB.getCount() + northC.getCount() + AB.getCount() + AIndustrialPark.getCount()+ BA.getCount()+ BC.getCount()+ CB.getCount()+CShoppingCentre.getCount()+CD.getCount()+ DUniversity.getCount()+ DStation.getCount();
        int created = south.getCarsGenerated() + east.getCarsGenerated() + north.getCarsGenerated();
        int parked = uni.getCount() + industrial.getCount() + station.getCount() + shop.getCount();
        System.out.println("Created: "+created+"\n"+"Queued: "+queued+"\n"+"Parked: "+parked+"\n");
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