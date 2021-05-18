package Controller;

import Domain.*;
import Services.Service;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class UserConsoleViewController {

    private Service service;
    private User user;
    private Stage stage;
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

    public void initUserConsoleView(Service service, User user, Stage stage){
        this.service = service;
        this.user = user;
        this.stage = stage;
        stage.setTitle("WELCOME " + user.getFirstName());
        initModel();
    }

    private void initModel(){
        //racesTable
        List<Race> races = service.getAllRaces();
        List<RowData> rowDataList = new ArrayList<>();

        for (Race race : races){
            int registeredRiders = service.registeredRidersInARace(race.getId());
            RowData rowData = new RowData(race, registeredRiders);
            rowDataList.add(rowData);
        }

        modelRacesTable.setAll(rowDataList);
    }

    public void handleSearchRidersByTeam(){

        String team = textFieldSearch.getText();
        List<Rider> riders = service.getRidersByTeam(team);

        modelRidersTable.setAll(riders);
    }

    public void handleRegister(){
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String team = textFieldTeam.getText();
        int engineCapacity = Integer.parseInt(comboBoxEngineCapacity.getValue().toString());

        Rider rider = service.getRiderByName(firstName, lastName);
        Race race = service.getRaceByEngineCapacity(engineCapacity);

        if (rider == null) {
            Rider newRider = new Rider(firstName, lastName, team, engineCapacity);
            service.saveRider(newRider);
            rider = service.getRiderByName(firstName, lastName);
            service.saveRegistration(new Registration(rider.getId(), race.getId()));
        }
        else {
            service.saveRegistration(new Registration(rider.getId(), race.getId()));
        }

        textFieldFirstName.clear();
        textFieldLastName.clear();
        textFieldTeam.clear();
        comboBoxEngineCapacity.getSelectionModel().clearSelection();

        initModel();
    }

    public void handleLogOut(){
        stage.close();
    }
}
