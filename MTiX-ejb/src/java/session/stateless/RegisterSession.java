/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.UserEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Student-ID
 */
@Stateless
public class RegisterSession implements RegisterSessionLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createUser(String username, String password, String mobileNumber) {
        UserEntity u = new UserEntity();
        u.createAccount(username, password, mobileNumber);
        entityManager.persist(u);
        //entityManager.flush();
    }

}
