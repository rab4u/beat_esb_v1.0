/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Adithya
 */

/*Bean for all other Counts on the STM data except Total Counts & Advanced table data Processing */
public class CountsMaxMinBean {

    public SimpleStringProperty srcCol = new SimpleStringProperty();
    public SimpleStringProperty srcColCount = new SimpleStringProperty();
    public SimpleStringProperty trgCol = new SimpleStringProperty();
    public SimpleStringProperty trgColCount = new SimpleStringProperty();
    public SimpleStringProperty result = new SimpleStringProperty();

    public String getSrcCol() {
        return srcCol.get();
    }

    public String getSrcColCount() {
        return srcColCount.get();
    }

    public String getTrgCol() {
        return trgCol.get();
    }

    public String getTrgColCount() {
        return trgColCount.get();
    }

    public String getResult() {
        return result.get();
    }

    @Override
    public String toString() {
        return srcCol.get() + "," + srcColCount.get() + "," + trgCol.get() + "," + trgColCount.get() + "," + result.get();
    }

}
