/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.BulletinEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Student-ID
 */
@Stateless
public class BulletinSession implements BulletinSessionLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void broadcast(String message) {
        BulletinEntity b = new BulletinEntity();
        b.createBulletin(message);
        entityManager.persist(b);
    }
    
    
}
