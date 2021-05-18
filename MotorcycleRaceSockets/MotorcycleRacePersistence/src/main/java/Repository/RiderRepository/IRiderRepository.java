package Repository.RiderRepository;

import Domain.Rider;

public interface IRiderRepository {

    Iterable<Rider> findAll();
    void save(Rider entity);
    Iterable<Rider> getRidersByTeam(String team);
    Rider getRiderByName(String firstName, String lastName);
}
