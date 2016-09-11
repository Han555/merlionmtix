/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Student-ID
 */
public class LogManager {

    Logger logger = Logger.getLogger("MyLog");
    FileHandler fh = new FileHandler("C:/Users/Student-ID/Desktop/Project/MTiXFile.log");

    public LogManager() throws IOException {
        try {
            
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            e.printStackTrace();
        } 
    }
    
    public void logMessage(String msg) {
        logger.info(msg); 
    }
}
