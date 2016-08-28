/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.UserEntity;
import java.util.List;
import java.util.Vector;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Student-ID
 */
@Stateless
public class LoginSession implements LoginSessionLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean identifyUser(String username, String password) {
        Query q = entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.username = " +"'"+ username+"'" + " AND u.password = " +"'"+password+"'");
        
        if(q.getResultList().isEmpty()) {
            return false;
        } else{
            return true;
        }
    }

    @Override
    public boolean checkFirstLogin(String username) {
        Query q = entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.username = " +"'"+ username+"'");
        
        for(Object o: q.getResultList()) {
            UserEntity u = (UserEntity) o;
            
            if(u.isFirstLogin()) {
                return true;
            } else{
                return false;
            }
        }
        return false;
    }

    
    
    
    
}