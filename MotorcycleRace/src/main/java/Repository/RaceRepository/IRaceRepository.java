package Repository.RaceRepository;

import Domain.Race;

public interface IRaceRepository {

    Iterable<Race> findAll();
    void save(Race entity);
    Race getRaceByEngineCapacity(int engineCapacity);

}
