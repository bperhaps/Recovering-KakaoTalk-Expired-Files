package kakao.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import kakao.MainApp;
import kakao.model.Person;
import kakao.util.DateUtil;

public class PersonOverviewController {
	@FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    private MainApp mainApp;

    private void showPersonDetails(Person person) {
    	if(person != null){
    		firstNameLabel.setText(person.getFirstName());
    		lastNameLabel.setText(person.getLastName());
    		streetLabel.setText(person.getStreet());
    		postalCodeLabel.setText(Integer.toString(person.getpostalCode()));
    		cityLabel.setText(person.getCity());
    		birthdayLabel.setText(DateUtil.format(person.getBirthday()));

    	} else {
    		firstNameLabel.setText("");
    		lastNameLabel.setText("");
    		streetLabel.setText("");
    		postalCodeLabel.setText("");
    		cityLabel.setText("");
    		birthdayLabel.setText("");
    	}
    }


    public PersonOverviewController() {
    }

    @FXML
    private void initialize(){

    	firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
    	lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

    	showPersonDetails(null);

    	personTable.getSelectionModel().selectedItemProperty().addListener(
    			(observable, oldValue, newValue) -> showPersonDetails(newValue)
    			);
    }

    public void setmainApp(MainApp mainApp){
    	this.mainApp = mainApp;

    	personTable.setItems(mainApp.getPersonData());
    }

    @FXML
    private void handleDeletePerson() {
    	int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
    	if (selectedIndex >= 0 ){
    		personTable.getItems().remove(selectedIndex);
    	} else {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.initOwner(mainApp.getPrimaryStage());
    		alert.setTitle("No Selection");
    		alert.setHeaderText("No Person Selected");
    		alert.setContentText("Please select a person in the table");

    		alert.showAndWait();
    	}

    }

    @FXML
    private void handleEditPerson(){
    	Person selectedPerson = personTable.getSelectionModel().getSelectedItem();

    	if(selectedPerson != null){
    		boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
    		if(okClicked){
    			showPersonDetails(selectedPerson);
    		}
    	} else {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.initOwner(mainApp.getPrimaryStage());
    		alert.setTitle("No Selection");
    		alert.setHeaderText("No Person Selected");
    		alert.setContentText("Please select a person int he table");

    		alert.showAndWait();
    	}
    }
}
