import Domain.Race;
import Repository.RaceRepository.RaceRepository;
import Repository.RegistrationRepository.RegistrationRepository;
import Repository.RiderRepository.RiderRepository;
import Repository.UserRepository.UserRepository;
import Server.Service;
import Services.IService;
import Services.RaceException;
import utils.AbstractServer;
import utils.ObjectConcurrentServer;
import utils.ServerException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class StartObjectServer {

    private static int defaultPort=55555;
    public static void main(String[] args) {
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartObjectServer.class.getResourceAsStream("/raceserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find raceserver.properties "+e);
            return;
        }
        UserRepository userRepository = new UserRepository(serverProps);
        RiderRepository riderRepository = new RiderRepository(serverProps);
        RaceRepository raceRepository = new RaceRepository(serverProps);
        RegistrationRepository registrationRepository = new RegistrationRepository(serverProps);

        IService service = new Service(userRepository, raceRepository, riderRepository, registrationRepository);

        int raceServerPort=defaultPort;
        try {
            raceServerPort = Integer.parseInt(serverProps.getProperty("race.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+raceServerPort);

        AbstractServer server = new ObjectConcurrentServer(raceServerPort, service);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
