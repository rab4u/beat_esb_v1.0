/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.NumberFormat;

/**
 *
 * @author Adithya
 */

/*STM Class to load the Excel File and proccess the data to the map and list*/
public class ESBStmData {

    /*Map Object to store the STM Connection Data*/
    private ObservableMap<String, String> stmConData = FXCollections.observableHashMap();

    /*List Object to Store the STM data using the Object of the ESBStmBean*/
    private ObservableList<ESBStmBean> bStmBeansData = FXCollections.observableArrayList();

    private Workbook xlBook;

    private Sheet xlSheet;

    public ESBStmData(String file) throws IOException, BiffException {
        xlBook = Workbook.getWorkbook(new File(file));

    }

    public ObservableMap<String, String> getSTMConData() {
        xlSheet = xlBook.getSheet(0);
        stmConData.put(xlSheet.getCell(0, 1).getContents(), xlSheet.getCell(1, 1).getContents());
        stmConData.put(xlSheet.getCell(0, 2).getContents(), xlSheet.getCell(1, 2).getContents());
        stmConData.put(xlSheet.getCell(0, 3).getContents(), xlSheet.getCell(1, 3).getContents());
       
        stmConData.put(xlSheet.getCell(0, 4).getContents(), xlSheet.getCell(1, 4).getContents());
        stmConData.put(xlSheet.getCell(0, 5).getContents(), xlSheet.getCell(1, 5).getContents());
        stmConData.put(xlSheet.getCell(0, 6).getContents(), xlSheet.getCell(1, 6).getContents());
        stmConData.put(xlSheet.getCell(0, 7).getContents(), xlSheet.getCell(1, 7).getContents());
        stmConData.put(xlSheet.getCell(0, 8).getContents(), xlSheet.getCell(1, 8).getContents());
        stmConData.put(xlSheet.getCell(0, 9).getContents(), xlSheet.getCell(1, 9).getContents());
        stmConData.put(xlSheet.getCell(0, 10).getContents(), xlSheet.getCell(1, 10).getContents());
        stmConData.put(xlSheet.getCell(0, 11).getContents(), xlSheet.getCell(1, 11).getContents());

        stmConData.put(xlSheet.getCell(0, 12).getContents(), xlSheet.getCell(1, 12).getContents());
        stmConData.put(xlSheet.getCell(0, 13).getContents(), xlSheet.getCell(1, 13).getContents());
        stmConData.put(xlSheet.getCell(0, 14).getContents(), xlSheet.getCell(1, 14).getContents());
        stmConData.put(xlSheet.getCell(0, 15).getContents(), xlSheet.getCell(1, 15).getContents());
        stmConData.put(xlSheet.getCell(0, 16).getContents(), xlSheet.getCell(1, 16).getContents());
        stmConData.put(xlSheet.getCell(0, 17).getContents(), xlSheet.getCell(1, 17).getContents());

        for (Map.Entry m : stmConData.entrySet()) {
            System.out.println(m.getKey() + " : " + m.getValue());
        }

        if (!stmConData.isEmpty()) {
            return stmConData;
        }

        return null;
    }

    public ObservableList<ESBStmBean> getStmData() {
        xlSheet = xlBook.getSheet(0);

        for (int row = 19; row < xlSheet.getRows(); row++) {
            ESBStmBean bStmBean = new ESBStmBean();
            bStmBean.sourceFieldName.setValue(xlSheet.getCell(0, row).getContents());
            bStmBean.transRule.setValue(xlSheet.getCell(1, row).getContents());
            bStmBean.targetFieldName.setValue(xlSheet.getCell(2, row).getContents());
            bStmBean.dataProcesHint.setValue(xlSheet.getCell(3, row).getContents());
            bStmBean.proposedTransRule.setValue(xlSheet.getCell(4, row).getContents());

            bStmBeansData.add(bStmBean);
        }

        if (!bStmBeansData.isEmpty()) {
            return bStmBeansData;
        }

        return null;
    }

}
