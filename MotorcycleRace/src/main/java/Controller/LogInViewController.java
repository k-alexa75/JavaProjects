package Controller;

import Domain.User;
import Services.Service;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInViewController {

    private Service service;

    @FXML
    TextField textFieldUsername;

    @FXML
    PasswordField passwordFieldPassword;

    @FXML
    private void initialise(){
    }

    public void initLogInViewController(Service service){
        this.service = service;
    }

    @FXML
    public void handleLogIn(){
        String username = textFieldUsername.getText();
        String password = passwordFieldPassword.getText();
        User user = service.getUserByUsernameAndPassword(username, password);

        textFieldUsername.clear();
        passwordFieldPassword.clear();

        showUserConsoleView(user);
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
            userConsoleViewController.initUserConsoleView(service, user, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
