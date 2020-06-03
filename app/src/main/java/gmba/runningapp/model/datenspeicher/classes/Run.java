package gmba.runningapp.model.datenspeicher.classes;

import java.util.List;

public interface Run {
    int getId();
    float getTime();
    float getDistance();
    float getSpeed();
    List<Coordinates> getCoordinates();
}
