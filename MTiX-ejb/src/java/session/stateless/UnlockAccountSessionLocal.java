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
public interface UnlockAccountSessionLocal {

    void unlockAccount(String username);
    
}
