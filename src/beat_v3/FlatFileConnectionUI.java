/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *
 * @author Ravindra
 */
public class FlatFileConnectionUI {
    
    
// Create the custom dialog.
private Dialog<Pair<String, String>> dialog;
private ButtonType loginButtonType;
private Button test;
private Button localfilebt;
private TextField hosturl;
private TextField username;
private PasswordField password;    
private ComboBox filetypecmb;
private TextField jarpath;   
private Connection conn = null;
private Label msglabel;
private LoadFlatFilesTreeView lctv;
private CheckBox remotefilecb;

//file chooser
final FileChooser fileChooser = new FileChooser();
File file;

FlatFileConnectionUI(LoadFlatFilesTreeView lctv,VBox mainvbox){

    this.lctv = lctv;
    
    dialog = new Dialog<>();

    dialog.setTitle("FlatFile Connection");
    dialog.setHeaderText("Add the Flat File Connection");
    
    // Get the Stage.
    Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();

    // Add a custom icon.
    stage.getIcons().add(new Image(this.getClass().getResource("/icon/filesicon.png").toString()));
    
    // Set the icon (must be included in the project).
    dialog.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icon/filesicon.png"))));

    // Set the button types.
    loginButtonType = new ButtonType("ADD", ButtonBar.ButtonData.OK_DONE);
    test = new Button("Test");
    test.setMinWidth(100);
    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
    // Create the username and password labels and fields.
    GridPane grid = new GridPane();
    grid.setHgap(5);
    grid.setVgap(5);
    grid.setPadding(new Insets(20, 10, 10 ,10));

    
    localfilebt = new Button();
    localfilebt.setText("Local File");
    localfilebt.setMaxWidth(150);
    remotefilecb = new CheckBox("Remote File");
    //remotefilecb.setDisable(true);
    hosturl = new TextField();
    hosturl.setPromptText("Enter the FTP / SFTP server address : port");
    hosturl.setDisable(true);
    username = new TextField();
    username.setPromptText("Username");
    username.setDisable(true);
    password = new PasswordField();
    password.setPromptText("Password");
    password.setDisable(true);
    jarpath = new TextField();
    jarpath.setPromptText("Please Enter Remote path : /home/myfolder/file.csv");
    jarpath.setDisable(true);
    filetypecmb = new ComboBox();
    filetypecmb.setPromptText("Choose File Type");
    ObservableList ftypelist =FXCollections.observableArrayList();
    ftypelist.add("TXT");
    ftypelist.add("CSV");
    ftypelist.add("XLS");
    ftypelist.add("XLSX");
    ftypelist.add("JSON");
    ftypelist.add("XML");
    filetypecmb.setItems((ObservableList) ftypelist);
       
    
    grid.add(new Label("Choose:"), 0, 0);
    grid.add(remotefilecb, 1, 0);
    grid.add(localfilebt, 2, 0);   
    grid.add(new Label("Host URL:"), 0, 1);
    grid.add(hosturl, 1, 1,2,1);
    grid.add(new Label("UserName:"), 0, 2);
    grid.add(username, 1, 2,2,1);
    grid.add(new Label("Password:"), 0, 3);
    grid.add(password, 1, 3,2,1);
    grid.add(new Label("Choose File type:"), 0, 4);
    grid.add(filetypecmb, 1, 4,2,1);
    grid.add(new Label("Remote Path:"), 0, 5);
    grid.add(jarpath, 1, 5,2,1);
    msglabel = new Label("Message: Please Test to enable Add button");
    grid.add(msglabel, 0, 6,2,1);
    grid.add(test,2,6);

    // Enable/Disable login button depending on whether a username was entered.
    Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
    loginButton.setDisable(true);
    test.setDisable(true);

    // Do some validation (using the Java 8 lambda syntax). 
    
    remotefilecb.selectedProperty().addListener((observable, oldValue, newValue) -> {
        
        if(remotefilecb.isSelected()){
        hosturl.setDisable(false);
        username.setDisable(false);
        password.setDisable(false);
        jarpath.setDisable(false);
        }
        else
        {
        hosturl.setDisable(true);
        username.setDisable(true);
        password.setDisable(true);
        jarpath.setDisable(true);
        }
        
    });
    
     localfilebt.textProperty().addListener((observable, oldValue, newValue) -> {
        
        if(!filetypecmb.getValue().toString().trim().isEmpty() && !hosturl.getText().trim().isEmpty() && !jarpath.getText().trim().isEmpty()){
        //loginButton.setDisable(newValue.trim().isEmpty());
        test.setDisable(newValue.trim().isEmpty());
        loginButton.setDisable(true);
        msglabel.setText("Message: Please Test to enable Add button");
        }
        
    });
     
    hosturl.textProperty().addListener((observable, oldValue, newValue) -> {
        
        if(!localfilebt.getText().trim().isEmpty() && !filetypecmb.getValue().toString().trim().isEmpty() && !jarpath.getText().trim().isEmpty()){
        //loginButton.setDisable(newValue.trim().isEmpty());
        test.setDisable(newValue.trim().isEmpty());
        loginButton.setDisable(true);
        msglabel.setText("Message: Please Test to enable Add button");
        }
        
    }); 
    
    jarpath.textProperty().addListener((observable, oldValue, newValue) -> {
        
        if(!localfilebt.getText().trim().isEmpty() && !hosturl.getText().trim().isEmpty() && !filetypecmb.getValue().toString().trim().isEmpty()){
        //loginButton.setDisable(newValue.trim().isEmpty());
        test.setDisable(newValue.trim().isEmpty());
        loginButton.setDisable(true);
        msglabel.setText("Message: Please Test to enable Add button");
        }
        
    });

    dialog.getDialogPane().setContent(grid);
   
    //localfilebt
    localfilebt.setOnAction((ActionEvent event) -> {
        
        String fname="";
        String fpath="";
        try{
            
        Stage mainstage = (Stage) mainvbox.getScene().getWindow();
        file = fileChooser.showOpenDialog(mainstage);
        FileReader fr = null;
        fname = file.getAbsoluteFile().getName();
        fpath = file.getAbsoluteFile().getAbsolutePath();
        }
        catch(Exception  ex){
           // new ExceptionUI(ex);
        }
        if(file != null){
            msglabel.setStyle("-fx-text-fill: black");
            msglabel.setTooltip(new Tooltip(fpath));
            msglabel.setText("Message: "+fname);
        }
        else{
            msglabel.setStyle("-fx-text-fill: red");
            msglabel.setText("Message: Unmatched File Format");
        }
            
    });

    //test file connection    
    test.setOnAction((ActionEvent event) -> {
        
        try{
            if (file.isFile() && file.getAbsolutePath().contains(filetypecmb.getValue().toString().toLowerCase())) {
                msglabel.setStyle("-fx-text-fill: green");
                msglabel.setTooltip(new Tooltip(file.getAbsolutePath()));
                msglabel.setText("Message: Format Matched ..."+file.getName());
                loginButton.setDisable(false);
            } else {
                msglabel.setStyle("-fx-text-fill: red");
                msglabel.setText("Message: Unmatched File Format");
            }
        
        }
        catch(Exception  ex){
            new ExceptionUI(ex);
        }

        
            
    });
    
    
    filetypecmb.valueProperty().addListener((observable, oldValue, newValue) -> {
        
        if((filetypecmb.getValue().toString().equals("TXT") || filetypecmb.getValue().toString().equals("CSV") || filetypecmb.getValue().toString().equals("JSON") || filetypecmb.getValue().toString().equals("XLSX") || filetypecmb.getValue().toString().equals("XML") || filetypecmb.getValue().toString().equals("XLS"))){
            test.setDisable(false);
        }
        else
        {
            test.setDisable(true);
        }
        
    });
    
     
     
     // Convert the result to a username-password-pair when the login button is clicked.
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == loginButtonType) {
            return new Pair<>(hosturl.getText(), username.getText() + ";;" + password.getText());
        }
        return null;
    });
     
    Optional<Pair<String, String>> result = dialog.showAndWait();
    
     result.ifPresent( dbconstring -> {
        System.out.println("Clicked - Add Button");
        new SaveFF(file.getName(),file.getAbsolutePath(),filetypecmb.getValue().toString(),lctv);
        
    });
     
    }
    
    
}
