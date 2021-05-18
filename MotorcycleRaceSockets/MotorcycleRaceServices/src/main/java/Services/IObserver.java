package Services;

import java.rmi.Remote;

public interface IObserver {
    void riderRegistered() throws RaceException;
}
