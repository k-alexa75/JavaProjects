import Controller.LogInViewController;

import Repository.RaceRepository.RaceRepository;
import Repository.RegistrationRepository.RegistrationRepository;
import Repository.RiderRepository.RiderRepository;
import Repository.UserRepository.UserRepository;

import Services.Service;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/LogInView.fxml"));
            Parent root = loader.load();
            LogInViewController logInViewController = loader.getController();
            logInViewController.initLogInViewController(getService());
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Log In");
            primaryStage.show();
        }catch(Exception e){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Error while starting app "+e);
            alert.showAndWait();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
    static Service getService() {
        Properties properties = new Properties();
        try {
            System.out.println("Searching bd.config in directory " + ((new File(".")).getAbsolutePath()));
            properties.load(new FileReader("D:\\GitHubRepository -- Java\\JavaProjects\\MotorcycleRace\\src\\main\\resources\\bd.config"));
            UserRepository userRepository = new UserRepository(properties);
            RaceRepository raceRepository = new RaceRepository(properties);
            RiderRepository riderRepository = new RiderRepository(properties);
            RegistrationRepository registrationRepository = new RegistrationRepository(properties);

            return new Service(userRepository, raceRepository, riderRepository, registrationRepository);

        }catch (IOException ex){
            System.err.println("Configuration file bd.config not found" + ex);
        }
        return null;
    }
}
