import java.util.ArrayList;

/**
 * Created by jmkhilario on 28/07/2017.
 */
public class Station {
    // MONITOR managing trains and passengers

    boolean station_occupied;
    Train current_train;
    int station_id;
    int waiting_passengers = 0;
    ArrayList<Passenger> list_waiting_passengers;
    ArrayList<Train> list_waiting_trains;

    Station(int station_id) {
        this.station_id = station_id;
        station_occupied = false;
        current_train = null;
        list_waiting_passengers = new ArrayList<>();
        list_waiting_trains = new ArrayList<>();

    }

    public void load_train(Train train) throws InterruptedException {

        synchronized (this) {
            // check if a train is in the station already
            if (station_occupied) {
                System.out.println("Station " + station_id + " is occupied. Train " + train.TRAIN_ID + " is waiting");
                list_waiting_trains.add(train);
                wait();
                System.out.println("Station " + station_id + " is no longer occupied. Train " + train.TRAIN_ID + " is now entering.");
            }

        }

        // occupy station
        station_occupied = true;
        current_train = train;
        System.out.println("Train " + train.TRAIN_ID + " has occupied Station " + station_id + ".");

        //start boarding
        synchronized (current_train) {
            if (waiting_passengers > 0) {
                start_boarding();
            }
            else return;
            current_train.wait(); // wait while passengers are boarding
        }

        //notify next train that the station is not available
        synchronized (this) {
            notify();
        }
        System.out.println("Next train may now enter");
        System.out.println("===============================");
    }

    public void wait_for_train(Passenger passenger) throws InterruptedException {
        synchronized (this) {
            waiting_passengers++;
            list_waiting_passengers.add(passenger);
            System.out.println("Passenger " + passenger.passenger_id + " is now waiting for the train is Station " + station_id + ".");
            if (!station_occupied) wait();
            else wait();
        }
        System.out.println("Passenger " + passenger.passenger_id + " has been called to board");
        on_board();
    }

    private void start_boarding() {
        System.out.println("Train " + current_train.TRAIN_ID + " has started boarding.");
        synchronized (this) {
            notify();
        }
    }

    public void on_board() {
        System.out.println("Passenger " + list_waiting_passengers.get(0).passenger_id + " is boarding.");

        synchronized (this) {
            waiting_passengers--;
            list_waiting_passengers.remove(0);
            System.out.println("Remaining passengers: " + waiting_passengers);
        }

        if (waiting_passengers == 0) {
            synchronized (current_train) {
                current_train.notify();
            }
            System.out.println("Train " + current_train.TRAIN_ID + " has left Station " + station_id);
        } else {
            synchronized (this) {
                notify();
            }
        }

    }

}
