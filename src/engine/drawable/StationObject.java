package engine.drawable;

import jGame.model.game.GameObject;
import jGame.model.graphics.Camera;
import jGame.model.input.Input;
import jGame.model.math.Vector2f;
import jGame.model.physics.Physics2D;
import jGame.view.Renderer;

import java.awt.*;
import java.util.Arrays;

/**
 * JM_Monitors created by jmkhilario on 02/08/2017.
 */
public class StationObject extends Physics2D implements GameObject {

    private static final Color ACTIVE = Color.GREEN;
    private static final Color INACTIVE = Color.BLACK;
    private static final int DIAMETER = 50;

    private Vector2f initialPos;
    private Color fill;
    private int size;
    private int stroke;
    private String name;
    private /*String*/ int train_id = 0;
    private String passenger_ids = null;
    private int available_seats = 0;

    public StationObject(float x, float y, String name, int station_object_id){
        super();
        size = DIAMETER;
        initialPos = new Vector2f(x,y);
        setName(name);
        reset();
    }

    public void isActive(boolean value){
        if(value)
            fill = ACTIVE;
        else
            fill = INACTIVE;
    }

    @Override
    public void reset() {
        fill = INACTIVE;
        stroke = 2;
        position.setValue(initialPos.getX() - size / 2, initialPos.getY() - size / 2);
    }

    @Override
    public void input(Input input, long deltaTime, Camera camera) {

    }

    @Override
    public void logic(long deltaTime) {

    }

    @Override
    public void render(Renderer renderer, Camera camera) {
        renderer.getRendIn().setStroke(new BasicStroke(stroke));
        renderer.getRendIn().setColor(fill);
        // state shape
            renderer.getRendIn().drawOval(
                    (int)(position.getX() + camera.getPosition().getX()),
                    (int)(position.getY() + camera.getPosition().getY()),
                    size, size);
        // current train
        if (train_id!=0) {
            renderer.getRendIn().drawString(
                "Train: " + String.valueOf(train_id),
                (int)(position.getX() + camera.getPosition().getX() + size + 10),
                    (int)(position.getY() + camera.getPosition().getY() + size / 2 - 15));
        }
        else if (train_id==0){
            renderer.getRendIn().drawString(
                    "Train: N/A",
                    (int)(position.getX() + camera.getPosition().getX() + size + 10),
                    (int)(position.getY() + camera.getPosition().getY() + size / 2 - 15));
        }
        // station ID
            renderer.getRendIn().drawString(
                name,
                (int)(position.getX() + camera.getPosition().getX() + size / 2 - 5),
                (int)(position.getY() + camera.getPosition().getY() + size / 2 - 5));
        // current passengers
            if (passenger_ids!=null && passenger_ids!="null") {
                renderer.getRendIn().drawString(
                    "Waiting Passengers: " + passenger_ids,
                    (int)(position.getX() + camera.getPosition().getX() + size + 10),
                        (int)(position.getY() + camera.getPosition().getY() + size / 2));
            }
            else {
                renderer.getRendIn().drawString(
                        "Waiting Passengers: None",
                        (int)(position.getX() + camera.getPosition().getX() + size + 10),
                        (int)(position.getY() + camera.getPosition().getY() + size / 2));
            }
        // available seats
            renderer.getRendIn().drawString(
                    "Available seats: " + available_seats,
                    (int)(position.getX() + camera.getPosition().getX() + size + 10),
                    (int)(position.getY() + camera.getPosition().getY() + size / 2 + 15));
    }

    @Override
    public void close() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTrain_id() {
        return train_id;
    }

    public void setTrain_id(int train_id) {
        this.train_id = /*String.valueOf*/(train_id);
    }

    /*public int[] getPassenger_ids() {
        return passenger_ids;
    }*/

    public void setPassenger_ids(int[] passenger_ids) {
        this.passenger_ids = Arrays.toString(passenger_ids);
    }

    public int getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(int available_seats) {
        this.available_seats = available_seats;
    }
}
