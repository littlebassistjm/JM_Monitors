package engine.model;

import engine.drawable.StationObject;

import java.util.ArrayList;

/**
 * Created by jmkhilario on 28/07/2017.
 */
public class Station {
    // MONITOR managing trains and passengers

    private boolean station_occupied;
    private boolean train_boarding;
    Train current_train;
    int station_id;
    int waiting_passengers = 0;
    private ArrayList<Passenger> list_waiting_passengers;
    private ArrayList<Train> list_waiting_trains;
    private StationObject stationObject;
    private int[] passenger_ids;
    private int last_train_boarded = 0;
    // stationObject.setFill etc to change color when something happens

    private final static Object leave_station_lock = new Object();
    private final  Object enter_station_lock;
    private final  Object passenger_lock;
    private final  Object current_train_lock;
    private final static Object passenger_get_off_lock = new Object();
    private final static Object passenger_get_on_lock = new Object();

    public Station(int station_id, StationObject stationObject) {
        this.station_id = station_id;
        this.station_occupied = false;
        this.train_boarding = false;
        this.current_train = null;
        this.list_waiting_passengers = new ArrayList<>();
        this.list_waiting_trains = new ArrayList<>();
        this.stationObject = stationObject;
        this.enter_station_lock = new Object();
        this.passenger_lock = new Object();
        this.current_train_lock = new Object();
    }

    public void load_train(Train train) throws InterruptedException {

        getList_waiting_trains().add(train);

        synchronized (this.enter_station_lock) {
            if (getList_waiting_trains().size() > 1) {
                //System.out.println("LOCK 1 CHECK: TRAIN " + train.train_id + " STATION " + station_id);
                // check if a train is in the station already
                /*if (station_occupied) { // if occupied, wait
                    TODO: System.out.println("TRAIN: STATION " + station_id + " is occupied. TRAIN " + train.train_id + " is waiting");
                    //while (station_occupied || train_boarding) {
                    enter_station_lock.wait();
                    //}
                }*/

                while (this.last_train_boarded != train.train_id - 1) {
                    this.enter_station_lock.wait();
                }

                // enter_station_lock.wait();
            }


            //System.out.println("LOCK 1 RELEASE: TRAIN " + train.train_id + "STATION " + station_id);

            synchronized (this.current_train_lock) {
                //System.out.println("LOCK 2 CHECK: TRAIN " + train.train_id + " STATION " + station_id);
                // occupy station


                // remove train from waiting trains
                // TODO: System.out.println("TRAIN: STATION " + this.station_id + " is no longer occupied. TRAIN " + train.train_id + " is now entering.");
                this.station_occupied = true;
                this.current_train = train;
                getList_waiting_trains().remove(this.current_train);
                // TODO: System.out.println("TRAIN: TRAIN " + train.train_id + " has occupied STATION " + this.station_id + ".");

                //STATUS
                Status.getInstance().print_status();

                // release previously boarded passengers
                if (current_train.available_seats < current_train.max_seats) {
                    for (int i = 0; i < current_train.max_seats - current_train.available_seats; i++) {
//                      System.out.println(current_train.passenger_list.get(i).station.station_id + " " + current_train.passenger_list.get(i).destined_station + " " + station_id);
                        if (current_train.passenger_list.get(i).destined_station == this.station_id) {
                            current_train.passenger_list.remove(i);
                            current_train.available_seats++;
                        }
                    }
                }

                // TODO: System.out.println("TRAIN: TRAIN " + train.train_id + " has released its passengers.");
                // if there are waiting passengers, board
                if (this.waiting_passengers > 0) {
                    // start boarding
                    start_boarding();
                    // wait while passengers are boarding
                    this.current_train_lock.wait();
                } else { // if there are no waiting passengers, leave/do nothing
                    // TODO: System.out.println("TRAIN: There are no waiting PASSENGERS.");
                }

// leave, release all locks
                this.train_boarding = false;
                this.station_occupied = false;
                this.current_train = null;
                this.last_train_boarded = train.train_id;

                // TODO: System.out.println("TRAIN: Remaining PASSENGERS: " + waiting_passengers);
                // TODO: System.out.println("TRAIN: Next TRAIN may now enter");
                // TODO: System.out.println("===============================");

                synchronized (this.enter_station_lock) {
                    this.enter_station_lock.notify();
                }

            } // OUT AFTER THIS
            //enter_station_lock.notify(); // notify next train that the station is now available
            //  } //< ---- THIS
            // System.out.println("LOCK 2 RELEASE: TRAIN " + train.train_id + " STATION " + station_id);

            // synchronized (enter_station_lock) { // THIS


        }
        //  synchronized (enter_station_lock) {
        //  }

        // } // THAT

        return; // not sure if needed

    }

    public void wait_for_train(Passenger passenger) throws InterruptedException {
        synchronized (this.passenger_lock) {
            // add this passenger to the waiting list
            this.waiting_passengers++;
            getList_waiting_passengers().add(passenger);

            // wait for train
            // TODO: System.out.println("PASSENGER: PASSENGER " + passenger.passenger_id + " is now waiting to be called in STATION " + this.station_id);

            if (this.train_boarding) {
                this.passenger_lock.wait();
            } else {
                while (!this.train_boarding) {
                    this.passenger_lock.wait();
                }
            }
            // board
            // TODO: System.out.println("PASSENGER: PASSENGER " + passenger.passenger_id + " has been called to board in STATION " + this.station_id);
            on_board(passenger);
        }
    }

    private void start_boarding() {
        synchronized (this.passenger_lock) {
            // TODO:   System.out.println("START_BOARDING: TRAIN " + current_train.train_id + " has started boarding.");
            this.train_boarding = true;
            this.passenger_lock.notify();
        }
    }

    public void on_board(Passenger passenger) throws InterruptedException {

        synchronized (this.passenger_lock) {
            // TODO: System.out.println("ON_BOARD: PASSENGER " + list_waiting_passengers.get(0).passenger_id + " is boarding.");
            // remove current passenger from waiting passengers
            this.waiting_passengers--;
            getList_waiting_passengers().remove(passenger);

            // take a seat from train
            current_train.available_seats--;

            // add this passenger to passenger list of train
            current_train.passenger_list.add(passenger);

            // TODO: System.out.println("ON_BOARD: Remaining PASSENGERS: " + waiting_passengers);
            // TODO: System.out.println("ON_BOARD: Remaining seats: " + current_train.available_seats);
            // if there are available seats and there are other waiting passengers
            if (current_train.available_seats > 0 && this.waiting_passengers > 0) {
                // notify next passenger to board
                // TODO: System.out.println("ON_BOARD: Calling next PASSENGER");
                this.passenger_lock.notify();
            }
            // else notify train to leave (and return?)
            else {
                if (this.waiting_passengers == 0) {
                    // TODO: System.out.println("ON, BOARD: There are no more waiting PASSENGERS.");
                } else if (current_train.available_seats == 0) {
                    // TODO:  System.out.println("ON, BOARD: " + current_train.available_seats + " has no more available seats.");
                }
                // TODO:  System.out.println("ON_BOARD, TRAIN LEAVING: TRAIN " + current_train.train_id);
                synchronized (this.current_train_lock) {
                    this.current_train_lock.notify();
                }
            }
        }
    }

    public StationObject getStationObject() {
        return this.stationObject;
    }

    public ArrayList<Passenger> getList_waiting_passengers() {
        return this.list_waiting_passengers;
    }

    public ArrayList<Train> getList_waiting_trains() {
        return this.list_waiting_trains;
    }
}
