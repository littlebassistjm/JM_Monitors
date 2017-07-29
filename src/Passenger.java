/**
 * Created by jmkhilario on 29/07/2017.
 */
public class Passenger extends Thread{

    Station station;

    public Passenger(Station station){
        this.station = station;
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
