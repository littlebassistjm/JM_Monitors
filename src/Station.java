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
                    System.out.println("TRAIN: STATION " + station_id + " is occupied. TRAIN " + train.train_id + " is waiting");
                    list_waiting_trains.add(train);
                    wait();
                    //System.out.println("Station " + station_id + " is no longer occupied. Train " + train.train_id + " is now entering.");
                }

            System.out.println("TRAIN: STATION " + station_id + " is no longer occupied. TRAIN " + train.train_id + " is now entering.");

        // occupy station
            current_train = train;
            station_occupied = true;
            System.out.println("TRAIN: TRAIN " + train.train_id + " has occupied STATION " + station_id + ".");
            //} </ FIRST SYNC>

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
                System.out.println("TRAIN: Next TRAIN may now enter");
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
                    System.out.println("PASSENGER: PASSENGER " + passenger.passenger_id + " is now waiting for the train in STATION " + station_id + ".");
                    wait();
                }
                else if (!train_boarding){
                    System.out.println("PASSENGER: STATION " + station_id + " is now occupied. PASSENGER" + passenger.passenger_id + " waiting for boarding.");
                    wait();
                }

            // board
                if(current_train!=null) {
                    System.out.println("PASSENGER: PASSENGER " + passenger.passenger_id + " has been called to board");
                    on_board();
                }
        }
    }

    private void start_boarding() {
        System.out.println("START_BOARDING: TRAIN " + current_train.train_id + " has started boarding.");
        synchronized (this) {
            train_boarding = true;
            notify();
        }
    }

    public void on_board() {
        System.out.println("ON_BOARD: PASSENGER " + list_waiting_passengers.get(0).passenger_id + " is boarding.");

        synchronized (this) {
            waiting_passengers--;
            current_train.available_seats--;
            list_waiting_passengers.remove(0);
            System.out.println("ON_BOARD: Remaining PASSENGERS: " + waiting_passengers);
        }

        if (waiting_passengers == 0 || current_train.available_seats==0) {
            System.out.println("TEST");
            synchronized (current_train) {
                current_train.notify();
                System.out.println("ON_BOARD, TRAIN LEAVING: TRAIN " + current_train.train_id + " has left STATION " + station_id);
                station_occupied = false;
                train_boarding = false;
                current_train = null;
            }
        } else {
            synchronized (this) {
                System.out.println("ON_BOARD: calling next PASSENGER.");
                notify();
            }
        }

    }

}
