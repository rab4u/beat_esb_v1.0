/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Ravindra
 */
public final class LoadConnectionsTreeView {
    
    private TreeItem <String> connections,connt,databases,tables,views;
    private TreeView conntree;
    private String connpath = "conn/";
    private List connnames;
    private Image childIcon;
    private Image dbIcon;
    private Image schIcon;
    private Image tabIcon;
    private Image tfoldIcon;
    private Image vfoldIcon;
    
    public LoadConnectionsTreeView(TreeView conntree){
        
        this.conntree = conntree;
        ImageView rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/conns.png")));
        connections = new TreeItem<>("Connections",rootIcon);
        connections.setExpanded(true);
        
        connnames = getConnectionNames(connpath);
        childIcon = new Image(getClass().getResourceAsStream("/icon/cont.png")); 
        dbIcon = new Image(getClass().getResourceAsStream("/icon/dbsicon.png"));
        schIcon = new Image(getClass().getResourceAsStream("/icon/schema.png"));
        tabIcon = new Image(getClass().getResourceAsStream("/icon/tabicon.png"));
        tfoldIcon = new Image(getClass().getResourceAsStream("/icon/tfold.png"));
        vfoldIcon = new Image(getClass().getResourceAsStream("/icon/vfold.png"));
        for (Iterator it = connnames.iterator(); it.hasNext();) {
        Object itemString = it.next();
        //System.out.println(itemString);
        connt = new TreeItem<>(itemString.toString(),new ImageView(childIcon));
        databases = new TreeItem<>("Databases",new ImageView(dbIcon));
        connt.getChildren().add(databases);
        connections.getChildren().add(connt);
    }
    conntree.setRoot(connections); 
    }
    
    public void appendConnectionTreeView(String connname){
        
        connt = new TreeItem<>(connname,new ImageView(childIcon));
        databases = new TreeItem<>("Databases",new ImageView(dbIcon));
        connt.getChildren().add(databases);
        connections.getChildren().add(connt);
        conntree.setRoot(connections);
    }
    
    public void loadSchemaTreeView(List schlist,TreeItem ti){
        if(ti.getChildren().isEmpty()){
        ti.getChildren().clear();
        for (Iterator it = schlist.iterator(); it.hasNext();) {
        Object itemString = it.next();
        //System.out.println(itemString);
        connt = new TreeItem<>(itemString.toString(),new ImageView(schIcon));
        tables = new TreeItem<>("Tables",new ImageView(tfoldIcon));
        views = new TreeItem<>("Views",new ImageView(vfoldIcon));
        connt.getChildren().add(tables);
        connt.getChildren().add(views);
        ti.getChildren().add(connt);
        }
        ti.setExpanded(true);
        conntree.setRoot(connections);
        }
    }
    
     public void loadTableTreeView(List tablist,TreeItem ti){
        if(ti.getChildren().isEmpty()){
        ti.getChildren().clear();
        for (Iterator it = tablist.iterator(); it.hasNext();) {
        Object itemString = it.next();
        //System.out.println(itemString);
        connt = new TreeItem<>(itemString.toString(),new ImageView(tabIcon));
        ti.getChildren().add(connt);
        }
        ti.setExpanded(true);
        conntree.setRoot(connections); 
        }
    }
    
    
    public List getConnectionNames(String path){
        
        List connlist = new ArrayList();
        
        try {
                File directory = new File(path);
                File[] fList = directory.listFiles();
                
                for (File file : fList){
                    if (file.isFile()) {
                       // System.out.println(file.getAbsolutePath());
                        connlist.add(file.getName().split(".con")[0]);    
                    } 
                }
            } catch (SecurityException | IllegalArgumentException ex) {
                new ExceptionUI(ex);
            }
        
        return connlist;
        
    }
}
    
  