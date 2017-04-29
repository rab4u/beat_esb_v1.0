/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Ravindra
 */
public class SaveConnections {
    
    private String connname="";
    private String hosturl="";
    private String usrname="";
    private String pswd="";
    private String drvclassnme="";
    private String drvpath="";
     
    
    public SaveConnections(String connname, String hosturl, String usrname, String pswd, String  drvclassnme, String drvpath){
   
        this.connname = connname;
        this.hosturl = hosturl;
        this.usrname = usrname;
        this.pswd = pswd;
        this.drvclassnme = drvclassnme;
        this.drvpath = drvpath;
        
        try {
            File file = new File("conn/" + connname + ".con");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                    file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(connname);
            bw.write("\n");
            bw.write(hosturl);
            bw.write("\n");
            bw.write(usrname);
            bw.write("\n");
            bw.write(pswd);
            bw.write("\n");
            bw.write(drvclassnme);
            bw.write("\n");
            bw.write(drvpath);
            bw.write("\n");
            bw.close();
	} catch (IOException ex) {
            new ExceptionUI(ex);
	}
        
    }
    
    
}
