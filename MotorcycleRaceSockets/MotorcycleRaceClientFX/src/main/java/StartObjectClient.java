import Controller.LogInViewController;
import Controller.UserConsoleViewController;
import Services.IService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import objectprotocol.ServicesObjectProxy;

import java.io.IOException;
import java.util.Properties;


public class StartObjectClient extends Application {

    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {

        Properties clientProps = new Properties();
        try {
            clientProps.load(StartObjectClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("race.server.host", defaultServer);
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(clientProps.getProperty("race.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IService server = new ServicesObjectProxy(serverIP, serverPort);
        System.out.println("OK");

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
