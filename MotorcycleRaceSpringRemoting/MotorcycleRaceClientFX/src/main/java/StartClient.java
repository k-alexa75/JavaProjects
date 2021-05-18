import Controller.LogInViewController;
import Controller.UserConsoleViewController;
import Services.IService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Properties;


public class StartClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //System.setProperty("java.rmi.server.hostname","188.24.52.126");

        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
        IService server=(IService)factory.getBean("raceService");
        System.out.println("Obtained a reference to remote race server");

        server.getRaces().forEach(System.out::println);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("LogInView.fxml"));
            Parent root = loader.load();

            LogInViewController logInViewController = loader.getController();
            logInViewController.initLogInViewController(server);

            FXMLLoader loader2 = new FXMLLoader(getClass().getClassLoader().getResource("UserConsoleView.fxml"));
            Parent root2 = loader2.load();

            UserConsoleViewController userConsoleViewController = loader2.getController();
            userConsoleViewController.setServer(server);

            logInViewController.setUserConsoleViewController(userConsoleViewController);
            logInViewController.setParent(root2);


            primaryStage.setScene(new Scene(root, 500, 300));
            primaryStage.show();


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Error while starting app " + e);
            alert.showAndWait();
        }


    }
}
