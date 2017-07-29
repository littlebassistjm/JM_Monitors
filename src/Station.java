import java.util.ArrayList;

/**
 * Created by jmkhilario on 28/07/2017.
 */
public class Station {
    // MONITOR managing trains and passengers

    boolean station_occupied;
    Train current_train;
    int station_id;
    int waiting_passengers = 1;
    ArrayList<Passenger> list_waiting_passengers;

    Station(int station_id) {
        this.station_id = station_id;
        station_occupied = false;
        current_train = null;
        list_waiting_passengers = new ArrayList<>();
    }

    public void load_train(Train train) throws InterruptedException {

        synchronized (train) {
            if (station_occupied) {
                System.out.println("Station " + station_id + " is occupied. Train " + train.TRAIN_ID + " is waiting");
                wait();
                System.out.println("Station " + station_id + " is no longer occupied. Train " + train.TRAIN_ID + " is now entering.");
            }

            station_occupied = true;
            current_train = train;
            System.out.println("Train " + train.TRAIN_ID + " has occupied Station " + station_id + ".");
            if (waiting_passengers>0) {
                on_board();
            }
            synchronized (current_train) {
                current_train.wait();
            }
        }
    }

    public void wait_for_train(Passenger passenger) throws InterruptedException {
        synchronized (this) {
            waiting_passengers++;
            list_waiting_passengers.add(passenger);
            System.out.println("Passenger " + waiting_passengers + " is now waiting for the train is Station " + station_id + ".");
            if (!station_occupied) wait();
        }
        on_board();
    }

    private void start_boarding(){
        list_waiting_passengers.get(0).notify();
    }

    public void on_board() {
        synchronized (this) {
            waiting_passengers--;
            //list_waiting_passengers.remove(0);
        }
      //  if (waiting_passengers == 0) {
        synchronized (current_train) {
            current_train.notify();
        }
        System.out.println("Train " + current_train.TRAIN_ID + " has left Station " + station_id);
     //   }
        //else
        synchronized (list_waiting_passengers.get(0)) {
            list_waiting_passengers.get(0).notify();
        }

    }

}
