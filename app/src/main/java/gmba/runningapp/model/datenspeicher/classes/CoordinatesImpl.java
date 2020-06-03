package gmba.runningapp.model.datenspeicher.classes;

public class CoordinatesImpl implements Coordinates {
    double[] coords;

    public CoordinatesImpl(double longitude, double latitude ){
        coords[0] = longitude;
        coords[1] = latitude;
    }

    @Override
    public double[] getCoordinates() {
        return this.coords;
    }
}
