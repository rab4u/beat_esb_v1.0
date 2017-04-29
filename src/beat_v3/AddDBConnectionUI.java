/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *
 * @author Ravindra
 */
public class AddDBConnectionUI {
    
// Create the custom dialog.
private Dialog<Pair<String, String>> dialog;
private ButtonType loginButtonType;
private Button test;
private TextField dbname;
private TextField hosturl;
private TextField username;
private PasswordField password;    
private TextField driverclass;
private TextField jarpath;   
private Connection conn = null;
private Label msglabel;
private LoadConnectionsTreeView lctv;

AddDBConnectionUI(LoadConnectionsTreeView lctv){
    
    this.lctv = lctv;
    
    dialog = new Dialog<>();

    dialog.setTitle("DB Connection");
    dialog.setHeaderText("Add the DB Connection");
    
    // Get the Stage.
    Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();

    // Add a custom icon.
    stage.getIcons().add(new Image(this.getClass().getResource("/icon/dbadd.png").toString()));
    
    // Set the icon (must be included in the project).
    dialog.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icon/dbadd.png"))));

    // Set the button types.
    loginButtonType = new ButtonType("ADD", ButtonData.OK_DONE);
    test = new Button("Test");
    test.setMinWidth(100);
    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
    // Create the username and password labels and fields.
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 30, 10, 10));

    
    dbname = new TextField();
    dbname.setMinWidth(300);
    dbname.setPromptText("Name the Connection");
    hosturl = new TextField();
    hosturl.setPromptText("Enter the JDBC url with port address to connect");
    username = new TextField();
    username.setPromptText("Username");
    password = new PasswordField();
    password.setPromptText("Password");
    jarpath = new TextField();
    jarpath.setPromptText("Please Enter drive class : com.mysql.jdbc.Driver");
    driverclass = new TextField();
    driverclass.setPromptText("Please enter the path of jar");
       
    
    grid.add(new Label("Connection Name:"), 0, 0);
    grid.add(dbname, 1, 0);
    grid.add(new Label("Host URL:"), 0, 1);
    grid.add(hosturl, 1, 1);
    grid.add(new Label("UserName:"), 0, 2);
    grid.add(username, 1, 2);
    grid.add(new Label("Password:"), 0, 3);
    grid.add(password, 1, 3);
    grid.add(new Label("Driver Class:"), 0, 4);
    grid.add(driverclass, 1, 4);
    grid.add(new Label("JDBC JAR Path :"), 0, 5);
    grid.add(jarpath, 1, 5);
    msglabel = new Label("Message: Please Test to enable Add button");
    grid.add(msglabel, 0, 6);
    grid.add(test,2,6);

    // Enable/Disable login button depending on whether a username was entered.
    Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
    loginButton.setDisable(true);
    test.setDisable(true);

    // Do some validation (using the Java 8 lambda syntax).  
     dbname.textProperty().addListener((observable, oldValue, newValue) -> {
        
        if(!driverclass.getText().trim().isEmpty() && !hosturl.getText().trim().isEmpty() && !jarpath.getText().trim().isEmpty()){
        //loginButton.setDisable(newValue.trim().isEmpty());
        test.setDisable(newValue.trim().isEmpty());
        loginButton.setDisable(true);
        msglabel.setText("Message: Please Test to enable Add button");
        }
        
    });
     
    hosturl.textProperty().addListener((observable, oldValue, newValue) -> {
        
        if(!dbname.getText().trim().isEmpty() && !driverclass.getText().trim().isEmpty() && !jarpath.getText().trim().isEmpty()){
        //loginButton.setDisable(newValue.trim().isEmpty());
        test.setDisable(newValue.trim().isEmpty());
        loginButton.setDisable(true);
        msglabel.setText("Message: Please Test to enable Add button");
        }
        
    }); 
    
    jarpath.textProperty().addListener((observable, oldValue, newValue) -> {
        
        if(!dbname.getText().trim().isEmpty() && !hosturl.getText().trim().isEmpty() && !driverclass.getText().trim().isEmpty()){
        //loginButton.setDisable(newValue.trim().isEmpty());
        test.setDisable(newValue.trim().isEmpty());
        loginButton.setDisable(true);
        msglabel.setText("Message: Please Test to enable Add button");
        }
        
    });
    
    
    driverclass.textProperty().addListener((observable, oldValue, newValue) -> {
        
        if(!dbname.getText().trim().isEmpty() && !hosturl.getText().trim().isEmpty() && !jarpath.getText().trim().isEmpty()){
        //loginButton.setDisable(newValue.trim().isEmpty());
        test.setDisable(newValue.trim().isEmpty());
        loginButton.setDisable(true);
        msglabel.setText("Message: Please Test to enable Add button");
        }
        
    });

    dialog.getDialogPane().setContent(grid);

    //test db connection    
    test.setOnAction((ActionEvent event) -> {
        
        try{
        conn = new DBConnectionManager(driverclass.getText(),hosturl.getText(),username.getText(),password.getText(), jarpath.getText()).getDBCon();
        }
        catch(Exception  ex){
            new ExceptionUI(ex);
        }
        if(conn != null){
            loginButton.setDisable(false);
            msglabel.setStyle("-fx-text-fill: green");
            msglabel.setTooltip(new Tooltip(conn.getClass().toString()));
            msglabel.setText("Message: Connection Successfull - " + conn);
            try {
                conn.close();
            } catch (SQLException ex) {
                new ExceptionUI(ex);
            }
        }
        else{
            msglabel.setStyle("-fx-text-fill: red");
            msglabel.setText("Message: Connection Unsuccessfull - " + conn);
        }
            
    });
    
    // Request focus on the username field by default.
    Platform.runLater(() -> dbname.requestFocus());

    // Convert the result to a username-password-pair when the login button is clicked.
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == loginButtonType) {
            return new Pair<>(dbname.getText(), hosturl.getText() +";;"+ username.getText() + ";;" + password.getText());
        }
        return null;
    });
   
    Optional<Pair<String, String>> result = dialog.showAndWait();

    result.ifPresent( dbconstring -> {
        System.out.println("Username=" + dbconstring.getKey() + ", Password=" + dbconstring.getValue());
        new SaveConnections(dbname.getText(),hosturl.getText(),username.getText(),password.getText(),driverclass.getText(),jarpath.getText());
        lctv.appendConnectionTreeView(dbname.getText());
    });

}
}
