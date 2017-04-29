/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 *
 * @author Ravindra
 */
class LoadFlatFilesTreeView {
    
    private TreeItem <String> flatfiles, textcsvfiles, excel, xml, json, csv;
    private TreeView flatfiletree;
    private String connpath = "files/";
    private Image csvicon;
    private Image textcsvicon;
    private Image textfileicon;
    private Image csvfileicon;
    private Image excelfileicon;
    private Image jsonfileicon;
    private Image xmlfileicon;    
    private Image jsonicon;
    private Image excelicon;
    private Image xmlicon;
    
    
    List txtfilesList = new ArrayList();
    List excelfilesList = new ArrayList();
    List jsonfilesList = new ArrayList();
    List csvfilesList = new ArrayList();
    List xmlfilesList = new ArrayList();
    

    public LoadFlatFilesTreeView(TreeView flatfiletree) {
        
        flatfiletree.getChildrenUnmodifiable().clear();
        this.flatfiletree = flatfiletree;
        ImageView rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/fficon.png")));
        flatfiles = new TreeItem<>("Flat Files",rootIcon);
        flatfiles.setExpanded(true);
        
        
        textcsvicon = new Image(getClass().getResourceAsStream("/icon/txticon.png")); 
        csvicon = new Image(getClass().getResourceAsStream("/icon/csvicon.png"));
        jsonicon = new Image(getClass().getResourceAsStream("/icon/jsonicon.png"));
        excelicon = new Image(getClass().getResourceAsStream("/icon/excelicon.png"));
        xmlicon = new Image(getClass().getResourceAsStream("/icon/xmlicon.png"));
        
        
        textfileicon = new Image(getClass().getResourceAsStream("/icon/txtfileicon.png")); 
        csvfileicon = new Image(getClass().getResourceAsStream("/icon/csvfileicon.png")); 
        excelfileicon = new Image(getClass().getResourceAsStream("/icon/excelfileicon.png")); 
        jsonfileicon = new Image(getClass().getResourceAsStream("/icon/jsonfileicon.png"));
        xmlfileicon = new Image(getClass().getResourceAsStream("/icon/xmlfileicon.png"));
        
        
        textcsvfiles = new TreeItem<>("TEXT",new ImageView(textcsvicon));
        csv = new TreeItem<>("CSV",new ImageView(csvicon));
        json = new TreeItem<>("JSON",new ImageView(jsonicon));
        excel = new TreeItem<>("EXCEL",new ImageView(excelicon));
        xml = new TreeItem<>("XML",new ImageView(xmlicon));
     
        flatfiles.getChildren().add(textcsvfiles);
        flatfiles.getChildren().add(csv);
        flatfiles.getChildren().add(json);
        flatfiles.getChildren().add(excel);
        flatfiles.getChildren().add(xml);
        
        flatfiletree.setRoot(flatfiles);
       
        getFileNames();
        
        addtxtfilestree(txtfilesList);
        addxmlfilestree(xmlfilesList);
        addexcelfilestree(excelfilesList);
        addjsonfilestree(jsonfilesList);
        addcsvfilestree(csvfilesList);
        
    }
    
    public void getFileNames(){
        
        
        System.out.println("Entered - getFileNames");
        
        List<File> files= listf(connpath);
        
        System.out.println("FileList :"+files);
        
        for(File f: files){
            if(f.getName().contains(".txt")){
                txtfilesList.add(f.getName());
            }
            else if(f.getName().contains(".xls") || f.getName().contains(".xlsx")){
                excelfilesList.add(f.getName());
            }
            else if(f.getName().contains(".xml")){
                xmlfilesList.add(f.getName());
            }
            else if(f.getName().contains(".csv")){
                csvfilesList.add(f.getName());
            }
            else if(f.getName().contains(".json")){
                jsonfilesList.add(f.getName());
            }
            //else{
              //  new ExceptionUI(new Exception("Unrecognized File Format"));
            //}
                
        }
        
        System.out.println("jsonfilesList : "+jsonfilesList);
        System.out.println("csvfilesList : "+csvfilesList);
        System.out.println("txtfilesList : "+txtfilesList);
        System.out.println("xmlfilesList : "+xmlfilesList);
        System.out.println("excelfilesList : "+excelfilesList);
        
        
    }
    
    public static List<File> listf(String directoryName) {
        
         System.out.println("Entered - listf");
        
        File directory = new File(directoryName);

        List<File> resultList = new ArrayList<File>();

        // get all the files from a directory
        File[] fList = directory.listFiles();
        resultList.addAll(Arrays.asList(fList));
        for (File file : fList) {
            if (file.isFile()) {
                System.out.println(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                resultList.addAll(listf(file.getAbsolutePath()));
            }
        }
        
        return resultList;
    } 
    
    public void addtxtfilestree(List l) {

        TreeItem item = null;

        for (Object a : l) {

            item = new TreeItem<>(a.toString(), new ImageView(textfileicon));
            textcsvfiles.getChildren().add(item);
        }
    }
    
    public void addcsvfilestree(List l){
        
        TreeItem item = null;

        for (Object a : l) {

            item = new TreeItem<>(a.toString(), new ImageView(csvfileicon));
            csv.getChildren().add(item);
        }

    }
    public void addjsonfilestree(List l){
        
        TreeItem item = null;

        for (Object a : l) {

            item = new TreeItem<>(a.toString(), new ImageView(jsonfileicon));
            json.getChildren().add(item);
        }

        
    }
    public void addxmlfilestree(List l){
        
        TreeItem item = null;

        for (Object a : l) {

            item = new TreeItem<>(a.toString(), new ImageView(xmlfileicon));
            xml.getChildren().add(item);
        }
 
    }
    public void addexcelfilestree(List l){
        
        TreeItem item = null;

        for (Object a : l) {

            item = new TreeItem<>(a.toString(), new ImageView(excelfileicon));
            excel.getChildren().add(item);
        }
        
        
    }
    
    public void appendConnectionTreeView(String filename) {
       
        
        if(filename.contains("txt")){
            List l = new ArrayList();
            l.add(filename);
            addtxtfilestree(l);
            textcsvfiles.setExpanded(true);
        }
        else if(filename.contains("xml")){
            List l = new ArrayList();
            l.add(filename);
            addxmlfilestree(l);
            xml.setExpanded(true);
        }
        else if(filename.contains("json")){
            List l = new ArrayList();
            l.add(filename);
            addjsonfilestree(l);
            json.setExpanded(true);
        }
        else if(filename.contains("csv")){
            List l = new ArrayList();
            l.add(filename);
            addcsvfilestree(l);
            csv.setExpanded(true);
        }
        else if(filename.contains("xls")||filename.contains("xlsx")){
            List l = new ArrayList();
            l.add(filename);
            addexcelfilestree(l);
            excel.setExpanded(true);
        }
        
    }
    
}
    
