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

    private static Object train_lock = new Object();
    private static Object passenger_lock = new Object();
    private static Object current_train_lock = new Object();

    Station(int station_id) {
        this.station_id = station_id;
        this.station_occupied = false;
        train_boarding = false;
        current_train = null;
        list_waiting_passengers = new ArrayList<>();
        list_waiting_trains = new ArrayList<>();

    }

    public void load_train(Train train) throws InterruptedException {

        synchronized (train_lock) {
            // check if a train is in the station already
            if (station_occupied) { // if occupied, wait
                System.out.println("TRAIN: STATION " + station_id + " is occupied. TRAIN " + train.train_id + " is waiting");
                list_waiting_trains.add(train);
                while (station_occupied && train_boarding) {
                    train_lock.wait();
                }
            }

            System.out.println("LOCK CHECK 1: TRAIN " + train.train_id);

            synchronized (current_train_lock) {
                System.out.println("LOCK CHECK 2: TRAIN " + train.train_id);
                // occupy station
                if(!station_occupied){
                    // remove train from waiting trains
                        System.out.println("TRAIN: STATION " + station_id + " is no longer occupied. TRAIN " + train.train_id + " is now entering.");
                        station_occupied = true;
                        current_train = train;
                        list_waiting_trains.remove(current_train);
                        System.out.println("TRAIN: TRAIN " + train.train_id + " has occupied STATION " + station_id + ".");
                    // release previously boarded passengers
                        current_train.available_seats = current_train.max_seats; // <-- CHANGE THIS IF NEEDED
                        System.out.println("TRAIN: TRAIN " + train.train_id + " has released its passengers.");
                    // if there are waiting passengers, board
                        if (waiting_passengers>0) {
                            // start boarding
                            start_boarding();
                            // wait while passengers are boarding
                            current_train_lock.wait();
                        } else { // if there are no waiting passengers, leave/do nothing
                            System.out.println("TRAIN: There are no waiting PASSENGERS.");
                        }
                }
                System.out.println("LOCK EXIT CHECK: TRAIN " + train.train_id);
            }

            // leave, release all locks
                train_boarding = false;
                station_occupied = false;
                current_train = null;

            System.out.println("TRAIN: Remaining PASSENGERS: " + waiting_passengers);
            // notify next train that the station is now available
                System.out.println("TRAIN: Next TRAIN may now enter");
                System.out.println("===============================");
                train_lock.notify();
        }


        return; // not sure if needed
    }

    public void wait_for_train(Passenger passenger) throws InterruptedException {
        synchronized (passenger_lock) {
            // add this passenger to the waiting list
                waiting_passengers++;
                list_waiting_passengers.add(passenger);
            // wait for train
                System.out.println("PASSENGER: PASSENGER " + passenger.passenger_id + " is now waiting to be called in STATION " + station_id);
            if (train_boarding) {
                passenger_lock.wait();
            } else {
                while (!train_boarding) {
                    passenger_lock.wait();
                }
            }
            // board
                System.out.println("PASSENGER: PASSENGER " + passenger.passenger_id + " has been called to board in STATION " + station_id);
                on_board();
        }
    }

    private void start_boarding() {
        synchronized (passenger_lock) {
            System.out.println("START_BOARDING: TRAIN " + current_train.train_id + " has started boarding.");
            train_boarding = true;
            passenger_lock.notify();
        }
    }

    public void on_board() {

        synchronized (passenger_lock) {
            System.out.println("ON_BOARD: PASSENGER " + list_waiting_passengers.get(0).passenger_id + " is boarding.");
            // remove current passenger from waiting passengers
                waiting_passengers--;
            // take a seat from train
                current_train.available_seats--;
                System.out.println("ON_BOARD: Remaining PASSENGERS: " + waiting_passengers);
                System.out.println("ON_BOARD: Remaining seats: " + current_train.available_seats);
            // if there are available seats and there are other waiting passengers
                if(current_train.available_seats>0 && waiting_passengers>0){
                    // notify next passenger to board
                        System.out.println("ON_BOARD: Calling next PASSENGER");
                        passenger_lock.notify();
                }
            // else notify train to leave (and return?)
                else{
                    if (waiting_passengers==0) {
                        System.out.println("ON, BOARD: There are no more waiting PASSENGERS.");
                    } else if (current_train.available_seats==0){
                        System.out.println("ON, BOARD: " + current_train.available_seats + " has no more available seats.");
                    }
                    synchronized (current_train_lock){
                        System.out.println("ON_BOARD, TRAIN LEAVING: TRAIN " + current_train.train_id);
                        current_train_lock.notify();
                    }
                }
        }
    }

}
