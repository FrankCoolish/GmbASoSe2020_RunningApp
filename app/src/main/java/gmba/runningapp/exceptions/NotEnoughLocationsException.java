package gmba.runningapp.exceptions;

public class NotEnoughLocationsException extends Exception {
    public NotEnoughLocationsException(String errorMessage){
        super(errorMessage);
    }
}
