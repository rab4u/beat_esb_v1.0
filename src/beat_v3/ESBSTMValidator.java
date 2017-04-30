/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beat_v3;

import java.io.File;

/**
 *
 * @author Ravindra
 */
public class ESBSTMValidator {
    
    public boolean checkSrcTrgFiles(String srcfile,String trgfile){
        
        System.out.println("checkSrcTrgFiles - called");
        System.out.println("[INFO] SRC FILE NAME : "+ srcfile +" TRG FILE NAME : "+trgfile);
        
        File src = new File(srcfile.trim());
        File trg = new File(trgfile.trim());
        
        if(src.exists()&&trg.exists())
            return true;
        else
            return false;
    }
    
}
