package Services;

import Domain.Race;
import Domain.Registration;
import Domain.Rider;
import Domain.User;
import Repository.RaceRepository.RaceRepository;
import Repository.RegistrationRepository.RegistrationRepository;
import Repository.RiderRepository.RiderRepository;
import Repository.UserRepository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service {
    private UserRepository userRepository;
    private RaceRepository raceRepository;
    private RiderRepository riderRepository;
    private RegistrationRepository registrationRepository;

    public Service(UserRepository userRepository, RaceRepository raceRepository, RiderRepository riderRepository, RegistrationRepository registrationRepository) {
        this.userRepository = userRepository;
        this.raceRepository = raceRepository;
        this.riderRepository = riderRepository;
        this.registrationRepository = registrationRepository;
    }

    public List<Race> getAllRaces(){
        Iterable<Race> raceIterable = raceRepository.findAll();
        List<Race> races = StreamSupport.stream(raceIterable.spliterator(), false).collect(Collectors.toList());
        return races;
    }

    public Race getRaceByEngineCapacity(int engineCapacity){
        return raceRepository.getRaceByEngineCapacity(engineCapacity);
    }

    public void saveRider(Rider rider){
        riderRepository.save(rider);
    }

    public List<Rider> getRidersByTeam(String team){
        Iterable<Rider> riderIterable = riderRepository.getRidersByTeam(team);
        List<Rider> riders = StreamSupport.stream(riderIterable.spliterator(), false).collect(Collectors.toList());
        return riders;
    }

    public void saveRegistration(Registration registration){
        registrationRepository.save(registration);
    }

    public int registeredRidersInARace(int raceID){
        int riders=0;
        for(Registration registration:registrationRepository.findAll()){
            if (registration.getRaceID() == raceID)
                riders+=1;
        }
        return riders;
    }

    public User getUserByUsernameAndPassword(String username, String password){
        return userRepository.getUserByUsernameAndPassword(username, password);
    }

    public Rider getRiderByName(String firstName, String lastName){
        return riderRepository.getRiderByName(firstName, lastName);
    }
}
