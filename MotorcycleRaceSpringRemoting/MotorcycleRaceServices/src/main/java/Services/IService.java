package Services;

import Domain.Rider;
import Domain.RowData;
import Domain.User;

import java.util.List;

public interface IService{

    void login(User user, IObserver client) throws RaceException;

    void logout(User user, IObserver client) throws RaceException;

    void registerRider(Rider rider, User user) throws RaceException;

    List<RowData> getRaces() throws RaceException;

    List<Rider> getRidersByTeam(String team) throws RaceException;

    User getUserByUsernameAndPassword(String username, String password) throws RaceException;

}
