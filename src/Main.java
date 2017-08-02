import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by jmkhilario on 28/07/2017.
 */
public class Main {


    public static void main(String[] args) throws InterruptedException {

        int num_stations=2;
        int num_passengers=2;
        int num_trains;
        int[] num_max_seats;

        ArrayList<Station> stations = new ArrayList<>();
        ArrayList<Train> trains = new ArrayList<>();
        ArrayList<Passenger> passengers = new ArrayList<>();
        Status status = new Status(stations, trains, passengers);

        Scanner sc = new Scanner(System.in);
        System.out.println("How many trains: ");
        num_trains = sc.nextInt();
        num_max_seats = new int[num_trains];

        for (int i=0; i<num_trains; i++) {
            System.out.println("Max seats for train " + i + ": ");
            num_max_seats[i] = sc.nextInt();
        }

        // create stations
        for(int i=0; i<num_stations; i++){
            stations.add(new Station(i+1, status));
        }
        // create passenger threads
            for(int i=0; i<num_passengers; i++){
                passengers.add(new Passenger(stations.get(i%num_stations), i+1));
            }
         // create train threads
            for(int i=0; i<num_trains; i++){
                trains.add(new Train((i+1),num_max_seats[i], stations));
            }

        // START THREADS
        //passengers
            for(int i=0; i<num_passengers; i++){
                passengers.get(i).start();
            }
        //trains
            for(int i=0; i<num_trains; i++){
                trains.get(i).start();
            }

        // JOIN THREADS
        //passengers
            for(int i=0; i<num_passengers; i++){
                passengers.get(i).join();
            }
        //trains
        for(int i=0; i<num_trains; i++){
            trains.get(i).join();
        }
    }
}
