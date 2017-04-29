/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Adithya
 */

/*Bean for the STM Data Storage*/
public class ESBStmBean {

    /*Field for the STM Document*/
    public SimpleStringProperty sourceFieldName = new SimpleStringProperty();
    public SimpleStringProperty transRule = new SimpleStringProperty();
    public SimpleStringProperty targetFieldName = new SimpleStringProperty();
    public SimpleStringProperty dataProcesHint = new SimpleStringProperty();
    public SimpleStringProperty proposedTransRule = new SimpleStringProperty();

    public String getSourceFieldName() {
        return sourceFieldName.get();
    }

    public String getTransRule() {
        return transRule.get();
    }

    public String getTargetFieldName() {
        return targetFieldName.get();
    }

    public String getDataProcesHint() {
        return dataProcesHint.get();
    }

    public String getProposedTransRule() {
        return proposedTransRule.get();
    }

}
