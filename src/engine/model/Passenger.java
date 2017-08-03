package engine.model;

/**
 * Created by jmkhilario on 29/07/2017.
 */
public class Passenger extends Thread{

    public Station station;
    public int passenger_id;
    public int destined_station;

    public Passenger(Station station, int passenger_id, int destined_station){
        this.station = station;
        this.passenger_id = passenger_id;
        this.destined_station = destined_station;
    }

    @Override
    public void run() {
        try {
            station.wait_for_train(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
