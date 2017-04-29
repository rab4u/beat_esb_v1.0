/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ravindra
 */
public class ReadPropertyFile {

    String url_50070;
    String url_8088;
    Properties prop;

    public ReadPropertyFile() {

        try {
            prop = new Properties();
            InputStream input = null;
            input = new FileInputStream("config.properties");
            prop.load(input);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadPropertyFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadPropertyFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getDBType(String conname) {

        FileReader fr;
        String db_url;
        String dbtype = null;
        try {
            File file = new File("conn/" + conname + ".con");
            fr = new FileReader(file.getAbsoluteFile());
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            db_url = br.readLine();

            dbtype = db_url.split(":")[1];

            System.out.println("DBTYPE : " + dbtype);

        } catch (FileNotFoundException ex) {
            new ExceptionUI(ex);
        } catch (IOException ex) {
            new ExceptionUI(ex);
        }

        return dbtype;
    }

    public String getSchemaPropval(String conname) {

        return prop.getProperty("" + getDBType(conname) + "_schema_sql");

    }

    public String getTablePropval(String conname) {

        return prop.getProperty("" + getDBType(conname) + "_table_sql");

    }

    public String getViewPropval(String conname) {

        return prop.getProperty("" + getDBType(conname) + "_view_sql");

    }

    public String getColPropval(String conname) {

        return prop.getProperty("" + getDBType(conname) + "_col_sql");

    }

    //Test code   
   /* public static void main(String argd[]) {

        ReadPropertyFile rpf = new ReadPropertyFile();
        System.out.println("Schema Sql: "+rpf.getSchemaPropval("testderby"));
        System.out.println("Table Sql: "+rpf.getTablePropval("testderby"));
        System.out.println("View Sql: "+rpf.getViewPropval("testderby"));
        System.out.println("Col sql: "+rpf.getColPropval("testderby"));
    }
*/
}
