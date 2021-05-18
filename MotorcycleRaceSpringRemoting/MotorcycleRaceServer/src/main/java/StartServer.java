import Repository.RaceRepository.RaceRepository;
import Repository.RegistrationRepository.RegistrationRepository;
import Repository.RiderRepository.RiderRepository;
import Repository.UserRepository.UserRepository;
import Server.Service;
import Services.IService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Properties;


public class StartServer {

    private static int defaultPort=55555;
    public static void main(String[] args) {
        //System.setProperty("java.rmi.server.hostname","188.24.52.126");
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-server.xml");
    }
}
