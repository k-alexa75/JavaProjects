package Services;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver extends Remote {
    void riderRegistered() throws RaceException, RemoteException;
}
