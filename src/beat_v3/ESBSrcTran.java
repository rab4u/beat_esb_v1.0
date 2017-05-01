package beat_v3;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

/**
 *
 * @author Adithya
 */

/*Data parsing for the transformation of the Data to the required data*/
public class ESBSrcTran {

    private ObservableList<ESBStmBean> esbStmData;
    private CSVSQLEngine csvengine;
    //Final Table Rows for each order number
    private ObservableList<ObservableList<String>> input_file_final = FXCollections.observableArrayList();
    //Set for the Order Number
    private ObservableSet indiOrdernum = FXCollections.observableSet();

    //List for the Date Transformation
    ObservableList<String> dateTrans = FXCollections.observableArrayList();
    //List for the 

    ObservableList<String> inp = FXCollections.observableArrayList();
    ObservableList<String> date = FXCollections.observableArrayList();

    public ESBSrcTran(ObservableList<ESBStmBean> esbStmData) {
        this.esbStmData = esbStmData;
        csvengine = new CSVSQLEngine();

    }

    /*Appling the STM Transformation Rule  */
    public void applySRCTran(String srcfile) throws Exception {

        System.out.println("applySRCTran - called");

        for (ESBStmBean eSBStmBean : esbStmData) {
            String stmSrcFieldname = null;
            String stmPropTransRul = null;
            if (eSBStmBean.getProposedTransRule().contains("split")) {

                stmSrcFieldname = eSBStmBean.getSourceFieldName();

                String[] transRules = eSBStmBean.getProposedTransRule().split("-->");
                System.out.println("[INFO] Source Field : " + stmSrcFieldname + "Sub Transformation1 : " + stmPropTransRul);

                //Read the file Name & Sql Statement
                File file = new File(srcfile);
                String filename = file.getName().substring(0, file.getName().lastIndexOf('.'));
                String sql = "select " + stmSrcFieldname.replace(" ", "") + " from " + filename;

                System.out.println("[INFO] sql : " + sql);

                String header = "OrderNumber,PrimaryReference,BOL,ActualShip,ActualDelivery,CarrierCharge,CarrierCurrency,CarrierDistance,CarrierMode,CarrierName,CarrierSCAC,CreateBy,CreateDate,PRO,Status,TargetShipEarly,TargetShipLate,TargetDeliveryEarly,TargetDeliveryLate,UpdateDate";

                ObservableList rowData = csvengine.getFFTableData(srcfile, sql, header);

                rowData.remove(0);
                //System.out.println("[INFO] collist : "+collist.toString());
                /*Data Transformation Code --Adithya */

                for (Object o : rowData) {

                    System.out.println("Final Table : " + input_file_final.size());

                    String[] col = null;
                    for (String s : transRules) {
                        /*Transforming the Order data*/
                        String stmPropTransRul1 = s.substring(s.indexOf("(") + 1, s.length() - 1);

                        if (stmPropTransRul1.contains(" ")) {
                            col = o.toString().split(" ");

                        } else {

                            if (col != null) {
                                for (String string : col) {
                                    String ds = string.substring(Integer.parseInt(stmPropTransRul1.split(",")[0]), Integer.parseInt(stmPropTransRul1.split(",")[1]));
                                    indiOrdernum.add(ds);
                                }
                            } else {
                                String ds = o.toString().substring(Integer.parseInt(stmPropTransRul1.split(",")[0] + 1), Integer.parseInt(stmPropTransRul1.split(",")[1] + 1));

                                indiOrdernum.add(ds);
                            }

                        }

                    }

                }
                dateTrans.add(null);

                /* Getting the Data for the order number */
                System.out.println("Order Number: " + indiOrdernum.size());
                /* Total Data from the File */
                for (Object row : indiOrdernum) {
                    ObservableList collist1 = null;
                    String sql1 = "select '" + row.toString() + "',PrimaryReference,BOL,ActualShip,ActualShip,ActualDelivery,ActualDelivery,CarrierCharge,CarrierCurrency,CarrierDistance,CarrierMode,CarrierName,CarrierSCAC,CreateBy,CreateDate,PRO,Status,TargetShipEarly,TargetShipEarly,TargetShipLate,TargetShipLate,TargetDeliveryEarly,TargetDeliveryEarly,TargetDeliveryLate,TargetDeliveryLate from " + filename + " where OrderNumber like '%" + row.toString() + "%'";

                    System.out.println("SQL1: " + sql1);
                    collist1 = csvengine.getFFTableData(srcfile, sql1, header);

                    input_file_final.add(collist1);
                    System.out.println("Final order data: " + input_file_final.size());

                }
            } else if (eSBStmBean.getProposedTransRule().contains("date")) {
                /* Transformation of the Date*/
                stmSrcFieldname = eSBStmBean.getSourceFieldName().replace(" ", "");
                stmPropTransRul = eSBStmBean.getProposedTransRule().split("[\"]")[1];
                System.out.println("[INFO] Source Field : " + stmSrcFieldname + " Transformation : " + stmPropTransRul);
                System.out.println("Size of the Final: " + input_file_final.size());
                dateTrans.add(stmPropTransRul);

            } else if (eSBStmBean.getProposedTransRule().contains("replace")) {
                /* Transformation for the Replace */
                dateTrans.add(null);
                stmSrcFieldname = eSBStmBean.getSourceFieldName().replace(" ", "");
                stmPropTransRul = eSBStmBean.getProposedTransRule().substring(26, 45);
                System.out.println("[INFO] Source Field : " + stmSrcFieldname + " Transformation : " + stmPropTransRul);
                System.out.println("Final: " + input_file_final.size());
                System.out.println("Inp replace:" + inp.size());
                for (Object object : input_file_final) {
                    ObservableList dd = (ObservableList) object;

                    String[] singleData = dd.toString().split(",");

                    for (int i = 0; i < singleData.length; i++) {
                        if (singleData[i].contains(stmPropTransRul)) {
                            singleData[i] = singleData[i].replace(stmPropTransRul, "");
                            System.out.println(singleData[i] + " : ");
                        }
                    }
                    StringBuffer buffer = new StringBuffer();

                    for (int i = 0; i < singleData.length; i++) {

                        if (i < singleData.length - 1) {
                            buffer.append(singleData[i] + ",");
                        } else {
                            buffer.append(singleData[i]);
                        }
                    }
                    System.out.println(buffer.toString());
                    dd.clear();
                    dd.add(buffer.toString());

                    System.out.println("index: " + object);
                    System.out.println("DDA: " + dd);
                }

            } else {
                dateTrans.add(null);
            }

        }

        getDateTransformation();

    }

    public void chechFinal() {
        System.out.println("Final: " + input_file_final.size());
        for (Object o : input_file_final) {
            ObservableList<String> od = (ObservableList) o;

            System.out.println("Final: " + od.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

        }

    }

    /* Date Transformation --Adithya */
    public void getDateTransformation() throws ParseException {
        System.out.println("Date Tran Sie: " + dateTrans);
        for (Object object : input_file_final) {

            ObservableList dd = (ObservableList) object;
            String[] singleData = dd.toString().split(",");
            System.out.println(singleData.length);
            for (int i = 0, m = 0; i < singleData.length; i++, m++) {

                String data_check = singleData[i].replaceAll(" ", "");
                System.out.println("Data: " + data_check);
                System.out.println("Transformation Rule: " + dateTrans.get(m));

                if (data_check.matches("[0-9]{1,2}/[0-9]{1,2}/[0-9]{1,4}[0-9]{1,2}:[0-9]{1,2}(am|pm)")) {

                    System.out.println("Transformation Rule: " + dateTrans.get(m));
                    Date d = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(singleData[i]);
                    System.out.println("Date: " + d);
                    singleData[i] = new SimpleDateFormat(dateTrans.get(m)).format(d);
                    System.out.println("Date Time : " + singleData[i]);

                }

            }
            StringBuffer buffer = new StringBuffer();

            for (int j = 0; j < singleData.length; j++) {

                if (j < singleData.length - 1) {
                    buffer.append(singleData[j] + ",");
                } else {
                    buffer.append(singleData[j]);
                }
            }
            dd.clear();
            dd.add(buffer.toString());

        }
    }
}
