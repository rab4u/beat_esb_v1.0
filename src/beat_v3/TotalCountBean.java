package beat_v3;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Adithya
 */

/*Bean for the Total Count  */
public class TotalCountBean {

    public SimpleStringProperty srcCnt = new SimpleStringProperty();
    public SimpleStringProperty trgCnt = new SimpleStringProperty();
    public SimpleBooleanProperty totCnt = new SimpleBooleanProperty();

    public String getSrcCnt() {
        return srcCnt.get();
    }

    public String getTrgCnt() {
        return trgCnt.get();
    }

    public boolean getTotCnt() {
        return totCnt.get();
    }

}
