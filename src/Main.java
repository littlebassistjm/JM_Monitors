import java.util.ArrayList;

/**
 * Created by jmkhilario on 28/07/2017.
 */
public class Main {


    public static void main(String[] args) throws InterruptedException {

        int num_passengers=2;
        int num_trains=1;
        int num_max_seats=2;
        int num_stations=1;

        ArrayList<Station> stations = new ArrayList<Station>();
        ArrayList<Train> trains = new ArrayList<Train>();
        ArrayList<Passenger> passengers = new ArrayList<>();

        Station station = new Station(1);
        // create passenger threads
            for(int i=0; i<num_passengers; i++){
                passengers.add(new Passenger(station, i+1));
            }
         // create train threads
            for(int i=0; i<num_trains; i++){
                trains.add(new Train((i+1),num_max_seats, station));
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
