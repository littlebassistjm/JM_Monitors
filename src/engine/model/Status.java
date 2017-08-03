package engine.model;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by jmkhilario on 02/08/2017.
 */
public class Status {

    private static Status instance = null;
    Scanner sc = new Scanner(System.in);
    private static final Object wait_lock = new Object();
    private int occurrence = 0;

    ArrayList<Station> stations = Logic.getInstance().getStations();

    public static Status getInstance() {
        return instance == null ? instance = new Status() : instance;
    }

    public void print_status() throws InterruptedException {

        occurrence++;
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~ Occurrence: " + occurrence);
        for (int i = 0; i < stations.size(); i++) {
            System.out.println();
            System.out.println("STATION " + stations.get(i).station_id + " STATUS");
            if (stations.get(i).current_train != null) {
                stations.get(i).getStationObject().isActive(true);
                stations.get(i).getStationObject().setTrain_id(stations.get(i).current_train.train_id);
                // update available seats in GUI
                stations.get(i).getStationObject().setAvailable_seats(stations.get(i).current_train.available_seats);
                System.out.println("Train: " + stations.get(i).current_train.train_id);
                System.out.println("Available Seats: " + stations.get(i).current_train.available_seats);
            } else {
                stations.get(i).getStationObject().isActive(false);
                stations.get(i).getStationObject().setTrain_id(0);
                stations.get(i).getStationObject().setAvailable_seats(0);
                System.out.println("Train: N/A");
                System.out.println("Available Seats: N/A");
            }
            System.out.println("Waiting Passengers: " + stations.get(i).waiting_passengers);
            // update waiting passengers in GUI
            if (stations.get(i).waiting_passengers > 0) {
                int[] passenger_ids = new int[stations.get(i).waiting_passengers];
                for (int j = 0; j < stations.get(i).waiting_passengers; j++) {
                    passenger_ids[j] = stations.get(i).getList_waiting_passengers().get(j).passenger_id;
                }
                stations.get(i).getStationObject().setPassenger_ids(passenger_ids);
            } else {
                stations.get(i).getStationObject().setPassenger_ids(null);
            }
        }
        System.out.println();
//        System.out.println("Press enter to continue:");
//        sc.nextLine();
        TimeUnit.SECONDS.sleep(1);
    }
}
