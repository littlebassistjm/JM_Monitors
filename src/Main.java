import java.util.ArrayList;

/**
 * Created by jmkhilario on 28/07/2017.
 */
public class Main {


    public static void main(String[] args) throws InterruptedException {

        //ArrayList<Station> stations = new ArrayList<Station>();
        //ArrayList<Train> trains = new ArrayList<Train>();

        Station station = new Station(1);
        Passenger passenger = new Passenger(station);
        Train train = new Train(1, 1, station);

        passenger.start();

        train.start();
        passenger.join();
        train.join();
    }
}
