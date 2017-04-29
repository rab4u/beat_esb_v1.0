/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

/**
 *
 * @author Ravindra
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveFF {

    private String filename = "";
    private String filepath = "";
    private String type = "";

    public SaveFF(String filename, String filepath, String type, LoadFlatFilesTreeView lctv) {

        this.filename = filename;
        this.filepath = filepath;
        this.type = type;

        try {

            File file = new File("files/" + type.toLowerCase() + "/" + filename);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                System.out.println(filename);
                file.createNewFile();
                lctv.appendConnectionTreeView(file.getName());
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("local");
            bw.write("\n");
            bw.write(filepath);
            bw.write("\n");
            bw.close();
            
        } catch (IOException ex) {
            new ExceptionUI(ex);
        }

    }
    
    
}