/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 * @author Ravindra
 */
public class ClassLoaderEngine {
    
    String jarpath="";
    
    public ClassLoaderEngine(String jarpath){
        this.jarpath=jarpath;
        loadClasses(jarpath);
    }
    
    public void loadClasses(String dirpath){
        
       try {
                File directory = new File(dirpath);
                File[] fList = directory.listFiles();
                
                for (File file : fList){
                    if (file.isFile()) {
                       // System.out.println(file.getAbsolutePath());
                        URL url = file.toURI().toURL();
                        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                        method.setAccessible(true);
                        method.invoke(classLoader, url);
                        
                    } else if (file.isDirectory()) {
                        loadClasses(file.getAbsolutePath());
                    }
                }
            } catch (MalformedURLException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                new ExceptionUI(ex);
            }
    }

}
