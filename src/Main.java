import engine.CalTrain2;
import engine.model.Logic;

/**
 * Created by jmkhilario on 28/07/2017.
 */
public class Main {


    public static void main(String[] args) throws InterruptedException {

        CalTrain2 calTrain2 = new CalTrain2("CalTrain2", 720, 480, 60);

        calTrain2.start();
    }
}
