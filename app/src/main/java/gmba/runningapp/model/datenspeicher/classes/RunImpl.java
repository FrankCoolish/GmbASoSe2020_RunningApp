package gmba.runningapp.model.datenspeicher.classes;

import java.util.List;

public class RunImpl implements Run {
    private int id;
    private float time;
    private float distance;
    private float speed;
    private String date;
    private List<Coordinates> coordinates;

    public RunImpl(int id, float time, float distance, float speed,String date, List<Coordinates> coordinates){
        this.id = id;
        this.time = time;
        this.distance = distance;
        this.speed = speed;
        this.date = date;
        this.coordinates = coordinates;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public float getTime() {
        return this.time;
    }

    @Override
    public float getDistance() {
        return this.distance;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public String getDate() {
        return this.date;
    }

    @Override
    public List<Coordinates> getCoordinates() {
        return this.coordinates;
    }
}
