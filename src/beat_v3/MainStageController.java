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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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
import javafx.util.Callback;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.collections4.CollectionUtils;

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
    private Tab tabmantesting, esbautotab;
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
    @FXML
    private Button autoresultrunbt, result_save_btn, result_clear_btn;


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
    private TableColumn src_count_tbl_col, trg_count_tbl_col, result_count_tbl_col, src_col_tbl_col, src_col_count_tbl_col, trg_col_tbl_col, trg_col_count_tbl_col, result_count_nulls_tbl_col,
            cnt_null_src_col_tbl_col,
            cnt_null_src_col_count_tbl_col,
            cnt_null_trg_col_tbl_col,
            cnt_null_trg_col_count_tbl_col,
            cnt_null_result_count_nulls_tbl_col,
            cnt_dup_src_col_tbl_col,
            cnt_dup_src_col_count_tbl_col,
            cnt_dup_trg_col_tbl_col,
            cnt_dup_trg_col_count_tbl_col,
            cnt_dup_result_count_nulls_tbl_col,
            cnt_dis_src_col_tbl_col,
            cnt_dis_src_col_count_tbl_col,
            cnt_dis_trg_col_tbl_col,
            cnt_dis_trg_col_count_tbl_col,
            cnt_dis_result_count_tbl_col,
            cnt_num_src_col_tbl_col,
            cnt_num_src_col_count_tbl_col,
            cnt_num_trg_col_tbl_col,
            cnt_num_trg_col_count_tbl_col,
            cnt_num_result_count_tbl_col,
            cnt_max_src_col_tbl_col,
            cnt_max_src_col_count_tbl_col,
            cnt_max_trg_col_tbl_col,
            cnt_max_trg_col_count_tbl_col,
            cnt_max_result_count_tbl_col,
            cnt_min_src_col_tbl_col,
            cnt_min_src_col_count_tbl_col,
            cnt_min_trg_col_tbl_col,
            cnt_min_trg_col_count_tbl_col,
            cnt_min_result_count_tbl_col;

    @FXML
    private Label dataValid_status_lbl;

    @FXML
    private TableColumn tabPane_tbl_columns;

    private TotalCountBean bean;

    private String srcCol;
    private String trgCol;

    private ObservableSet<CheckBox> selectedCheckBoxes = FXCollections.observableSet();
    private ObservableSet<CheckBox> unselectedCheckBoxes = FXCollections.observableSet();

    private IntegerBinding numCheckBoxesSelected = Bindings.size(selectedCheckBoxes);

    private File esbStmFile;
    private final int maxNumSelected = 1;

    private CSVSQLEngine cssqleng;

    /*End of the Code by Adithya  */
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
    Map<String, String> frst_1000_testplan;
    Map<String, String> last_1000_testplan;
    Map<String, String> cmpl_data_tesplan;

    //Test Plan Data
    ObservableList<TotalCountBean> total_cnt_testplan_data;
    ObservableList<CountsMaxMinBean> null_cnt_testplan_data;
    ObservableList<CountsMaxMinBean> notnull_cnt_testplan_data;
    ObservableList<CountsMaxMinBean> dup_cnt_testplan_data;
    ObservableList<CountsMaxMinBean> dst_cnt_testplan_data;
    ObservableList<CountsMaxMinBean> sum_num_testplan_data;
    ObservableList<CountsMaxMinBean> max_col_testplan_data;
    ObservableList<CountsMaxMinBean> min_col_testplan_data;
    ObservableList<CountsMaxMinBean> frst_1000_testplan_data;
    ObservableList<CountsMaxMinBean> last_1000_testplan_data;
    ObservableList<CountsMaxMinBean> cmpl_data_tesplan_data;

    private Map<String, TableView> resultTableList;

    private CSVSQLEngine csvengine;

    //file chooser
    final FileChooser fileChooser = new FileChooser();

    //source & file files
    private String srcfile;
    private String trgfile;

    //List to Store the Headers 
    private List src_table;
    private List trg_table;

    //List for Data Fetch from the Table --Data Changes frequently from query to query
    private ObservableList srcCmplResult;
    private ObservableList trgCmplResult;

    //Total Count Variable
    private String srcCnt;
    private String trgCnt;

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

        //initailizing csv sql engine
        cssqleng = new CSVSQLEngine();

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
                System.out.println("Hello");
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

        configureCheckBox(chkfst1000);
        configureCheckBox(chklst1000);
        configureCheckBox(chkcmpltdata);
        configureCheckBox(chkincrdata);
        configureCheckBox(chkschtest);

//        submitButton.setDisable(true);
        numCheckBoxesSelected.addListener((obs, oldSelectedCount, newSelectedCount) -> {
            if (newSelectedCount.intValue() >= maxNumSelected) {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(true));

            } else {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(false));

            }
        });

        /*The below statements are used STM table */
        stm_src_field_tbl_col.setCellValueFactory(new PropertyValueFactory<ESBStmBean, String>("sourceFieldName"));
        stm_src_tran_tbl_col.setCellValueFactory(new PropertyValueFactory<ESBStmBean, String>("proposedTransRule"));
        stm_trg_field_tbl_col.setCellValueFactory(new PropertyValueFactory<ESBStmBean, String>("targetFieldName"));

        src_count_tbl_col.setCellValueFactory(new PropertyValueFactory<TotalCountBean, String>("srcCnt"));
        trg_count_tbl_col.setCellValueFactory(new PropertyValueFactory<TotalCountBean, String>("trgCnt"));
        result_count_tbl_col.setCellValueFactory(new PropertyValueFactory<TotalCountBean, String>("totCnt"));

        total_cnt_testplan_data = FXCollections.observableArrayList();

        //Null Count Coloumns
        src_col_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("srcCol"));
        src_col_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("srcColCount"));
        trg_col_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("trgCol"));
        trg_col_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("trgColCount"));
        result_count_nulls_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("result"));
        null_cnt_testplan_data = FXCollections.observableArrayList();

        //NOt Null Count Coloumns
        cnt_null_src_col_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("srcCol"));
        cnt_null_src_col_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("srcColCount"));
        cnt_null_trg_col_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("trgCol"));
        cnt_null_trg_col_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("trgColCount"));
        cnt_null_result_count_nulls_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("result"));
        notnull_cnt_testplan_data = FXCollections.observableArrayList();

        //Count Distinct
        cnt_dup_src_col_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("srcCol"));
        cnt_dup_src_col_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("srcColCount"));
        cnt_dup_trg_col_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("trgCol"));
        cnt_dup_trg_col_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("trgColCount"));
        cnt_dup_result_count_nulls_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("result"));
        dup_cnt_testplan_data = FXCollections.observableArrayList();

        //Distinct 
        cnt_dis_src_col_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("srcCol"));
        cnt_dis_src_col_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("srcColCount"));
        cnt_dis_trg_col_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("trgCol"));
        cnt_dis_trg_col_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("trgColCount"));
        cnt_dis_result_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("result"));
        dst_cnt_testplan_data = FXCollections.observableArrayList();

        cnt_num_src_col_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("srcCol"));
        cnt_num_src_col_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("srcColData"));
        cnt_num_trg_col_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("trgCol"));
        cnt_num_trg_col_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("trgColCount"));
        cnt_num_result_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("result"));
        sum_num_testplan_data = FXCollections.observableArrayList();

        cnt_max_src_col_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("srcCol"));
        cnt_max_src_col_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("srcColCount"));
        cnt_max_trg_col_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("trgCol"));
        cnt_max_trg_col_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("trgColCount"));
        cnt_max_result_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("result"));
        max_col_testplan_data = FXCollections.observableArrayList();

        cnt_min_src_col_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("srcCol"));
        cnt_min_src_col_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("srcColCount"));
        cnt_min_trg_col_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("trgCol"));
        cnt_min_trg_col_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("trgColCount"));
        cnt_min_result_count_tbl_col.setCellValueFactory(new PropertyValueFactory<CountsMaxMinBean, String>("result"));
        min_col_testplan_data = FXCollections.observableArrayList();

        resultTableList = new HashMap<String, TableView>();
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

        String tablename = nodeselect.getValue();
        String dbname = nodeselect.getParent().getParent().getValue();
        String connname = nodeselect.getParent().getParent().getParent().getParent().getValue();

        tftrgconname.setText(connname + "::" + dbname + "::" + tablename);

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

    }

    @FXML
    private void ffaddTrgButtonAction(ActionEvent event) {
        System.out.println("Clicked - FF DB Add Trg Button");

        String filename = nodeselect1.getValue();
        String filetype = nodeselect1.getParent().getValue();

        getSetFileName(filename, filetype, tftrgconname);
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
    public void autoResultRunButtonAction() throws Exception {
        List ll = getSelectedTestCases();

        System.out.println("Selected TestCases : " + ll);

        List testplan = new ArrayList();

        //autosemifulltabpane.getSelectionModel().getSelectedItem()
        if (esbautotab.isSelected()) {
            generateTestplan(ll);

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
        tmp = chkfst1000.isSelected() ? "frst_1000" : "";
        testcaselist.add(tmp);
        tmp = chklst1000.isSelected() ? "lst_1000" : "";
        testcaselist.add(tmp);
        tmp = chkcmpltdata.isSelected() ? "cmpl_data" : "";
        testcaselist.add(tmp);
        tmp = chkincrdata.isSelected() ? "incr_data" : "";
        testcaselist.add(tmp);
        tmp = chkschtest.isSelected() ? "schl_test" : "";
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

    private void generateTestplan(List ll) throws Exception {
        csvengine = new CSVSQLEngine();
        progstatus_label.setText("Applying Source Transformations on Input File");
        progressLoadingImage();

        //applying Transformation and to the source file
        ESBSrcTran bSrcTran = new ESBSrcTran(stmData);
        bSrcTran.applySRCTran(srcfile);

        //saving to tmp files
        bSrcTran.saveFinalSourceFile(srcfile);
        bSrcTran.saveFinalTargetFile(trgfile);

        if (!tfsrcconname.getText().contains("_final")) {
            tfsrcconname.setText(tfsrcconname.getText().replace(".csv", "_final.csv"));
            tftrgconname.setText(tftrgconname.getText().replace(".txt", "_final.csv"));
        }

        progstatus_label.setText("Applying Source Transformations on Input File Completed");
        progressCompletedImage();

        QueryGenerator qgen = new QueryGenerator();

        total_cnt_testplan = new HashMap<String, String>();
        null_cnt_testplan = new HashMap<String, String>();
        notnull_cnt_testplan = new HashMap<String, String>();
        dup_cnt_testplan = new HashMap<String, String>();
        dst_cnt_testplan = new HashMap<String, String>();
        sum_num_testplan = new HashMap<String, String>();
        max_col_testplan = new HashMap<String, String>();
        min_col_testplan = new HashMap<String, String>();
        frst_1000_testplan = new HashMap<String, String>();
        cmpl_data_tesplan = new HashMap<String, String>();
        last_1000_testplan = new HashMap<String, String>();

        src_table = cssqleng.getFFColumns(this.getConnNameFromUI("src"), "select * from " + this.getTableNameFromUI("src"));
        trg_table = cssqleng.getFFColumns(this.getConnNameFromUI("trg"), "select * from " + this.getTableNameFromUI("trg"));

        Task task = new Task<Void>() {
            @Override
            public Void call() {
                //non ui code

                for (Object item : ll) {
                    System.out.println("Calling: " + item.toString());
                    if (item.toString().equals("total_cnts")) {

                        if (getSourceType().equalsIgnoreCase("ff")) {
                            total_cnt_testplan.put("Total_Cnt_Src_Testcase", qgen.getTotalCntQueries(getDBNameFromUI("src"), getTableNameFromUI("src")).replace(".", ""));
                        } else {
                            total_cnt_testplan.put("Total_Cnt_Src_Testcase", qgen.getTotalCntQueries(getDBNameFromUI("src"), getTableNameFromUI("src")));
                        }

                        if (getTargetType().equalsIgnoreCase("ff")) {
                            total_cnt_testplan.put("Total_Cnt_Trg_Testcase", qgen.getTotalCntQueries(getDBNameFromUI("trg"), getTableNameFromUI("trg")).replace(".", ""));
                        } else {
                            total_cnt_testplan.put("Total_Cnt_Trg_Testcase", qgen.getTotalCntQueries(getDBNameFromUI("trg"), getTableNameFromUI("trg")));
                        }

                        System.out.println("Count Test Plan :" + total_cnt_testplan);

                    }

                    if (item.toString().equals("null_cnts")) {
                        System.out.println("Null Cnts: " + item.toString());
                        //src and trg
                        for (int i = 0; i < src_table.size(); i++) {

                            String row = src_table.get(i).toString();
                            null_cnt_testplan.put("Null_Cnt_Src_Testcase_" + i, qgen.getNullCntQueries(getDBNameFromUI("src"), getTableNameFromUI("src"), row, getSourceType()));
                            row = trg_table.get(i).toString();
                            null_cnt_testplan.put("Null_Cnt_Trg_Testcase_" + i, qgen.getNullCntQueries(getDBNameFromUI("trg"), getTableNameFromUI("trg"), row, getTargetType()));
                        }

                        System.out.println("Null Count Test Plan :" + null_cnt_testplan);

                    }

                    if (item.toString().equals("not_null_cnts")) {

                        //src and trg
                        for (int i = 0; i < src_table.size(); i++) {

                            String row = src_table.get(i).toString();

                            notnull_cnt_testplan.put("NotNull_Cnt_Src_Testcase_" + i, qgen.getNotNullCntQueries(getDBNameFromUI("src"), getTableNameFromUI("src"), row, getSourceType()));

                            row = trg_table.get(i).toString();

                            notnull_cnt_testplan.put("NotNull_Cnt_Trg_Testcase_" + i, qgen.getNotNullCntQueries(getDBNameFromUI("trg"), getTableNameFromUI("trg"), row, getTargetType()));

                        }

                        System.out.println("Not Null Count Test Plan :" + notnull_cnt_testplan);

                    }

                    if (item.toString().equals("dst_cnts")) {

                        //src and trg
                        for (int i = 0; i < src_table.size(); i++) {

                            String row = src_table.get(i).toString();

                            dst_cnt_testplan.put("Dst_Cnt_Src_Testcase_" + i, qgen.getDistinctCntQueries(getDBNameFromUI("src"), getTableNameFromUI("src"), row, getSourceType()));

                            row = trg_table.get(i).toString();

                            dst_cnt_testplan.put("Dst_Cnt_Trg_Testcase_" + i, qgen.getDistinctCntQueries(getDBNameFromUI("trg"), getTableNameFromUI("trg"), row, getTargetType()));

                        }

                        System.out.println("Distinct Count Test Plan :" + dst_cnt_testplan);

                    }

                    if (item.toString().equals("dup_cnts")) {

                        //src and trg
                        for (int i = 0; i < src_table.size(); i++) {

                            String row = src_table.get(i).toString();

                            dup_cnt_testplan.put("Dup_Cnt_Src_Testcase_" + i, qgen.getDistinctCntQueries(getDBNameFromUI("src"), getTableNameFromUI("src"), row, getSourceType()));

                            row = trg_table.get(i).toString();

                            dup_cnt_testplan.put("Dup_Cnt_Trg_Testcase_" + i, qgen.getDistinctCntQueries(getDBNameFromUI("trg"), getTableNameFromUI("trg"), row, getTargetType()));

                        }

                        System.out.println("Duplicate Count Test Plan :" + dup_cnt_testplan);

                    }

                    if (item.toString().equals("max_cols")) {

                        //src and trg
                        for (int i = 0; i < src_table.size(); i++) {

                            String row = src_table.get(i).toString();

                            max_col_testplan.put("Max_Col_Src_Testcase_" + i, qgen.getMaxColQueries(getDBNameFromUI("src"), getTableNameFromUI("src"), row, getSourceType()));

                            row = trg_table.get(i).toString();

                            max_col_testplan.put("Max_Col_Trg_Testcase_" + i, qgen.getMaxColQueries(getDBNameFromUI("trg"), getTableNameFromUI("trg"), row, getTargetType()));

                        }

                        System.out.println("Max of Col Test Plan :" + max_col_testplan);

                    }

                    if (item.toString().equals("min_cols")) {

                        //src and trg
                        for (int i = 0; i < src_table.size(); i++) {

                            String row = src_table.get(i).toString();

                            min_col_testplan.put("Min_Col_Src_Testcase_" + i, qgen.getMinColQueries(getDBNameFromUI("src"), getTableNameFromUI("src"), row, getSourceType()));

                            row = trg_table.get(i).toString();

                            min_col_testplan.put("Min_Col_Trg_Testcase_" + i, qgen.getMinColQueries(getDBNameFromUI("trg"), getTableNameFromUI("trg"), row, getTargetType()));

                        }

                        System.out.println("Min of Col Test Plan :" + min_col_testplan);

                    }

                    if (item.toString().equals("sum_num_cols")) {

                        //src and trg
                        for (int i = 0; i < src_table.size(); i++) {

                            String row = src_table.get(i).toString();

                            String qry = qgen.getSumColQueries(getDBNameFromUI("src"), getTableNameFromUI("src"), row, getSourceType());

                            if (!qry.equals("")) {

                                sum_num_testplan.put("Sum_Col_Src_Testcase_" + i, qry);

                            }
                            row = trg_table.get(i).toString();

                            qry = qgen.getSumColQueries(getDBNameFromUI("trg"), getTableNameFromUI("trg"), row, getTargetType());

                            if (!qry.equals("")) {

                                sum_num_testplan.put("Sum_Col_Trg_Testcase_" + i, qry);

                            }
                        }

                        System.out.println("Sum of Col Test Plan :" + sum_num_testplan);

                    }

                    /*Fetch the Complete records */
                    if (item.toString().equals("cmpl_data")) {

                        for (int i = 0; i < src_table.size(); i++) {

                            String row = src_table.get(i).toString();

                            String qry = qgen.getCmplDataQueries(getDBNameFromUI("src"), getTableNameFromUI("src"), row, getSourceType(), src_table);

                            if (!qry.equals("")) {

                                cmpl_data_tesplan.put("Compl_Data_Src_Testcase", qry);

                            }
                            row = trg_table.get(i).toString();

                            qry = qgen.getCmplDataQueries(getDBNameFromUI("trg"), getTableNameFromUI("trg"), row, getTargetType(), trg_table);

                            if (!qry.equals("")) {

                                cmpl_data_tesplan.put("Compl_Data_Trg_Testcase", qry);

                            }
                        }

                        System.out.println("Complete Data :" + cmpl_data_tesplan);

                    }

                }

                Platform.runLater(new Runnable() {
                    public void run() {
                        //ui code
                        progstatus_label.setText("Generating Test Plan");
                        progressLoadingImage();

                    }
                });
                return null;
            }
        };

        task.setOnRunning(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

            }
        });

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progstatus_label.setText("Test Plan Generated");
                progressCompletedImage();

                // clearResultButton(new ActionEvent());
                processTestPlan(ll);

            }

        });

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();

    }

    public void progressCompletedImage() {

        proggif_image.setImage(new Image(getClass().getResourceAsStream("/icon/progcmpleted.gif")));

    }

    public void progressLoadingImage() {

        proggif_image.setImage(new Image(getClass().getResourceAsStream("/icon/proggif.gif")));

    }

    @FXML
    public void esbStmImportButton(ActionEvent event) throws NullPointerException, IOException, BiffException, Exception {

        System.out.println("Clicked - esbStmImportButton");
        Stage mainstage = (Stage) mainvbox.getScene().getWindow();
        esbStmFile = fileChooser.showOpenDialog(mainstage);

        System.out.println("File - " + esbStmFile);

        /* Fetching the STM Data from the STM File */
        if (esbStmFile != null) {
            ESBStmData eSBStmData = new ESBStmData(esbStmFile.toString());
            stmConData = eSBStmData.getSTMConData();
            stmData = eSBStmData.getStmData();
            System.out.println("Getting STM Data");

            /*Setting the Connection Data to the FX Fields  --Adithya 29-04-2017*/
            if (!stmData.isEmpty() && !stmConData.isEmpty()) {

                //STM VALIDATOR OBJECT
                ESBSTMValidator estmvalid = new ESBSTMValidator();
                this.srcfile = stmConData.get("Source File Location").toString() + "\\" + stmConData.get("*Source DB/File Name").trim();
                this.trgfile = stmConData.get("Target File Location").toString() + "\\" + stmConData.get("*Target DB/File Name").trim();

                if (estmvalid.checkSrcTrgFiles(srcfile, trgfile)) {
                    tfsrcconname.setText("FlatFile::" + stmConData.get("*Source Host Name").toString() + "::" + srcfile);
                    tftrgconname.setText("FlatFile::" + stmConData.get("*Target Host Name").toString() + "::" + trgfile);
                    stm_conTitle_txt_field.setText(stmConData.get("*Title").toString());
                    stm_conAut_txt_field.setText(stmConData.get("*Author").toString());
                    stm_conVer_txt_field.setText(stmConData.get("Version & History").toString());
                    esb_stm_tableview.setItems(stmData);

                    autoresultrunbt.setDisable(false);

                } else {
                    new ExceptionUI(new Exception("[Error] Source or Target File not found! Please check"));
                }
            }

        }

    }

    //

    /*Method to set Columns Dynamically to the Multiple Table View --Adithya 30-04-2017*/
    public void setColumnsTableView(TableView tableView, List tableColumns) {
        System.out.println("setTableView method Called");
        if (!tableColumns.isEmpty()) {

            for (Object colname : tableColumns) {
                tabPane_tbl_columns = new TableColumn(colname.toString());
                final int j = tableColumns.indexOf(colname);
                tabPane_tbl_columns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                tableView.getColumns().add(tabPane_tbl_columns);
            }

        } else {
            System.out.println("(Error) No Columns Data found");
        }
    }

    //table clear function
    @FXML
    public void stmTblClear() {

        esb_stm_tableview.getItems().clear();
        tfsrcconname.setText("");
        tftrgconname.setText("");
        stm_conTitle_txt_field.setText("");
        stm_conAut_txt_field.setText("");
        stm_conVer_txt_field.setText("");

        autoresultrunbt.setDisable(true);

    }

    //Method to Check only One Check box in Advance tab
    private void configureCheckBox(CheckBox checkBox) {

        if (checkBox.isSelected()) {
            selectedCheckBoxes.add(checkBox);
        } else {
            unselectedCheckBoxes.add(checkBox);
        }

        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                unselectedCheckBoxes.remove(checkBox);
                selectedCheckBoxes.add(checkBox);
            } else {
                selectedCheckBoxes.remove(checkBox);
                unselectedCheckBoxes.add(checkBox);
            }

        });

    }

    //Method to execute and fetch the data of the test plan --Adithya
    public void execueteTestPlan(ObservableList dataStore, String item, String srcQuery, String trgQuery, List srcHeader, List trgHeader) {
        System.out.println("execueteTestPlan Called");
        System.out.println("Option: " + item);
        System.out.println("SRC File: " + srcfile + " : src Query: " + srcQuery);
        System.out.println("TRG File: " + trgfile + " : trg Query: " + trgQuery);

        try {
            ObservableList srcResult = csvengine.getFFTableData(srcfile, srcQuery);
            ObservableList trgResult = csvengine.getFFTableData(trgfile, trgQuery);

            if (item.equals("total_cnts")) {

                srcCnt = srcResult.get(0).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                trgCnt = trgResult.get(0).toString().replaceAll("\\[", "").replaceAll("\\]", "");

                System.out.println("SRC Count: " + srcCnt);
                System.out.println("trg Count: " + trgCnt);

                if (bean == null) {
                    bean = new TotalCountBean();
                    bean.srcCnt.setValue(srcCnt);
                    bean.trgCnt.setValue(trgCnt);
//bean.getSrcCnt().equals(bean.getTrgCnt())
                    bean.totCnt.setValue(bean.getSrcCnt().equals(bean.getTrgCnt()) ? "Passed" : "Failed");
                    total_cnt_testplan_data.add(bean);
                }

                System.out.println("List: " + total_cnt_testplan_data.toString());

            } else if (item.equals("cmpl_data")) {

                System.out.println("Src Data : " + srcResult.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
                System.out.println("Target Data: " + trgResult.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
                srcCmplResult = srcResult;
                trgCmplResult = trgResult;

            } else if (item.equals("dup_cnts")) {

                System.out.println("MinMAx Count: " + srcResult.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
                System.out.println("MinMax Count: " + trgResult.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
                CountsMaxMinBean countsMaxMinBean = new CountsMaxMinBean();
//                System.out.println("Source Col: " + srcQuery.split(" ")[3] + " : " + srcQuery.split(" ")[3]);
                countsMaxMinBean.srcCol.setValue(srcQuery.split(" ")[3]);

                countsMaxMinBean.srcColCount.setValue(Integer.toString(Integer.parseInt(srcCnt) - Integer.parseInt(srcResult.toString().replaceAll("\\[", "").replaceAll("\\]", ""))));

                countsMaxMinBean.trgCol.setValue(trgQuery.split(" ")[3]);
                countsMaxMinBean.trgColCount.setValue(Integer.toString(Integer.parseInt(trgCnt) - Integer.parseInt(trgResult.toString().replaceAll("\\[", "").replaceAll("\\]", ""))));
                countsMaxMinBean.result.setValue(countsMaxMinBean.getSrcColCount().equals(countsMaxMinBean.getTrgColCount()) ? "Passed" : "Failed");

                dataStore.add(countsMaxMinBean);

                System.out.println(dataStore.get(0));

            } else {
                CountsMaxMinBean countsMaxMinBean = new CountsMaxMinBean();
//                System.out.println("Source Col: " + srcQuery.split(" ")[3] + " : " + srcQuery.split(" ")[3]);
                countsMaxMinBean.srcCol.setValue(srcQuery.split(" ")[3]);
                countsMaxMinBean.srcColCount.setValue(srcResult.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
                countsMaxMinBean.trgCol.setValue(trgQuery.split(" ")[3]);
                countsMaxMinBean.trgColCount.setValue(trgResult.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
                countsMaxMinBean.result.setValue(countsMaxMinBean.getSrcColCount().equals(countsMaxMinBean.getTrgColCount()) ? "Passed" : "Failed");

                dataStore.add(countsMaxMinBean);

                System.out.println(dataStore.get(0));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Methods to call the executeTestPlan method to fetch the Data --Adithya
    public void processTestPlan(List checkOptionData) {
        System.out.println("processTestPlan called");

        for (Object item : checkOptionData) {

            System.out.println("Calling Check Option " + item.toString());

            if (item.toString().equals("total_cnts")) {
                //Calling the Plan Execution

                Task task = new Task<Void>() {
                    @Override
                    public Void call() {

                        //non ui code
                        // totalCounts_tbl_view.getItems().clear();
                        execueteTestPlan(total_cnt_testplan_data, item.toString(), total_cnt_testplan.get("Total_Cnt_Src_Testcase").toString(), total_cnt_testplan.get("Total_Cnt_Trg_Testcase").toString(), src_table, trg_table);
                        Platform.runLater(new Runnable() {
                            public void run() {
                                //ui code

                                totalCounts_tbl_view.setItems(total_cnt_testplan_data);
                                CountTableRowHighlighter();
                            }
                        });
                        return null;
                    }
                };

                task.setOnRunning(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Executing Count Test Plan");
                        progressLoadingImage();

                    }
                });

                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Execution Completed");
                        progressCompletedImage();
                        result_save_btn.setDisable(false);
                        result_clear_btn.setDisable(false);

                    }
                });

                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();

            }

            if (item.toString().equals("null_cnts")) {
                totalCounts_null_tbl_view.getItems().clear();
                System.out.println("Null Count Test Plan :" + null_cnt_testplan);
                Task task = new Task<Void>() {
                    @Override
                    public Void call() {
                        //non ui code

                        for (int j = 0; j < null_cnt_testplan.size() / 2; j++) {
                            System.out.println("SRC Test : " + null_cnt_testplan.get("Null_Cnt_Src_Testcase_" + j));
                            //Calling the Plan Execution
                            execueteTestPlan(null_cnt_testplan_data, item.toString(), null_cnt_testplan.get("Null_Cnt_Src_Testcase_" + j), null_cnt_testplan.get("Null_Cnt_Trg_Testcase_" + j), src_table, trg_table);

                        }

                        Platform.runLater(new Runnable() {
                            public void run() {
                                //ui code

                                BasicTestResulTableRowHighlighter(totalCounts_null_tbl_view);
                                totalCounts_null_tbl_view.setItems(null_cnt_testplan_data);

                            }
                        });
                        return null;
                    }
                };

                task.setOnRunning(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Executing Null Test Plan");
                        progressLoadingImage();

                    }
                });

                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Execution Completed");
                        progressCompletedImage();
                        result_save_btn.setDisable(false);
                        result_clear_btn.setDisable(false);

                    }
                });

                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();

            }

            if (item.toString().equals("not_null_cnts")) {
                totalCountsnot_null_tbl_view.getItems().clear();
                System.out.println("Not Null Count Test Plan :" + notnull_cnt_testplan);
                Task task = new Task<Void>() {
                    @Override
                    public Void call() {
                        //non ui code
                        for (int j = 0; j < notnull_cnt_testplan.size() / 2; j++) {
                            //Calling the Plan Execution
                            execueteTestPlan(notnull_cnt_testplan_data, item.toString(), notnull_cnt_testplan.get("NotNull_Cnt_Src_Testcase_" + j), notnull_cnt_testplan.get("NotNull_Cnt_Trg_Testcase_" + j), src_table, trg_table);

                        }
                        Platform.runLater(new Runnable() {
                            public void run() {
                                //ui code

                                BasicTestResulTableRowHighlighter(totalCountsnot_null_tbl_view);
                                totalCountsnot_null_tbl_view.setItems(notnull_cnt_testplan_data);
                            }
                        });
                        return null;
                    }
                };

                task.setOnRunning(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Executing Not Null Test Plan");
                        progressLoadingImage();

                    }
                });

                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Execution Completed");
                        progressCompletedImage();
                        result_save_btn.setDisable(false);
                        result_clear_btn.setDisable(false);

                    }
                });

                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();
            }

            if (item.toString()
                    .equals("dst_cnts")) {
                countDistinct_tbl_view.getItems().clear();
                System.out.println("Distinct Count Test Plan :" + dst_cnt_testplan);
                Task task = new Task<Void>() {
                    @Override
                    public Void call() {
                        //non ui code
                        for (int j = 0; j < dst_cnt_testplan.size() / 2; j++) {
                            //Calling the Plan Execution
                            execueteTestPlan(dst_cnt_testplan_data, item.toString(), dst_cnt_testplan.get("Dst_Cnt_Src_Testcase_" + j), dst_cnt_testplan.get("Dst_Cnt_Trg_Testcase_" + j), src_table, trg_table);

                        }
                        Platform.runLater(new Runnable() {
                            public void run() {
                                //ui code

                                BasicTestResulTableRowHighlighter(countDistinct_tbl_view);
                                countDistinct_tbl_view.setItems(dst_cnt_testplan_data);
                            }
                        });
                        return null;
                    }
                };

                task.setOnRunning(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Executing Distinct Test Plan");
                        progressLoadingImage();

                    }
                });

                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Execution Completed");
                        progressCompletedImage();
                        result_save_btn.setDisable(false);
                        result_clear_btn.setDisable(false);

                    }
                });

                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();
            }

            if (item.toString()
                    .equals("dup_cnts")) {

                System.out.println("Duplicate Count Test Plan :" + dup_cnt_testplan);
                countDupli_tbl_view.getItems().clear();
                Task task = new Task<Void>() {
                    @Override
                    public Void call() {
                        for (int j = 0; j < dup_cnt_testplan.size() / 2; j++) {
                            //Calling the Plan Execution
                            execueteTestPlan(dup_cnt_testplan_data, item.toString(), dup_cnt_testplan.get("Dup_Cnt_Src_Testcase_" + j), dup_cnt_testplan.get("Dup_Cnt_Trg_Testcase_" + j), src_table, trg_table);

                        }

                        Platform.runLater(new Runnable() {
                            public void run() {
                                //ui code

                                BasicTestResulTableRowHighlighter(countDupli_tbl_view);
                                countDupli_tbl_view.setItems(dup_cnt_testplan_data);
                            }
                        });
                        return null;
                    }
                };

                task.setOnRunning(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Executing Duplicate Test Plan");
                        progressLoadingImage();

                    }
                });

                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Execution Completed");
                        progressCompletedImage();
                        result_save_btn.setDisable(false);
                        result_clear_btn.setDisable(false);

                    }
                });

                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();

            }

            if (item.toString()
                    .equals("max_cols")) {
                max_tbl_view.getItems().clear();
                System.out.println("Max of Col Test Plan :" + max_col_testplan);
                Task task = new Task<Void>() {
                    @Override
                    public Void call() {

                        for (int j = 0; j < max_col_testplan.size() / 2; j++) {
                            //Calling the Plan Execution
                            execueteTestPlan(max_col_testplan_data, item.toString(), max_col_testplan.get("Max_Col_Src_Testcase_" + j), max_col_testplan.get("Max_Col_Trg_Testcase_" + j), src_table, trg_table);

                        }
                        Platform.runLater(new Runnable() {
                            public void run() {
                                //ui code

                                BasicTestResulTableRowHighlighter(max_tbl_view);
                                max_tbl_view.setItems(max_col_testplan_data);
                            }
                        });
                        return null;
                    }
                };

                task.setOnRunning(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Executing Max Test Plan");
                        progressLoadingImage();

                    }
                });

                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Execution Completed");
                        progressCompletedImage();
                        result_save_btn.setDisable(false);
                        result_clear_btn.setDisable(false);

                    }
                });

                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();
            }

            if (item.toString()
                    .equals("min_cols")) {
                min_tbl_view.getItems().clear();
                System.out.println("Min of Col Test Plan :" + min_col_testplan);
                Task task = new Task<Void>() {
                    @Override
                    public Void call() {
                        //non ui code
                        for (int j = 0; j < min_col_testplan.size() / 2; j++) {
                            //Calling the Plan Execution
                            execueteTestPlan(min_col_testplan_data, item.toString(), min_col_testplan.get("Min_Col_Src_Testcase_" + j), min_col_testplan.get("Min_Col_Trg_Testcase_" + j), src_table, trg_table);

                        }
                        Platform.runLater(new Runnable() {
                            public void run() {
                                //ui code

                                BasicTestResulTableRowHighlighter(min_tbl_view);
                                min_tbl_view.setItems(min_col_testplan_data);
                            }
                        });
                        return null;
                    }
                };

                task.setOnRunning(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Executing Min Test Plan");
                        progressLoadingImage();

                    }
                });

                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Execution Completed");
                        progressCompletedImage();
                        result_save_btn.setDisable(false);
                        result_clear_btn.setDisable(false);

                    }
                });

                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();
            }

            if (item.toString()
                    .equals("sum_num_cols")) {
                countNumerics_tbl_view.getItems().clear();
                Task task = new Task<Void>() {
                    @Override
                    public Void call() {
                        //non ui code

                        for (int j = 0; j < sum_num_testplan.size() / 2; j++) {
                            //Calling the Plan Execution
                            execueteTestPlan(sum_num_testplan_data, item.toString(), sum_num_testplan.get("Sum_Col_Src_Testcase_" + j), sum_num_testplan.get("Sum_Col_Trg_Testcase_" + j), src_table, trg_table);

                        }
                        Platform.runLater(new Runnable() {
                            public void run() {
                                //ui code

                                BasicTestResulTableRowHighlighter(countNumerics_tbl_view);
                                countNumerics_tbl_view.setItems(sum_num_testplan_data);
                            }
                        });
                        return null;
                    }
                };

                task.setOnRunning(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Executing Sum Test Plan");
                        progressLoadingImage();

                    }
                });

                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Execution Completed");
                        progressCompletedImage();
                        result_save_btn.setDisable(false);
                        result_clear_btn.setDisable(false);

                    }
                });

                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();
            }

            //Check the Calling
            /*Fetch the Complete records */
            if (item.toString()
                    .equals("cmpl_data")) {

                System.out.println("Complete Data :" + cmpl_data_tesplan);
                Task task = new Task<Void>() {
                    @Override
                    public Void call() {
                        //non ui code
                        //Calling the Plan Execution - sourceData_tbl_view not useful and need to pass a param
                        execueteTestPlan(stmData, item.toString(), cmpl_data_tesplan.get("Compl_Data_Src_Testcase"), cmpl_data_tesplan.get("Compl_Data_Trg_Testcase"), src_table, trg_table);
                        ArrayList srcremovedList = (ArrayList) CollectionUtils.removeAll(srcCmplResult, trgCmplResult);
                        ArrayList trgremovedList = (ArrayList) CollectionUtils.removeAll(trgCmplResult, srcCmplResult);
                        Platform.runLater(new Runnable() {
                            public void run() {
                                //ui code
                                sourceData_tbl_view.getItems().clear();
                                sourceData_tbl_view.getColumns().clear();
                                targetData_tbl_view.getItems().clear();
                                targetData_tbl_view.getColumns().clear();
                                setColumnsTableView(sourceData_tbl_view, src_table);
                                setColumnsTableView(targetData_tbl_view, trg_table);
                                sourceData_tbl_view.setItems(srcCmplResult);
                                targetData_tbl_view.setItems(trgCmplResult);

                                System.out.println("Unmatch Source: " + unMatchsourceData_tbl_view);
                                System.out.println("Unmatch Target: " + unMatchtargetData_tbl_view);
                                unMatchsourceData_tbl_view.getItems().clear();
                                unMatchtargetData_tbl_view.getItems().clear();
                                unMatchsourceData_tbl_view.getColumns().clear();
                                unMatchtargetData_tbl_view.getColumns().clear();

                                setColumnsTableView(unMatchsourceData_tbl_view, src_table);
                                setColumnsTableView(unMatchtargetData_tbl_view, trg_table);

                                ObservableList src = FXCollections.observableArrayList(srcremovedList);
                                ObservableList trg = FXCollections.observableArrayList(trgremovedList);
                                unMatchsourceData_tbl_view.setItems(src);
                                unMatchtargetData_tbl_view.setItems(trg);

                            }
                        });
                        return null;
                    }
                };

                task.setOnRunning(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        progstatus_label.setText("Executing Complete Test Plan");
                        progressLoadingImage();

                    }
                });

                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        if (unMatchsourceData_tbl_view.getItems().size() == 0 && unMatchtargetData_tbl_view.getItems().size() == 0) {
                            dataValid_status_lbl.setText("Matched");
                            dataValid_status_lbl.setStyle("-fx-background-color: green");
                        } else {
                            dataValid_status_lbl.setText("Unmatched");
                            dataValid_status_lbl.setStyle("-fx-background-color: red");
                        }

                        progstatus_label.setText("Execution Completed");
                        progressCompletedImage();
                        result_save_btn.setDisable(false);
                        result_clear_btn.setDisable(false);
                    }
                });

                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();
            }

        }

        //File Deletions
        File src = new File(getConnNameFromUI("src"));
        src.deleteOnExit();

        File trg = new File(getConnNameFromUI("trg"));
        trg.deleteOnExit();

    }

    public void CountTableRowHighlighter() {
        totalCounts_tbl_view.setRowFactory(row -> new TableRow<TotalCountBean>() {
            @Override
            public void updateItem(TotalCountBean item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {

                    setStyle("");
                } else {
                    //Now 'item' has all the info of the Person in this row
//                    !item.totCnt.getValue()
                    if (item.totCnt.getValue().equalsIgnoreCase("Failed")) {
                        //We apply now the changes in all the cells of the row
                        //setStyle("-fx-background-color: #ff9999");
                        setStyle("-fx-background-color: red");

                    }

                }
            }
        });

    }

    public void BasicTestResulTableRowHighlighter(TableView tblview) {
        tblview.setRowFactory(row -> new TableRow<CountsMaxMinBean>() {
            @Override
            public void updateItem(CountsMaxMinBean item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else {

                    //Now 'item' has all the info of the Person in this row
                    if (item.result.getValue().equalsIgnoreCase("Failed")) {
                        //We apply now the changes in all the cells of the row
                        //setStyle("-fx-background-color: #ff9999");
                        setStyle("-fx-background-color: red");

                    } else {
                        setStyle("");
                    }
                }
            }
        });

    }

    @FXML
    public void clearResultButton(ActionEvent ae) {

        Map<String, TableView> resultTableList = getSelectedResultTableList(this.getSelectedTestCases());

        result_save_btn.setDisable(true);
        for (Map.Entry<String, TableView> entry : resultTableList.entrySet()) {
            String key = entry.getKey();
            TableView value = entry.getValue();

            value.getItems().clear();
        }

        result_clear_btn.setDisable(true);

    }

    //Storing the tableview into List
    public Map<String, TableView> getSelectedResultTableList(List checkOptions) {
        for (Object checkOption : checkOptions) {
            if (checkOption.equals("total_cnts")) {
                resultTableList.put("total_cnts", totalCounts_tbl_view);
            }

            if (checkOption.toString().equals("null_cnts")) {
                resultTableList.put("null_cnts", totalCounts_null_tbl_view);
            }

            if (checkOption.toString().equals("not_null_cnts")) {
                resultTableList.put("not_null_cnts", totalCountsnot_null_tbl_view);
            }

            if (checkOption.toString()
                    .equals("dst_cnts")) {
                resultTableList.put("dst_cnts", countDistinct_tbl_view);
            }

            if (checkOption.toString()
                    .equals("dup_cnts")) {
                resultTableList.put("dup_cnts", countDupli_tbl_view);
            }

            if (checkOption.toString()
                    .equals("max_cols")) {
                resultTableList.put("max_cols", max_tbl_view);
            }

            if (checkOption.toString()
                    .equals("min_cols")) {
                resultTableList.put("min_cols", min_tbl_view);
            }

            if (checkOption.toString()
                    .equals("sum_num_cols")) {
                resultTableList.put("sum_num_cols", countNumerics_tbl_view);
            }

            if (checkOption.toString()
                    .equals("cmpl_data")) {
                resultTableList.put("cmpl_data_src", sourceData_tbl_view);
                resultTableList.put("cmpl_data_trg", targetData_tbl_view);
                resultTableList.put("cmpl_un_data_src", unMatchsourceData_tbl_view);
                resultTableList.put("cmpl_un_data_trg", unMatchtargetData_tbl_view);
            }
        }

        return resultTableList;
    }

    public void saveTestResults(List checkOptions) {

        Map<String, TableView> resultTableList = getSelectedResultTableList(checkOptions);
        WritableWorkbook wbook;
        WritableSheet shSheet;
        WritableSheet tmpSheet;

        if (esbStmFile != null) //create excel sheet
        {
            try {
                wbook = Workbook.createWorkbook(new File(esbStmFile.toString().substring(0, esbStmFile.toString().lastIndexOf('.')) + "_Results.xls"));

//                System.out.println("File Name: " + esbStmFile.getName().substring(0, esbStmFile.getName().lastIndexOf('.')));
                int mapIndex = 0;

                for (Map.Entry<String, TableView> entry : resultTableList.entrySet()) {
                    String key = entry.getKey();
                    TableView value = entry.getValue();

                    int k = 0, j = 0;

                    shSheet = wbook.createSheet(key.toUpperCase(), mapIndex++);

                    System.out.println("srccolnames" + value.getColumns());

                    String[] colNames = {"Source Column", "Source Value", "Target Column", "Target Value", "Result"};

                    if (!key.contains("data")) {
                        for (int m = 0; m < value.getItems().size(); m++) {

                            for (int l = 0; l < colNames.length; l++) {

                                jxl.write.Label labTemp = new jxl.write.Label(l, 0, colNames[l]);
                                try {
                                    shSheet.addCell(labTemp);
                                } catch (WriteException ex) {
                                    Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }

                            String itemData = value.getItems().get(m).toString();
                            System.out.println(value.getItems().get(m).toString());

                            String[] itemDataList = itemData.split(",");

                            for (int l = 0; l < itemDataList.length; l++) {
                                String data = itemDataList[l];
                                jxl.write.Label labTemp = new jxl.write.Label(l, m + 1, data);
                                try {
                                    shSheet.addCell(labTemp);
                                } catch (WriteException ex) {
                                    Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        }

                    } else {

                        //For Complete Data
                        List cmpltColNames;

                        for (int m = 0; m < value.getItems().size(); m++) {
                            if (key.contains("src")) {
                                cmpltColNames = src_table;
                            } else {
                                cmpltColNames = trg_table;
                            }

                            for (int l = 0; l < cmpltColNames.size(); l++) {

                                jxl.write.Label labTemp = new jxl.write.Label(l, 0, cmpltColNames.get(l).toString());
                                try {
                                    shSheet.addCell(labTemp);
                                } catch (WriteException ex) {
                                    Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }

                            String itemData = value.getItems().get(m).toString();
                            System.out.println(value.getItems().get(m).toString());

                            String[] itemDataList = itemData.split(",");

                            for (int l = 0; l < itemDataList.length; l++) {
                                String data = itemDataList[l];
                                jxl.write.Label labTemp = new jxl.write.Label(l, m + 1, data);
                                try {
                                    shSheet.addCell(labTemp);
                                } catch (WriteException ex) {
                                    Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        }

                    }
                }

                wbook.write();
                wbook.close();
            } catch (IOException ex) {
                Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);

            } catch (WriteException ex) {
                Logger.getLogger(MainStageController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void saveResultAction(ActionEvent ae) {

        List ll = getSelectedTestCases();
        saveTestResults(ll);
    }
}
