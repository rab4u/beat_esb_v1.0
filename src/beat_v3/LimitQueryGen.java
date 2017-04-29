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

/**
 *
 * @author Ravindra
 */
public class LimitQueryGen {
    
    FileReader fr = null;
    
    public LimitQueryGen(String conname){
        try {
            File file = new File("conn/" + conname + ".con");
            fr = new FileReader(file.getAbsoluteFile());
        } catch (FileNotFoundException ex) {
            new ExceptionUI(ex);
        }
       }
    
    public String getLimitQuery(String qry, String limit) throws IOException{
           
        String limitq="";

            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            String url_line = br.readLine();
            
            if(url_line.contains("db2")||url_line.contains("derby")){
                
                limitq = qry + " FETCH FIRST "+limit+" ROWS ONLY with UR";
                
                
            }
            else if(url_line.contains("oracle")){
                
                if(qry.toLowerCase().contains("where")){
                    
                    limitq = qry + " AND ROWNUM <= "+limit;
                    
                }
                else{
                    limitq = qry + " WHERE ROWNUM <= "+limit;
                }
            }
            else if(url_line.contains("sqlserver")){
             
                limitq= "select TOP "+limit + " ";
                limitq = qry.toLowerCase().replaceFirst("select ",  limitq);
                
            }
            else
            {
                limitq = qry + " LIMIT "+limit;
            }
            
        
      return limitq;
    }
    
}
