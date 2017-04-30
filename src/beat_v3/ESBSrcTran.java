package beat_v3;

import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 *
 * @author Adithya
 */

/*Data parsing for the transformation of the Data to the required data*/
public class ESBSrcTran {

    private ObservableList<ESBStmBean> esbStmData;

    private ObservableList<String> input_file_final = FXCollections.observableArrayList();

    public ESBSrcTran(ObservableList<ESBStmBean> esbStmData) {
        this.esbStmData = esbStmData;
    }

    public void applySRCTran() {
        for (ESBStmBean eSBStmBean : esbStmData) {
            String stmSrcFieldname = null;
            String stmPropTransRul = null;
            if (eSBStmBean.getProposedTransRule().contains("split")) {
                stmSrcFieldname = eSBStmBean.getSourceFieldName();
                stmPropTransRul = eSBStmBean.getProposedTransRule().split("[\"]")[1];
                System.out.println("Source Field: " + stmSrcFieldname + " <--> Transformation: " + eSBStmBean.getProposedTransRule().split("[\"]")[1]);
            } else if (eSBStmBean.getProposedTransRule().contains("date")) {
                stmSrcFieldname = eSBStmBean.getSourceFieldName();
                stmPropTransRul = eSBStmBean.getProposedTransRule().split("[\"]")[1];
                System.out.println("Source Field: " + stmSrcFieldname + " <--> Transformation: " + eSBStmBean.getProposedTransRule().split("[\"]")[1]);
            }

        }
    }

}
