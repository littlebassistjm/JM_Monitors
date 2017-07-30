/**
 * Created by jmkhilario on 28/07/2017.
 */
public class Train extends Thread {
    int train_id;
    int MAX_SEATS;
    int available_seats;
    Station current_station = null;

    public Train(int train_id, int MAX_SEATS, Station station){
        this.train_id = train_id;
        this.MAX_SEATS = MAX_SEATS;
        this.available_seats = MAX_SEATS;
        this.current_station = station;
    }

    @Override
    public void run() {
        // while (condition?){station(i%8).load_train(this); }
        System.out.println("MOVEMENT: Train " + train_id + " is now approaching Station " + current_station.station_id);
        try {
            this.current_station.load_train(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
