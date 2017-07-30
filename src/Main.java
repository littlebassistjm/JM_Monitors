import java.util.ArrayList;

/**
 * Created by jmkhilario on 28/07/2017.
 */
public class Main {


    public static void main(String[] args) throws InterruptedException {

        int num_stations=1;
        int num_trains=1;
        int num_max_seats=1;
        int num_passengers=1;

        ArrayList<Station> stations = new ArrayList<>();
        ArrayList<Train> trains = new ArrayList<>();
        ArrayList<Passenger> passengers = new ArrayList<>();

        // create stations
        for(int i=0; i<num_stations; i++){
            stations.add(new Station(i+1));
        }
        // create passenger threads
            for(int i=0; i<num_passengers; i++){
                passengers.add(new Passenger(stations.get(i%num_stations), i+1));
            }
         // create train threads
            for(int i=0; i<num_trains; i++){
                trains.add(new Train((i+1),num_max_seats, stations.get(i%num_stations)));
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
