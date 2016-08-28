/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

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
}
