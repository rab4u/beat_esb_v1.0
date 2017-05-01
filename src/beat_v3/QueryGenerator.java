/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import java.util.List;

/**
 *
 * @author Ravindra
 */
public class QueryGenerator {

    public String getTotalCntQueries(String schema, String tblname) {

        String qry = "select count(1) from " + schema + "." + tblname;

        return qry;

    }

    public String getNullCntQueries(String schema, String tblname, String data, String type) {

        String qry;

        if (type.equalsIgnoreCase("db")) {
            qry = "select count(1) as " + data.trim() + " from " + schema + "." + tblname + " where " + data.trim() + " is null";
        } else {
            qry = "select count(1) as " + data.trim() + " from " + tblname + " where lower(" + data.trim() + ") = 'null' or lower(" + data.trim() + ") = ''";
        }

        return qry;

    }

    public String getNotNullCntQueries(String schema, String tblname, String data, String type) {

        String qry;

        if (type.equalsIgnoreCase("db")) {
            qry = "select count(1) as " + data.trim() + " from " + schema + "." + tblname + " where " + data.trim() + " is  not null";
        } else {
            qry = "select count(1) as " + data.trim() + " from " + tblname + " where lower(" + data.trim() + ") != 'null' or lower(" + data.trim() + ") != ''";
        }

        return qry;

    }

    public String getDistinctCntQueries(String schema, String tblname, String data, String type) {

        String qry;

        if (type.equalsIgnoreCase("db")) {
            qry = "select count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + schema + "." + tblname;
        } else {
            qry = "select count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + tblname;
        }
        return qry;

    }

    public String getMaxColQueries(String schema, String tblname, String data, String type) {

        String qry;
        if (type.equalsIgnoreCase("db")) {
            qry = "select max(" + data.trim() + ") as " + data.trim() + " from " + schema + "." + tblname;
        } else {
            qry = "select max(" + data.trim() + ") as " + data.trim() + " from " + tblname;
        }
        return qry;

    }

    public String getMinColQueries(String schema, String tblname, String data, String type) {

        String qry;
        if (type.equalsIgnoreCase("db")) {
            qry = "select min(" + data.trim() + ") as " + data.trim() + " from " + schema + "." + tblname;
        } else {
            qry = "select min(" + data.trim() + ") as " + data.trim() + " from " + tblname;
        }
        return qry;

    }

    public String getSumColQueries(String schema, String tblname, String data, String type) {

        String qry = "";

        String dtype = data.trim().toLowerCase();

        //System.out.println("Datatype : "+data[3]);
        if (dtype.equals("int") || dtype.equals("integer") || dtype.equals("numeric") || dtype.equals("long") || dtype.equals("double") || dtype.equals("decimal") || dtype.equals("bigint") || dtype.equals("smallint")) {

            if (type.equalsIgnoreCase("db")) {
                qry = "select sum(" + data.trim() + ") as " + data.trim() + " from " + schema + "." + tblname;
            } else {
                qry = "select sum(" + data.trim() + ") as " + data.trim() + " from " + tblname;
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

    public String getCmplDataQueries(String schema, String tblname, String data, String type, List header) {
        String qry;
        if (type.equalsIgnoreCase("db")) {
            qry = "select * from " + schema + "." + tblname;
        } else {
            qry = "select " + header.toString().replaceAll("\\[", "").replaceAll("\\]", "").trim() + "  from " + tblname;
        }
        return qry;
    }

    public String getFrst1000Queries(String schema, String tblname, String data, String type, String orderCol, List header) {
        String qry;
        if (type.equalsIgnoreCase("db")) {
            qry = "select * from " + schema + "." + tblname + " order by " + orderCol + " asc limit 1000";
        } else {
            qry = "select " + header.toString().replaceAll("\\[", "").replaceAll("\\]", "").trim() + " from " + tblname + " order by " + orderCol + " asc limit 1000";
        }
        return qry;
    }

    public String getLast1000Queries(String schema, String tblname, String data, String type, String orderCol) {
        String qry;
        if (type.equalsIgnoreCase("db")) {
            qry = "select * from " + schema + "." + tblname + " order by " + orderCol + " desc limit 1000";
        } else {
            qry = "select * from " + tblname + " order by " + orderCol + " desc limit 1000";
        }
        return qry;
    }

}
