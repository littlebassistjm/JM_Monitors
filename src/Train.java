/**
 * Created by jmkhilario on 28/07/2017.
 */
public class Train extends Thread {
    int TRAIN_ID;
    int MAX_SEATS;
    int num_passengers = 0;
    Station current_station = null;

    public Train(int TRAIN_ID, int MAX_SEATS, Station station){
        this.TRAIN_ID = TRAIN_ID;
        this.MAX_SEATS = MAX_SEATS;
        this.current_station = station;
    }

    @Override
    public void run() {
        // while (condition?){station(i%8).load_train(this); }
        try {
            this.current_station.load_train(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
