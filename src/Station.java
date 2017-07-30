import java.util.ArrayList;

/**
 * Created by jmkhilario on 28/07/2017.
 */
public class Station {
    // MONITOR managing trains and passengers

    boolean station_occupied;
    boolean train_boarding;
    Train current_train;
    int station_id;
    int waiting_passengers = 0;
    ArrayList<Passenger> list_waiting_passengers;
    ArrayList<Train> list_waiting_trains;

    Station(int station_id) {
        this.station_id = station_id;
        this.station_occupied = false;
        train_boarding = false;
        current_train = null;
        list_waiting_passengers = new ArrayList<>();
        list_waiting_trains = new ArrayList<>();

    }

    public void load_train(Train train) throws InterruptedException {

        synchronized (this) {
            // check if a train is in the station already
                if (station_occupied) {
                    System.out.println("TRAIN: Station " + station_id + " is occupied. Train " + train.train_id + " is waiting");
                    list_waiting_trains.add(train);
                    wait();
                    //System.out.println("Station " + station_id + " is no longer occupied. Train " + train.train_id + " is now entering.");
                }

            System.out.println("TRAIN: Station " + station_id + " is no longer occupied. Train " + train.train_id + " is now entering.");

        // occupy station
            current_train = train;
            station_occupied = true;
            System.out.println("TRAIN: Train " + train.train_id + " has occupied Station " + station_id + ".");
            }

        //start boarding
            synchronized (current_train) {
                if (waiting_passengers > 0) {
                    start_boarding();
                }
                else return;
                current_train.wait(); // wait while passengers are boarding
            }

        //notify next train that the station is now available
            synchronized (this) {
                notify();
                System.out.println("TRAIN: Next train may now enter");
                System.out.println("===============================");
            }
    }

    public void wait_for_train(Passenger passenger) throws InterruptedException {
        synchronized (this) {
            // add this passenger to the waiting list
                waiting_passengers++;
                list_waiting_passengers.add(passenger);
            // wait for train
                if (!station_occupied) {
                    System.out.println("PASSENGER: Passenger " + passenger.passenger_id + " is now waiting for the train in Station " + station_id + ".");
                    wait();
                }
                else if (!train_boarding){
                    System.out.println("PASSENGER: Station " + station_id + " is now occupied. Passenger" + passenger.passenger_id + " waiting for boarding.");
                    wait();
                }

            // board
                if(current_train!=null) {
                    System.out.println("PASSENGER: Passenger " + passenger.passenger_id + " has been called to board");
                    on_board();
                }
        //}
            /*
       if (current_train.available_seats>0) {
            System.out.println("PASSENGER: Passenger " + passenger.passenger_id + " has been called to board");
            on_board();
        } else {
            // tell the train to leave then wait again
            System.out.println("PASSENGER: Not enough seats on Train " + current_train.train_id + ". Passenger " + passenger.passenger_id + " is now waiting again.");
            synchronized (current_train){
                current_train.notify();
            }
            //synchronized (this) {
                wait();
            }*/
        }
    }

    private void start_boarding() {
        System.out.println("START_BOARDING: Train " + current_train.train_id + " has started boarding.");
        synchronized (this) {
            notify();
        }
    }

    public void on_board() {
        System.out.println("ON_BOARD: Passenger " + list_waiting_passengers.get(0).passenger_id + " is boarding.");

        synchronized (this) {
            waiting_passengers--;
            current_train.available_seats--;
            list_waiting_passengers.remove(0);
            System.out.println("ON_BOARD: Remaining passengers: " + waiting_passengers);
        }

        if (waiting_passengers == 0 || current_train.available_seats==0) {
            synchronized (current_train) {
                current_train.notify();
                System.out.println("ON_BOARD: Train " + current_train.train_id + " has left Station " + station_id);
                station_occupied = false;
                current_train = null;
            }
        } else {
            synchronized (this) {
                System.out.println("ON_BOARD: calling next passenger.");
                notify();
            }
        }

    }

}
