package Controller;

import Domain.Rider;
import Domain.RowData;
import Domain.User;
import Services.IObserver;
import Services.IService;
import Services.RaceException;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class UserConsoleViewController extends UnicastRemoteObject implements  IObserver, Serializable {

    private IService service;
    private User user;
    ObservableList<RowData> modelRacesTable = FXCollections.observableArrayList();
    ObservableList<Rider> modelRidersTable = FXCollections.observableArrayList();

    @FXML
    TableView<RowData> tableViewRaces;
    @FXML
    TableColumn<RowData, String> columnName;
    @FXML
    TableColumn<RowData, String> columnEngineCapacity;
    @FXML
    TableColumn<RowData, String> columnRegisteredRiders;
    @FXML
    TableView<Rider> tableViewRiders;
    @FXML
    TableColumn<Rider, String> columnFirstName;
    @FXML
    TableColumn<Rider, String> columnLastName;
    @FXML
    TableColumn<Rider, String> columnEngineCapacity2;
    @FXML
    Button buttonSearch;
    @FXML
    Button buttonLogOut;
    @FXML
    Button buttonRegister;
    @FXML
    TextField textFieldFirstName;
    @FXML
    TextField textFieldLastName;
    @FXML
    TextField textFieldTeam;
    @FXML
    TextField textFieldSearch;
    @FXML
    ComboBox comboBoxEngineCapacity;

    public UserConsoleViewController() throws RemoteException {
    }


    @FXML
    private void initialize(){
        //races table
        columnName.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getRace().getName()));
        columnEngineCapacity.setCellValueFactory(param -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getRace().getEngineCapacity())));
        columnRegisteredRiders.setCellValueFactory(param -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getRiders())));

        tableViewRaces.setItems(modelRacesTable);

        //riders table
        columnFirstName.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getFirstName()));
        columnLastName.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getLastName()));
        columnEngineCapacity2.setCellValueFactory(param -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getEngineCapacity())));

        tableViewRiders.setItems(modelRidersTable);

        comboBoxEngineCapacity.getItems().addAll("1000", "600", "300", "125");

    }

    public void initUserConsoleView() throws RaceException {

        //stage.setTitle("WELCOME " + user.getFirstName());
        initModel();
    }

    public void setServer(IService server){this.service=server;}

    public void setUser(User user){
        this.user = user;
    }

    private void initModel() throws RaceException {
        //racesTable

        tableViewRaces.getItems().clear();
        List<RowData> rowDataList = service.getRaces();
        modelRacesTable.setAll(rowDataList);
    }

    public void handleSearchRidersByTeam() throws RaceException{

        String team = textFieldSearch.getText();
        List<Rider> riders = service.getRidersByTeam(team);

        modelRidersTable.setAll(riders);
    }

    public void handleRegister() throws RaceException{
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String team = textFieldTeam.getText();
        int engineCapacity = Integer.parseInt(comboBoxEngineCapacity.getValue().toString());

        Rider rider = new Rider(firstName, lastName, team, engineCapacity);

        service.registerRider(rider, user);

        textFieldFirstName.clear();
        textFieldLastName.clear();
        textFieldTeam.clear();
        comboBoxEngineCapacity.getSelectionModel().clearSelection();

        Platform.runLater(() -> {
            try {
                initModel();
            } catch (RaceException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void logOut(){
        try{
            service.logout(user, this);
        }catch (RaceException exception){
            System.out.println("Logout error "+ exception);
        }
    }

    @FXML
    public void handleLogOut(ActionEvent actionEvent){
        logOut();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void riderRegistered() throws RaceException {
        Platform.runLater(() -> {
            try {
                initModel();
            } catch (RaceException exception) {
                exception.printStackTrace();
            }
        });
    }


}
