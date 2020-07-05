package gmba.runningapp.model.datenspeicher.classes;

public class CoordinatesImpl implements Coordinates {
    double[] coords = new double[2];

    public CoordinatesImpl(double latitude, double longitude ){
        coords[0] = latitude;
        coords[1] = longitude;
    }

    @Override
    public double[] getCoordinates() {
        return this.coords;
    }
}
