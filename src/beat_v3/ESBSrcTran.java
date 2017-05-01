package beat_v3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    StringBuffer srcHeader = new StringBuffer();

    private String header = "OrderNumber,PrimaryReference,BOL,ActualShip,ActualDelivery,CarrierCharge,CarrierCurrency,CarrierDistance,CarrierMode,CarrierName,CarrierSCAC,CreateBy,CreateDate,PRO,Status,TargetShipEarly,TargetShipLate,TargetDeliveryEarly,TargetDeliveryLate,UpdateDate";

    public ESBSrcTran(ObservableList<ESBStmBean> esbStmData) {
        this.esbStmData = esbStmData;
        csvengine = new CSVSQLEngine();

    }

    /*Appling the STM Transformation Rule  */
    public void applySRCTran(String srcfile) throws Exception {

        System.out.println("applySRCTran - called");
        int dataPos = 0;

        for (ESBStmBean eSBStmBean : esbStmData) {
            if (++dataPos < esbStmData.size()) {
                srcHeader.append(eSBStmBean.getSourceFieldName().replaceAll(" ", "").trim() + "_" + eSBStmBean.getTargetFieldName().replaceAll(" ", "").trim() + ",");
            } else {
                srcHeader.append(eSBStmBean.getSourceFieldName().replaceAll(" ", "").trim() + "_" + eSBStmBean.getTargetFieldName().replaceAll(" ", "").trim());
            }
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

                ObservableList rowData = csvengine.getFFTableData(srcfile, sql, header);

                rowData.remove(0);
                //System.out.println("[INFO] collist : "+collist.toString());
                /*Data Transformation Code --Adithya */

                for (Object o : rowData) {

                    String[] col = null;
                    for (String s : transRules) {
                        /*Transforming the Order data*/
                        String stmPropTransRul1 = s.substring(s.indexOf("(") + 1, s.length() - 1);

                        if (stmPropTransRul1.contains(" ")) {
                            col = o.toString().split(" ");

                        } else {

                            if (col != null) {
                                for (String string : col) {
                                    String ds = string.replaceAll("\\[", "").replaceAll("\\]", "").substring(Integer.parseInt(stmPropTransRul1.split(",")[0]), Integer.parseInt(stmPropTransRul1.split(",")[1]));
                                    indiOrdernum.add(ds);
                                    System.out.println("order Num: " + ds + " : " + col);
                                }
                            } else {
                                String ds = o.toString().replaceAll("\\[", "").replaceAll("\\]", "").substring(Integer.parseInt(stmPropTransRul1.split(",")[0]), Integer.parseInt(stmPropTransRul1.split(",")[1]));

                                indiOrdernum.add(ds);
                                System.out.println("order Num: " + ds + " : " + col);
                            }

                        }

                    }

                }
                dateTrans.add(null);

                /* Getting the Data for the order number */
                for (Object row : indiOrdernum) {
                    ObservableList collist1 = null;
                    String sql1 = "select '" + row.toString() + "',PrimaryReference,BOL,ActualShip,ActualShip,ActualDelivery,ActualDelivery,CarrierCharge,CarrierCurrency,CarrierDistance,CarrierMode,CarrierName,CarrierSCAC,CreateBy,CreateDate,PRO,Status,TargetShipEarly,TargetShipEarly,TargetShipLate,TargetShipLate,TargetDeliveryEarly,TargetDeliveryEarly,TargetDeliveryLate,TargetDeliveryLate from " + filename + " where OrderNumber like '%" + row.toString() + "%'";

                    System.out.println("SQL1: " + sql1);
                    collist1 = csvengine.getFFTableData(srcfile, sql1, header);

                    input_file_final.add(collist1);

                }
            } else if (eSBStmBean.getProposedTransRule().contains("date")) {
                /* Transformation of the Date*/
                stmSrcFieldname = eSBStmBean.getSourceFieldName().replace(" ", "");
                stmPropTransRul = eSBStmBean.getProposedTransRule().split("[\"]")[1];
                System.out.println("[INFO] Source Field : " + stmSrcFieldname + " Transformation : " + stmPropTransRul);

                dateTrans.add(stmPropTransRul);

            } else if (eSBStmBean.getProposedTransRule().contains("replace")) {
                /* Transformation for the Replace */
                dateTrans.add(null);
                stmSrcFieldname = eSBStmBean.getSourceFieldName().replace(" ", "");
                stmPropTransRul = eSBStmBean.getProposedTransRule().substring(26, 45);
                System.out.println("[INFO] Source Field : " + stmSrcFieldname + " Transformation : " + stmPropTransRul);

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
                            buffer.append(singleData[i].trim() + ",");
                        } else {
                            buffer.append(singleData[i].trim());
                        }
                    }
                    System.out.println(buffer.toString());
                    dd.clear();
                    dd.add(buffer.toString().trim());

                }

            } else if (eSBStmBean.getProposedTransRule().contains("Constant")) {
                dateTrans.add(null);

                for (Object o : input_file_final) {
                    ObservableList<String> od = (ObservableList) o;
                    StringBuffer s = new StringBuffer(od.toString()).append("," + eSBStmBean.getProposedTransRule().replaceAll("\\[", "").replaceAll("\\]", "").split("[\"]")[1]);
                    System.out.println("Data: " + s);
                    od.clear();
                    od.add(s.toString());

                }
            } else {
                dateTrans.add(null);
            }

        }

        if (!input_file_final.isEmpty()) {
            getDateTransformation();
        }

    }

    /*Printing the Final List Data --Adithya  */
    public void printFinalData() {
        System.out.println("printFinalData called");
//        System.out.println("Final: " + input_file_final.size());
        for (Object o : input_file_final) {
            ObservableList<String> od = (ObservableList) o;

            System.out.println("Final: " + od.toString().replaceAll("\\[", "").replaceAll("\\]", "").trim());

        }

    }

    /* Date Transformation --Adithya */
    public void getDateTransformation() throws ParseException {
        System.out.println("getDateTransformation called");
        for (Object object : input_file_final) {

            ObservableList dd = (ObservableList) object;
            String[] singleData = dd.toString().split(",");
            System.out.println(singleData.length);
            for (int i = 0, m = 0; i < singleData.length; i++, m++) {

                String data_check = singleData[i].replaceAll(" ", "").replaceAll("\\[", "").replaceAll("\\]", "").trim();

                if (data_check.matches("[0-9]{1,2}/[0-9]{1,2}/[0-9]{1,4}[0-9]{1,2}:[0-9]{1,2}(am|pm)")) {

                    System.out.println("Transformation Rule: " + dateTrans.get(m));
                    String da = singleData[i].replaceAll("\\[", "").replaceAll("\\]", "");
                    System.out.println("Date: " + da);
                    Date d = new SimpleDateFormat("MM/dd/yyyy hh:mma").parse(da);
                    singleData[i] = new SimpleDateFormat(dateTrans.get(m)).format(d);

                    System.out.println("Date Time : " + singleData[i]);

                }

            }
            StringBuffer buffer = new StringBuffer();

            for (int j = 0; j < singleData.length; j++) {

                if (j < singleData.length - 1) {
                    buffer.append(singleData[j].trim() + ",");
                } else {
                    buffer.append(singleData[j].trim());
                }
            }
            dd.clear();
            dd.add(buffer.toString());

        }
    }

    public void saveTargetFile(String trgtFile) throws FileNotFoundException, IOException {
        File file = new File(trgtFile);
        String filename = file.getName().substring(0, file.getName().lastIndexOf('.'));
        System.out.println("saveTargetFile called: " + filename);
        System.out.println("File Path: " + file.getParent());
        File saveFile = new File(file.getParent() + "\\" + filename + "_final.csv");
        System.out.println("Header: " + srcHeader);
        FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
        fileOutputStream.write(srcHeader.toString().getBytes());
        fileOutputStream.write("\n".getBytes());
        for (Object o : input_file_final) {
            ObservableList<String> od = (ObservableList) o;
            System.out.println("Inserting the Data");
//            System.out.println("Final: " + od.toString().replaceAll("\\[", "").replaceAll("\\]", "").trim());?

            fileOutputStream.write(od.toString().replaceAll("\\[", "").replaceAll("\\]", "").trim().getBytes());
            fileOutputStream.write("\n".getBytes());

        }
        fileOutputStream.close();

        System.out.println("File Closed after insertion");

    }
}
