/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jxl.read.biff.BiffException;

/**
 *
 * @author Ravindra
 */
public class MainStageController implements Initializable {

    @FXML
    private TreeView dbtreeview;
    @FXML
    private ContextMenu dbrghtclkmenu;
    @FXML
    private ContextMenu ffrghtclkmenu;
    @FXML
    private VBox mainvbox;
    @FXML
    private TreeView flatfilelist;
    @FXML
    private CheckBox chktcnts;
    @FXML
    private CheckBox chkncnts;
    @FXML
    private CheckBox chknncnts;
    @FXML
    private CheckBox chkdupcnts;
    @FXML
    private CheckBox chkdstcnts;
    @FXML
    private CheckBox chksumnum;
    @FXML
    private CheckBox chkmax;
    @FXML
    private CheckBox chkmin;
    @FXML
    private CheckBox chkfst1000;
    @FXML
    private CheckBox chklst1000;
    @FXML
    private CheckBox chkcmpltdata;
    @FXML
    private CheckBox chkincrdata;
    @FXML
    private CheckBox chkschtest;
    @FXML
    private TabPane tbpanemanautotest, autosemifulltabpane;
    @FXML
    private TabPane tbpaneautoresult, advanResultTabPane, testScenTabPane;
    @FXML
    private TextField tfsrcconname;
    @FXML
    private TextField tftrgconname;
    @FXML
    private Tab tabmantesting, semiautotab, fullautotab;
    @FXML
    private Tab tabautotesting, advanceTestScenTab, advanResultTab, basicResultTab, basicTestScenTab;
    @FXML
    private Tab tabrsltmetadata, tabrslttotalcnt, tabrsltnullcol, tabrsltnotnullcol, tabrsltdupcol, tabrsltdstcol, tabrsltsumcol, tabrsltmincol, tabrsltmaxcol;
    @FXML
    private ComboBox combosrccolnames;
    @FXML
    private ComboBox combotrgcolnames;
    @FXML
    private TableView trgsemikeycoltbl;
    @FXML
    private TableView srcsemikeycoltbl;
    @FXML
    private TableColumn trgsemikeycol, srcsemikeycol, trgsemicolname, srcsemicolname, trgsemicolcond, srcsemicolcond, trgsemicoltype, srcsemicoltype;
    @FXML
    private TextField srcsemisplcondtxtfld, trgsemisplcondtxtfld;
    @FXML
    private ImageView ffloadinggif, dbloadinggif, proggif_image;
    @FXML
    private Label progstatus_label;
    @FXML
    private AnchorPane resultAnchorPane;
    @FXML
    private SplitPane workspaceSplitPane;

    /*Code Created By the Adithya 29-04-2017 */
 /*STM Table Data */
    @FXML
    private TextField stm_conTitle_txt_field, stm_conAut_txt_field, stm_conVer_txt_field;

    @FXML
    private TableView esb_stm_tableview;
    @FXML
    private TableColumn stm_src_field_tbl_col, stm_src_tran_tbl_col, stm_trg_field_tbl_col;

    /*Objects to Store the STM Connection and processing Data */
    private ObservableList<ESBStmBean> stmData;
    private ObservableMap<String, String> stmConData;

    /*Tabbed Pane Tables and Columns 30-04-2017 */
    @FXML
    private TableView totalCounts_tbl_view,
            totalCounts_null_tbl_view,
            totalCountsnot_null_tbl_view,
            countDupli_tbl_view,
            countDistinct_tbl_view,
            countNumerics_tbl_view,
            max_tbl_view,
            min_tbl_view,
            sourceData_tbl_view,
            targetData_tbl_view,
            unMatchsourceData_tbl_view,
            unMatchtargetData_tbl_view;

    @FXML
    private TableColumn tabPane_tbl_columns;

    /*End of the Code by Adithya  */
    // @FXML
    // private WebView cnt_result_webview;
    //Variables
    private LoadConnectionsTreeView lctv;
    private LoadFlatFilesTreeView lfftv;
    private TreeItem<String> connections, databases, tables;
    private TreeItem<String> nodeselect, nodeselect1;
    private List tblkeys, srccoltypes, trgcoltypes;
    private int tabindex;

    //testplan
    Map<String, String> total_cnt_testplan;
    Map<String, String> null_cnt_testplan;
    Map<String, String> notnull_cnt_testplan;
    Map<String, String> dup_cnt_testplan;
    Map<String, String> dst_cnt_testplan;
    Map<String, String> sum_num_testplan;
    Map<String, String> max_col_testplan;
    Map<String, String> min_col_testplan;

    //file chooser
    final FileChooser fileChooser = new FileChooser();

    @FXML
    private void dbAddButtonAction(ActionEvent event) {

        System.out.print("Clicked ADD DB Button");

        //CALLING ADD DB CONNECTION UI
        new AddDBConnectionUI(lctv);

    }

    @FXML
    private void ffAddButtonAction(ActionEvent event) {

        System.out.print("Clicked ADD FILE Button");

        //CALLING ADD FILE CONNECTION UI
        new FlatFileConnectionUI(lfftv, mainvbox);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //workspaceSplitPane.setDividerPositions(10);       
        //resultAnchorPane.setVisible(false);
        //tab selector based test scenario
        testScenTabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {

                if (testScenTabPane.getSelectionModel().getSelectedItem().getText().equalsIgnoreCase("Advanced Test Scenarios")) {
                    advanResultTabPane.getSelectionModel().select(advanResultTab);
                } else {
                    advanResultTabPane.getSelectionModel().select(basicResultTab);
                }
            }
        }
        );

        //setting loading images to db / ff
        dbloadinggif.setImage(new Image(getClass().getResourceAsStream("/icon/dbtabicon.png")));
        ffloadinggif.setImage(new Image(getClass().getResourceAsStream("/icon/filesicon.png")));

        //loading db connection into treeview
        lctv = new LoadConnectionsTreeView(this.dbtreeview);
        dbtreeview.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    dbtree_SelectionChanged((TreeItem<String>) newValue);
                });

        //loading flat files
        lfftv = new LoadFlatFilesTreeView(this.flatfilelist);
        flatfilelist.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    fftree_SelectionChanged((TreeItem<String>) newValue);
                });

        //check box listeners
        chktcnts.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {

                if (chktcnts.isSelected()) {
                    addTab(tbpaneautoresult, tabrslttotalcnt, tabindex);

                } else {
                    tabindex = tbpaneautoresult.getTabs().indexOf(tabrslttotalcnt);
                    closeTab(tabrslttotalcnt);
                }

            }
        });

        chkncnts.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {

                if (chkncnts.isSelected()) {
                    addTab(tbpaneautoresult, tabrsltnullcol, tabindex);

                } else {
                    tabindex = tbpaneautoresult.getTabs().indexOf(tabrsltnullcol);
                    closeTab(tabrsltnullcol);
                }

            }
        });

        chknncnts.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {

                if (chknncnts.isSelected()) {
                    addTab(tbpaneautoresult, tabrsltnotnullcol, tabindex);

                } else {
                    tabindex = tbpaneautoresult.getTabs().indexOf(tabrsltnotnullcol);
                    closeTab(tabrsltnotnullcol);
                }

            }
        });

        chkdupcnts.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {

                if (chkdupcnts.isSelected()) {
                    addTab(tbpaneautoresult, tabrsltdupcol, tabindex);

                } else {
                    tabindex = tbpaneautoresult.getTabs().indexOf(tabrsltdupcol);
                    closeTab(tabrsltdupcol);
                }

            }
        });

        chkdstcnts.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {

                if (chkdstcnts.isSelected()) {
                    addTab(tbpaneautoresult, tabrsltdstcol, tabindex);

                } else {
                    tabindex = tbpaneautoresult.getTabs().indexOf(tabrsltdstcol);
                    closeTab(tabrsltdstcol);
                }

            }
        });

        chksumnum.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {

                if (chksumnum.isSelected()) {
                    addTab(tbpaneautoresult, tabrsltsumcol, tabindex);

                } else {
                    tabindex = tbpaneautoresult.getTabs().indexOf(tabrsltsumcol);
                    closeTab(tabrsltsumcol);
                }

            }
        });

        chkmax.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {

                if (chkmax.isSelected()) {
                    addTab(tbpaneautoresult, tabrsltmaxcol, tabindex);

                } else {
                    tabindex = tbpaneautoresult.getTabs().indexOf(tabrsltmaxcol);
                    closeTab(tabrsltmaxcol);
                }

            }
        });

        chkmin.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {

                if (chkmin.isSelected()) {
                    addTab(tbpaneautoresult, tabrsltmincol, tabindex);

                } else {
                    tabindex = tbpaneautoresult.getTabs().indexOf(tabrsltmincol);
                    closeTab(tabrsltmincol);
                }

            }
        });

        chkfst1000.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {

            }
        });

        chklst1000.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {

            }
        });

        chkcmpltdata.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {

            }
        });

        chkincrdata.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {

            }
        });

        chkschtest.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {

            }
        });

        /*The below statements are used STM table */
        stm_src_field_tbl_col.setCellValueFactory(new PropertyValueFactory<>("sourceFieldName"));
        stm_src_tran_tbl_col.setCellValueFactory(new PropertyValueFactory<>("proposedTransRule"));
        stm_trg_field_tbl_col.setCellValueFactory(new PropertyValueFactory<>("targetFieldName"));

    }

    private void dbtree_SelectionChanged(TreeItem<String> nodeselect) {

        this.nodeselect = nodeselect;

        dbrghtclkmenu.getItems().get(0).setDisable(true);
        dbrghtclkmenu.getItems().get(1).setDisable(true);
        dbrghtclkmenu.getItems().get(2).setDisable(true);
        dbrghtclkmenu.getItems().get(3).setDisable(true);
        dbrghtclkmenu.getItems().get(4).setDisable(true);

        String nodename = nodeselect.getValue();
        String parent;
        if (!nodename.equalsIgnoreCase("connections")) {
            dbrghtclkmenu.getItems().get(3).setDisable(false);
            dbrghtclkmenu.getItems().get(4).setDisable(false);
            parent = nodeselect.getParent().getValue();
            Connection conn = null;
            DBConnectionManager ct = null;
            List schlist;
            List tablist;
            List viewlist;
            System.out.println("Name :" + nodename);
            System.out.println("Parent :" + nodeselect.getParent().getValue());

            //For schema list  
            if (nodename.equalsIgnoreCase("databases")) {

                dbrghtclkmenu.getItems().get(0).setDisable(false);
                dbrghtclkmenu.getItems().get(3).setDisable(true);
                dbrghtclkmenu.getItems().get(4).setDisable(true);
                if (nodeselect.getChildren().isEmpty()) {
                    try {
                        ct = new DBConnectionManager();
                        conn = ct.getDBConFromFile(parent);
                        schlist = ct.getSchemaNames(conn, parent);
                        lctv.loadSchemaTreeView(schlist, nodeselect);
                    } catch (IOException ex) {
                        new ExceptionUI(ex);
                        Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        new ExceptionUI(ex);
                        Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        new ExceptionUI(ex);
                        Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            //schema select
            if (nodeselect.getParent().getValue().equalsIgnoreCase("databases")) {
                dbrghtclkmenu.getItems().get(0).setDisable(false);
                dbrghtclkmenu.getItems().get(3).setDisable(true);
                dbrghtclkmenu.getItems().get(4).setDisable(true);
            }

            //table list   
            if (nodeselect.getValue().equalsIgnoreCase("tables")) {
                dbrghtclkmenu.getItems().get(0).setDisable(false);
                dbrghtclkmenu.getItems().get(3).setDisable(true);
                dbrghtclkmenu.getItems().get(4).setDisable(true);
                if (nodeselect.getChildren().isEmpty()) {
                    try {
                        ct = new DBConnectionManager();
                        conn = ct.getDBConFromFile(nodeselect.getParent().getParent().getParent().getValue());
                        tablist = ct.getTableNames(conn, nodeselect.getParent().getParent().getParent().getValue(), nodeselect.getParent().getValue());

                        lctv.loadTableTreeView(tablist, nodeselect);
                        //System.out.println(tablist);
                    } catch (IOException ex) {
                        new ExceptionUI(ex);
                        Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        new ExceptionUI(ex);
                        Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        new ExceptionUI(ex);
                        Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            //view list
            if (nodeselect.getValue().equalsIgnoreCase("views")) {
                dbrghtclkmenu.getItems().get(0).setDisable(false);
                dbrghtclkmenu.getItems().get(3).setDisable(true);
                dbrghtclkmenu.getItems().get(4).setDisable(true);
                if (nodeselect.getChildren().isEmpty()) {
                    try {
                        ct = new DBConnectionManager();
                        conn = ct.getDBConFromFile(nodeselect.getParent().getParent().getParent().getValue());
                        viewlist = ct.getViewNames(conn, nodeselect.getParent().getParent().getParent().getValue(), nodeselect.getParent().getValue());

                        lctv.loadTableTreeView(viewlist, nodeselect);
                        //System.out.println(tablist);
                    } catch (IOException ex) {
                        new ExceptionUI(ex);
                        Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        new ExceptionUI(ex);
                        Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        new ExceptionUI(ex);
                        Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            //enable full righ click menu  
            try {

                if (nodeselect.getParent().getParent().getParent().getValue().equalsIgnoreCase("databases")) {
                    dbrghtclkmenu.getItems().get(0).setDisable(false);
                    dbrghtclkmenu.getItems().get(1).setDisable(false);
                    dbrghtclkmenu.getItems().get(2).setDisable(false);
                    dbrghtclkmenu.getItems().get(3).setDisable(true);
                    dbrghtclkmenu.getItems().get(4).setDisable(true);
                }
            } catch (Exception e) {
                //ignore
            }

        }
    }

    private void fftree_SelectionChanged(TreeItem<String> nodeselect1) {

        this.nodeselect1 = nodeselect1;

        ffrghtclkmenu.getItems().get(0).setDisable(true);
        ffrghtclkmenu.getItems().get(1).setDisable(true);
        ffrghtclkmenu.getItems().get(2).setDisable(true);
        ffrghtclkmenu.getItems().get(3).setDisable(true);
        ffrghtclkmenu.getItems().get(4).setDisable(true);

        String nodename = nodeselect1.getValue();
        System.out.println("Selected Node :" + nodename);
        if (!nodename.equalsIgnoreCase("Flat Files") && !nodename.equalsIgnoreCase("TEXT") && !nodename.equalsIgnoreCase("CSV") && !nodename.equalsIgnoreCase("XML") && !nodename.equalsIgnoreCase("EXCEL") && !nodename.equalsIgnoreCase("JSON")) {

            ffrghtclkmenu.getItems().get(0).setDisable(true);
            ffrghtclkmenu.getItems().get(1).setDisable(false);
            ffrghtclkmenu.getItems().get(2).setDisable(false);
            ffrghtclkmenu.getItems().get(3).setDisable(false);
            ffrghtclkmenu.getItems().get(4).setDisable(false);

        } else if (nodename.equalsIgnoreCase("Flat Files") || nodename.equalsIgnoreCase("TEXT") || nodename.equalsIgnoreCase("CSV") || nodename.equalsIgnoreCase("XML") || nodename.equalsIgnoreCase("EXCEL") || nodename.equalsIgnoreCase("JSON")) {

            ffrghtclkmenu.getItems().get(0).setDisable(false);

        }

    }

    @FXML
    private void dbaddSrcButtonAction(ActionEvent event) {

        System.out.println("Clicked - DB Add Src Button");
        //srcsemikeycoltbl.getItems().clear();

        String tablename = nodeselect.getValue();
        String dbname = nodeselect.getParent().getParent().getValue();
        String connname = nodeselect.getParent().getParent().getParent().getParent().getValue();

        tfsrcconname.setText(connname + "::" + dbname + "::" + tablename);

        //dbLoadColumnNamesUI(connname, dbname, tablename, combosrccolnames,"src");
    }

    @FXML
    private void dbaddTrgButtonAction(ActionEvent event) {

        System.out.println("Clicked - DB Add Trg Button");

        //trgsemikeycoltbl.getItems().clear();
        String tablename = nodeselect.getValue();
        String dbname = nodeselect.getParent().getParent().getValue();
        String connname = nodeselect.getParent().getParent().getParent().getParent().getValue();

        tftrgconname.setText(connname + "::" + dbname + "::" + tablename);

        //dbLoadColumnNamesUI(connname, dbname, tablename, combotrgcolnames,"trg");
    }

    @FXML
    private void dbConnEditButtonAction(ActionEvent event) {
        System.out.println("Clicked - DB Conn Edit Button");
    }

    @FXML
    private void dbConnDeltButtonAction(ActionEvent event) {
        System.out.println("Clicked - DB Conn Delete Button");
    }

    @FXML
    private void dbConnRefreshButtonAction(ActionEvent event) {
        System.out.println("Clicked - DB Conn Refresh Button");
    }

    @FXML
    private void ffaddSrcButtonAction(ActionEvent event) {

        System.out.println("Clicked - FF Add Src Button");

        String filename = nodeselect1.getValue();
        String filetype = nodeselect1.getParent().getValue();
        System.out.println("File Type:  " + filetype);

        getSetFileName(filename, filetype, tfsrcconname);

        // srcsemikeycoltbl.getItems().clear();
        /*
        try {
           
            String filename = nodeselect1.getValue();
            String filetype = nodeselect1.getParent().getValue();

            getSetFileName(filename, filetype, tfsrcconname);
            CSVSQLEngine cse = new CSVSQLEngine();
            String ffdetails = tfsrcconname.getText().split("::")[2];
            String qry = "Select * from " + filename.split("\\.")[0];

            List colslist = cse.getFFColumns(ffdetails, qry);
            srccoltypes = new ArrayList();
            
            for(Object item:colslist){
                   
                  System.out.println("COl NAME: "+item);
                  srccoltypes.add(cse.getFFColumnType(ffdetails, item.toString(),filename.split("\\.")[0],false));
            }
          
            
            colslist.add(0, "ALL");
            colslist.add(1,"-----------------");
            combosrccolnames.getItems().clear();
            combosrccolnames.getItems().addAll(colslist);

        } catch (Exception ex) {
            Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
            new ExceptionUI(ex);
            
        }
         */
    }

    @FXML
    private void ffaddTrgButtonAction(ActionEvent event) {
        System.out.println("Clicked - FF DB Add Trg Button");

        String filename = nodeselect1.getValue();
        String filetype = nodeselect1.getParent().getValue();

        getSetFileName(filename, filetype, tftrgconname);

        //trgsemikeycoltbl.getItems().clear();
        /*
        try {

            String filename = nodeselect1.getValue();
            String filetype = nodeselect1.getParent().getValue();

            getSetFileName(filename, filetype, tftrgconname);

            CSVSQLEngine cse = new CSVSQLEngine();
            String ffdetails = tftrgconname.getText().split("::")[2];
            String qry = "Select * from " + filename.split("\\.")[0];

            List colslist = cse.getFFColumns(ffdetails, qry);
            trgcoltypes = new ArrayList();
            for (Object item : colslist) {
                trgcoltypes.add(cse.getFFColumnType(ffdetails, item.toString(), filename.split("\\.")[0],false));
            }
            
            colslist.add(0, "ALL");
            colslist.add(1,"-----------------");
            combotrgcolnames.getItems().clear();
            combotrgcolnames.getItems().addAll(colslist);

        } catch (Exception ex) {
            Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
            new ExceptionUI(ex);
        }
         */
    }

    @FXML
    private void ffConnEditButtonAction(ActionEvent event) {
        System.out.println("Clicked - FF Conn Edit Button");
    }

    @FXML
    private void ffConnDeltButtonAction(ActionEvent event) {
        System.out.println("Clicked - FF Conn Delete Button");
    }

    @FXML
    private void ffConnRefreshButtonAction(ActionEvent event) {
        System.out.println("Clicked - FF Conn Refresh Button");
    }

    @FXML
    private void manualTestTabAction() {

        System.out.println("Clicked - Manual Test Tab Action");

        chktcnts.setDisable(true);
        chkncnts.setDisable(true);
        chknncnts.setDisable(true);
        chkdupcnts.setDisable(true);
        chkdstcnts.setDisable(true);
        chksumnum.setDisable(true);
        chkmax.setDisable(true);
        chkmin.setDisable(true);
        chkfst1000.setDisable(true);
        chklst1000.setDisable(true);
        chkcmpltdata.setDisable(true);
        chkincrdata.setDisable(true);
        chkschtest.setDisable(true);
    }

    @FXML
    private void automatedTestTabAction() {

        System.out.println("Clicked - Automated Test Tab Action");

        chktcnts.setDisable(false);
        chkncnts.setDisable(false);
        chknncnts.setDisable(false);
        chkdupcnts.setDisable(false);
        chkdstcnts.setDisable(false);
        chksumnum.setDisable(false);
        chkmax.setDisable(false);
        chkmin.setDisable(false);
        chkfst1000.setDisable(false);
        chklst1000.setDisable(false);
        chkcmpltdata.setDisable(false);
        chkincrdata.setDisable(false);
        chkschtest.setDisable(false);
    }

    private void dbLoadColumnNamesUI(String connname, String dbname, String tablename, ComboBox cb, String src_or_trg) {

        System.out.println("Entered - getColumnNamesFromDB");

        Task task = new Task<Void>() {
            @Override
            public Void call() {

                //NON UI CODE
                Connection conn = null;
                try {
                    DBConnectionManager dbconmgr = new DBConnectionManager();
                    conn = dbconmgr.getDBConFromFile(connname);
                    String SQL = "SELECT * From " + dbname + "." + tablename;
                    String qry = new LimitQueryGen(connname).getLimitQuery(SQL, "1");
                    System.out.println("Query Submitted : " + qry);
                    List data = dbconmgr.getColNames(conn, qry);
                    List keys = dbconmgr.getKeyColNames(conn, dbname, tablename);
                    if (src_or_trg.equalsIgnoreCase("src")) {
                        srccoltypes = dbconmgr.getColType(conn, qry);
                    } else {
                        trgcoltypes = dbconmgr.getColType(conn, qry);
                    }
                    if (!keys.isEmpty()) {
                        for (Object key : keys) {
                            if (data.contains(key)) {
                                data.set(data.indexOf(key), key + " [Key] ");
                            }

                        }
                    }
                    //System.out.println(data);
                    //System.out.println(keys);
                    Platform.runLater(new Runnable() {
                        public void run() {
                            //UI CODE   
                            ObservableList items = FXCollections.observableArrayList();
                            items.add("ALL");
                            items.add("-----------------");
                            items.addAll(data);
                            cb.setItems(items);
                        }
                    });
                } catch (Exception ex) {
                    new ExceptionUI(ex);
                    Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        System.out.println("DB Connection Closed");
                        conn.close();
                    } catch (Exception e) {
                    }
                }
                return null;
            }
        };

        task.setOnFailed(evt -> {
            new ExceptionUI(new Exception("Problem in Retriving Columns & Keys"));
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();

    }

    public String getSetFileName(String filename, String filetype, TextField connname) {

        FileReader fr = null;
        try {
            File file = new File("files/" + filetype + "/" + filename);
            fr = new FileReader(file.getAbsoluteFile());
            BufferedReader br = new BufferedReader(fr);
            String from = br.readLine();
            String actualname = br.readLine();
            connname.setText("FlatFile::" + from + "::" + actualname);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
            new ExceptionUI(ex);
        } catch (IOException ex) {
            Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
            new ExceptionUI(ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                new ExceptionUI(ex);
            }
        }

        return null;
    }

    private void closeTab(Tab tab) {
        EventHandler<Event> handler = tab.getOnClosed();
        if (null != handler) {
            handler.handle(null);
        } else {
            tab.getTabPane().getTabs().remove(tab);
        }
    }

    private void addTab(TabPane tabpane, Tab tab, int index) {
        EventHandler<Event> handler = tab.getOnClosed();
        if (null != handler) {
            handler.handle(null);
        } else {
            tabpane.getTabs().add(index, tab);
            tabpane.getSelectionModel().select(tab);
        }
    }

    @FXML
    public void autoResultRunButtonAction() {

        List ll = getSelectedTestCases();
        System.out.println("Selected TestCases : " + ll);

        List testplan = new ArrayList();

        //autosemifulltabpane.getSelectionModel().getSelectedItem()
        if (semiautotab.isSelected()) {

            System.out.println("Selected semiautotab");
            generateTestplan(ll);

        } else if (fullautotab.isSelected()) {

            System.out.println("Selected fullautotab");

        }

    }

    public List getSelectedTestCases() {

        List testcaselist = new ArrayList();

        String tmp = chktcnts.isSelected() ? "total_cnts" : "";
        testcaselist.add(tmp);
        tmp = chkncnts.isSelected() ? "null_cnts" : "";
        testcaselist.add(tmp);
        tmp = chknncnts.isSelected() ? "not_null_cnts" : "";
        testcaselist.add(tmp);
        tmp = chkdupcnts.isSelected() ? "dup_cnts" : "";
        testcaselist.add(tmp);
        tmp = chkdstcnts.isSelected() ? "dst_cnts" : "";
        testcaselist.add(tmp);
        tmp = chksumnum.isSelected() ? "sum_num_cols" : "";
        testcaselist.add(tmp);
        tmp = chkmax.isSelected() ? "max_cols" : "";
        testcaselist.add(tmp);
        tmp = chkmin.isSelected() ? "min_cols" : "";
        testcaselist.add(tmp);

        return testcaselist;
    }

    public String getTableNameFromUI(String type) {

        String tablename = "";

        if (type.equalsIgnoreCase("src")) {

            if (!tfsrcconname.getText().equalsIgnoreCase("") && !tfsrcconname.getText().contains("FlatFile")) {

                tablename = tfsrcconname.getText().split("::")[2];

            } else if (tfsrcconname.getText().contains("FlatFile")) {

                tablename = tfsrcconname.getText().split("::")[2];

                File f = new File(tablename.split("\\.")[0]);
                tablename = f.getName();

            }
        } else if (type.equalsIgnoreCase("trg")) {

            if (!tftrgconname.getText().equalsIgnoreCase("") && !tftrgconname.getText().contains("FlatFile")) {

                tablename = tftrgconname.getText().split("::")[2];

            } else if (tftrgconname.getText().contains("FlatFile")) {

                tablename = tftrgconname.getText().split("::")[2];

                File f = new File(tablename.split("\\.")[0]);
                tablename = f.getName();

            }

        }

        return tablename;
    }

    public String getConnNameFromUI(String type) {

        String connname = "";

        if (type.equalsIgnoreCase("src")) {

            if (!tfsrcconname.getText().equalsIgnoreCase("") && !tfsrcconname.getText().contains("FlatFile")) {
                connname = tfsrcconname.getText().split("::")[0];
            } else if (tfsrcconname.getText().contains("FlatFile")) {

                connname = tfsrcconname.getText().split("::")[2];
            }
        } else if (type.equalsIgnoreCase("trg")) {

            if (!tftrgconname.getText().equalsIgnoreCase("") && !tftrgconname.getText().contains("FlatFile")) {
                connname = tftrgconname.getText().split("::")[0];
            } else if (tftrgconname.getText().contains("FlatFile")) {

                connname = tftrgconname.getText().split("::")[2];
            }

        }

        return connname;
    }

    public String getDBNameFromUI(String type) {

        String dbname = "";

        if (type.equalsIgnoreCase("src")) {
            if (!tfsrcconname.getText().equalsIgnoreCase("") && !tfsrcconname.getText().contains("FlatFile")) {
                dbname = tfsrcconname.getText().split("::")[1];
            }
        } else {
            if (!tftrgconname.getText().equalsIgnoreCase("") && !tftrgconname.getText().contains("FlatFile")) {
                dbname = tftrgconname.getText().split("::")[1];
            }
        }

        return dbname;
    }

    public String getSourceType() {
        if (tfsrcconname.getText().toLowerCase().contains("flatfile")) {
            return "ff";
        } else {
            return "db";
        }

    }

    public String getTargetType() {
        if (tftrgconname.getText().toLowerCase().contains("flatfile")) {
            return "ff";
        } else {
            return "db";
        }

    }

    private void generateTestplan(List ll) {

        //total_cnts, null_cnts, not_null_cnts, dup_cnts, dst_cnts, sum_num_cols, max_cols, min_cols
        ObservableList src_table = srcsemikeycoltbl.getItems();
        ObservableList trg_table = trgsemikeycoltbl.getItems();

        if (src_table.size() == 0 || trg_table.size() == 0) {

            new AlertUI("[ERROR] Missing Source / Target");

        } else {

            progstatus_label.setText("Generating Test Plan...");
            progressLoadingImage();

            QueryGenerator qgen = new QueryGenerator();

            total_cnt_testplan = new HashMap<String, String>();
            null_cnt_testplan = new HashMap<String, String>();
            notnull_cnt_testplan = new HashMap<String, String>();
            dup_cnt_testplan = new HashMap<String, String>();
            dst_cnt_testplan = new HashMap<String, String>();
            sum_num_testplan = new HashMap<String, String>();
            max_col_testplan = new HashMap<String, String>();
            min_col_testplan = new HashMap<String, String>();

            for (Object item : ll) {

                if (item.toString().equals("total_cnts")) {

                    if (this.getSourceType().equalsIgnoreCase("ff")) {
                        total_cnt_testplan.put("Total_Cnt_Src_Testcase", qgen.getTotalCntQueries(this.getDBNameFromUI("src"), this.getTableNameFromUI("src")).replace(".", ""));
                    } else {
                        total_cnt_testplan.put("Total_Cnt_Src_Testcase", qgen.getTotalCntQueries(this.getDBNameFromUI("src"), this.getTableNameFromUI("src")));
                    }

                    if (this.getTargetType().equalsIgnoreCase("ff")) {
                        total_cnt_testplan.put("Total_Cnt_Trg_Testcase", qgen.getTotalCntQueries(this.getDBNameFromUI("trg"), this.getTableNameFromUI("trg")).replace(".", ""));
                    } else {
                        total_cnt_testplan.put("Total_Cnt_Trg_Testcase", qgen.getTotalCntQueries(this.getDBNameFromUI("trg"), this.getTableNameFromUI("trg")));
                    }

                    System.out.println("Count Test Plan :" + total_cnt_testplan);

                    //imp for html table rendering
                    //cnt_result_webview.getEngine().load("file:///C:/Users/Ravindra/Desktop/sample.html");
                }

                if (item.toString().equals("null_cnts")) {

                    //ObservableList src_table = srcsemikeycoltbl.getItems();
                    //ObservableList trg_table = trgsemikeycoltbl.getItems();
                    List testcase = new ArrayList();

                    //src and trg
                    for (int i = 0; i < src_table.size(); i++) {

                        String[] row = src_table.get(i).toString().replace("[", "").replace("]", "").split(",");

                        null_cnt_testplan.put("Null_Cnt_Src_Testcase_" + i, qgen.getNullCntQueries(this.getDBNameFromUI("src"), this.getTableNameFromUI("src"), row, this.getSourceType()));

                        row = trg_table.get(i).toString().replace("[", "").replace("]", "").split(",");

                        null_cnt_testplan.put("Null_Cnt_Trg_Testcase_" + i, qgen.getNullCntQueries(this.getDBNameFromUI("trg"), this.getTableNameFromUI("trg"), row, this.getTargetType()));

                    }

                    System.out.println("Null Count Test Plan :" + null_cnt_testplan);

                }

                if (item.toString().equals("not_null_cnts")) {

                    //ObservableList src_table = srcsemikeycoltbl.getItems();
                    //ObservableList trg_table = trgsemikeycoltbl.getItems();
                    List testcase = new ArrayList();

                    //src and trg
                    for (int i = 0; i < src_table.size(); i++) {

                        String[] row = src_table.get(i).toString().replace("[", "").replace("]", "").split(",");

                        notnull_cnt_testplan.put("NotNull_Cnt_Src_Testcase_" + i, qgen.getNotNullCntQueries(this.getDBNameFromUI("src"), this.getTableNameFromUI("src"), row, this.getSourceType()));

                        row = trg_table.get(i).toString().replace("[", "").replace("]", "").split(",");

                        notnull_cnt_testplan.put("NotNull_Cnt_Trg_Testcase_" + i, qgen.getNotNullCntQueries(this.getDBNameFromUI("trg"), this.getTableNameFromUI("trg"), row, this.getTargetType()));

                    }

                    System.out.println("Not Null Count Test Plan :" + notnull_cnt_testplan);

                }

                if (item.toString().equals("dst_cnts")) {

                    //ObservableList src_table = srcsemikeycoltbl.getItems();
                    //ObservableList trg_table = trgsemikeycoltbl.getItems();
                    List testcase = new ArrayList();

                    //src and trg
                    for (int i = 0; i < src_table.size(); i++) {

                        String[] row = src_table.get(i).toString().replace("[", "").replace("]", "").split(",");

                        dst_cnt_testplan.put("Dst_Cnt_Src_Testcase_" + i, qgen.getDistinctCntQueries(this.getDBNameFromUI("src"), this.getTableNameFromUI("src"), row, this.getSourceType()));

                        row = trg_table.get(i).toString().replace("[", "").replace("]", "").split(",");

                        dst_cnt_testplan.put("Dst_Cnt_Trg_Testcase_" + i, qgen.getDistinctCntQueries(this.getDBNameFromUI("trg"), this.getTableNameFromUI("trg"), row, this.getTargetType()));

                    }

                    System.out.println("Distinct Count Test Plan :" + dst_cnt_testplan);

                }

                if (item.toString().equals("dup_cnts")) {

                    //ObservableList src_table = srcsemikeycoltbl.getItems();
                    //ObservableList trg_table = trgsemikeycoltbl.getItems();
                    List testcase = new ArrayList();

                    //src and trg
                    for (int i = 0; i < src_table.size(); i++) {

                        String[] row = src_table.get(i).toString().replace("[", "").replace("]", "").split(",");

                        dup_cnt_testplan.put("Dup_Cnt_Src_Testcase_" + i, qgen.getDistinctCntQueries(this.getDBNameFromUI("src"), this.getTableNameFromUI("src"), row, this.getSourceType()));

                        row = trg_table.get(i).toString().replace("[", "").replace("]", "").split(",");

                        dup_cnt_testplan.put("Dup_Cnt_Trg_Testcase_" + i, qgen.getDistinctCntQueries(this.getDBNameFromUI("trg"), this.getTableNameFromUI("trg"), row, this.getTargetType()));

                    }

                    System.out.println("Duplicate Count Test Plan :" + dup_cnt_testplan);

                }

                if (item.toString().equals("max_cols")) {

                    //ObservableList src_table = srcsemikeycoltbl.getItems();
                    //ObservableList trg_table = trgsemikeycoltbl.getItems();
                    List testcase = new ArrayList();

                    //src and trg
                    for (int i = 0; i < src_table.size(); i++) {

                        String[] row = src_table.get(i).toString().replace("[", "").replace("]", "").split(",");

                        max_col_testplan.put("Max_Col_Src_Testcase_" + i, qgen.getMaxColQueries(this.getDBNameFromUI("src"), this.getTableNameFromUI("src"), row, this.getSourceType()));

                        row = trg_table.get(i).toString().replace("[", "").replace("]", "").split(",");

                        max_col_testplan.put("Max_Col_Trg_Testcase_" + i, qgen.getMaxColQueries(this.getDBNameFromUI("trg"), this.getTableNameFromUI("trg"), row, this.getTargetType()));

                    }

                    System.out.println("Max of Col Test Plan :" + max_col_testplan);

                }

                if (item.toString().equals("min_cols")) {

                    //ObservableList src_table = srcsemikeycoltbl.getItems();
                    //ObservableList trg_table = trgsemikeycoltbl.getItems();
                    List testcase = new ArrayList();

                    //src and trg
                    for (int i = 0; i < src_table.size(); i++) {

                        String[] row = src_table.get(i).toString().replace("[", "").replace("]", "").split(",");

                        min_col_testplan.put("Min_Col_Src_Testcase_" + i, qgen.getMinColQueries(this.getDBNameFromUI("src"), this.getTableNameFromUI("src"), row, this.getSourceType()));

                        row = trg_table.get(i).toString().replace("[", "").replace("]", "").split(",");

                        min_col_testplan.put("Min_Col_Trg_Testcase_" + i, qgen.getMinColQueries(this.getDBNameFromUI("trg"), this.getTableNameFromUI("trg"), row, this.getTargetType()));

                    }

                    System.out.println("Min of Col Test Plan :" + min_col_testplan);

                }

                if (item.toString().equals("sum_num_cols")) {

                    //ObservableList src_table = srcsemikeycoltbl.getItems();
                    //ObservableList trg_table = trgsemikeycoltbl.getItems();
                    List testcase = new ArrayList();

                    //src and trg
                    for (int i = 0; i < src_table.size(); i++) {

                        String[] row = src_table.get(i).toString().replace("[", "").replace("]", "").split(",");

                        String qry = qgen.getSumColQueries(this.getDBNameFromUI("src"), this.getTableNameFromUI("src"), row, this.getSourceType());

                        if (!qry.equals("")) {

                            sum_num_testplan.put("Sum_Col_Src_Testcase_" + i, qry);

                        }
                        row = trg_table.get(i).toString().replace("[", "").replace("]", "").split(",");

                        qry = qgen.getSumColQueries(this.getDBNameFromUI("trg"), this.getTableNameFromUI("trg"), row, this.getTargetType());

                        if (!qry.equals("")) {

                            sum_num_testplan.put("Sum_Col_Trg_Testcase_" + i, qry);

                        }
                    }

                    System.out.println("Sum of Col Test Plan :" + sum_num_testplan);

                }

            }

            progstatus_label.setText("Test Plan Generated");
            progressCompletedImage();

        }
    }

    public void progressCompletedImage() {

        proggif_image.setImage(new Image(getClass().getResourceAsStream("/icon/progcmpleted.gif")));

    }

    public void progressLoadingImage() {

        proggif_image.setImage(new Image(getClass().getResourceAsStream("/icon/proggif.gif")));

    }

    public void executeCountTestPlan() {

        progstatus_label.setText("Executing Test Plan...");
        progressLoadingImage();

    }

    @FXML
    public void esbStmImportButton(ActionEvent event) throws NullPointerException, IOException, BiffException, Exception {

        System.out.println("Clicked - esbStmImportButton");
        Stage mainstage = (Stage) mainvbox.getScene().getWindow();
        File file = fileChooser.showOpenDialog(mainstage);

        System.out.println("File - " + file);

        /* Fetching the STM Data from the STM File */
        if (file != null) {
            ESBStmData eSBStmData = new ESBStmData(file.toString());
            stmConData = eSBStmData.getSTMConData();
            stmData = eSBStmData.getStmData();
            System.out.println("Getting STM Data");

            /*Setting the Connection Data to the FX Fields  --Adithya 29-04-2017*/
            if (!stmData.isEmpty() && !stmConData.isEmpty()) {

                //STM VALIDATOR OBJECT
                ESBSTMValidator estmvalid = new ESBSTMValidator();
                String srcfile = stmConData.get("Source File Location").toString() + "\\" + stmConData.get("*Source DB/File Name").trim();
                String trgfile = stmConData.get("Target File Location").toString() + "\\" + stmConData.get("*Target DB/File Name").trim();

                if (estmvalid.checkSrcTrgFiles(srcfile, trgfile)) {
                    tfsrcconname.setText("FlatFile::" + stmConData.get("*Source Host Name").toString() + "::" + srcfile);
                    tftrgconname.setText("FlatFile::" + stmConData.get("*Target Host Name").toString() + "::" + trgfile);
                    stm_conTitle_txt_field.setText(stmConData.get("*Title").toString());
                    stm_conAut_txt_field.setText(stmConData.get("*Author").toString());
                    stm_conVer_txt_field.setText(stmConData.get("Version & History").toString());
                    esb_stm_tableview.setItems(stmData);

                    //applying Transformation and to the source file
                    ESBSrcTran bSrcTran = new ESBSrcTran(stmData);
                    bSrcTran.applySRCTran(srcfile);
                    bSrcTran.chechFinal();

                } else {
                    new ExceptionUI(new Exception("[Error] Source or Target File not found! Please check"));
                }
            }

        }

    }

    /*Method to set Columns Dynamically to the Multiple Table View --Adithya 30-04-2017*/
    public void setColumnsTableView(TableView tableView, ObservableList<String> tableColumns) {
        System.out.println("setTableView method Called");
        if (!tableColumns.isEmpty()) {
            for (String tableColumn : tableColumns) {
                tabPane_tbl_columns = new TableColumn(tableColumn);
                tabPane_tbl_columns.setCellValueFactory(new PropertyValueFactory(tableColumn));
                tableView.getColumns().add(tabPane_tbl_columns);
            }
        } else {
            System.out.println("(Error) No Columns Data found");
        }
    }

}
