/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Ravindra
 */
public class DBConnectionManager {

    private String JDBCDRIVER = "";
    private String DB_URL = "";
    private String USER = "";
    private String PASS = "";
    private String PATH = "";
    private Connection conn = null;
    private String key = "dangerousboy";

    public DBConnectionManager() {

    }

    public DBConnectionManager(String driver, String url, String uid, String pass, String jarpath) throws ClassNotFoundException, SQLException {

        JDBCDRIVER = driver;
        DB_URL = url;
        USER = uid;
        PASS = pass;
        PATH = jarpath;

        //SETP 1: LOAD THE CLASSPATH
        new ClassLoaderEngine(PATH);

        //STEP 2: Register JDBC driver
        Class.forName(JDBCDRIVER);

            //STEP 3: Open a connection
        //System.out.println("Connecting to database...");
        if (!USER.isEmpty()) {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } else {
            conn = DriverManager.getConnection(DB_URL);
        }
    }

    public Connection getDBConFromFile(String conname) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {

        FileReader fr = null;
        File file = new File("conn/" + conname + ".con");
        fr = new FileReader(file.getAbsoluteFile());
        BufferedReader br = new BufferedReader(fr);
        br.readLine();
        DB_URL = br.readLine();
        USER = br.readLine();
        PASS = br.readLine();
        JDBCDRIVER = br.readLine();
        PATH = br.readLine();

        System.out.println("DB_URL : " + DB_URL);
        System.out.println("USER : " + USER);
        System.out.println("PASS : " + PASS);
        System.out.println("JDBCDRIVER : " + JDBCDRIVER);
        System.out.println("PATH : " + PATH);

        new ClassLoaderEngine(PATH);
        Class.forName(JDBCDRIVER);

        if (!USER.isEmpty()) {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 //conn.getMetaData().getSchemas().next();

            //System.out.println(conn.getMetaData().getSchemas().getString(1));
        } else {
            conn = DriverManager.getConnection(DB_URL);
        }
        return conn;
    }

    public Connection getDBCon() {
        return conn;
    }

    //To GET SCHEMA NAMES Woking for all DBs  
    public List getSchemaNames(Connection conn, String conname) throws SQLException {

        List schlist = new ArrayList();

        String schema_sql_type = new ReadPropertyFile().getSchemaPropval(conname);

        System.out.println("schema_sql_type : " + schema_sql_type);

        if (schema_sql_type.equalsIgnoreCase("default")) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet schemas = meta.getSchemas();

            if (!schemas.next()) {
                schemas = meta.getCatalogs();
                schemas.next();
            }

            do {
                String tableSchema = schemas.getString(1);    // "TABLE_SCHEM"
                //String tableCatalog = schemas.getString(2); //"TABLE_CATALOG"
                schlist.add(tableSchema);
                System.out.println("tableSchema : " + tableSchema);
            } while (schemas.next());
        }
        
        Collections.sort(schlist);
        return schlist;
    }

//to get table names    
    public List getTableNames(Connection conn, String conname, String SCHEMA_NAME) throws SQLException {

        List tablist = new ArrayList();
        String tableType[] = {"TABLE", "T"};
        int index = 3;

        System.out.println("conname :" + conname);
        System.out.println("SCHEMA_NAME :" + SCHEMA_NAME);

        String table_sql_type = new ReadPropertyFile().getTablePropval(conname);

        System.out.println("table_sql_type : " + table_sql_type);

        if (table_sql_type.equalsIgnoreCase("default")) {
            DatabaseMetaData meta = conn.getMetaData();
            // ResultSet tabs = meta.getTables(null,SCHEMA_NAME,null,tableType);   
            ResultSet tabs = meta.getTables(SCHEMA_NAME, SCHEMA_NAME, "%", tableType);

            if (!tabs.next()) {
                Statement stmt = conn.createStatement();
                tabs = stmt.executeQuery("select TABNAME from syscat.tables where tabschema = '" + SCHEMA_NAME + "'");
                tabs.next();
                index = 1;
                System.out.println("Table : " + tabs);

            }

            do {
                String tablename = tabs.getString(index);    // "TABLES"
                tablist.add(tablename);
                System.out.println("table : " + tablename);
            } while (tabs.next());
        } //if default is not working
        else {

            Statement stmt = conn.createStatement();
            ResultSet tabs;
            String qry = table_sql_type + "'" + SCHEMA_NAME + "'";
            System.out.println("Table List Qry : " + qry);
            tabs = stmt.executeQuery(table_sql_type + "'" + SCHEMA_NAME + "'");
            tabs.next();
            index = 1;

            do {
                String tablename = tabs.getString(index);    // "TABLES"
                tablist.add(tablename);
                System.out.println("table : " + tablename);
            } while (tabs.next());

        }
        Collections.sort(tablist);
        return tablist;
    }

    //To get view names
    public List getViewNames(Connection conn, String conname, String SCHEMA_NAME) throws SQLException {

        List viewlist = new ArrayList();
        String viewType[] = {"VIEW", "V"};
        int index = 3;

        System.out.println("conname :" + conname);
        System.out.println("SCHEMA_NAME :" + SCHEMA_NAME);

        String view_sql_type = new ReadPropertyFile().getViewPropval(conname);

        System.out.println("view_sql_type : " + view_sql_type);
        if (view_sql_type.equalsIgnoreCase("default")) {
            DatabaseMetaData meta = conn.getMetaData();
            // ResultSet tabs = meta.getTables(null,SCHEMA_NAME,null,tableType);   
            ResultSet views = meta.getTables(SCHEMA_NAME, SCHEMA_NAME, "%", viewType);

            views.next();
            do {
                String tablename = views.getString(index);    // "TABLES"
                viewlist.add(tablename);
                System.out.println("View : " + tablename);
            } while (views.next());

        } //if default is not working
        else {
            Statement stmt = conn.createStatement();
            ResultSet views;
            String qry = view_sql_type + "'" + SCHEMA_NAME + "'";
            System.out.println("View List Qry : " + qry);
            views = stmt.executeQuery(view_sql_type + "'" + SCHEMA_NAME + "'");
            views.next();
            index = 1;

            do {
                String viewname = views.getString(index);    // "Views"
                viewlist.add(viewname);
                System.out.println("view : " + viewname);
            } while (views.next());

        }
        Collections.sort(viewlist);
        return viewlist;
    }

    public List getColNames(Connection conn,String SQL) throws SQLException {
        
        List data =  new ArrayList();
        
        ResultSet rs = conn.createStatement().executeQuery(SQL);
            
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int numberOfColumns = rsMetaData.getColumnCount();
            String mtdat;
            System.out.println("Column Names :");
             for (int i = 1; i <= numberOfColumns; i++) {
              // get the designated column's SQL type.
              mtdat = rsMetaData.getColumnName(i);
              String[] col = mtdat.split("\\.");
              if(col.length > 1){
                  data.add(col[1]);
              }
              else
              {
                  data.add(mtdat);
              }  
              System.out.println(i+") "+mtdat);
            }
        return data;
        
    }

    public List getColType(Connection conn, String SQL) throws SQLException {

        List data = new ArrayList();

        ResultSet rs = conn.createStatement().executeQuery(SQL);

        ResultSetMetaData rsMetaData = rs.getMetaData();
        int numberOfColumns = rsMetaData.getColumnCount();
        String mtdat;
        System.out.println("Column Types :");
        for (int i = 1; i <= numberOfColumns; i++) {
            // get the designated column's SQL type.
              mtdat = rsMetaData.getColumnTypeName(i);
              String[] col = mtdat.split("\\.");
              if(col.length > 1){
                  data.add(col[1]);
              }
              else
              {
                  data.add(mtdat);
              }  
              System.out.println(i+") "+mtdat);
            }
        return data;
        
    }

    public List getKeyColNames(Connection conn, String SCHEMA_NAME, String table_name) throws SQLException {
        
        List data =  new ArrayList();
        
        String dbtype = conn.getClass().getName();
        ResultSet rs;
        String mtdat;
        
        System.out.println("DBTYPE : "+dbtype);
        try{
        if(dbtype.contains("db2")){
            System.out.println("DBTYPE : "+dbtype);
            String PKSQL =  "SELECT sc.name FROM SYSIBM.SYSCOLUMNS SC WHERE SC.TBNAME = '"+table_name+"' AND sc.identity ='N' AND sc.tbcreator='"+SCHEMA_NAME+"' AND sc.keyseq=1";
            System.out.println("PKSQL : "+PKSQL);
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(PKSQL);
            
            while (rs.next()) {
            mtdat = rs.getString("name");
            data.add(mtdat);
            System.out.println("getPrimaryKeys(): columnName=" + mtdat);
            
            }
        }
        else {
        DatabaseMetaData meta = conn.getMetaData(); 
        rs = meta.getPrimaryKeys(SCHEMA_NAME, SCHEMA_NAME, table_name);
        
        
        while (rs.next()) {
            mtdat = rs.getString("COLUMN_NAME");
            data.add(mtdat);
            System.out.println("getPrimaryKeys(): columnName=" + mtdat);
            }
        }
        
        }
        catch(Exception e){
            System.out.println(e);
        }
        return data;
    }
    
}
