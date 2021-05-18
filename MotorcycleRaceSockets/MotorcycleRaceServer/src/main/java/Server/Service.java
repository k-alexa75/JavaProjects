package Server;

import Domain.*;
import Repository.RaceRepository.RaceRepository;
import Repository.RegistrationRepository.RegistrationRepository;
import Repository.RiderRepository.RiderRepository;
import Repository.UserRepository.UserRepository;
import Services.IObserver;
import Services.IService;
import Services.RaceException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service implements IService {
    private UserRepository userRepository;
    private RaceRepository raceRepository;
    private RiderRepository riderRepository;
    private RegistrationRepository registrationRepository;
    private Map<Integer, IObserver> loggedClients;


    public Service(UserRepository userRepository, RaceRepository raceRepository, RiderRepository riderRepository, RegistrationRepository registrationRepository) {
        this.userRepository = userRepository;
        this.raceRepository = raceRepository;
        this.riderRepository = riderRepository;
        this.registrationRepository = registrationRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login (User user, IObserver client) throws RaceException {
        System.out.println("user in login service");
        System.out.println(user);
        User userR = userRepository.getUserByUsernameAndPassword(user.getUsername(), user.getPassword());

        if (userR!=null){
            if (loggedClients.get(userR.getId())!=null){
                throw new RaceException("User already logged in!");
            }
            loggedClients.put(userR.getId(), client);
        }
        else{
            throw new RaceException("Authentication failed!");
        }
    }

    @Override
    public synchronized void logout(User user, IObserver client) throws RaceException {
        IObserver localClient = loggedClients.remove(user.getId());
        if (localClient == null)
            throw new RaceException("User "+ user.getFirstName() + "is not logged in");

    }

    private final int defaultThreadsNo=5;
    private void notifyRiderRegistered(User user) throws RaceException{
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        System.out.println("User in notify registered");
        System.out.println(user);
        for (User user1 : userRepository.findAll()){
            System.out.println(user1);
            if (!user1.getId().equals(user.getId())){
                System.out.println(user1.getId());
                IObserver client = loggedClients.get(user1.getId());
                if (client != null){
                    executor.execute(() -> {
                        try {
                            System.out.println("Notifying ");
                            client.riderRegistered();
                        } catch (RaceException exception) {
                            System.out.println("Error notifying " + exception);
                        }
                    });
                }
            }
        }
        executor.shutdown();
    }



    @Override
    public synchronized void registerRider(Rider rider, User user) throws RaceException{
        Rider rider1 = riderRepository.getRiderByName(rider.getFirstName(), rider.getLastName());
        Race race = raceRepository.getRaceByEngineCapacity(rider.getEngineCapacity());
        if (rider1 == null){
             riderRepository.save(rider);
             rider = riderRepository.getRiderByName(rider.getFirstName(), rider.getLastName());
             registrationRepository.save(new Registration(rider.getId(), race.getId()));
        }
        else{
            registrationRepository.save(new Registration(rider1.getId(), race.getId()));
        }
        notifyRiderRegistered(user);
    }

    @Override
    public List<Rider> getRidersByTeam(String team){
        Iterable<Rider> riderIterable = riderRepository.getRidersByTeam(team);
        List<Rider> riders = StreamSupport.stream(riderIterable.spliterator(), false).collect(Collectors.toList());
        return riders;
    }


    private List<Race> getAllRaces(){
        Iterable<Race> raceIterable = raceRepository.findAll();
        List<Race> races = StreamSupport.stream(raceIterable.spliterator(), false).collect(Collectors.toList());
        return races;
    }

    private int registeredRidersInARace(int raceID){
        int riders=0;
        for(Registration registration:registrationRepository.findAll()){
            if (registration.getRaceID() == raceID)
                riders+=1;
        }
        return riders;
    }

    @Override
    public List<RowData> getRaces(){
        List<Race> races = getAllRaces();
        List<RowData> rowDataList = new ArrayList<>();

        for (Race race : races){
            int registeredRiders = registeredRidersInARace(race.getId());
            RowData rowData = new RowData(race, registeredRiders);
            rowDataList.add(rowData);
        }
        return rowDataList;
    }

    @Override
    public synchronized User getUserByUsernameAndPassword(String username, String password) {
        return userRepository.getUserByUsernameAndPassword(username, password);
    }


}
