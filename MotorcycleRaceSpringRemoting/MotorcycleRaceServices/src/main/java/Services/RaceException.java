package Services;

public class RaceException extends Exception {
    public RaceException(){
    }

    public RaceException(String message){
        super(message);
    }

    public RaceException(String message, Throwable cause){
        super(message, cause);
    }
}
