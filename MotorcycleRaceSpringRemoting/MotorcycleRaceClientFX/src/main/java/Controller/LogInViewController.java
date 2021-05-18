package Controller;

import Domain.User;
import Services.IService;
import Services.RaceException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LogInViewController extends UnicastRemoteObject implements Serializable {

    private IService server;
    private User user;
    private UserConsoleViewController userConsoleViewController;

    Parent mainParent;

    @FXML
    TextField textFieldUsername;

    @FXML
    PasswordField passwordFieldPassword;

    public LogInViewController() throws RemoteException {
    }

    @FXML
    private void initialise(){
    }

    public void initLogInViewController(IService server){
        this.server = server;
    }

    public void setParent(Parent parent){
        mainParent = parent;
    }

    public void setUserConsoleViewController(UserConsoleViewController userConsoleViewController){
        this.userConsoleViewController = userConsoleViewController;
    }

    @FXML
    public void handleLogIn(ActionEvent actionEvent){
        String username = textFieldUsername.getText();
        String password = passwordFieldPassword.getText();

        try {
            user = new User(username, password);
            System.out.println(user);
            server.login(user, userConsoleViewController);

            Stage stage=new Stage();

            stage.setScene(new Scene(mainParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    userConsoleViewController.logOut();
                    System.exit(0);
                }
            });

            user = server.getUserByUsernameAndPassword(username, password);
            System.out.println(user);
            userConsoleViewController.setUser(user);
            userConsoleViewController.initUserConsoleView();
            stage.setTitle("WELCOME " + user.getFirstName());
            stage.show();

            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }catch (RaceException exception){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Motorcycle Race");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }

        textFieldUsername.clear();
        passwordFieldPassword.clear();

    }

    public void showUserConsoleView(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/UserConsoleView.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            //dialogStage.setTitle("Welcome" + user.getFirstName());

            UserConsoleViewController userConsoleViewController = loader.getController();
            //userConsoleViewController.initUserConsoleView(server, ;

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
