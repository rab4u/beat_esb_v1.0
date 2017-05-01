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
public class QueryGenerator {
    
    
    public String getTotalCntQueries(String schema, String tblname) {

        String qry = "select count(1) from " + schema + "." + tblname;

        return qry;

    }
    
    
    
        
    public String getNullCntQueries(String schema, String tblname, String data,String type) {

        String qry;
 
        if(type.equalsIgnoreCase("db"))
        {
            qry = "select count(1) as "+ data.trim() +" from " + schema + "." + tblname + " where " +data.trim() + " is null";
        }
        else
        {
            qry = "select count(1) as "+ data.trim() +" from " + tblname + " where lower("+data.trim()+") = 'null' or lower("+data.trim()+") = ''";
        }
        
        return qry;

    }
    
    public String getNotNullCntQueries(String schema, String tblname, String data, String type) {

        String qry;

        if (type.equalsIgnoreCase("db")) {
            qry = "select count(1) as " + data.trim() + " from " + schema + "." + tblname + " where " + data.trim()+ " is  not null";
        } else {
            qry = "select count(1) as " + data.trim() + " from " + tblname + " where lower(" + data.trim()+ ") != 'null' or lower("+data.trim()+") != ''";
        }

        return qry;

    }
    
        
    public String getDistinctCntQueries(String schema, String tblname, String[] data, String type) {

        String qry;

        if (type.equalsIgnoreCase("db")) {
            qry = "select count(distinct("+data[2].trim()+")) as " + data[1].trim() + " from " + schema + "." + tblname;
        }
        else{
            qry = "select count(distinct("+data[2].trim()+")) as " + data[1].trim() + " from " + tblname;
        }
        return qry;

    }

        
        
    public String getMaxColQueries(String schema, String tblname, String[] data, String type) {

        String qry;
        if (type.equalsIgnoreCase("db")) {
                qry = "select max("+data[2].trim()+") as " + data[1].trim() + " from " + schema + "." + tblname;
        }
        else{
            qry = "select max("+data[2].trim()+") as " + data[1].trim() + " from " + tblname;
        }
        return qry;

    }
    
    
    public String getMinColQueries(String schema, String tblname, String[] data, String type) {

        String qry;
        if (type.equalsIgnoreCase("db")) {
            qry = "select min("+data[2].trim()+") as " + data[1].trim() + " from " + schema + "." + tblname;
        }
        else{
            qry = "select min("+data[2].trim()+") as " + data[1].trim() + " from " + tblname;
        }
        return qry;

    }
    
    
    public String getSumColQueries(String schema, String tblname, String[] data, String type) {

        String qry="";
        
        String dtype = data[3].trim().toLowerCase();
        
        //System.out.println("Datatype : "+data[3]);
        
        if(dtype.equals("int")||dtype.equals("integer")||dtype.equals("numeric")||dtype.equals("long")||dtype.equals("double")||dtype.equals("decimal")||dtype.equals("bigint")||dtype.equals("smallint")){
            
        if (type.equalsIgnoreCase("db")) {    
            qry = "select sum("+data[2].trim()+") as " + data[1].trim() + " from " + schema + "." + tblname;
        }
        else{
            qry = "select sum("+data[2].trim()+") as " + data[1].trim() + " from " + tblname;
        }
        
        }
        return qry;

    }
    
    public boolean validateSpecialConditon(String data) {

        boolean flag = false;

        String[] notallowed = {"count", "sum", "max", "min", "avg"};

        for (String item : notallowed) {

            if (data.toLowerCase().contains(item)) {
                flag = false;
                break;
            } else {
                flag = true;
            }

        }

        return flag;
    }
    
    
}
