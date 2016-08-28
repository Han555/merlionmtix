/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import java.util.List;
import java.util.Vector;
import session.stateless.RegisterSessionLocal;

/**
 *
 * @author Student-ID
 */
public class RegisterManager {
     private RegisterSessionLocal registerSessionLocal;
    
    public RegisterManager(RegisterSessionLocal registerSessionLocal) {
        this.registerSessionLocal = registerSessionLocal;
    }
    
    public void register(String username, String password, String mobileNumber) {
        registerSessionLocal.createUser(username, password, mobileNumber);
    }
    
    public boolean checkConflict(String username) {
        return registerSessionLocal.checkUserConflict(username);
    }
    
    public void sendEmail(String to, String from, String message, String subject, String smtpServ) {
        registerSessionLocal.sendMail(to, from, message, subject, smtpServ);
    }
    
    public boolean checkOldPassword(String username, String oldPassword) {
        List<Vector> user = registerSessionLocal.retrieveUser(username);
        
        if(user.isEmpty()) {
            return false;
        }
        if(oldPassword.equals(user.get(0).get(1))) {
            return true;
        } else {
            return false;
        }
    }
    
    public void verify(String username) {
        registerSessionLocal.verifyAccount(username);
    }
    
    public void changePassword(String username, String newPassword) {
        registerSessionLocal.changeFirstPassword(username, newPassword);
    }
}
