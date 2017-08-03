package engine.model;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JM_Monitors created by jmkhilario on 02/08/2017.
 */
public class Logic extends Thread {

    private static Logic instance = null;

    //variables

    private boolean isSimulationRunning = false;


    public int num_stations = 8;
    private ArrayList<Station> stations;
    ArrayList<Train> trains;
    ArrayList<Passenger> passengers;
    Status status;


    private Logic() {

    }

    public boolean isSimulationRunning() {
        return isSimulationRunning;
    }

    public static Logic getInstance() {
        return (instance == null) ? (instance = new Logic()) : instance;
    }


    @Override
    public void run() {
        super.run();
        try {
            runProgram();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runProgram() throws InterruptedException, IOException {

        // list initializations
        //int num_trains, int num_passengers, int[] initial_station_list, int[] destined_station_list, int[] max_seats_list
        this.trains = new ArrayList<>();
        this.passengers = new ArrayList<>();
        //this.status = new Status(stations, trains, passengers);

        // get the number of passengers, initial station and their destination
            String[] string_initial_station = (PropertiesIO.getInstance().read("Data.properties", "initial_station_list")).split(",");
            String[] string_destined_station = (PropertiesIO.getInstance().read("Data.properties", "destined_station_list")).split(",");
            int[] initial_station_list = new int[string_initial_station.length];
            int[] destined_station_list = new int[string_destined_station.length];
            int num_passengers = Integer.parseInt(PropertiesIO.getInstance().read("Data.properties", "num_passengers"));

            for (int i = 0; i < string_initial_station.length; i++) {
                initial_station_list[i] = Integer.parseInt(string_initial_station[i]);
               // System.out.println(initial_station_list[i]);
            }

            for (int i = 0; i < string_destined_station.length; i++) {
                destined_station_list[i] = Integer.parseInt(string_destined_station[i]);
//                System.out.println(destined_station_list[i]);
            }


        // get the number of trains and their max seats
            String[] string_max_seats = (PropertiesIO.getInstance().read("Data.properties", "max_seats_list")).split(",");
            int[] max_seats_list = new int[string_max_seats.length];
            int num_trains = Integer.parseInt(PropertiesIO.getInstance().read("Data.properties", "num_trains"));

            for (int i = 0; i < string_max_seats.length; i++) {
                max_seats_list[i] = Integer.parseInt(string_max_seats[i]);
            }

        //TODO: Scanner sc = new Scanner(System.in);

        // create passenger threads
            for (int i = 0; i < num_passengers; i++) {
                passengers.add(new Passenger(getStations().get(initial_station_list[i]-1), (i + 1), destined_station_list[i]));
            }

        // create train threads
            for (int i = 0; i < num_trains; i++) {
                trains.add(new Train((i + 1), max_seats_list[i], getStations()));
            }

        // START THREADS
        //passengers
            for (int i = 0; i < num_passengers; i++) {
                passengers.get(i).start();
            }
        //trains
            for (int i = 0; i < num_trains; i++) {
                trains.get(i).start();
            }

        // JOIN THREADS
        //passengers
            for (int i = 0; i < num_passengers; i++) {
                passengers.get(i).join();
            }
        //trains
            for (int i = 0; i < num_trains; i++) {
                trains.get(i).join();
            }
    }

    public void setSimulationRunning(boolean simulationRunning) {
        isSimulationRunning = simulationRunning;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }
}
