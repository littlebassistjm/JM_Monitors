/**
 * Created by jmkhilario on 29/07/2017.
 */
public class Passenger extends Thread{

    public Station station;
    public int passenger_id;

    public Passenger(Station station, int passenger_id){
        this.station = station;
        this.passenger_id = passenger_id;
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
