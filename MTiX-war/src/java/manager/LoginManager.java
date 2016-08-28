/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import session.stateless.LoginSessionLocal;

/**
 *
 * @author Student-ID
 */
public class LoginManager {
    private LoginSessionLocal loginSessionLocal;
    
    public LoginManager(LoginSessionLocal loginSessionLocal) {
        this.loginSessionLocal = loginSessionLocal;
    }
    
    public boolean identify(String username, String password) {
        return loginSessionLocal.identifyUser(username, password);
    }
    
    public boolean checkVerification(String username) {
        return loginSessionLocal.checkFirstLogin(username);
    }
}
