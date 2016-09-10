/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import java.util.ArrayList;
import javax.persistence.Query;
import session.stateless.MessageSessionLocal;



/**
 *
 * @author Student-ID
 */
public class MessageManager {
    private MessageSessionLocal messageSessionLocal;
    
    public MessageManager(MessageSessionLocal messageSessionLocal) {
        this.messageSessionLocal = messageSessionLocal;
    }
    
    public boolean checkUser(String username) {
        return messageSessionLocal.checkAccount(username);
    }
    
    public boolean sendMessage(String from, String to, String subject, String content) {
        if(this.checkUser(to)) {
            messageSessionLocal.createMessage(to, from, content, subject);
            messageSessionLocal.sendMail(to, "is3102mtix@gmail.com", from +" sent you a message on MTiX with regards to " + subject, "MTiX Messaging", "smtp.gmail.com");
            
            return true;
        } else {
            return false;
        }
    }
    
    public ArrayList<ArrayList<String>> getInbox(String username) {
        ArrayList<ArrayList<String>> inbox = messageSessionLocal.retrieveInbox(username);
        
        return inbox;
    }
}
