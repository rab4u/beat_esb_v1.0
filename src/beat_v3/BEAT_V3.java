/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Ravindra
 */
public class BEAT_V3 extends Application {
    
    Stage mainStage;
    Parent root;
    MainStageController controller;
      
    @Override
    public void start(Stage stage) throws Exception {
    this.mainStage=stage;
    mainStage.setTitle("BEAT - BIG DATA & ETL Analytics Tester");
    mainStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/cont.png")));
    mainStage.resizableProperty().setValue(Boolean.TRUE);
    mainStage.setMaximized(true);

    showMainStage();  
    }

    private void showMainStage() throws IOException{
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/MainStage.fxml"));
        root = fxmlLoader.load();   
        System.out.println("Root :" +root);
        controller = fxmlLoader.<MainStageController>getController();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.show();        
    }    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
