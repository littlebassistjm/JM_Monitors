package engine.model;

import java.util.ArrayList;

/**
 * Created by jmkhilario on 28/07/2017.
 */
public class Train extends Thread {
    int train_id;
    int max_seats;
    int available_seats = 0;
    Station current_station = null;
    ArrayList<Station> station_list;
    ArrayList<Passenger> passenger_list;
    int station_index;
    int num_stations;
    boolean thereAreWaitingPassengers = true;

    public Train(int train_id, int max_seats, ArrayList<Station> station_list){
        this.train_id = train_id;
        this.max_seats = max_seats;
        this.available_seats = max_seats;
        this.station_list = station_list;
        this.num_stations = station_list.size();
        this.current_station = station_list.get(0);
        this.station_index = 0;
        this.passenger_list = new ArrayList<>();
    }

    @Override
    public void run() {
        while (thereAreWaitingPassengers) {
            //while (true) { // <--- infinite version
            while(station_index<num_stations){ // <-- finite version
                // TODO: System.out.println("MOVEMENT: Train " + train_id + " is now approaching Station " + current_station.station_id);
                try {
                    this.current_station.load_train(this);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // TODO: System.out.println("MOVEMENT: engine.model.Train " + train_id + " is going to the next Station.");
                station_index++;
                current_station = station_list.get(station_index%station_list.size());
            }
            station_index = 0;
            thereAreWaitingPassengers = false;
            if(passenger_list.size()>0){
                thereAreWaitingPassengers = true;
            }
            else{
                for (int i=0; i<num_stations; i++){
                    if (station_list.get(i).waiting_passengers>0) thereAreWaitingPassengers = true;
                }
            }

        }
    }

}
