package beat_v3;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Adithya
 */

/*Data parsing for the transformation of the Data to the required data*/
public class ESBSrcTran {

    private ObservableList<ESBStmBean> esbStmData;
    private CSVSQLEngine csvengine;

    private ObservableList<String> input_file_final = FXCollections.observableArrayList();

    public ESBSrcTran(ObservableList<ESBStmBean> esbStmData) {
        this.esbStmData = esbStmData;
        csvengine = new CSVSQLEngine();

    }

    public void applySRCTran(String srcfile) throws Exception {

        System.out.println("applySRCTran - called");

        for (ESBStmBean eSBStmBean : esbStmData) {
            String stmSrcFieldname = null;
            String stmPropTransRul = null;
            if (eSBStmBean.getProposedTransRule().contains("split")) {

                stmSrcFieldname = eSBStmBean.getSourceFieldName();
                stmPropTransRul = eSBStmBean.getProposedTransRule().split("[\"]")[1];
                System.out.println("[INFO] Source Field : " + stmSrcFieldname + "Sub Transformation1 : " + stmPropTransRul);

                File file = new File(srcfile);

                String filename = file.getName().substring(0, file.getName().lastIndexOf('.'));

                String sql = "select " + stmSrcFieldname.replace(" ", "") + " from " + filename;

                System.out.println("[INFO] sql : " + sql);

                String header = "OrderNumber,PrimaryReference,BOL,ActualShip,ActualDelivery,CarrierCharge,CarrierCurrency,CarrierDistance,CarrierMode,CarrierName,CarrierSCAC,CreateBy,CreateDate,PRO,Status,TargetShip(Early),TargetShip(Late),TargetDelivery(Early),TargetDelivery(Late),UpdateDate";

                ObservableList collist = csvengine.getFFTableData(srcfile, sql, header);

                collist.remove(0);

                //System.out.println("[INFO] collist : "+collist.toString());
                /*Data Transformation Code --Adithya */
                
                
                
            } else if (eSBStmBean.getProposedTransRule().contains("date")) {
                stmSrcFieldname = eSBStmBean.getSourceFieldName();
                stmPropTransRul = eSBStmBean.getProposedTransRule().split("[\"]")[1];
                System.out.println("[INFO] Source Field : " + stmSrcFieldname + " Transformation : " + stmPropTransRul);
            }

        }
    }

}
