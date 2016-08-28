/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import javax.ejb.Local;

/**
 *
 * @author Student-ID
 */
@Local
public interface RegisterSessionLocal {

    void createUser(String username, String password, String mobileNumber);

    boolean checkUserConflict(String username);

    int sendMail(String to, String from, String message, String subject, String smtpServ);
    
}
