/**
 * Created by jmkhilario on 28/07/2017.
 */
public class Train extends Thread {
    int train_id;
    int max_seats;
    int available_seats;
    Station current_station = null;

    public Train(int train_id, int max_seats, Station station){
        this.train_id = train_id;
        this.max_seats = max_seats;
        this.available_seats = max_seats;
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

        System.out.println("MOVEMENT: Train " + train_id + " is going to the next Station.");
    }

}
