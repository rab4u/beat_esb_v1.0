/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.relique.jdbc.csv.CsvDriver;

/**
 *
 * @author Ravindra
 */
public class CSVSQLEngine {
    
    public Connection getFFConn(String path) throws Exception {
        Connection conn = null;

        File f = new File(path);
        path = f.getParent();
        System.out.println("CSV ENGINE FF CONN PATH:" + path);
        // Load the driver.
        Class.forName("org.relique.jdbc.csv.CsvDriver");
        conn = DriverManager.getConnection("jdbc:relique:csv:" + path);
        return conn;
    }
    
    public Connection getFFConn(String path,String header) throws Exception {
        Connection conn = null;

        File f = new File(path);
        path = f.getParent();
        System.out.println("CSV ENGINE FF CONN PATH:" + path);
        // Load the driver.
        Class.forName("org.relique.jdbc.csv.CsvDriver");
        Properties csvprops = new java.util.Properties();
        csvprops.put("suppressHeaders", "true");
        csvprops.put("headerline", header);
        csvprops.put("skipLeadingDataLines",1);
        conn = DriverManager.getConnection("jdbc:relique:csv:" + path,csvprops);
        return conn;
    }

    public String getFFCount(Connection conn, String qry) throws SQLException {

        String count = "";

        System.out.println("CSV ENGINE FF COUNT QRY:" + qry);
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery(qry);

        results.next();
        count = results.getString(1);
        boolean append = true;
        CsvDriver.writeToCsv(results, System.out, append);

        // Clean up
        conn.close();

        return count;

    }

    public String getFFDuplicateCount(Connection conn, String qry) throws SQLException {

        System.out.println("CSV ENGINE FF DUP QRY:" + qry);

        Statement stmt = conn.createStatement();

        // Select the ID and NAME columns from sample.csv
        ResultSet results = stmt.executeQuery(qry);

        int count = 0;

        while (results.next()) {
            count++;
        }
        boolean append = true;
        CsvDriver.writeToCsv(results, System.out, append);

        // Clean up
        conn.close();

        return String.valueOf(count);

    }
    
    public List getFFColumns(String path, String qry) throws Exception {

        System.out.println("CSV ENGINE FF COLUMNS");

        Connection conn = getFFConn(path);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(qry);
        String colname;
        List collist = new ArrayList();

        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            colname = rs.getMetaData().getColumnName(i);
            System.out.println(rs.getMetaData().getColumnTypeName(i));
            collist.add(colname);

        }
        System.out.println("CSV COLUMN LIST : " + collist);
        return collist;
    }
    
    public String getFFColumnType(String path, String col, String filename,boolean flag) throws Exception {

        System.out.println("CSV ENGINE FF COLUMNS");

        Connection conn = getFFConn(path);
        Statement stmt = conn.createStatement();
        String qry;
        if(flag){
            qry = "select "+col+" from "+filename;
        }
        else{
            qry =  "select sum("+col+") from "+filename;
        }
        System.out.println("Query Gen: "+qry);
        ResultSet rs = stmt.executeQuery(qry);
        String coltype;
        //List collist = new ArrayList();

        //for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            coltype = rs.getMetaData().getColumnTypeName(1);
            coltype = coltype.equalsIgnoreCase("expression")?"string":coltype;
          //  collist.add(coltype);

        //}
        System.out.println("CSV COLUMN TYPE : " + coltype);
        return coltype;
    }

    public ObservableList getFFTableData(String path, String SQL) throws Exception {

        System.out.println("CSV ENGINE FF TABLE DATA");

        Connection conn = getFFConn(path);
        String temp;
        int j = 0;
        ObservableList<Object> data = FXCollections.observableArrayList();

        Statement stmt = conn.createStatement();

        // Select the ID and NAME columns from sample.csv
        ResultSet rs = stmt.executeQuery(SQL);
        
        while (rs.next()) {
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            //row.add(String.valueOf(++j));
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                //Iterate Column
                temp = rs.getString(i);
                if (temp == null) {
                    row.add("null");
                } else {
                    row.add(temp);
                }
            }
            System.out.println("Row [1] added " + row);
            data.add(row);
        }
        
        return data;
    }
    

        public ObservableList getFFTableData(String path, String SQL, String header) throws Exception {

        System.out.println("CSV ENGINE FF TABLE DATA");

        Connection conn = getFFConn(path,header);
        String temp;
        int j = 0;
        ObservableList<Object> data = FXCollections.observableArrayList();

        Statement stmt = conn.createStatement();

        // Select the ID and NAME columns from sample.csv
        ResultSet rs = stmt.executeQuery(SQL);
        
        while (rs.next()) {
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            //row.add(String.valueOf(++j));
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                //Iterate Column
                temp = rs.getString(i);
                if (temp == null) {
                    row.add("null");
                } else {
                    row.add(temp);
                }
            }
            System.out.println("Row [1] added " + row);
            data.add(row);
        }
        
        return data;
    }
    
    
  /*
  public static void main(String[] args) throws Exception {
        CSVSQLEngine cse = new CSVSQLEngine();
        //cse.getFFColumns("F:/CollegeScorecard_Raw_Data/test.csv","select * from test");
       // cse.getFFColumnType("F:/CollegeScorecard_Raw_Data/test.csv","col2","test");
        cse.getFFTableData("F:/CollegeScorecard_Raw_Data/test.csv", " select count(col1) from test group by col1 having count(col1) > 5");
    }
    
  */  
}
